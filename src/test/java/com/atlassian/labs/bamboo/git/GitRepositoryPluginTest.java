package com.atlassian.labs.bamboo.git;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

/**
 * @author David Matějček
 */
public class GitRepositoryPluginTest extends AbstractTestWithRepo {

    private static final Log LOG = LogFactory.getLog(GitRepositoryPluginTest.class);

    private GitRepositoryPlugin getGitRepositoryPlugin() throws IOException {
        GitRepositoryPlugin plugin = new GitRepositoryPlugin();
        plugin.setRepositoryUrl(master.getCheckoutDirectory().getAbsolutePath());
        return plugin;
    }


    @Test
    public void testInitialization() throws IOException {
        GitRepositoryPlugin plugin = getGitRepositoryPlugin();
        LOG.debug("plugin.name=" + plugin.getName());
        assertTrue("Plugin name is null or empty", StringUtils.isNotBlank(plugin.getName()));
        assertEquals(master.getCheckoutDirectory().getAbsolutePath(), plugin.getRepositoryUrl());
    }
}
