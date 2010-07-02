package com.atlassian.labs.bamboo.git;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.atlassian.bamboo.author.Author;
import com.atlassian.bamboo.author.AuthorImpl;
import com.atlassian.bamboo.commit.Commit;
import com.atlassian.bamboo.commit.CommitFile;
import com.atlassian.bamboo.commit.CommitFileImpl;
import com.atlassian.bamboo.commit.CommitImpl;
import com.atlassian.bamboo.repository.AbstractRepository;
import com.atlassian.bamboo.repository.InitialBuildAwareRepository;
import com.atlassian.bamboo.repository.MutableQuietPeriodAwareRepository;
import com.atlassian.bamboo.repository.QuietPeriodHelper;
import com.atlassian.bamboo.repository.Repository;
import com.atlassian.bamboo.repository.RepositoryException;
import com.atlassian.bamboo.utils.ConfigUtils;
import com.atlassian.bamboo.utils.error.ErrorCollection;
import com.atlassian.bamboo.v2.build.BuildChanges;
import com.atlassian.bamboo.v2.build.BuildChangesImpl;
import com.atlassian.bamboo.v2.build.BuildContext;
import com.atlassian.bamboo.v2.build.repository.RepositoryV2;
import com.atlassian.bamboo.ww2.actions.build.admin.create.BuildConfiguration;

import edu.nyu.cs.javagit.api.JavaGitException;
import edu.nyu.cs.javagit.api.Ref;
import edu.nyu.cs.javagit.api.commands.GitBranch;
import edu.nyu.cs.javagit.api.commands.GitBranchOptions;
import edu.nyu.cs.javagit.api.commands.GitBranchResponse;
import edu.nyu.cs.javagit.api.commands.GitCheckout;
import edu.nyu.cs.javagit.api.commands.GitCheckoutOptions;
import edu.nyu.cs.javagit.api.commands.GitCloneOptions;
import edu.nyu.cs.javagit.api.commands.GitLog;
import edu.nyu.cs.javagit.api.commands.GitLogOptions;
import edu.nyu.cs.javagit.api.commands.GitLogResponse;
import edu.nyu.cs.javagit.api.commands.GitReset;
import edu.nyu.cs.javagit.api.commands.GitResetOptions;
import edu.nyu.cs.javagit.api.commands.GitStatus;
import edu.nyu.cs.javagit.api.commands.GitStatusOptions;
import edu.nyu.cs.javagit.api.commands.GitStatusResponse;
import edu.nyu.cs.javagit.client.cli.CliGitClone;
import edu.nyu.cs.javagit.client.cli.CliGitFetch;
import edu.nyu.cs.javagit.client.cli.CliGitSubmodule;

public class GitRepository extends AbstractRepository implements InitialBuildAwareRepository, MutableQuietPeriodAwareRepository, RepositoryV2
{
    private static final Log log = LogFactory.getLog(GitRepository.class);

    // ------------------------------------------------------------------------------------------------------- Constants
    public static final String NAME = "Git";

    private static final String REPO_PREFIX = "repository.git.";
    public static final String GIT_REPO_URL = REPO_PREFIX + "repositoryUrl";
    public static final String GIT_REMOTE_BRANCH = REPO_PREFIX + "remoteBranch";


    private static final String USE_EXTERNALS = REPO_PREFIX + "useExternals";

    private static final String TEMPORARY_GIT_ADVANCED = "temporary.git.advanced";


    private static final String EXTERNAL_PATH_MAPPINGS2 = REPO_PREFIX + "externalsToRevisionMappings";

    private static final Pattern EMAIL_PATTERN = Pattern.compile("[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}");


    // ------------------------------------------------------------------------------------------------- Type Properties
    private String repositoryUrl;
    private String webRepositoryUrl;
    private String remoteBranch;
    private boolean hideAuthorEmail = true;

    // Quiet Period
    private final QuietPeriodHelper quietPeriodHelper = new QuietPeriodHelper(REPO_PREFIX);
    private boolean quietPeriodEnabled = false;
    private int quietPeriod = QuietPeriodHelper.DEFAULT_QUIET_PERIOD;
    private int maxRetries = QuietPeriodHelper.DEFAULT_MAX_RETRIES;
    private static final long serialVersionUID = -5031786714275269805L;

