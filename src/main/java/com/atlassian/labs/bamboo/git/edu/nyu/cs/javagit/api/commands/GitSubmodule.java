package com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands;

import java.io.File;
import java.io.IOException;

import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.JavaGitException;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.ClientManager;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.IClient;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.IGitSubmodule;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.utilities.CheckUtilities;


public class GitSubmodule
{

    public void init(File repositoryPath) throws JavaGitException, IOException
    {
        CheckUtilities.checkNullArgument(repositoryPath, "repository");

        IClient client = ClientManager.getInstance().getPreferredClient();
        IGitSubmodule gitSubmodule = client.getGitSubmoduleInstance();
        gitSubmodule.init(repositoryPath);
    }

    public void update(File repositoryPath) throws JavaGitException, IOException
    {
        CheckUtilities.checkNullArgument(repositoryPath, "repository");

        IClient client = ClientManager.getInstance().getPreferredClient();
        IGitSubmodule gitSubmodule = client.getGitSubmoduleInstance();
        gitSubmodule.update(repositoryPath);
    }

}