/**
 *
 */
package com.atlassian.labs.bamboo.git;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atlassian.bamboo.author.Author;
import com.atlassian.bamboo.author.AuthorImpl;
import com.atlassian.bamboo.commit.Commit;
import com.atlassian.bamboo.commit.CommitFile;
import com.atlassian.bamboo.commit.CommitFileImpl;
import com.atlassian.bamboo.commit.CommitImpl;
import com.atlassian.bamboo.repository.RepositoryException;
import com.atlassian.bamboo.v2.build.BuildChanges;
import com.atlassian.bamboo.v2.build.BuildChangesImpl;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.JavaGitException;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.Ref;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitBranch;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitBranchOptions;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitBranchResponse;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitCheckout;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitCheckoutOptions;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitCloneOptions;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitLog;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitLogOptions;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitLogResponse;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitReset;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitResetOptions;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitStatus;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitStatusOptions;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitStatusResponse;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.cli.CliGitClone;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.cli.CliGitFetch;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.cli.CliGitSubmodule;

/**
 * @author David Matějček
 */
public class GitRepository {

    private final Log log = LogFactory.getLog(GitRepository.class);

    private static final Pattern EMAIL_PATTERN = Pattern.compile("[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}");

    private String repoUrl;
    private File checkoutDirectory;
    private String remoteBranchName;
    private boolean hideEmails;


    public static boolean containsValidRepo(File sourceDir) throws IOException {
        return sourceDir.exists() && (new File(sourceDir, ".git").exists() || new File(sourceDir, "HEAD").exists());
    }


    public GitRepository(String repoUrl, File checkoutDirectory) {
        this(repoUrl, checkoutDirectory, null);
    }


    public GitRepository(String repoUrl, File checkoutDirectory, String remoteBranchName) {
        log.debug("GitRepository(checkoutDirectory=" + checkoutDirectory + ", remoteBranchName=" + remoteBranchName
                + ")");
        this.repoUrl = repoUrl;
        this.checkoutDirectory = checkoutDirectory;
        this.remoteBranchName = remoteBranchName;
        // turning off is not implemented, maybe old or svn implementation?
        this.hideEmails = true;
    }


    public void setRemoteBranchName(String remoteBranchName) {
        this.remoteBranchName = remoteBranchName;
    }


    public synchronized BuildChanges getChangesSinceLastBuild(String planKey, String lastRevisionKey)
        throws RepositoryException {
        try {
            cloneOrFetch();

            final List<Commit> commits = new ArrayList<Commit>();
            final String latestRevision = detectCommitsForUrl(lastRevisionKey, commits);
            log.debug("last revision: " + latestRevision);

            return new BuildChangesImpl(String.valueOf(latestRevision), commits);

        } catch (IOException e) {
            throw new RepositoryException("collectChangesSinceLastBuild", e);
        } catch (JavaGitException e) {
            throw new RepositoryException("collectChangesSinceLastBuild", e);
        }

    }


    /**
     * Clones or fetches the specified repository.
     * This method supports exactly 2 use cases:
     * A) A clone of a repository. When cloning, the proper branch is checked if it is not correct
     * by default.
     * B) A fetch. Since the repo is created by use case A, it will always be on the proper branch.
     * Branch switching is supported.
     *
     * @param sourceDir The checkout directory
     * @throws IOException When something bad happens
     * @throws JavaGitException When something else bad happens.
     */
    void cloneOrFetch() throws IOException, JavaGitException {
        reallyCloneOrFetch(null);
    }


    void cloneOrFetch(String requestedVersion) throws IOException, JavaGitException {
        reallyCloneOrFetch(isSha1(requestedVersion) ? Ref.createSha1Ref(requestedVersion) : null);
    }