    public GitRepository() {
    }

    public GitRepository(String repositoryUrl, String remoteBranch) {
        this.repositoryUrl = repositoryUrl;
        this.remoteBranch = remoteBranch;
    }

    /**
     * Maps the path to the latest checked revision
     */
    private Map<String, Long> externalPathRevisionMappings = new HashMap<String, Long>();


    /*
     * Used by central bamboo server to determine changes. 
     */

    @NotNull
    public synchronized  BuildChanges collectChangesSinceLastBuild( @NotNull String planKey, @NotNull String lastVcsRevisionKey) throws RepositoryException
    {
        log.debug("determining if there have been changes for " + planKey + " since "+lastVcsRevisionKey);
        try
        {

            getSubstitutedRepositoryUrl();

            File sourceDir = getCheckoutDirectory(planKey);    //  Project/checkout is value

            cloneOrFetch(sourceDir);
            
            final List<Commit> commits = new ArrayList<Commit>();

            
            final String latestRevisionOnSvnServer = detectCommitsForUrl(lastVcsRevisionKey, commits, sourceDir, planKey);

            log.debug("last revision:"+latestRevisionOnSvnServer);

            return new BuildChangesImpl(String.valueOf(latestRevisionOnSvnServer), commits);
        } catch (IOException e)
        {                                                      
            throw new RepositoryException("collectChangesSinceLastBuild", e);
        } catch (JavaGitException e)
        {
            throw new RepositoryException("collectChangesSinceLastBuild", e);
        }
    }


    @Override
    public boolean referencesDifferentRepository() {
        //Ref ref = gitStatus(new File("checkout"));
        //return !ref.getName().equals( remoteBranch);
        // Also check repo url
        return super.referencesDifferentRepository();
    }

    @NotNull
    @Override
    public File getSourceCodeDirectory(@NotNull String s) throws RepositoryException {
        File codeDirectory = super.getSourceCodeDirectory(s);
        try {
            return new File(codeDirectory.getCanonicalPath() + File.separator + "checkout");  
        } catch (IOException e) {
            throw new RepositoryException("getSourceCodeDirectory", e);
        }
    }

    // Todo: Make sure we use vcsRevisionKey in cloneOrFetch

    @NotNull public String retrieveSourceCode( @NotNull String planKey, @Nullable String vcsRevisionKey) throws RepositoryException
    {
        log.debug("retrieving source code");
        try
        {
                getSubstitutedRepositoryUrl();
                File sourceDir = getCheckoutDirectory(planKey); // sourceedir = xxx/checkout
                cloneOrFetch(sourceDir, vcsRevisionKey);
                submodule_update(sourceDir);
                return detectCommitsForUrl(vcsRevisionKey, new ArrayList<Commit>(), sourceDir, planKey);
        } catch (IOException e) {
            throw new RepositoryException("retrieveSourceCode", e);
        } catch (JavaGitException e) {
            throw new RepositoryException(e.getMessage(), e);
        }
    }

    @NotNull
	public String retrieveSourceCode(@NotNull BuildContext buildContext, @Nullable String vcsRevisionKey) throws RepositoryException {
    	return retrieveSourceCode(buildContext.getPlanKey(), vcsRevisionKey);
	}
    /**
     *
     * Detects the commits for the given repository since the revision and HEAD for that URL
     *
     * @param lastRevisionChecked - latest revision checked for this URL. Null if never checked
     * @param commits - the commits are added to this list
     * @param checkoutDir The directory to check out to
     * @param planKey - used for debugging only
     * @return The date/time of the last commit found.
     * @throws RepositoryException when something goes wrong
     * @throws IOException when something goes wrong
     * @throws JavaGitException when something goes wrong
     */

