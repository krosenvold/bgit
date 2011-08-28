package com.atlassian.labs.bamboo.git;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.atlassian.bamboo.commit.Commit;
import com.atlassian.bamboo.repository.RepositoryException;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.JavaGitException;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.Ref;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.CommandResponse;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitReset;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitResetOptions;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.cli.IParser;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.cli.ProcessUtilities;
import com.atlassian.labs.bamboo.git.model.CommitDescriptor;
import com.atlassian.labs.bamboo.git.model.HardCodedRepo;
import com.atlassian.labs.bamboo.git.model.Sha;

/**
 * @author Kristian Rosenvold
 */
public class GitRepositoryTest extends AbstractTestWithRepo {

    @Test
    public void testClone() throws IOException, JavaGitException {
        GitRepository repo = getGitRepository("feature1");
        getFreshCopyInCheckoutDir(repo, null);
        assertEquals("Repository should be on feature1 branch", "feature1", repo.gitStatus().getName());
    }


    @Test
    public void testCloneWithREquestedSha1() throws IOException, JavaGitException {
        GitRepository repo = getGitRepository("feature1");
        getFreshCopyInCheckoutDir(repo, HardCodedRepo.second_a55e.getSha().getSha());

        assertEquals("Repository should be on feature1 branch", "feature1", repo.gitStatus().getName());
        assertEquals(repo.gitLog(1).get(0).getSha(), HardCodedRepo.second_a55e.getSha().getSha());

        repo.cloneOrFetch(HardCodedRepo.first.getSha().getSha());
        assertEquals(repo.gitLog(1).get(0).getSha(), HardCodedRepo.first.getSha().getSha());
    }


    @Test
    public void testCloneThenMoveHeadThenFetch() throws IOException, JavaGitException {
        GitRepository repo = getGitRepository("feature1");
        getFreshCopyInCheckoutDir(repo, null);

        assertEquals("Repository should be on feature1 branch", "feature1", repo.gitStatus().getName());

        GitResetOptions gitResetOptions = new GitResetOptions(GitResetOptions.ResetType.HARD,
                HardCodedRepo.second_a55e.getShaRef());
        GitReset.gitReset(clone.getCheckoutDirectory(), gitResetOptions);

        repo.cloneOrFetch();
        final Ref ref = repo.gitStatus();
        assertEquals("Repository should be on feature1 branch", "feature1", ref.getName());
    }


    @Test(expected = JavaGitException.class)
    public void testResetWithNonExistantSha() throws IOException, JavaGitException {
        GitRepository repo = getGitRepository("feature1");
        getFreshCopyInCheckoutDir(repo, null);

        assertEquals("Repository should be on feature1 branch", "feature1", repo.gitStatus().getName());

        GitResetOptions gitResetOptions = new GitResetOptions(GitResetOptions.ResetType.HARD,
                Ref.createSha1Ref("ABADCAFECAFEBABEeaa17774dddc4890852396a7"));
        GitReset.gitReset(clone.getCheckoutDirectory(), gitResetOptions);

    }


    @Test
    public void testCloneThenRebaseLocal() throws IOException, JavaGitException {
        // Rebasing local branch should be more or less equivalent to rebasing remote branch.
        // Oops. Brain damaged git repo does not support rebase.

        GitRepository repo = getGitRepository("feature1");
        getFreshCopyInCheckoutDir(repo, null);

        IParser rebaseParser = new IParser() {

            public void parseLine(String line) {
                // To change body of implemented methods use File | Settings | File Templates.
            }


            public void processExitCode(int code) {
                // To change body of implemented methods use File | Settings | File Templates.
            }


            public CommandResponse getResponse() throws JavaGitException {
                return null; // To change body of implemented methods use File | Settings | File
                             // Templates.
            }
        };

        List<String> commandLine = Arrays.asList("git", "rebase", "origin/feature2");
        CommandResponse rebase = ProcessUtilities.runCommand(clone.getCheckoutDirectory(), commandLine, rebaseParser);

        // Todo: Need to assert the head points to a given commit.
        assertEquals("Repository should be on feature1 branch", "feature1", repo.gitStatus().getName());

        repo.cloneOrFetch();
        final Ref ref = repo.gitStatus();
        assertEquals("Repository should be on feature1 branch", "feature1", ref.getName());
    }


