package com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands;

import java.io.File;
import java.io.IOException;

import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.JavaGitException;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.Ref;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.ClientManager;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.IClient;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.IGitRemote;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.utilities.CheckUtilities;


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