    void reallyCloneOrFetch(Ref requestedTargetRevision) throws IOException, JavaGitException {
        Ref branchWithOriginPrefix = Ref.createBranchRef("origin/" + this.remoteBranchName);

        if (containsValidRepo(this.checkoutDirectory)) {
            CliGitFetch fetch = new CliGitFetch();
            log.debug("doing fetch");
            fetch.fetch(this.checkoutDirectory);

            final Ref currentCheckoutBranch = gitStatus();
            if (this.remoteBranchName != null) {
                if (!branchWithOriginPrefix.isThisBranch(currentCheckoutBranch)) {
                    GitBranchResponse branchList = getAllBranches(this.checkoutDirectory);
                    final Ref branch = Ref.createBranchRef(this.remoteBranchName);
                    if (!branchList.containsExactBranchMatch(branch)) {
                        checkout(this.checkoutDirectory, branchWithOriginPrefix, branch);
                        return; // No need to reset here.
                    }
                    checkoutExistingLocalBranch(this.checkoutDirectory, branch);
                }

            }
        } else {
            log.debug("no repo found, creating new clone");
            clone(this.repoUrl, this.checkoutDirectory, null);
            submodule_update();

            if (this.remoteBranchName != null) {
                Ref desiredBranch = Ref.createBranchRef(this.remoteBranchName);
                GitBranchResponse branchList = getAllBranches(this.checkoutDirectory);
                boolean branchFound = branchList.containsBranch(branchWithOriginPrefix);
                if (!branchFound) {
                    throw new JavaGitException(12, "The branch " + branchWithOriginPrefix.getName() + " does not exist");
                }
                if (!branchList.getCurrentBranch().equals(desiredBranch)) {
                    checkout(this.checkoutDirectory, branchWithOriginPrefix, desiredBranch);
                }
            }
        }
        // At this point the proper branch is checked out or created. NO matter which path is used.

        final Ref currentCheckoutBranch = gitStatus();
        if (requestedTargetRevision == null) {
            requestedTargetRevision = Ref.createRemoteRef("origin", currentCheckoutBranch.getName());
        }

        log.debug("resetting local branch to point at " + requestedTargetRevision);
        boolean result = tryGitReset(this.checkoutDirectory, requestedTargetRevision, false);
        if (!result) {
            log.warn("Had problem resetting head, trying " + branchWithOriginPrefix.getName());
            tryGitReset(this.checkoutDirectory, branchWithOriginPrefix, true);
        }
    }


    private boolean tryGitReset(File sourceDir, Ref ref, boolean rethrowOnError)
        throws IOException, JavaGitException {
        log.debug("tryGitReset(sourceDir=" + sourceDir + ", ref=" + ref + ", rethrowOnError="
                + rethrowOnError + ")");
        try {
            GitResetOptions options = new GitResetOptions(GitResetOptions.ResetType.HARD, ref);
            GitReset.gitReset(sourceDir, options);
            return true;
        } catch (JavaGitException e) {
            log.warn("Had problem resetting ref='" + ref + "' in dir='" + sourceDir + "'" , e);
            if (rethrowOnError) {
                throw e;
            }
            return false;
        }
    }


    public String getLastRevision(String lastRevisionChecked) throws RepositoryException, IOException, JavaGitException {
        return detectCommitsForUrl(lastRevisionChecked, new ArrayList<Commit>());
    }