    @Test
    public void testCloneThenSeveralBranchChanges() throws IOException, JavaGitException {
        GitRepository repo = getGitRepository("feature1");
        getFreshCopyInCheckoutDir(repo, null);

        assertEquals("Repository should be on feature1 branch", "feature1", repo.gitStatus().getName());

        repo.setRemoteBranchName("aBranch");
        repo.cloneOrFetch();
        assertEquals("Repository should be on feature1 branch", "aBranch", repo.gitStatus().getName());

        // Switch back to
        repo.setRemoteBranchName("feature1");
        repo.cloneOrFetch();
        assertEquals("Repository should be on feature1 branch", "feature1", repo.gitStatus().getName());
    }


    @Test
    public void testCloneDefault() throws IOException, JavaGitException {
        GitRepository repo = getGitRepository(null);
        getFreshCopyInCheckoutDir(repo, null);
        assertEquals("featureDefault", repo.gitStatus().getName());
        assertTrue(repo.isOnBranch(clone.getCheckoutDirectory(), Ref.createBranchRef("featureDefault")));
     }


    @Test
    public void testHistory() throws IOException, JavaGitException, RepositoryException {
        GitRepository repo = getGitRepository("featureDefault");
        getFreshCopyInCheckoutDir(repo, null);

        List<Commit> results = new ArrayList<Commit>();
        repo.detectCommitsForUrl(HardCodedRepo.first.getSha().getSha(), results);
        final CommitDescriptor commitDescriptor = HardCodedRepo.getBranchPointerFeatureDefault();

        System.out.println("commitDescriptor = "
                + commitDescriptor.collectNodesInRealGitLogOrder(HardCodedRepo.first.getSha()).toString());
        commitDescriptor.assertHistoryMatch(results, HardCodedRepo.first.getSha());
    }


    @Test
    public void testCollectChangesFromNonExistingSha1() throws IOException, JavaGitException, RepositoryException {
        GitRepository repo = getGitRepository("featureDefault");
        getFreshCopyInCheckoutDir(repo, null);

        List<Commit> results = new ArrayList<Commit>();

        final String s = repo.detectCommitsForUrl(HardCodedRepo.NONEXISTANT_SHA1.getSha().getSha(), results);
        assertEquals(HardCodedRepo.COMMIT_Merge_aBranch_featureDefault.getSha().getSha(), s);
        // This is a bit of a weird assert since I do not exactly understand why it gives me 10 items.
        assertEquals(10, results.size());
    }


    @Test
    public void testHistoryWithMergeCommit() throws IOException, JavaGitException, RepositoryException {
        GitRepository repo = getGitRepository("featureDefault");
        getFreshCopyInCheckoutDir(repo, null);

        List<Commit> results = new ArrayList<Commit>();
        repo.detectCommitsForUrl(HardCodedRepo.getFristCommitInBranch().getSha().getSha(), results);

        assertEquals(7, results.size());
    }


    @Test
    public void testHistoryFeature1() throws IOException, JavaGitException, RepositoryException {
        GitRepository repo = getGitRepository("feature1");
        getFreshCopyInCheckoutDir(repo, null);

        List<Commit> results = new ArrayList<Commit>();
        final Sha untilSha = HardCodedRepo.getRootCommit().getSha();
        repo.detectCommitsForUrl(untilSha.getSha(), results);

        HardCodedRepo.getFeature1Head().assertHistoryMatch(results, untilSha);
    }


    @Test
    public void testHistoryFeature2() throws IOException, JavaGitException, RepositoryException {
        GitRepository repo = getGitRepository("feature2");
        getFreshCopyInCheckoutDir(repo, null);

        List<Commit> results = new ArrayList<Commit>();
        final Sha untilSha = HardCodedRepo.getRootCommit().getSha();
        repo.detectCommitsForUrl(untilSha.getSha(), results);
        HardCodedRepo.getFeature2Head().assertHistoryMatch(results, untilSha);
    }


