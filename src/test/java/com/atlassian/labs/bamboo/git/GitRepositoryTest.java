package com.atlassian.labs.bamboo.git;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import com.atlassian.bamboo.commit.CommitFile;
import edu.nyu.cs.javagit.api.JavaGitException;
import edu.nyu.cs.javagit.api.Ref;
import edu.nyu.cs.javagit.api.commands.GitCloneOptions;
import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.After;
import static org.junit.Assert.*;
import com.atlassian.bamboo.commit.Commit;
import com.atlassian.bamboo.repository.RepositoryException;

/**
 * @author Kristian Rosenvold
 */
public class GitRepositoryTest
{
    private static String getGitHubRepoUrl() {
        return "git://github.com/krosenvold/bgit-unittest.git";
    }


    @BeforeClass
    public static void getFromGitHub() throws IOException, JavaGitException {
        final File localRepo = getMasterRepoCheckoutDirectory();
        if ( !GitRepository.containsValidRepo(localRepo)){
            GitCloneOptions gitCloneOptions = new GitCloneOptions(false, false, true);
            GitRepository.clone( getGitHubRepoUrl(), localRepo, gitCloneOptions);
        }
    }


    @After
    public void deleteWorkingCopy() throws IOException, JavaGitException {
        deleteDir( getWorkingCopyDir());
    }


    @Test
    public void testClone() throws IOException, JavaGitException {
        GitRepository gitRepository = getGitRepository("feature1");
        File sourceDir = getFreshCheckoutDir();
        
        assertFalse( GitRepository.containsValidRepo( sourceDir));
        gitRepository.cloneOrFetch(sourceDir);
        assertTrue( GitRepository.containsValidRepo( sourceDir));

        assertEquals("Repository should be on feature1 branch", "feature1", gitRepository.gitStatus(sourceDir).getName());
    }

    private File getFreshCheckoutDir() {
        return getCheckoutDirectory(getFreshWorkingCopyDir());
    }


    @Test
    public void testCloneDefault() throws IOException, JavaGitException {
        GitRepository gitRepository = getGitRepository(null);
        File sourceDir = getFreshCheckoutDir();
        gitRepository.cloneOrFetch(sourceDir);
        assertEquals("featureDefault", gitRepository.gitStatus(sourceDir).getName());
    }

    @Test
    public void testIsOnBranch() throws IOException, JavaGitException {
        GitRepository gitRepository = getGitRepository( null);
        File sourceDir = getFreshCopyInCheckoutDir(gitRepository);

        assertTrue(gitRepository.isOnBranch(sourceDir, Ref.createBranchRef("featureDefault")));
        assertFalse(gitRepository.isOnBranch(sourceDir, Ref.createBranchRef("feature1")));
    }


    @Test
    public void testHistory() throws IOException, JavaGitException, RepositoryException {
        GitRepository gitRepository = getGitRepository( "featureDefault");
        File sourceDir = getFreshCopyInCheckoutDir(gitRepository);

        List<com.atlassian.bamboo.commit.Commit> results = new ArrayList<Commit>();
        gitRepository.detectCommitsForUrl(COMMIT_1_Initial.getSha().getSha(), results, sourceDir, "UT-KEY");

        assertHistoryMatch( results, getFeatureDefault(), 1);
    }

    @Test
    public void testHistoryFeature1() throws IOException, JavaGitException, RepositoryException {
        GitRepository gitRepository = getGitRepository( "feature1");
        File sourceDir = getFreshCopyInCheckoutDir(gitRepository);

        List<com.atlassian.bamboo.commit.Commit> results = new ArrayList<Commit>();
        gitRepository.detectCommitsForUrl(COMMIT_1_Initial.getSha().getSha(), results, sourceDir, "UT-KEY");

        assertHistoryMatch( results, getFeature1(), 1);
    }
    @Test
    public void testHistoryFeature2() throws IOException, JavaGitException, RepositoryException {
        GitRepository gitRepository = getGitRepository( "feature2");
        File sourceDir = getFreshCopyInCheckoutDir(gitRepository);

        List<com.atlassian.bamboo.commit.Commit> results = new ArrayList<Commit>();
        gitRepository.detectCommitsForUrl(COMMIT_1_Initial.getSha().getSha(), results, sourceDir, "UT-KEY");

        assertHistoryMatch( results, getFeature2(), 1);
    }

