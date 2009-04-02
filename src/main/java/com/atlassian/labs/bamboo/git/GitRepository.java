package com.atlassian.labs.bamboo.git;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
import com.atlassian.bamboo.repository.ViewCvsFileLinkGenerator;
import com.atlassian.bamboo.repository.WebRepositoryEnabledRepository;
import com.atlassian.bamboo.security.EncryptionException;
import com.atlassian.bamboo.security.StringEncrypter;
import com.atlassian.bamboo.utils.ConfigUtils;
import com.atlassian.bamboo.utils.error.ErrorCollection;
import com.atlassian.bamboo.v2.build.BuildChanges;
import com.atlassian.bamboo.v2.build.BuildChangesImpl;
import com.atlassian.bamboo.v2.build.BuildContext;
import com.atlassian.bamboo.ww2.actions.build.admin.create.BuildConfiguration;
import com.opensymphony.util.UrlUtils;

import edu.nyu.cs.javagit.api.DotGit;
import edu.nyu.cs.javagit.api.JavaGitException;
import edu.nyu.cs.javagit.api.Ref;
import edu.nyu.cs.javagit.api.commands.GitLog;
import edu.nyu.cs.javagit.api.commands.GitLogOptions;
import edu.nyu.cs.javagit.api.commands.GitLogResponse;
import edu.nyu.cs.javagit.api.commands.GitMerge;
import edu.nyu.cs.javagit.client.cli.CliGitClone;
import edu.nyu.cs.javagit.client.cli.CliGitFetch;

public class GitRepository extends AbstractRepository implements WebRepositoryEnabledRepository, InitialBuildAwareRepository, MutableQuietPeriodAwareRepository
{
    private static final Log log = LogFactory.getLog(GitRepository.class);


    // ------------------------------------------------------------------------------------------------------- Constants
    public static final String NAME = "Git";
    public static final String KEY = "git";

    private static final String REPO_PREFIX = "repository.git.";
    public static final String GIT_REPO_URL = REPO_PREFIX + "repositoryUrl";
    public static final String GIT_USERNAME = REPO_PREFIX + "username";
    public static final String GIT_PASSWORD = REPO_PREFIX + "userPassword";
    public static final String GIT_PASSPHRASE = REPO_PREFIX + "passphrase";
    public static final String GIT_AUTHTYPE = REPO_PREFIX + "authType";
    public static final String GIT_KEYFILE = REPO_PREFIX + "keyFile";
    public static final String GIT_REMOTE_BRANCH = REPO_PREFIX + "remoteBranch";


    private static final String USE_EXTERNALS = REPO_PREFIX + "useExternals";

    private static final String AUTH_SSH = "ssh";
    private static final String PASSWORD_AUTHENTICATION = "password";

    private static final String TEMPORARY_GIT_ADVANCED = "temporary.git.advanced";

    private static final String TEMPORARY_GIT_PASSWORD_CHANGE = "temporary.git.passwordChange";
    private static final String TEMPORARY_GIT_PASSPHRASE_CHANGE = "temporary.git.passphraseChange";
    private static final String TEMPORARY_GIT_PASSWORD = "temporary.git.password";
    private static final String TEMPORARY_GIT_PASSPHRASE = "temporary.git.passphrase";
    private static final String GIT_AUTH_TYPE = "repository.git.authType";

    private static final String EXTERNAL_PATH_MAPPINGS2 = REPO_PREFIX + "externalsToRevisionMappings";


    // ------------------------------------------------------------------------------------------------- Type Properties
    private String repositoryUrl;
    private String webRepositoryUrl;
    private String username;
    private String password;
    private String passphrase;
    private String keyFile;
    private String webRepositoryUrlRepoName;
    private String authType;
    private String remoteBranch;

    // Quiet Period
    private final QuietPeriodHelper quietPeriodHelper = new QuietPeriodHelper(REPO_PREFIX);
    private boolean quietPeriodEnabled = false;
    private int quietPeriod = QuietPeriodHelper.DEFAULT_QUIET_PERIOD;
    private int maxRetries = QuietPeriodHelper.DEFAULT_MAX_RETRIES;

