/**
 *
 */
package com.atlassian.labs.bamboo.git;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.JavaGitException;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitCloneOptions;


/**
 * @author David Matějček
 *
 */
public abstract class AbstractTestWithRepo {

    protected static DirectoryController master;
    protected DirectoryController clone;


    private static String getGitHubRepoUrl() {
        return "git://github.com/krosenvold/bgit-unittest.git";
    }


    @BeforeClass
    public static void getFromGitHub() throws IOException, JavaGitException {
        master = new DirectoryController(Settings.getMasterRepositoryDir());
        master.clean();
        final File localRepo = master.getCheckoutDirectory();
        if (!GitRepository.containsValidRepo(localRepo)) {
            GitRepository repo = new GitRepository(getGitHubRepoUrl(), localRepo, null);
            GitCloneOptions gitCloneOptions = new GitCloneOptions(false, false, true);
            repo.clone(getGitHubRepoUrl(), localRepo, gitCloneOptions);
        }
    }


    @Before
    public void createWorkingCopy() {
        clone = new DirectoryController(Settings.getCloneRepositoryDir());
        clone.clean();
    }


    @After
    public void deleteWorkingCopy() throws IOException, JavaGitException {
        clone.delete();
        clone = null;
    }

}