    @Test
    public void testNonLinearHistory() throws IOException, JavaGitException, RepositoryException {
        GitRepository repo = getGitRepository("featureDefault");
        getFreshCopyInCheckoutDir(repo, null);

        List<Commit> results = new ArrayList<Commit>();

        repo.detectCommitsForUrl(HardCodedRepo.COMMIT_fb65.getSha().getSha(), results);
        assertEquals(5, results.size());

        results = new ArrayList<Commit>();
        repo.detectCommitsForUrl(HardCodedRepo.COMMIT_2d9b.getSha().getSha(), results);
        HardCodedRepo.getBranchPointerFeatureDefault().assertHistoryMatch(results, HardCodedRepo.COMMIT_2d9b.getSha());

        results = new ArrayList<Commit>();
        repo.detectCommitsForUrl(HardCodedRepo.COMMIT_3a45.getSha().getSha(), results);
        HardCodedRepo.getBranchPointerFeatureDefault().assertHistoryMatch(results, HardCodedRepo.COMMIT_3a45.getSha());
    }


    @Test
    public void testPluginUpgrade() throws IOException, JavaGitException, RepositoryException {
        GitRepository repo = getGitRepository("featureDefault");
        getFreshCopyInCheckoutDir(repo, null);

        List<Commit> results = new ArrayList<Commit>();
        repo.detectCommitsForUrl("Fri Oct 9 15:38:10 2009 +0200", results);

        assertEquals(8, results.size());
    }


    @Test
    public void testCloneNonExistingPrevious() throws IOException, JavaGitException, RepositoryException {
        GitRepository repo = getGitRepository("feature1");
        getFreshCopyInCheckoutDir(repo, null);
        List<Commit> results = new ArrayList<Commit>();
        repo.detectCommitsForUrl("e3bed58f697792d6e603c4c4a90cad1e9326a053", results);
    }


    @Test
    public void testgetSha1FromCommitDate() throws IOException, JavaGitException, RepositoryException {
        GitRepository repo = getGitRepository("feature1");
        getFreshCopyInCheckoutDir(repo, null);
        File sourceDir = clone.getCheckoutDirectory();

        String commit = repo.getSha1FromCommitDate("Fri Oct 9 15:38:10 2009 +0200", sourceDir);
        assertEquals("a55e4702a0fdc210eaa17774dddc4890852396a7", commit);

        commit = repo.getSha1FromCommitDate("Fri Oct 19 22:38:10 2009 +0200", sourceDir);// Fake
        assertEquals("84965cc8dfc8af7fca02c78373413aceafc73c2f", commit);

        commit = repo.getSha1FromCommitDate("YABBA", sourceDir);// Fake
        assertEquals("84965cc8dfc8af7fca02c78373413aceafc73c2f", commit);

    }


    @Test
    public void testLastCheckedRevisionIsNull() throws IOException, JavaGitException, RepositoryException {
        GitRepository repo = getGitRepository("featureDefault");
        getFreshCopyInCheckoutDir(repo, null);

        List<Commit> results = new ArrayList<Commit>();
        repo.detectCommitsForUrl(null, results);

        assertEquals(10, results.size());
    }


    private void getFreshCopyInCheckoutDir(GitRepository repo, String ref) throws IOException, JavaGitException {
        final File sourceDir = clone.getCheckoutDirectory();

        assertFalse(GitRepository.containsValidRepo(sourceDir));
        repo.cloneOrFetch(ref);
        assertTrue(GitRepository.containsValidRepo(sourceDir));
    }


    private GitRepository getGitRepository(String remoteBranch) throws IOException {
        GitRepository repo = new GitRepository(master.getCheckoutDirectory().getAbsolutePath(),
                this.clone.getCheckoutDirectory(), remoteBranch);
        return repo;
    }

}
