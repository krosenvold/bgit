package edu.nyu.cs.javagit.api.commands;

import java.io.File;
import java.io.IOException;

import edu.nyu.cs.javagit.api.JavaGitException;
import edu.nyu.cs.javagit.api.Ref;
import edu.nyu.cs.javagit.client.ClientManager;
import edu.nyu.cs.javagit.client.IClient;
import edu.nyu.cs.javagit.client.IGitSubmodule;
import edu.nyu.cs.javagit.utilities.CheckUtilities;

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