    /**
     * Maps the path to the latest checked revision
     */
    private Map<String, Long> externalPathRevisionMappings = new HashMap<String, Long>();

    // ---------------------------------------------------------------------------------------------------- Dependencies

    private static final ThreadLocal<StringEncrypter> stringEncrypter = new ThreadLocal<StringEncrypter>() {

        protected StringEncrypter initialValue()
        {
            return new StringEncrypter();
        }

    };

    // ---------------------------------------------------------------------------------------------------- Constructors

    // -------------------------------------------------------------------------------------------------- Public Methods

    public void addDefaultValues( BuildConfiguration buildConfiguration)
    {
        super.addDefaultValues(buildConfiguration);
        quietPeriodHelper.addDefaultValues(buildConfiguration);
    }

    
    public synchronized BuildChanges collectChangesSinceLastBuild( String planKey,  String lastVcsRevisionKey) throws RepositoryException
    {
        log.error("determining if there have been changes for " + planKey + " since "+lastVcsRevisionKey);
        try
        {

            String repositoryUrl = getSubstitutedRepositoryUrl();

            File sourceDir = getCheckoutDirectory(planKey);

            DotGit dotGet = fetch(sourceDir, repositoryUrl);
            
            final List<Commit> commits = new ArrayList<Commit>();

            final String latestRevisionOnSvnServer = detectCommitsForUrl(repositoryUrl, lastVcsRevisionKey, commits, planKey);

            String lastRevisionChecked = latestRevisionOnSvnServer;
            log.error("last revision:"+lastRevisionChecked);

            return new BuildChangesImpl(String.valueOf(lastRevisionChecked), commits);
        } catch (IOException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (JavaGitException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    private DotGit fetch(File sourceDir, String repositoryUrl) throws IOException, JavaGitException
    {
        log.error("fetching repo");
        DotGit dotGit = DotGit.getInstance(sourceDir);
        if (!sourceDir.exists()) {
            log.error("no repo found, creating");
            CliGitClone clone = new CliGitClone();
            clone.clone(sourceDir.getParentFile(), repositoryUrl);
            //dotGit.init();
            //CliGitRemote remote = new CliGitRemote();
            //remote.remote(sourceDir, Ref.createBranchRef("origin"), repositoryUrl);
        }
        CliGitFetch fetch = new CliGitFetch();
        log.error("doing fetch");
        fetch.fetch(sourceDir);
        log.error("fetch complete");
        return dotGit;
    }

    private File getCheckoutDirectory(String planKey) throws RepositoryException
    {
        return new File(getSourceCodeDirectory(planKey), "checkout");
    }

    /**
     *
     * Detects the commits for the given repositpry since the revision and HEAD for that URL
     *
     * @param repositoryUrl - the URL to chaeck
     * @param lastRevisionChecked - latest revision checked for this URL. Null if never checked
     * @param commits - the commits are added to this list
     * @param planKey - used for debugging only
     * @return
     */
    
    private String detectCommitsForUrl( String repositoryUrl, final String lastRevisionChecked,  final List<Commit> commits,  String planKey) throws RepositoryException, IOException, JavaGitException
    {
        log.error("detecting commits for "+lastRevisionChecked);
        GitLog gitLog = new GitLog();
        GitLogOptions opt = new GitLogOptions();
        if (lastRevisionChecked != null)
        {
            opt.setOptLimitCommitAfter(true, lastRevisionChecked);
        }
        List<GitLogResponse.Commit> gitCommits = gitLog.log(getCheckoutDirectory(planKey), opt, Ref.createBranchRef("origin/"+remoteBranch));
        if (gitCommits.size() > 1)
        {
            gitCommits.remove(gitCommits.size()-1);
            log.error("commits found:"+gitCommits.size());
            String startRevision = gitCommits.get(gitCommits.size() - 1).getDateString();
            String latestRevisionOnServer = gitCommits.get(0).getDateString();
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
                commit.setAuthor(new AuthorImpl(authorName));
                commit.setDate(new Date(logEntry.getDateString()));
                commit.setComment(logEntry.getMessage());
                List<CommitFile> files = new ArrayList();

                if (logEntry.getFiles() != null) {
                    for (GitLogResponse.CommitFile file : logEntry.getFiles())
                    {
                        CommitFileImpl commitFile = new CommitFileImpl();
                        commitFile.setName(file.getName());
                        commitFile.setRevision(logEntry.getSha());
                        files.add(commitFile);
                    }
                }
                commit.setFiles(files);

                commits.add(commit);
            }
            return latestRevisionOnServer;
        }
        log.error("returning last revision:"+lastRevisionChecked);
        return lastRevisionChecked;
    }

    
    public String retrieveSourceCode( String planKey, String vcsRevisionKey) throws RepositoryException
    {
        log.error("retrieving source code");
        try
        {
            return retreiveSourceCodeWithException(planKey, vcsRevisionKey);
        } catch (IOException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (JavaGitException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    String retreiveSourceCodeWithException(String planKey, String vcsRevisionKey) throws RepositoryException, IOException, JavaGitException
    {
        String repositoryUrl = getSubstitutedRepositoryUrl();

        File sourceDir = getCheckoutDirectory(planKey);

        fetch(sourceDir, repositoryUrl);
        log.error("doing merge");
        GitMerge merge = new GitMerge();

        // FIXME: should really only merge to the target revision
        merge.merge(sourceDir, Ref.createBranchRef("origin/"+remoteBranch));

        return detectCommitsForUrl(repositoryUrl, vcsRevisionKey, new ArrayList<Commit>(), planKey);
    }


    
    public ErrorCollection validate( BuildConfiguration buildConfiguration)
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

        String webRepoUrl = buildConfiguration.getString(WEB_REPO_URL);
        if (!StringUtils.isEmpty(webRepoUrl) && !UrlUtils.verifyHierachicalURI(webRepoUrl))
        {
            errorCollection.addError(WEB_REPO_URL, "This is not a valid url");
        }

        quietPeriodHelper.validate(buildConfiguration, errorCollection);
        log.error("validation results:"+errorCollection);
        return errorCollection;
    }


    public boolean isRepositoryDifferent( Repository repository)
    {
        if (repository != null && repository instanceof GitRepository)
        {
            GitRepository svn = (GitRepository) repository;
            return !new EqualsBuilder()
                    .append(this.getName(), svn.getName())
                    .append(getRepositoryUrl(), svn.getRepositoryUrl())
                    .isEquals();
        }
        else
        {
            return true;
        }
    }

    public void prepareConfigObject( BuildConfiguration buildConfiguration)
    {
        String repositoryKey = buildConfiguration.getString(SELECTED_REPOSITORY);

        String authType = buildConfiguration.getString(GIT_AUTH_TYPE);
        if (PASSWORD_AUTHENTICATION.equals(authType))
        {
            boolean svnPasswordChanged = buildConfiguration.getBoolean(TEMPORARY_GIT_PASSWORD_CHANGE);
            if (svnPasswordChanged)
            {
                String newPassword = buildConfiguration.getString(TEMPORARY_GIT_PASSWORD);
                if (getKey().equals(repositoryKey))
                {
                    buildConfiguration.setProperty(GitRepository.GIT_PASSWORD, stringEncrypter.get().encrypt(newPassword));
                }
            }
        }
        else
        {
            boolean passphraseChanged = buildConfiguration.getBoolean(TEMPORARY_GIT_PASSPHRASE_CHANGE);
            if (passphraseChanged)
            {
                String newPassphrase = buildConfiguration.getString(TEMPORARY_GIT_PASSPHRASE);
                buildConfiguration.setProperty(GitRepository.GIT_PASSPHRASE, stringEncrypter.get().encrypt(newPassphrase));
            }
        }

        // Disabling advanced will clear all advanced
        if (!buildConfiguration.getBoolean(TEMPORARY_GIT_ADVANCED, false))
        {
            quietPeriodHelper.clearFromBuildConfiguration(buildConfiguration);
            buildConfiguration.clearTree(USE_EXTERNALS);
        }
    }

    public void populateFromConfig( HierarchicalConfiguration config)
    {
        super.populateFromConfig(config);

        setRepositoryUrl(config.getString(GIT_REPO_URL));
        setUsername(config.getString(GIT_USERNAME));
        setRemoteBranch(config.getString(GIT_REMOTE_BRANCH));
        setAuthType(config.getString(GIT_AUTHTYPE));
        if (AUTH_SSH.equals(authType))
        {
            setEncryptedPassphrase(config.getString(GIT_PASSPHRASE));
            setKeyFile(config.getString(GIT_KEYFILE));
        }
        else
        {
            setEncryptedPassword(config.getString(GIT_PASSWORD));
        }
        setWebRepositoryUrl(config.getString(WEB_REPO_URL));
        setWebRepositoryUrlRepoName(config.getString(WEB_REPO_MODULE_NAME));

        final Map<String, String> stringMaps = ConfigUtils.getMapFromConfiguration(EXTERNAL_PATH_MAPPINGS2, config);
        externalPathRevisionMappings = ConfigUtils.toLongMap(stringMaps);

        quietPeriodHelper.populateFromConfig(config, this);
    }

    
    public HierarchicalConfiguration toConfiguration()
    {
        HierarchicalConfiguration configuration = super.toConfiguration();
        configuration.setProperty(GIT_REPO_URL, getRepositoryUrl());
        configuration.setProperty(GIT_REMOTE_BRANCH, getRemoteBranch());
        configuration.setProperty(GIT_USERNAME, getUsername());
        configuration.setProperty(GIT_AUTHTYPE, getAuthType());
        if (AUTH_SSH.equals(authType))
        {
            configuration.setProperty(GIT_PASSPHRASE, getEncryptedPassphrase());
            configuration.setProperty(GIT_KEYFILE, getKeyFile());
        }
        else
        {
            configuration.setProperty(GIT_PASSWORD, getEncryptedPassword());
        }
        configuration.setProperty(WEB_REPO_URL, getWebRepositoryUrl());
        configuration.setProperty(WEB_REPO_MODULE_NAME, getWebRepositoryUrlRepoName());

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
    
    public String getName()
    {
        return NAME;
    }

    public String getPassphrase()
    {
        try
        {
            StringEncrypter stringEncrypter = new StringEncrypter();
            return stringEncrypter.decrypt(passphrase);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public void setPassphrase(String passphrase)
    {
        try
        {
            if (StringUtils.isNotEmpty(passphrase))
            {
                StringEncrypter stringEncrypter = new StringEncrypter();
                this.passphrase = stringEncrypter.encrypt(passphrase);
            }
            else
            {
                this.passphrase = passphrase;
            }
        }
        catch (EncryptionException e)
        {
            log.error("Failed to encrypt password", e);
            this.passphrase = null;
        }
    }

    public String getEncryptedPassphrase()
    {
        return passphrase;
    }

    public void setEncryptedPassphrase(String encryptedPassphrase)
    {
        passphrase = encryptedPassphrase;
    }

    public String getKeyFile()
    {
        return keyFile;
    }

    public void setKeyFile(String myKeyFile)
    {
        this.keyFile = myKeyFile;
    }

    public String getAuthType()
    {
        return authType;
    }

    public void setAuthType(String authType)
    {
        this.authType = authType;
    }

    /**
     * Where is the documentation and help about using Subversion?
     *
     * @return The web url
     */
    public String getUrl()
    {
        return "http://subversion.tigris.org/";
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

    /**
     * What's the username (if any) we are using to acces the repository?
     *
     * @param username The user name, null if there is no user
     */
    public void setUsername(String username)
    {
        this.username = StringUtils.trim(username);
    }

    /**
     * What username are we using to access the repository?
     *
     * @return The username, null if we are not using user authentication
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * Specify the password required to access the resposotory
     *
     * @param password The password (null if we are not using user authentication)
     */
    public void setUserPassword(String password)
    {
        try
        {
            if (StringUtils.isNotEmpty(password))
            {
                StringEncrypter stringEncrypter = new StringEncrypter();
                this.password = stringEncrypter.encrypt(password);
            }
            else
            {
                this.password = password;
            }
        }
        catch (EncryptionException e)
        {
            log.error("Failed to encrypt password", e);
            this.password = null;
        }
    }

    /**
     * What password are we using to access the repository
     *
     * @return The password (null if we are not using user authentication)
     */
    public String getUserPassword()
    {
        try
        {
            StringEncrypter stringEncrypter = new StringEncrypter();
            return stringEncrypter.decrypt(password);
        }
        catch (Exception e)
        {
            return null;
        }
    }


    public String getEncryptedPassword()
    {
        return password;
    }

    public void setEncryptedPassword(String encryptedPassword)
    {
        password = encryptedPassword;
    }


    public boolean hasWebBasedRepositoryAccess()
    {
        return StringUtils.isNotBlank(webRepositoryUrl);
    }

    public String getWebRepositoryUrl()
    {
        return webRepositoryUrl;
    }

    public void setWebRepositoryUrl(String url)
    {
        webRepositoryUrl = StringUtils.trim(url);
    }

    public String getWebRepositoryUrlRepoName()
    {
        return webRepositoryUrlRepoName;
    }

    public void setWebRepositoryUrlRepoName(String repoName)
    {
        webRepositoryUrlRepoName = StringUtils.trim(repoName);
    }

    public String getWebRepositoryUrlForFile(CommitFile file)
    {
        ViewCvsFileLinkGenerator fileLinkGenerator = new ViewCvsFileLinkGenerator(webRepositoryUrl);
        return null;//fileLinkGenerator.getWebRepositoryUrlForFile(file, webRepositoryUrlRepoName, ViewCvsFileLinkGenerator.GIT_REPO_TYPE);
    }

    public String getWebRepositoryUrlForDiff(CommitFile file)
    {
        ViewCvsFileLinkGenerator fileLinkGenerator = new ViewCvsFileLinkGenerator(webRepositoryUrl);
        return null;// fileLinkGenerator.getWebRepositoryUrlForDiff(file, webRepositoryUrlRepoName, ViewCvsFileLinkGenerator.GIT_REPO_TYPE);
    }

    public String getWebRepositoryUrlForRevision(CommitFile file)
    {
        ViewCvsFileLinkGenerator fileLinkGenerator = new ViewCvsFileLinkGenerator(webRepositoryUrl);
        return null;// fileLinkGenerator.getWebRepositoryUrlForRevision(file, webRepositoryUrlRepoName, ViewCvsFileLinkGenerator.GIT_REPO_TYPE);
    }

    @Override
    
    public String getWebRepositoryUrlForCommit( Commit commit)
    {
        ViewCvsFileLinkGenerator fileLinkGenerator = new ViewCvsFileLinkGenerator(webRepositoryUrl);
        return null;// fileLinkGenerator.getWebRepositoryUrlForCommit(commit, webRepositoryUrlRepoName, ViewCvsFileLinkGenerator.GIT_REPO_TYPE);
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

    public int hashCode()
    {
        return new HashCodeBuilder(101, 11)
                .append(getKey())
                .append(getRepositoryUrl())
                .append(getUsername())
                .append(getEncryptedPassword())
                .append(getWebRepositoryUrl())
                .append(getWebRepositoryUrlRepoName())
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
                .append(getUsername(), rhs.getUsername())
                .append(getEncryptedPassword(), rhs.getEncryptedPassword())
                .append(getWebRepositoryUrl(), rhs.getWebRepositoryUrl())
                .append(getWebRepositoryUrlRepoName(), rhs.getWebRepositoryUrlRepoName())
                .append(getTriggerIpAddress(), rhs.getTriggerIpAddress())
                .isEquals();
    }

    public int compareTo(Object obj)
    {
        GitRepository o = (GitRepository) obj;
        return new CompareToBuilder()
                .append(getRepositoryUrl(), o.getRepositoryUrl())
                .append(getUsername(), o.getUsername())
                .append(getEncryptedPassword(), o.getEncryptedPassword())
                .append(getWebRepositoryUrl(), o.getWebRepositoryUrl())
                .append(getWebRepositoryUrlRepoName(), o.getWebRepositoryUrlRepoName())
                .append(getTriggerIpAddress(), o.getTriggerIpAddress())
                .toComparison();
    }


}