    String detectCommitsForUrl(String lastRevisionChecked, final List<Commit> commits, File checkoutDir, String planKey) throws RepositoryException, IOException, JavaGitException
    {
        log.debug("detecting commits for "+lastRevisionChecked);

        if (isANonSha1RevisionSpecifier(lastRevisionChecked)) {
            lastRevisionChecked = getSha1FromCommitDate(lastRevisionChecked, checkoutDir);
        }
        if (isANonSha1RevisionSpecifier(lastRevisionChecked)) {
            throw new RepositoryException("lastRevisionedChecked must be a SHA hash.  lastRevisionChecked=" + lastRevisionChecked);
        }
                                             
        GitLog gitLog = new GitLog();
        GitLogOptions opt = new GitLogOptions();
        if (lastRevisionChecked != null)
        {
            opt.setOptLimitCommitRange(true, lastRevisionChecked, "HEAD");
        }

        opt.setOptFileDetails(true);
        List<GitLogResponse.Commit> gitCommits;
        try {
           gitCommits = gitLog.log(checkoutDir, opt);
        } catch (JavaGitException e){
            // Typically because the sha1 does not exist. Rebase has happened.

            // Todo: In the checkout, if there is a checkout and it is diverged from origin/branch,
            // we could detect the git merge-base and diff from there
/*            wereHamster said:
                    to see if origin/bax has been rebased, do git fetch origin; test "$(git rev-parse origin/bax..origin/baz@{1})" && echo "origin/baz has been
                     rebased" */
            // We *should* do rebase-detection somewhere in the collectChangesSinceLastBuild, since the server will always be able to tell,
            // since it always has the history from the previous build.

            // Important note; we always need something here<

            gitCommits = getDefaultLogWhenWeDontKnowWhatElsetoDo(checkoutDir, gitLog);
        }
        if (gitCommits.size() > 0)
        {
            log.debug("commits found:"+gitCommits.size());
            String startRevision = gitCommits.get(gitCommits.size() - 1).getSha();
            String latestRevisionOnServer = gitCommits.get(0).getSha();
            log.info("Collecting changes for '" + planKey + "' on path '" + repositoryUrl + "' from version " + startRevision + " to " + latestRevisionOnServer);

            for (GitLogResponse.Commit logEntry : gitCommits)
            {
                CommitImpl commit = new CommitImpl();
                String authorName = logEntry.getAuthor();

                // it is possible to have commits with empty committer. BAM-2945
                if (StringUtils.isBlank(authorName))
                {
                    log.info("Author name is empty for " + commit.toString());
                    authorName = Author.UNKNOWN_AUTHOR;
                }

                if (hideAuthorEmail)
                {
                    authorName = EMAIL_PATTERN.matcher(authorName).replaceFirst("");
                    authorName.trim();
                }

                commit.setAuthor(new AuthorImpl(authorName));
                @SuppressWarnings({"deprecation"}) Date date2 = new Date(logEntry.getDateString());
                commit.setDate(date2);

                String msg = logEntry.getMessage() + " (version " + logEntry.getSha() + ")";
                commit.setComment(msg);
                List<CommitFile> files = new ArrayList<CommitFile>();

                if (logEntry.getFiles() != null) {
                    for (GitLogResponse.CommitFile file : logEntry.getFiles())
                    {
                        CommitFileImpl commitFile = new CommitFileImpl();
                        commitFile.setName(file.getName());
                        commitFile.setRevision(logEntry.getSha());
                        files.add(commitFile);
                    }
                }
                if ( files.size() == 0)  { // No files, add a dummy file to keep version number
                     CommitFileImpl commitFile = new CommitFileImpl();
                     commitFile.setName(".");
                     commitFile.setRevision(logEntry.getSha());
                     files.add(commitFile);
                 }
                 
                commit.setFiles(files);

                commits.add(commit);
            }
            log.debug("Repository change detected for " + repositoryUrl + ", returning "+latestRevisionOnServer);
            return latestRevisionOnServer;
        }
        log.debug("No change detected, returning previous last revision:"+lastRevisionChecked);
        return lastRevisionChecked;
    }