    @Test
    public void testNonLinearHistory() throws IOException, JavaGitException, RepositoryException {
        GitRepository gitRepository = getGitRepository( "featureDefault");
        File sourceDir = getFreshCopyInCheckoutDir(gitRepository);

        List<com.atlassian.bamboo.commit.Commit> results = new ArrayList<Commit>();

        gitRepository.detectCommitsForUrl(COMMIT_7_featureDefault1.getSha().getSha(), results, sourceDir, "UT-KEY");
        assertEquals(0, results.size());

        results = new ArrayList<Commit>();
        gitRepository.detectCommitsForUrl(COMMIT_6_featureDefault1.getSha().getSha(), results, sourceDir, "UT-KEY");
        COMMIT_7_featureDefault1.assertMatch( results.get(0));

        results = new ArrayList<Commit>();
        gitRepository.detectCommitsForUrl(COMMIT_5_featureDefault1.getSha().getSha()  , results, sourceDir, "UT-KEY");
        assertHistoryMatch( results, getFeatureDefault(), 3);
        COMMIT_7_featureDefault1.assertMatch( results.get(0));
    }

    @Test
    public void testPluginUpgrade() throws IOException, JavaGitException, RepositoryException {
        GitRepository gitRepository = getGitRepository( "featureDefault");
        File sourceDir = getFreshCopyInCheckoutDir(gitRepository);

        List<com.atlassian.bamboo.commit.Commit> results = new ArrayList<Commit>();
        gitRepository.detectCommitsForUrl("Fri Oct 9 15:38:10 2009 +0200", results, sourceDir, "UT-KEY");

        assertEquals(3, results.size());
    }

    @Test
    public void testLastCheckedRevisionIsNull() throws IOException, JavaGitException, RepositoryException {
        GitRepository gitRepository = getGitRepository("featureDefault");
        File sourceDir = getFreshCopyInCheckoutDir(gitRepository);

        List<com.atlassian.bamboo.commit.Commit> results = new ArrayList<Commit>();
        gitRepository.detectCommitsForUrl(null, results, sourceDir, "UT-KEY");

        assertEquals(5, results.size());
    }

    private static final CommitDescriptor COMMIT_1_Initial = new CommitDescriptor(new Sha("84965cc8dfc8af7fca02c78373413aceafc73c2f"), "Comments");
    private static final CommitDescriptor COMMIT_2_masterHead =  new CommitDescriptor(new Sha("a55e4702a0fdc210eaa17774dddc4890852396a7"), "File3.txt");
    private static final CommitDescriptor COMMIT_3_Feature1 =  new CommitDescriptor(new Sha("b3035918b551a0cbd72a242e5d00442a1bb59dbe"), "FileF1.txt");
    private static final CommitDescriptor COMMIT_4_Feature2 =  new CommitDescriptor(new Sha("e352af2f992d9c5f064b24ac7c0af87b4f7c959f"), "FileFeature2.txt");
    private static final CommitDescriptor COMMIT_5_featureDefault1 =  new CommitDescriptor(new Sha("2d9b1997d64fa9501a0e4dec26cc9a07e3e8247f"), "OnDefault.txt");
    private static final CommitDescriptor COMMIT_6_featureDefault1 =  new CommitDescriptor(new Sha("3a450411d6868221ae290bc0c17695de2990d5d8"), "File4.txt");
    private static final CommitDescriptor COMMIT_7_featureDefault1 =  new CommitDescriptor(new Sha("fb6562c90de470294b879655a14640ab454ff2ae"), "File5.txt");


    public List<CommitDescriptor> getMasterBranch(){
        return new ArrayList<CommitDescriptor>( Arrays.asList(COMMIT_1_Initial, COMMIT_2_masterHead));
    }

    public List<CommitDescriptor> getFeature1(){
        final List<CommitDescriptor> result = getMasterBranch();
        result.add( COMMIT_3_Feature1);
        return result;
    }
    public List<CommitDescriptor> getFeature2(){
        final List<CommitDescriptor> result = getMasterBranch();
        result.add( COMMIT_4_Feature2);
        return result;
    }
    public List<CommitDescriptor> getFeatureDefault(){
        final List<CommitDescriptor> result = getMasterBranch();
        result.addAll( Arrays.asList( COMMIT_5_featureDefault1, COMMIT_6_featureDefault1, COMMIT_7_featureDefault1));
        return result;
    }