    /**
     * Detects the commits for the given repository since the revision and HEAD for that URL
     *
     * @param lastRevisionChecked - latest revision checked for this URL. Null if never checked
     * @param commits - the commits are added to this list
     * @return The date/time of the last commit found.
     * @throws RepositoryException when something goes wrong
     * @throws IOException when something goes wrong
     * @throws JavaGitException when something goes wrong
     */
    public String detectCommitsForUrl(String lastRevisionChecked, final List<Commit> commits)
        throws RepositoryException, IOException, JavaGitException {
        log.debug("detecting commits for lastRevisionChecked=" + lastRevisionChecked);

        if (isANonSha1RevisionSpecifier(lastRevisionChecked)) {
            lastRevisionChecked = getSha1FromCommitDate(lastRevisionChecked, this.checkoutDirectory);
        }
        if (isANonSha1RevisionSpecifier(lastRevisionChecked)) {
            throw new RepositoryException("lastRevisionedChecked must be a SHA hash.  lastRevisionChecked="
                    + lastRevisionChecked);
        }

        GitLog gitLog = new GitLog();
        GitLogOptions opt = new GitLogOptions();
        if (lastRevisionChecked != null) {
            opt.setOptLimitCommitRange(true, lastRevisionChecked, "HEAD");
        }

        opt.setOptFileDetails(true);
        List<GitLogResponse.Commit> gitCommits;
        try {
            gitCommits = gitLog.log(this.checkoutDirectory, opt);
        } catch (JavaGitException e) {
            // Typically because the sha1 does not exist. Rebase has happened.

            // Todo: In the checkout, if there is a checkout and it is diverged from origin/branch,
            // we could detect the git merge-base and diff from there
            /*
             * wereHamster said:
             * to see if origin/bax has been rebased, do git fetch origin; test
             * "$(git rev-parse origin/bax..origin/baz@{1})" && echo "origin/baz has been
             * rebased"
             */
            // We *should* do rebase-detection somewhere in the collectChangesSinceLastBuild, since
            // the server will always be able to tell,
            // since it always has the history from the previous build.

            // Important note; we always need something here<

            gitCommits = getDefaultLogWhenWeDontKnowWhatElsetoDo(this.checkoutDirectory, gitLog);
        }

        if (gitCommits.isEmpty()) {
            log.debug("No change detected, returning previous last revision:" + lastRevisionChecked);
            return lastRevisionChecked;
        }

        log.debug("commits found: " + gitCommits.size());
        String startRevision = gitCommits.get(gitCommits.size() - 1).getSha();
        String latestRevisionOnServer = gitCommits.get(0).getSha();
        log.info("Collecting changes for repo '" + this.checkoutDirectory + "' on origin URL '" + this.repoUrl
                + "' from version " + startRevision + " to " + latestRevisionOnServer);

        for (GitLogResponse.Commit logEntry : gitCommits) {
            CommitImpl commit = new CommitImpl();
            String authorName = logEntry.getAuthor();

            // it is possible to have commits with empty committer. BAM-2945
            if (StringUtils.isBlank(authorName)) {
                log.info("Author name is empty for " + commit.toString());
                authorName = Author.UNKNOWN_AUTHOR;
            }

            if (this.hideEmails) {
                authorName = EMAIL_PATTERN.matcher(authorName).replaceFirst("");
                authorName.trim();
            }

            commit.setAuthor(new AuthorImpl(authorName));
            @SuppressWarnings({"deprecation"})
            Date date2 = new Date(logEntry.getDateString());
            commit.setDate(date2);

            String msg = logEntry.getMessage() + " (version " + logEntry.getSha() + ")";
            commit.setComment(msg);
            List<CommitFile> files = new ArrayList<CommitFile>();

            if (logEntry.getFiles() != null) {
                for (GitLogResponse.CommitFile file : logEntry.getFiles()) {
                    CommitFileImpl commitFile = new CommitFileImpl();
                    commitFile.setName(file.getName());
                    commitFile.setRevision(logEntry.getSha());
                    files.add(commitFile);
                }
            }
            if (files.size() == 0) { // No files, add a dummy file to keep version number
                CommitFileImpl commitFile = new CommitFileImpl();
                commitFile.setName(".");
                commitFile.setRevision(logEntry.getSha());
                files.add(commitFile);
            }

            commit.setFiles(files);
            commits.add(commit);
        }
        log.debug("Repository change detected for " + this.repoUrl + ", returning " + latestRevisionOnServer);
        return latestRevisionOnServer;

    }


    String getSha1FromCommitDate(String lastRevisionChecked, File checkoutDir) throws JavaGitException, IOException,
        RepositoryException {
        GitLog gitLog = new GitLog();
        GitLogOptions opt = new GitLogOptions();
        opt.setOptLimitCommitAfter(true, lastRevisionChecked);
        opt.setOptFileDetails(true);
        List<GitLogResponse.Commit> candidateGitCommits = null;
        try {
            candidateGitCommits = gitLog.log(checkoutDir, opt, Ref.createBranchRef("origin/" + this.remoteBranchName));
        } catch (JavaGitException e) {
            candidateGitCommits = getDefaultLogWhenWeDontKnowWhatElsetoDo(checkoutDir, gitLog);
            return candidateGitCommits.get(0).getSha(); // #fail, just take the most recent one
        }

        if (candidateGitCommits.size() < 1) {
            candidateGitCommits = getDefaultLogWhenWeDontKnowWhatElsetoDo(checkoutDir, gitLog);
            // We're just guessing, do an old one
            return candidateGitCommits.get(candidateGitCommits.size() - 1).getSha();
        }
        for (GitLogResponse.Commit commit : candidateGitCommits) {
            if (commit.getDateString().equals(lastRevisionChecked)) {
                log.info("Converting lastRevisionChecked from Date into SHA hash");
                return commit.getSha();
            }
        }
        log.info("lastRevisionChecked " + lastRevisionChecked
                + " did not look like a sha1, but did not match a commit date. This may happen if the commit is gone");
        return lastRevisionChecked;
    }


