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
import java.util.List;
import java.util.Map;

import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitAddResponse;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitBranch;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitBranchOptions;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitBranchResponse;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitCheckout;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitCommit;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitCommitResponse;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitStatus;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitStatusResponse;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.utilities.CheckUtilities;


/**
 * The <code>WorkingTree</code> represents the working copy of the files in the current branch.
 * 
 */
public final class WorkingTree {
  // This guy's a per-repository singleton, so we need a static place to store our instances.
  private static final Map<String, WorkingTree> INSTANCES = new HashMap<String, WorkingTree>();

  // The directory that contains the .git in question.
  private final File path;

  /*
   * The canonical pathname from this file. Store this here so that we don't need to continually hit
   * the filesystem to resolve it.
   */
  private final String canonicalPath;
  
  // A git-specific representation of the same place this class is pointing.
  private GitDirectory rootDir;

  /**
   * The constructor. Private because this singleton-ish (per each repository) class is only
   * available via the getInstance method.
   * 
   * @param path
   *          The path to the working directory represented by the instance being created.
   * 
   */
  private WorkingTree(File path, String canonicalPath) {
    this.path = path;
    this.canonicalPath = canonicalPath;
    try {
      this.rootDir = new GitDirectory(path, this);
    }
    catch(JavaGitException e) {
      //that is really impossible
    }
  }

  /**
   * Static factory method for retrieving an instance of this class.
   * 
   * @param path
   *          <code>File</code> object representing the path to the repository.
   * @return The <code>WorkingTree</code> instance for this path
   */
  public static synchronized WorkingTree getInstance(File path) {
    WorkingTree workingTree;

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
      return null; // Temporary placeholder
    }

    if (!(INSTANCES.containsKey(canonicalPath))) {
      workingTree = new WorkingTree(path, canonicalPath);
      INSTANCES.put(canonicalPath, workingTree);
    } else {
      workingTree = INSTANCES.get(canonicalPath);
    }

