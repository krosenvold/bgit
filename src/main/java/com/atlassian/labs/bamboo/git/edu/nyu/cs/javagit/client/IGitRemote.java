package com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client;

import java.io.File;
import java.io.IOException;

import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.JavaGitException;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.Ref;


public interface IGitRemote
{
    void remote(File repoDirectory, Ref remoteBranch, String remoteUrl)
            throws JavaGitException, IOException;
}