    String getSha1FromCommitDate(String lastRevisionChecked, File checkoutDir) throws JavaGitException, IOException, RepositoryException {
        GitLog gitLog = new GitLog();
        GitLogOptions opt = new GitLogOptions();
        opt.setOptLimitCommitAfter(true, lastRevisionChecked);
        opt.setOptFileDetails(true);
        List<GitLogResponse.Commit> candidateGitCommits = null;
        try {
             candidateGitCommits = gitLog.log(checkoutDir, opt, Ref.createBranchRef("origin/" + remoteBranch));
        } catch (JavaGitException e){
            candidateGitCommits = getDefaultLogWhenWeDontKnowWhatElsetoDo(checkoutDir, gitLog);
            return candidateGitCommits.get( 0  ).getSha(); // #fail, just take the most recent one
        }

        if (candidateGitCommits.size() < 1) {
            candidateGitCommits = getDefaultLogWhenWeDontKnowWhatElsetoDo(checkoutDir, gitLog);
            return candidateGitCommits.get( candidateGitCommits.size() -1 ).getSha(); // We're just guessing, do an old one
        }
        for (GitLogResponse.Commit commit : candidateGitCommits) {
            if (commit.getDateString().equals(lastRevisionChecked)) {
                log.info("Converting lastRevisionChecked from Date into SHA hash");
                return commit.getSha();
            }
        }
        log.info("lastRevisionChecked " + lastRevisionChecked + " did not look like a sha1, but did not match a commit date. This may happen if the commit is gone");
        return lastRevisionChecked;
    }

    private List<GitLogResponse.Commit> getDefaultLogWhenWeDontKnowWhatElsetoDo(File checkoutDir, GitLog gitLog) throws JavaGitException, IOException {
        GitLogOptions opt;
        List<GitLogResponse.Commit> candidateGitCommits;
        opt = new GitLogOptions();
        opt.setOptLimitCommitMax(true, 50);
        candidateGitCommits = gitLog.log(checkoutDir, opt);
        return candidateGitCommits;
    }

    private boolean isANonSha1RevisionSpecifier(String lastRevisionChecked) {
        return isARevision(lastRevisionChecked) && !isSha1(lastRevisionChecked);
    }

    private boolean isSha1(String lastRevisionChecked) {
        return isARevision(lastRevisionChecked) && (lastRevisionChecked.length() == 40);
    }

    private boolean isARevision(String lastRevisionChecked) {
        return (lastRevisionChecked != null);
    }
    

    Ref gitStatus(File sourceDir) throws IOException, JavaGitException {
        GitStatusResponse response = getGitStatusResponse(sourceDir);
         return response.getBranch();
     }

    GitStatusResponse getGitStatusResponse(File sourceDir) throws JavaGitException, IOException {
        GitStatus gitStatus = new GitStatus();
        GitStatusOptions gitStatusOptions = new GitStatusOptions();
        return gitStatus.status(sourceDir, gitStatusOptions);
    }

    
    private void checkout(File sourceDir, Ref remoteBranch, Ref localBranch) throws IOException, JavaGitException {
        GitCheckout gitCheckout = new GitCheckout();
        GitCheckoutOptions options = new GitCheckoutOptions();
        options.setOptB(localBranch);
        gitCheckout.checkout( sourceDir, options, remoteBranch );
    }
    private void checkoutExistingLocalBranch(File sourceDir, Ref localBranch) throws IOException, JavaGitException {
        GitCheckout gitCheckout = new GitCheckout();
        GitCheckoutOptions options = new GitCheckoutOptions();
        gitCheckout.checkout( sourceDir, options, localBranch );
    }


    private void submodule_update(File sourceDir) throws IOException, JavaGitException
    {
        log.debug("doing submodule update");
        CliGitSubmodule submodule = new CliGitSubmodule();
        submodule.init(sourceDir);
        submodule.update(sourceDir);
    }

    private File getCheckoutDirectory(String planKey) throws RepositoryException
    {
        return getSourceCodeDirectory(planKey);
    }



    public void addDefaultValues( @NotNull BuildConfiguration buildConfiguration)
    {
        super.addDefaultValues(buildConfiguration);
        quietPeriodHelper.addDefaultValues(buildConfiguration);
    }


    /**
     * Clones or fetches the specified repository.
     *
     * This method supports exactly 2 use cases:
     * A) A clone of a repository. When cloning, the proper branch is checked if it is not correct by default.
     * B) A fetch. Since the repo is created by use case A, it will always be on the proper branch.
     *
     * Branch switching is supported.
     * 
     * @param sourceDir The checkout directory
     * @throws IOException When something bad happens
     * @throws JavaGitException When something else bad happens.
     */
    void cloneOrFetch(File sourceDir) throws IOException, JavaGitException {
        reallyCloneOrFetch( sourceDir, null);
    }