    void clone(File sourceDir, GitCloneOptions gitCloneOptions) throws IOException, JavaGitException {
        clone(this.repoUrl, sourceDir, gitCloneOptions);
    }


    void clone(String repositoryUrl, File sourceDir, GitCloneOptions gitCloneOptions) throws IOException,
        JavaGitException {
        ensureDirExists(sourceDir);
        CliGitClone clone = new CliGitClone();
        if (sourceDir.exists()) {
            sourceDir.delete();
        }
        File parentDir = sourceDir.getParentFile();
        String parentDirS = parentDir.getPath();
        String checkoutDir = sourceDir.getPath().substring(parentDirS.length() + 1);
        if (gitCloneOptions == null) {
            gitCloneOptions = new GitCloneOptions();
        }
        clone.clone(parentDir, gitCloneOptions, repositoryUrl, new File(checkoutDir));
    }


    static void ensureDirExists(File dir) {
        if (!dir.exists()) {
            // noinspection ResultOfMethodCallIgnored
            dir.mkdir();
        }
    }


    private void checkout(File sourceDir, Ref remoteBranch, Ref localBranch) throws IOException, JavaGitException {
        log.debug("checkout(sourceDir=" + sourceDir + ", remoteBranch=" + remoteBranch + ", localBranch=" + localBranch
                + ")");
        GitCheckout gitCheckout = new GitCheckout();
        GitCheckoutOptions options = new GitCheckoutOptions();
        options.setOptB(localBranch);
        gitCheckout.checkout(sourceDir, options, remoteBranch);
    }


    private void checkoutExistingLocalBranch(File sourceDir, Ref localBranch) throws IOException, JavaGitException {
        GitCheckout gitCheckout = new GitCheckout();
        GitCheckoutOptions options = new GitCheckoutOptions();
        gitCheckout.checkout(sourceDir, options, localBranch);
    }


    public void submodule_update() throws IOException, JavaGitException {
        log.debug("doing submodule update; sourceDir=" + this.checkoutDirectory);
        CliGitSubmodule submodule = new CliGitSubmodule();
        submodule.init(this.checkoutDirectory);
        submodule.update(this.checkoutDirectory);
    }


    public List<GitLogResponse.Commit> gitLog(int numItems) throws IOException, JavaGitException {
        GitLog gitLog = new GitLog();
        GitLogOptions opt = new GitLogOptions();
        opt.setOptLimitCommitOutputs(true, numItems);
        opt.setOptFileDetails(true);
        return gitLog.log(this.checkoutDirectory, opt);
    }


    public Ref gitStatus() throws IOException, JavaGitException {
        GitStatusResponse response = getGitStatusResponse(this.checkoutDirectory);
        return response.getBranch();
    }


    private GitStatusResponse getGitStatusResponse(File sourceDir) throws JavaGitException, IOException {
        GitStatus gitStatus = new GitStatus();
        GitStatusOptions gitStatusOptions = new GitStatusOptions();
        return gitStatus.status(sourceDir, gitStatusOptions);
    }


    boolean isOnBranch(File sourceDir, Ref branchName) throws IOException, JavaGitException {
        GitBranchResponse response = getAllBranches(sourceDir);
        return response.getCurrentBranch().equals(branchName);
    }


    private GitBranchResponse getAllBranches(File sourceDir) throws IOException, JavaGitException {
        GitBranch gitBranch = new GitBranch();
        GitBranchOptions gitBranchOptions = new GitBranchOptions();
        gitBranchOptions.setOptA(true);
        return gitBranch.branch(sourceDir, gitBranchOptions);
    }


    private List<GitLogResponse.Commit> getDefaultLogWhenWeDontKnowWhatElsetoDo(File checkoutDir, GitLog gitLog)
        throws JavaGitException, IOException {
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

}
