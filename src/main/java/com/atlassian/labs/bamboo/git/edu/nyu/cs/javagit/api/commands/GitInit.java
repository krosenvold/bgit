package com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands;

import java.io.File;
import java.io.IOException;

import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.JavaGitException;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.ClientManager;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.IClient;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.IGitInit;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.utilities.CheckUtilities;


public class GitInit {
	
	public GitInitResponse init(File repositoryPath, GitInitOptions options) throws JavaGitException, IOException{
		CheckUtilities.checkNullArgument(repositoryPath, "repository");
	    
	    IClient client = ClientManager.getInstance().getPreferredClient();
	    IGitInit gitInit = client.getGitInitInstance();
	    return gitInit.init(repositoryPath,options);
	}
	
	public GitInitResponse init(File repositoryPath) throws JavaGitException, IOException{
		CheckUtilities.checkNullArgument(repositoryPath, "repository");
	    
	    IClient client = ClientManager.getInstance().getPreferredClient();
	    IGitInit gitInit = client.getGitInitInstance();
	    return gitInit.init(repositoryPath);
	}

}