    private static File getMasterRepoWorkingDirectory() {
        File masterRepoDir = new File("masterRepo");
        ensureDirExists(masterRepoDir);
        return masterRepoDir;
    }

    private static File getWorkingCopyDir() {
        return new File("testRepo");
    }

    
    private static File getMasterRepoCheckoutDirectory() {
        return getMasterRepoCheckoutDirectory(getMasterRepoWorkingDirectory().getPath());
    }
    private static File getCWDRelativeMasterRepoCheckoutDirectory() {
        return getMasterRepoCheckoutDirectory(".." + File.separator + getMasterRepoWorkingDirectory().getPath());
    }
    private static File getMasterRepoCheckoutDirectory(String localPart) {
        try {
            final File file = new File(localPart + File.separator + GitRepository.getLocalCheckoutSubfolder());
            ensureDirExists( file);
            return file;
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }


    private static File getCheckoutDirectory(File workingDirectory){
        try {
            return new File(workingDirectory.getCanonicalPath() + File.separator + GitRepository.getLocalCheckoutSubfolder());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static File getFreshWorkingCopyDir() {
        File workingCopyDir = new File("testRepo");
        if (workingCopyDir.exists()) deleteDir( workingCopyDir);
        ensureDirExists(workingCopyDir);
        return workingCopyDir;
    }

    private static void ensureDirExists(File workingCopyDir) {
        if (!workingCopyDir.exists()){
            //noinspection ResultOfMethodCallIgnored
            workingCopyDir.mkdir();
        }
    }


    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (String child : children) {
                boolean success = deleteDir(new File(dir, child));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }
    /**
     * Checks that the received history matches the supplied history.
     * Please note that the bamboo commit log is reverse ordered of the tree-ordered commit log.
     * @param commits The bamboo commit log
     * @param commitDescriptors The tree-ordered commit log
     * @param startAtCommit The 0 based index into commitDescriptors to start at
     */
    private void assertHistoryMatch(List<com.atlassian.bamboo.commit.Commit> commits, List<CommitDescriptor> commitDescriptors, int startAtCommit){
           assertEquals( "Expect history sized to be identical" , commitDescriptors.size() - startAtCommit, commits.size() );
        int numCommits = commits.size();
        for ( int i = 0 ; i < numCommits ; i++){
            Commit commit = commits.get(numCommits - (i +1));
            CommitDescriptor commitDescriptor = commitDescriptors.get( startAtCommit + i);
            commitDescriptor.assertMatch( commit);
        }

    }


     private File getFreshCopyInCheckoutDir(GitRepository gitRepository) throws IOException, JavaGitException {
        final File directory = getCheckoutDirectory(getFreshWorkingCopyDir());
//        GitCloneOptions gitCloneOptions = new GitCloneOptions(false, true, false);
        gitRepository.cloneOrFetch( directory);
        return directory;
    }


    private GitRepository getGitRepository(String remoteBranch) throws IOException {
        return new GitRepository(getCWDRelativeMasterRepoCheckoutDirectory().getPath(), remoteBranch);
    }
    
    static class Sha {
        private final String sha;

        Sha(String sha) {
            this.sha = sha;
        }

        public String getSha() {
            return sha;
        }
    }
    static class CommitDescriptor {
        private final Sha sha;
        private final String expectedFile;

        CommitDescriptor(Sha sha, String expectedFile) {
            this.sha = sha;
            this.expectedFile = expectedFile;
        }

        public Sha getSha() {
            return sha;
        }


        public void assertMatch(com.atlassian.bamboo.commit.Commit commit){
            final CommitFile commitFile = getCommitFile(commit, expectedFile);
            assertEquals( sha.getSha(), commitFile.getRevision());
        }

        private com.atlassian.bamboo.commit.CommitFile getCommitFile(com.atlassian.bamboo.commit.Commit commit, String file){
            for (CommitFile commitFile : commit.getFiles()){
                if (commitFile.getName().equals( file))
                    return commitFile;
            }
            throw new IllegalStateException("Expected to find file" + file);
        }
    }


}
