package com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client;

import java.io.File;
import java.io.IOException;

import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.JavaGitException;


public interface IGitFetch
{
    void fetch(File repoDirectory)
            throws JavaGitException, IOException;
}
