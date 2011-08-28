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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitBranch;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitBranchOptions;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitBranchResponse;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitInit;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitLog;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitLogOptions;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitLogResponse.Commit;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.utilities.CheckUtilities;


/**
 * The <code>DotGit</code> represents the .git directory.
 */
public final class DotGit {
  // This guy's a per-repository singleton, so we need a static place to store our instances.
  private static final Map<String, DotGit> INSTANCES = new HashMap<String, DotGit>();

  // The directory that contains the .git in question.
  private final File path;

  /*
   * The canonical pathname from this file. Store this here so that we don't need to continually hit
   * the filesystem to resolve it.
   */
  private final String canonicalPath;

  /**
   * The constructor. Private because this singleton-ish (per each repository) class is only
   * available via the getInstance method.
   * 
   * @param path
   *          The path to the directory containing the .git file in question.
   */
  private DotGit(File path, String canonicalPath) {
    // TODO (rs2705): Ensure that these arguments are valid (not null, not empty)
    this.path = path;
    this.canonicalPath = canonicalPath;
  }

  /**
   * Checks if there is a DotGit instance for a given path
   * 
   * @param path
   *          <code>File</code> object representing the path to the repository.
   * @return
   *          true if exits, false otherwise;
   */
  public static synchronized boolean existsInstance(File path) {
    String canonicalPath = "";

    try {
      canonicalPath = path.getCanonicalPath();
    } catch (IOException e) {
      //obviously, the answer is NO
      return false;
    }

    return INSTANCES.containsKey(canonicalPath);
  }

  /**
   * Static factory method for retrieving an instance of this class.
   * 
   * @param path
   *          <code>File</code> object representing the path to the repository.
   * @return The <code>DotGit</code> instance for this path
   */
  public static synchronized DotGit getInstance(File path) {
    DotGit dotGit;

    // TODO (rs2705): make sure that path is valid

    /*
     * We want to make sure we're dealing with the canonical path here, since there are multiple
     * ways to refer to the same dir with different strings.
     */
    String canonicalPath = "";

    try {
      canonicalPath = path.getCanonicalPath();
    } catch (Exception e) {
      /*
       * TODO (rs2705): Figure out which exception to throw here, and throw it - or should we simply
       * let it propogate up as-is?
       */

      // Temporary placeholder
      return null;
    }

    if (!(INSTANCES.containsKey(canonicalPath))) {
      dotGit = new DotGit(path, canonicalPath);
      INSTANCES.put(canonicalPath, dotGit);
    } else {
      dotGit = INSTANCES.get(canonicalPath);
    }

    return dotGit;
  }

  /**
   * Convenience method for retrieving an instance of the class using a <code>String</code>
   * instead of a <code>File</code>.
   * 
   * @param path
   *          <code>String</code> object representing the path to the repository.
   * @return The <code>DotGit</code> instance for this path
   */
  public static DotGit getInstance(String path) {
    // TODO (rs2705): make sure that path is valid
    return getInstance(new File(path));
  }

  /**
   * Since instances of this class are singletons, don't allow cloning.
   * 
   * @return None - always throws exception
   */
  @Override
  protected Object clone() throws CloneNotSupportedException {
    throw new CloneNotSupportedException();
  }

  /**
   * Clones the repository into new location
   * 
   * @param gitFrom
   *          The .git being cloned
   */
  public void gitClone(DotGit gitFrom) {
  }

  /**
   * Creates a new branch
   * 
   * @param name
   *          The name of the branch to create
   * 
   * @return The new branch
   */
  public Ref createBranch(String name) throws IOException, JavaGitException {
    Ref newBranch = Ref.createBranchRef(name);
    GitBranch gitBranch = new GitBranch();
    gitBranch.createBranch(path, newBranch);
    return newBranch;
  }

  /**
   * Deletes a branch
   * 
   * @param branch
   *          The branch to delete
   * @param forceDelete
   *          True if force delete option -D should be used, 
   *          false if -d should be used.
   * @throws IOException
   *          Thrown in case of I/O operation failure
   * @throws JavaGitException
   *          Thrown when there is an error executing git-branch.
   */
  public void deleteBranch(Ref branch, boolean forceDelete) 
    throws IOException, JavaGitException {
    GitBranch gitBranch = new GitBranch();
    gitBranch.deleteBranch(path, forceDelete, false, branch);
    branch = null;
  }
  
  /**
   * Renames a branch
   * 
   * @param branchFrom
   *          The branch to rename
   * @param nameTo
   *          New branch name
   * @param forceRename
   *          True if force rename option -M should be used. 
   *          False if -m should be used. 
   * @return new <code>Ref</code> instance
   * @throws IOException
   *          Thrown in case of I/O operation failure
   * @throws JavaGitException
   *          Thrown when there is an error executing git-branch.
   */
  public Ref renameBranch(Ref branchFrom, String nameTo, boolean forceRename) 
    throws IOException, JavaGitException {
    Ref newBranch = Ref.createBranchRef(nameTo);
    GitBranch gitBranch = new GitBranch();
    gitBranch.renameBranch(path, forceRename, branchFrom, newBranch);
    return newBranch;
  }

  /**
   * Gets a list of the branches in the repository.
   * 
   * @return The branches in the repository.
   */
  public Iterator<Ref> getBranches() throws IOException, JavaGitException {
    GitBranch gitBranch = new GitBranch();
    GitBranchOptions options = new GitBranchOptions();
    GitBranchResponse response = gitBranch.branch(path, options);
    return response.getBranchListIterator();
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof DotGit)) {
      return false;
    }

    DotGit dotGit = (DotGit) obj;
    return CheckUtilities.checkObjectsEqual(canonicalPath, dotGit.canonicalPath);
  }
  
  /**
   * Gets the repository path represented by this repository object.
   * 
   * @return The path to the repository
   */
  public File getPath() {
    return path;
  }

  /**
   * Gets the working tree for this git repository
   * 
   * @return The working tree
   */
  public WorkingTree getWorkingTree() {
    return WorkingTree.getInstance(path);
  }

  @Override
  public int hashCode() {
    return canonicalPath.hashCode();
  }

  /**
   * Initializes Git repository
   */
  public void init() {
    // TODO (ma1683): Implement this method
    GitInit init = new GitInit();
      try
      {
          init.init(path);
      } catch (JavaGitException e)
      {
          e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
      } catch (IOException e)
      {
          e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
      }
  }

  /**
   * Merges development histories together
   * 
   * @param otherGit
   *          The other .git to merge with
   * @param m
   *          The merge options (strategy, message, etc)
   */
  public void merge(DotGit otherGit, MergeOptions m) {
  }

  /**
   * Merges development histories together
   * 
   * @param gitList
   *          The list of .git to merge with; implies octopus
   * @param m
   *          The merge options (strategy, message, etc)
   */
  public void merge(List<DotGit> gitList, MergeOptions m) {
  }

  /**
   * Fetch from git repository and merge with the current one
   * 
   * @param gitFrom
   *          The git repository being fetched from
   */
  public void pull(DotGit gitFrom) {
  }

  /**
   * Updates the remote git repository with the content of the current one
   * 
   * @param gitTo
   *          The .git being updated
   */
  public void push(DotGit gitTo) {
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
		return gitLog.log(this.getPath());
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
		return gitLog.log(this.getPath(),options);
	}

}
