package com.atlassian.labs.bamboo.git;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

/**
 * @author Kristian Rosenvold
 */
public class GitRepositoryPluginTest extends AbstractTestWithRepo {

    private GitRepositoryPlugin getGitRepositoryPlugin() throws IOException {
        GitRepositoryPlugin plugin = new GitRepositoryPlugin();
        plugin.setRepositoryUrl(master.getCheckoutDirectory().getAbsolutePath());
        return plugin;
    }


    @Test
    public void testInitialization() throws IOException {
        GitRepositoryPlugin plugin = getGitRepositoryPlugin();
        assertEquals(master.getCheckoutDirectory().getAbsolutePath(), plugin.getRepositoryUrl());
    }
}
