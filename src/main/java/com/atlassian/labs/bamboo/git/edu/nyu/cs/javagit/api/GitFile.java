/*
 * ====================================================================
 * Copyright (c) 2008 JavaGit Project.  All rights reserved.
 *
 * This software is licensed using the GNU LGPL v2.1 license.  A copy
 * of the license is included with the distribution of this source
 * code in the LICENSE.txt file.  The text of the license can also
 * be obtained at:
 *
 *   http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 *
 * For more information on the JavaGit project, see:
 *
 *   http://www.javagit.com
 * ====================================================================
 */
package com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitLog;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitLogOptions;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitStatus;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitLogResponse.Commit;


/**
 * <code>GitFile</code> a file object in a git working tree.
 */
public class GitFile extends GitFileSystemObject {
  /**
   * The constructor. Both arguments are required (i.e. cannot be null).
   * 
   * @param file
   *          underlying <code>java.io.File</code> object
   * @param workingTree
   *          The <code>WorkingTree</code> that this file falls under.
   * 
   */
  protected GitFile(File file, WorkingTree workingTree) throws JavaGitException {
    super(file, workingTree);
  }
  
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof GitFile)) {
      return false;
    }

    GitFileSystemObject gitObj = (GitFileSystemObject) obj;
    return super.equals(gitObj);
  }

  /**
   * Show object's status in the working directory
   * 
   * @return Object's status in the working directory (untracked, changed but not updated, etc).
   */
  public Status getStatus() throws IOException, JavaGitException {
    GitStatus gitStatus = new GitStatus();
    // run git-status command
    return gitStatus.getFileStatus(workingTree.getPath(), relativePath);
  }

  /**
	 * Show commit logs
	 * 
	 * @return List of commits for the working directory
	 * @throws IOException 
	 * @throws JavaGitException 
	 */
	public List<Commit> getLog() throws JavaGitException, IOException {
		GitLog gitLog = new GitLog();
		GitLogOptions options = new GitLogOptions();
		options.setOptRelative(true,this.relativePath.toString());
		return gitLog.log(this.file);
	}

	/**
	 * 
	 * @param options	Options to the git log command
	 * @return	List of commits for the working directory
	 * @throws JavaGitException
	 * @throws IOException
	 */
	public List<Commit> getLog(GitLogOptions options) throws JavaGitException, IOException {
		GitLog gitLog = new GitLog();		
		options.setOptRelative(true,this.relativePath.toString());
		return gitLog.log(this.file,options);
	}

}