    void cloneOrFetch(File sourceDir, String requestedVersion) throws IOException, JavaGitException {
        reallyCloneOrFetch(sourceDir, isSha1( requestedVersion)  ? Ref.createSha1Ref(requestedVersion) :  null);

    }

    void reallyCloneOrFetch(File sourceDir, Ref requestedTargetRevision) throws IOException, JavaGitException {
        Ref branchWithOriginPrefix = Ref.createBranchRef("origin/" + this.remoteBranch);

        if (containsValidRepo(sourceDir)) {
            CliGitFetch fetch = new CliGitFetch();
            log.debug("doing fetch");
            fetch.fetch(sourceDir);


            final Ref currentCheckoutBranch = gitStatus(sourceDir);
            if (isRemoteBranchSpecified()){
                if (!branchWithOriginPrefix.isThisBranch(currentCheckoutBranch)){
                    GitBranchResponse branchList = getAllBranches(sourceDir);
                    final Ref branch = Ref.createBranchRef(this.remoteBranch);
                    if (!branchList.containsExactBranchMatch( branch )) {
                        checkout( sourceDir, branchWithOriginPrefix, branch);
                        return; // No need to reset here.
                    } else {
                        checkoutExistingLocalBranch( sourceDir, branch);
                    }
                }

            }
        } else {
            log.debug("no repo found, creating new clone");
            clone(repositoryUrl, sourceDir, null);
            submodule_update(sourceDir);

            if (isRemoteBranchSpecified()) {
                Ref desiredBranch = Ref.createBranchRef(this.remoteBranch);
                GitBranchResponse branchList = getAllBranches(sourceDir);
                boolean branchFound = branchList.containsBranch( branchWithOriginPrefix);
                if (!branchFound) {
                    throw new JavaGitException(12, "The branch " + branchWithOriginPrefix.getName() + " does not exist");
                }
                if (!branchList.getCurrentBranch().equals( desiredBranch)) {
                    checkout( sourceDir, branchWithOriginPrefix, desiredBranch);
                }
            }
        }
        // At this point the proper branch is checked out or created. NO matter which path is used.

        final Ref currentCheckoutBranch = gitStatus(sourceDir);
        if (requestedTargetRevision == null){
            requestedTargetRevision = Ref.createRemoteRef( "origin", currentCheckoutBranch.getName());
        }
        log.debug("resetting local branch to point at " + requestedTargetRevision);
        GitResetOptions gitResetOptions = new GitResetOptions(GitResetOptions.ResetType.HARD, requestedTargetRevision);
        try {
        GitReset.gitReset( sourceDir, gitResetOptions);
        } catch (JavaGitException e){
            log.warn("Had problem resetting head, trying " + branchWithOriginPrefix.getName());
            // Probably could not find SHA1
            gitResetOptions = new GitResetOptions(GitResetOptions.ResetType.HARD, branchWithOriginPrefix);
            GitReset.gitReset( sourceDir, gitResetOptions);
        }

    }

    void clone(File sourceDir, GitCloneOptions gitCloneOptions) throws IOException, JavaGitException {
        clone( repositoryUrl, sourceDir, gitCloneOptions);
    }
    static void clone(String repositoryUrl, File sourceDir, GitCloneOptions gitCloneOptions) throws IOException, JavaGitException {
        ensureDirExists( sourceDir);
        CliGitClone clone = new CliGitClone();
        if (sourceDir.exists()) sourceDir.delete();
        File parentDir = sourceDir.getParentFile();
        String parentDirS = parentDir.getPath();
        String checkoutDir = sourceDir.getPath().substring( parentDirS.length() + 1);
        if (gitCloneOptions == null)
            gitCloneOptions = new GitCloneOptions();
        clone.clone(parentDir, gitCloneOptions, repositoryUrl, new File(checkoutDir));
    }

    static void ensureDirExists(File dir) {
        if (!dir.exists()){
            //noinspection ResultOfMethodCallIgnored
            dir.mkdir();
        }
    }

    public static File getLocalCheckoutSubfolder() {
        return new File("checkout");
    }