    return workingTree;
  }

  /**
   * Convenience method for retrieving an instance of the class using a <code>String</code>
   * instead of a <code>File</code>.
   * 
   * @param path
   *          <code>String</code> object representing the path to the repository.
   * @return The <code>WorkingTree</code> instance for this path
   */
  public static WorkingTree getInstance(String path) {
    // TODO (rs2705): make sure that path is valid
    return getInstance(new File(path));
  }

  /**
   * Adds all known and modified files in the working directory to the index.
   * 
   * @return response from git add
   */
  public GitAddResponse add() throws IOException, JavaGitException {
    return rootDir.add();
  }

  /**
   * Adds a directory to the working directory (but not to the repository!)
   * 
   * @param dir
   *          name of the directory
   * 
   * @return The new <code>GitDirectory</code> object
   * 
   * @throws JavaGitException
   *         File path specified does not belong to git repo/ working tree
   */
  public GitDirectory addDirectory(String dir) throws JavaGitException {
    return new GitDirectory(new File(dir), this);
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
   * Commits the objects specified in the index to the repository.
   * 
   * @param comment
   *          Developer's comment about the change
   * 
   * @return response from git commit
   */
  public GitCommitResponse commit(String comment) throws IOException, JavaGitException {
    GitCommit gitCommit = new GitCommit();
    return gitCommit.commit(path, comment);
  }

  /**
   * Automatically stage files that have been modified and deleted, but new files you have not 
   * told git about are not affected
   * 
   * @param comment
   *          Developer's comment about the change
   * 
   * @return response from git commit
   */
  public GitCommitResponse commitAll(String comment) throws IOException, JavaGitException {
    GitCommit gitCommit = new GitCommit();
    return gitCommit.commitAll(path, comment);
  }

  /**
   * Stage all files and commit (including untracked)
   * 
   * @param comment
   *          Developer's comment about the change
   * @return <code>GitCommitResponse</code> object
   * @throws IOException
   *          I/O operation fails
   * @throws JavaGitException
   *          git command fails
   */
  public GitCommitResponse addAndCommitAll(String comment) throws IOException, JavaGitException {
    return rootDir.commit(comment);
  }


  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof WorkingTree)) {
      return false;
    }

    WorkingTree workingTree = (WorkingTree) obj;
    return CheckUtilities.checkObjectsEqual(canonicalPath, workingTree.canonicalPath);
  }

  /**
   * Gets the currently checked-out branch of the working directory.
   * 
   * @return The currently checked-out branch of the working directory.
   */
  public Ref getCurrentBranch() throws IOException, JavaGitException {
    GitBranch gitBranch = new GitBranch();
    GitBranchOptions options = new GitBranchOptions();
    GitBranchResponse response = gitBranch.branch(path, options);
    return response.getCurrentBranch();
  }

  /**
   * Take a standard <code>File</code> object and return it wrapped in a <code>GitDirectory</code>.
   * 
   * @return A new <code>GitDirectory</code> object representing the given <code>File</code>.
   * 
   * @throws JavaGitException
   *         File path specified does not belong to git repo/ working tree
   */
  public GitDirectory getDirectory(File file) throws JavaGitException {
    return new GitDirectory(file, this);
  }

  /**
   * Take a standard <code>File</code object and return it wrapped in a <code>GitFile</code>.
   * 
   * @return A new <code>GitFile</code> object representing the given <code>File</code>.
   * 
   * @throws JavaGitException
   *         File path specified does not belong to git repo/ working tree
   */
  public GitFile getFile(File file) throws JavaGitException {
    return new GitFile(file, this);
  }

  /**
   * Show commit logs
   * 
   * @return List of commits for the working directory
   */
  public List<Commit> getLog() {
    // TODO (ma1683): Implement this method
    return null;
  }

  /**
   * Gets the .git representation for this git repository
   * 
   * @return The DotGit
   */
  public DotGit getDotGit() {
    return DotGit.getInstance(path);
  }

  /**
   * Gets the path to the working directory represented by an instance.
   * 
   * @return The path to the working directory represented by an instance.
   */
  public File getPath() {
    return path;
  }

  /**
   * Gets the filesystem tree; equivalent to git-status
   * 
   * @return The list of objects at the root directory
   * 
   * @throws JavaGitException
   *         File path specified does not belong to git repo/ working tree
   */
  public List<GitFileSystemObject> getTree() throws IOException, JavaGitException {
    // TODO (rs2705): Make this work - will throw NullPointerException
    return new GitDirectory(path, this).getChildren();
  }

  @Override
  public int hashCode() {
    return canonicalPath.hashCode();
  }

  /**
   * Reverts the specified git commit
   * 
   * @param commit
   *          Git commit that user wishes to revert
   */
  public void revert(Commit commit) {
    // TODO (ma1683): Implement this method
    // GitRevert.revert(commit.getSHA1());
  }

  /**
   * Switches to a new branch
   * 
   * @param ref
   *          Git branch/sha1 to switch to
   */
  public void checkout(Ref ref) throws IOException, JavaGitException {
    GitCheckout gitCheckout = new GitCheckout();
    gitCheckout.checkout(path, null, ref);

    /*
     * TODO (rs2705): Figure out why this function is setting this.path. When does the WorkingTree
     * path change?
     */
    // this.path = branch.getBranchRoot().getPath();
  }
  

  /**
   * Gets the status of all files in the working directory
   * 
   * @return <code>GitStatusResponse</code> object
   * @throws IOException
   *         Exception is thrown if any of the IO operations fail.
   * @throws JavaGitException
   *         Exception thrown if the repositoryPath is null
   */
  public GitStatusResponse getStatus() throws IOException, JavaGitException {
    GitStatus gitStatus = new GitStatus();
    return gitStatus.status(path);
  }

}