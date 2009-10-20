package com.atlassian.labs.bamboo.git;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import edu.nyu.cs.javagit.api.JavaGitException;
import edu.nyu.cs.javagit.api.Ref;
import edu.nyu.cs.javagit.api.commands.GitCloneOptions;
import edu.nyu.cs.javagit.client.cli.CliGitClone;
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
        return "git://github.com/slnc/bgit-unittest.git";
    }


    @BeforeClass
    public static void getFromGitHub() throws IOException, JavaGitException {
        File masterRepoDir = getMasterRepoWorkingDirectory();
        if ( !GitRepository.containsValidRepo(getMasterRepoCheckoutDirectory())){
            CliGitClone clone = new CliGitClone();
            GitCloneOptions gitCloneOptions = new GitCloneOptions(false, false, true);
            clone.clone(getCheckoutDirectory(masterRepoDir), gitCloneOptions, getGitHubRepoUrl(),  getMasterRepoCheckoutDirectory());
        }
    }


    private void makeWorkingCopy() throws IOException, JavaGitException {
        CliGitClone clone = new CliGitClone();
        GitCloneOptions gitCloneOptions = new GitCloneOptions(false, true, false);
        clone.clone(new File("."),  gitCloneOptions, getMasterRepoCheckoutDirectory().getCanonicalPath(), getCheckoutDirectory( getWorkingCopyDir()));
    }
    
    @After
    public void deleteWorkingCopy() throws IOException, JavaGitException {
        deleteDir( getWorkingCopyDir());
    }


    @Test
    public void testClone() throws IOException, JavaGitException {
        GitRepository gitRepository = new GitRepository(getMasterRepoCheckoutDirectory().getCanonicalPath(), "feature1");
        File sourceDir = getCheckoutDirectory(getFreshWorkingCopyDir());
        
        assertFalse( GitRepository.containsValidRepo( sourceDir));
        gitRepository.cloneOrFetch(sourceDir);
        assertTrue( GitRepository.containsValidRepo( sourceDir));

        assertEquals("feature1", gitRepository.gitStatus(sourceDir).getName());
    }

    @Test
    public void testCloneDefault() throws IOException, JavaGitException {
        GitRepository gitRepository = new GitRepository(getMasterRepoCheckoutDirectory().getCanonicalPath(), null);
        File sourceDir = getCheckoutDirectory(getFreshWorkingCopyDir());
        gitRepository.cloneOrFetch(sourceDir);
        assertEquals("featureDefault", gitRepository.gitStatus(sourceDir).getName());
    }

    @Test
    public void testIsOnBranch() throws IOException, JavaGitException {
        GitRepository gitRepository = new GitRepository(getGitHubRepoUrl(), null);
        File sourceDir = getCheckoutDirectory(getFreshWorkingCopyDir());
        makeWorkingCopy();
        assertTrue(gitRepository.isOnBranch(sourceDir, Ref.createBranchRef("featureDefault")));
        assertFalse(gitRepository.isOnBranch(sourceDir, Ref.createBranchRef("feature1")));
    }

    @Test
    public void testHistory() throws IOException, JavaGitException, RepositoryException {
        GitRepository gitRepository = new GitRepository(getMasterRepoCheckoutDirectory().getCanonicalPath(), "featureDefault");
        File sourceDir = getCheckoutDirectory(getFreshWorkingCopyDir());
        makeWorkingCopy();

        // Fri Oct 9 14:51:41 2009 +0200
        List<com.atlassian.bamboo.commit.Commit> results = new ArrayList<Commit>();
        gitRepository.detectCommitsForUrl(getGitHubRepoUrl(), "Fri Oct 9 15:37:45 2009 +0200", results, sourceDir, "UT-KEY");

        assertEquals(2, results.size());
        Commit c0 = results.get(0);
        assertEquals(1, c0.getFiles().size());
        assertEquals("OnDefault.txt", c0.getFiles().get(0).getName());
        assertEquals("2d9b1997d64fa9501a0e4dec26cc9a07e3e8247f", c0.getFiles().get(0).getRevision());

        Commit c1 = results.get(1);
        assertEquals("File3.txt", c1.getFiles().get(0).getName());
        assertEquals("a55e4702a0fdc210eaa17774dddc4890852396a7", c1.getFiles().get(0).getRevision());

    }


    private static File getMasterRepoWorkingDirectory() {
        File masterRepoDir = new File("masterRepo");
        ensureDirExists(masterRepoDir);
        return masterRepoDir;
    }
    private static File getMasterRepoCheckoutDirectory() {
        try {
            return new File(getMasterRepoWorkingDirectory().getCanonicalPath() + File.separator + "checkout");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static File getWorkingCopyDir() {
        return new File("testRepo");
    }

    private static File getCheckoutDirectory(File workingDirectory){
        try {
            return new File(workingDirectory.getCanonicalPath() + File.separator + "checkout");
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
}
