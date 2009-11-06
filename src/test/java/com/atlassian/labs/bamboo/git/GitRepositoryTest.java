package com.atlassian.labs.bamboo.git;

import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import edu.nyu.cs.javagit.api.JavaGitException;
import edu.nyu.cs.javagit.api.Ref;
import edu.nyu.cs.javagit.api.commands.*;
import org.junit.Test;
import com.atlassian.bamboo.commit.Commit;
import com.atlassian.bamboo.repository.RepositoryException;

/**
 * @author Kristian Rosenvold
 * Testing {@link com.atlassian.labs.bamboo.git.ExampleReport}
 */
public class GitRepositoryTest extends TestCase
{
    @Test
    public void testClone() throws IOException, JavaGitException {
        GitRepository gitRepository = new GitRepository();
        gitRepository.setRemoteBranch("feature1");
        File sourceDir = getCheckoutDirectory("testRepo1");
        gitRepository.cloneOrFetch(sourceDir, "git://github.com/krosenvold/bgit-unittest.git");
        Ref ref = gitRepository.gitStatus(sourceDir);
        assertEquals("feature1", ref.getName());
    }

    @Test
    public void testCloneDefault() throws IOException, JavaGitException {
        // We dont support switching branches on a checkout yet, so check out to different folder.
        GitRepository gitRepository = new GitRepository();
        File sourceDir = getCheckoutDirectory("testRepo2");
        gitRepository.cloneOrFetch(sourceDir, "git://github.com/krosenvold/bgit-unittest.git");
        Ref ref = gitRepository.gitStatus(sourceDir);
        assertEquals("featureDefault", ref.getName());
    }

    @Test
    public void testHistory() throws IOException, JavaGitException, RepositoryException {
        GitRepository gitRepository = new GitRepository();
        gitRepository.setRemoteBranch("featureDefault");
        File sourceDir = getCheckoutDirectory("testRepo1");
        gitRepository.cloneOrFetch(sourceDir, "git://github.com/krosenvold/bgit-unittest.git");


        // Fri Oct 9 14:51:41 2009 +0200
        List<com.atlassian.bamboo.commit.Commit> results = new ArrayList<Commit>();
        String s = gitRepository.detectCommitsForUrl("git://github.com/krosenvold/bgit-unittest.git", "Fri Oct 9 15:37:45 2009 +0200", results, sourceDir, "UT-KEY");

        assertEquals(2, results.size());
        Commit c0 = results.get(0);
        assertEquals(1, c0.getFiles().size());
        assertEquals("OnDefault.txt", c0.getFiles().get(0).getName());
        assertEquals("2d9b1997d64fa9501a0e4dec26cc9a07e3e8247f", c0.getFiles().get(0).getRevision());

        Commit c1 = results.get(1);
        assertEquals("File3.txt", c1.getFiles().get(0).getName());
        assertEquals("a55e4702a0fdc210eaa17774dddc4890852396a7", c1.getFiles().get(0).getRevision());

    }

    private File getCheckoutDirectory(String loc) {
        File projectDir = new File(loc);
        if (projectDir.exists()) projectDir.delete();
        if (!projectDir.exists()) projectDir.mkdir();
        File sourceDir = new File(loc + "/checkout");
        return sourceDir;
    }

}