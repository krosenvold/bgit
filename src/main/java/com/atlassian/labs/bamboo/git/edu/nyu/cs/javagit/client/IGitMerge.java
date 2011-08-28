package com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client;

import java.io.File;
import java.io.IOException;

import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.JavaGitException;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.Ref;


public interface IGitMerge
{
    /**
	 *
	 * @param repoDirectory The repository Directroy to be initialized as a git repository
	 * @return	GitInitResponse object
	 * @throws com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.JavaGitException
	 * @throws java.io.IOException
	 */
	public void merge(File repoDirectory, Ref remoteBranch) throws JavaGitException, IOException;
}