    static boolean containsValidRepo(File sourceDir) throws IOException {
        return sourceDir.exists() &&  (new File( sourceDir.getCanonicalPath() + File.separator + ".git").exists() || new File( sourceDir.getCanonicalPath() + File.separator + "HEAD").exists()); 
    }

    boolean isOnBranch(File sourceDir, Ref branchName) throws IOException, JavaGitException {
        GitBranchResponse response = getAllBranches(sourceDir);
        return response.getCurrentBranch().equals( branchName);
    }

    List<GitLogResponse.Commit> gitLog(File sourceDir, int numItems) throws IOException, JavaGitException {
        GitLog gitLog = new GitLog();
        GitLogOptions opt = new GitLogOptions();
        opt.setOptLimitCommitOutputs(true, numItems);
        opt.setOptFileDetails(true);
        return gitLog.log(sourceDir, opt);
    }

    private GitBranchResponse getAllBranches(File sourceDir) throws IOException, JavaGitException {
        GitBranch gitBranch = new GitBranch();
        GitBranchOptions gitBranchOptions = new GitBranchOptions();
        gitBranchOptions.setOptA(true);
        return gitBranch.branch(sourceDir, gitBranchOptions);
    }


    private boolean isRemoteBranchSpecified(){
        return remoteBranch != null;
    }

    @NotNull public ErrorCollection validate( @NotNull BuildConfiguration buildConfiguration)
    {
        ErrorCollection errorCollection = super.validate(buildConfiguration);

        String repoUrl = buildConfiguration.getString(GIT_REPO_URL);
        repoUrl = variableSubstitutionBean.substituteBambooVariables(repoUrl);
        if (StringUtils.isEmpty(repoUrl))
        {
            errorCollection.addError(GIT_REPO_URL, "Please specify the build's Git Repository");
        }
        else
        {
            // FIXME: do validation
        }
        
        String remoBranch = buildConfiguration.getString(GIT_REMOTE_BRANCH);
        if (StringUtils.isEmpty(remoBranch))
        {
            errorCollection.addError(GIT_REMOTE_BRANCH, "Please specify the remote branch that will be checked out");
        }

//        String webRepoUrl = buildConfiguration.getString(WEB_REPO_URL);
//        if (!StringUtils.isEmpty(webRepoUrl) && !UrlUtils.verifyHierachicalURI(webRepoUrl))
//        {
//            errorCollection.addError(WEB_REPO_URL, "This is not a valid url");
//        }

        quietPeriodHelper.validate(buildConfiguration, errorCollection);
        log.debug("validation results:"+errorCollection);
        return errorCollection;
    }


    public boolean isRepositoryDifferent(@NotNull Repository repository)
    {
        if (repository instanceof GitRepository)
        {
            GitRepository existing = (GitRepository) repository;
            return !new EqualsBuilder()
                    .append(this.getName(), existing.getName())
                    .append(this.getRepositoryUrl(), existing.getRepositoryUrl())
                    .isEquals();
        }
        else
        {
            return true;
        }
    }

    public void prepareConfigObject( @NotNull BuildConfiguration buildConfiguration)
    {
        // Disabling advanced will clear all advanced
        if (!buildConfiguration.getBoolean(TEMPORARY_GIT_ADVANCED, false))
        {
            quietPeriodHelper.clearFromBuildConfiguration(buildConfiguration);
            buildConfiguration.clearTree(USE_EXTERNALS);
        }
    }

    public void populateFromConfig( @NotNull HierarchicalConfiguration config)
    {
        super.populateFromConfig(config);

        setRepositoryUrl(config.getString(GIT_REPO_URL));
        setRemoteBranch(config.getString(GIT_REMOTE_BRANCH));

        final Map<String, String> stringMaps = ConfigUtils.getMapFromConfiguration(EXTERNAL_PATH_MAPPINGS2, config);
        externalPathRevisionMappings = ConfigUtils.toLongMap(stringMaps);

        quietPeriodHelper.populateFromConfig(config, this);
    }

    
    @NotNull public HierarchicalConfiguration toConfiguration()
    {
        HierarchicalConfiguration configuration = super.toConfiguration();
        configuration.setProperty(GIT_REPO_URL, getRepositoryUrl());
        configuration.setProperty(GIT_REMOTE_BRANCH, getRemoteBranch());

        final Map<String, String> stringMap = ConfigUtils.toStringMap(externalPathRevisionMappings);
        ConfigUtils.addMapToBuilConfiguration(EXTERNAL_PATH_MAPPINGS2, stringMap, configuration);

        // Quiet period
        quietPeriodHelper.toConfiguration(configuration, this);
        return configuration;
    }

