package edu.nyu.cs.javagit.api.commands;

import java.io.File;
import java.io.IOException;

import edu.nyu.cs.javagit.api.JavaGitException;
import edu.nyu.cs.javagit.api.Ref;
import edu.nyu.cs.javagit.client.ClientManager;
import edu.nyu.cs.javagit.client.IClient;
import edu.nyu.cs.javagit.client.IGitRemote;
import edu.nyu.cs.javagit.utilities.CheckUtilities;

public class GitRemote
{

	public void remote(File repositoryPath, Ref branch, String remoteUrl) throws JavaGitException, IOException
    {
		CheckUtilities.checkNullArgument(repositoryPath, "repository");

	    IClient client = ClientManager.getInstance().getPreferredClient();
	    IGitRemote gitRemote = client.getGitRemoteInstance();
	    gitRemote.remote(repositoryPath,branch, remoteUrl);
	}

}