    public void onInitialBuild(BuildContext buildContext)
    {
    }

    public boolean isAdvancedOptionEnabled( BuildConfiguration buildConfiguration)
    {
        final boolean useExternals = buildConfiguration.getBoolean(USE_EXTERNALS, false);
        final boolean quietPeriodEnabled = quietPeriodHelper.isEnabled(buildConfiguration);
        return useExternals || quietPeriodEnabled;
    }

    // -------------------------------------------------------------------------------------- Basic accessors & mutators
    /**
     * What's the name of the plugin - appears in the GUI dropdown
     *
     * @return The name
     */
    
    @NotNull public String getName()
    {
        return NAME;
    }


    /**
     * Specify the subversion repository we are using
     *
     * @param repositoryUrl The subversion repository
     */
    public void setRepositoryUrl(String repositoryUrl)
    {
        this.repositoryUrl = StringUtils.trim(repositoryUrl);
    }

    /**
     * Which repository URL are we using?
     *
     * @return The subversion repository
     */
    public String getRepositoryUrl()
    {
        return repositoryUrl;
    }
    
    /**
     * Specify the subversion repository we are using
     *
     * @param remoteBranch The subversion repository
     */
    public void setRemoteBranch(String remoteBranch)
    {
        this.remoteBranch = StringUtils.trim(remoteBranch);
    }

    /**
     * Which repository URL are we using?
     *
     * @return The subversion repository
     */
    public String getRemoteBranch()
    {
        return remoteBranch;
    }


    public String getSubstitutedRepositoryUrl()
    {
        return variableSubstitutionBean.substituteBambooVariables(repositoryUrl);
    }



    public boolean hasWebBasedRepositoryAccess()
    {
        return StringUtils.isNotBlank(webRepositoryUrl);
    }

    public String getHost()
    {
    	return "localhost"; 
    	// with the code below bamboo says UNKNOWN_HOST and I can't use remote triggers (slnc) 
    	
//        if (repositoryUrl == null)
//        {
//            return UNKNOWN_HOST;
//        }
//
//        try
//        {
//            URL url = new URL(getSubstitutedRepositoryUrl());
//            return url.getHost();
//        } catch (MalformedURLException e)
//        {
//            return UNKNOWN_HOST;
//        }
    }

    public boolean isQuietPeriodEnabled()
    {
        return quietPeriodEnabled;
    }

    public void setQuietPeriodEnabled(boolean quietPeriodEnabled)
    {
        this.quietPeriodEnabled = quietPeriodEnabled;
    }

    public int getQuietPeriod()
    {
        return quietPeriod;
    }

    public void setQuietPeriod(int quietPeriod)
    {
        this.quietPeriod = quietPeriod;
    }

    public int getMaxRetries()
    {
        return maxRetries;
    }

    public void setMaxRetries(int maxRetries)
    {
        this.maxRetries = maxRetries;
    }

    public boolean isHideAuthorEmail() {
        return hideAuthorEmail;
    }

    public void setHideAuthorEmail(boolean hideAuthorEmail) {
        this.hideAuthorEmail = hideAuthorEmail;
    }

    public int hashCode()
    {
        return new HashCodeBuilder(101, 11)
                .append(getKey())
                .append(getRepositoryUrl())
                .append(getTriggerIpAddress())
                .toHashCode();
    }

    public boolean equals(Object o)
    {
        if (!(o instanceof GitRepository))
        {
            return false;
        }
        GitRepository rhs = (GitRepository) o;
        return new EqualsBuilder()
                .append(getRepositoryUrl(), rhs.getRepositoryUrl())
                .append(getTriggerIpAddress(), rhs.getTriggerIpAddress())
                .isEquals();
    }
}
