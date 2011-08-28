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
package com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.JavaGitException;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.Ref;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitBranchOptions;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitBranchResponse;


/**
 * An interface to represent the git-branch command.
 */
public interface IGitBranch {
  /**
   * Does a basic git-branch without any options. Displays branches.
   * 
   * @param repositoryPath
   *          A <code>File</code> instance for the path to the repository. If null is passed, 
   *          a <code>NullPointerException</code> will be thrown.
   * @return The result of the git branch.
   * @throws IOException
   *              There are many reasons for which an <code>IOException</code> may be thrown.
   *              Examples include:
   *              <ul>
   *              <li>a directory doesn't exist</li>
   *              <li>a command is not found on the PATH</li>
   *              </ul>
   * @throws JavaGitException
   *              Thrown when there is an error executing git-branch.
   */
  public GitBranchResponse branch(File repositoryPath) throws IOException, JavaGitException; 
  
  /**
   * Perform git-branch with the specified options against the repository. Displays branches.
   * 
   * @param repositoryPath
   *          A <code>File</code> instance for the path to the repository. If null is passed, 
   *          a <code>NullPointerException</code> will be thrown.
   * @param options
   *          The options for a git-branch command. If the value is null, a
   *          <code>NullPointerException</code> will be thrown.
   * @return The result of the git branch.
   * @throws IOException
   *              There are many reasons for which an <code>IOException</code> may be thrown.
   *              Examples include:
   *              <ul>
   *              <li>a directory doesn't exist</li>
   *              <li>a command is not found on the PATH</li>
   *              </ul>
   * @throws JavaGitException
   *              Thrown when there is an error executing git-branch.
   */
  public GitBranchResponse branch(File repositoryPath, GitBranchOptions options)
      throws IOException, JavaGitException;

  /**
   * Deletes the specified branch using the -d or -D command line option.
   * 
   * @param repositoryPath
   *          A <code>File</code> instance for the path to the repository. If null is passed, 
   *          a <code>NullPointerException</code> will be thrown.
   * @param forceDelete
   *          True if force delete option -D should be used, false if -d should be used.
   * @param remote
   *          True if delete branch should be acted upon a remote branch.
   * @param branchName
   *          A branch to be deleted.
   * @return The result of the git-branch with delete option.
   * @throws IOException
   *              There are many reasons for which an <code>IOException</code> may be thrown.
   *              Examples include:
   *              <ul>
   *              <li>a directory doesn't exist</li>
   *              <li>access to a file is denied</li>
   *              <li>a command is not found on the PATH</li>
   *              </ul>
   * @throws JavaGitException
   *              Thrown when there is an error executing git-branch.
   */
  public GitBranchResponse deleteBranch(File repositoryPath, boolean forceDelete, boolean remote, 
      Ref branchName) throws IOException, JavaGitException; 
  
  /**
   * Deletes the specified branches using the -d command line option. 
   * 
   * @param repositoryPath
   *          A <code>File</code> instance for the path to the repository. If null is passed, 
   *          a <code>NullPointerException</code> will be thrown.
   * @param forceDelete
   *          True if force delete option -D should be used, false if -d should be used.
   * @param remote
   *          True if delete branch should be acted upon a remote branch list.
   * @param branchList
   *          The list of branches to be deleted.
   * @return The result of the git-branch with -d option.
   * @throws IOException
   *              There are many reasons for which an <code>IOException</code> may be thrown.
   *              Examples include:
   *              <ul>
   *              <li>a directory doesn't exist</li>
   *              <li>access to a file is denied</li>
   *              <li>a command is not found on the PATH</li>
   *              </ul>
   * @throws JavaGitException
   *              Thrown when there is an error executing git-branch.
   */
  public GitBranchResponse deleteBranches(File repositoryPath, boolean forceDelete, boolean remote, 
      List<Ref> branchList) throws IOException, JavaGitException;
  
  /**
   * Renames the current branch to new branch.
   * 
   * @param repositoryPath
   *          A <code>File</code> instance for the path to the repository. If null is passed, 
   *          a <code>NullPointerException</code> will be thrown.
   * @param forceRename
   *          True if force rename option -M should be used. False if -m should be used.
   * @param newName
   *          When renaming the current branch to a new branch name, this is the new branch name.
   * @return The result of the git branch with -m option.
   * @throws IOException
   *              There are many reasons for which an <code>IOException</code> may be thrown.
   *              Examples include:
   *              <ul>
   *              <li>a directory doesn't exist</li>
   *              <li>access to a file is denied</li>
   *              <li>a command is not found on the PATH</li>
   *              </ul>
   * @throws JavaGitException
   *              Thrown when there is an error executing git-branch.
   */
  public GitBranchResponse renameBranch(File repositoryPath, boolean forceRename, Ref newName) 
      throws IOException, JavaGitException;
  
  /**
   * Renames old branch to new branch.
   * 
   * @param repositoryPath
   *          A <code>File</code> instance for the path to the repository. If null is passed, 
   *          a <code>NullPointerException</code> will be thrown.
   * @param forceRename
   *          True if force rename option -M should be used. False if -m should be used.
   * @param oldName
   *          When renaming a branch to a different name, this is the old branch name. 
   * @param newName
   *          When renaming a branch to a new branch name, this is the new branch name.
   * @return The result of the git branch with -m option.
   * @throws IOException
   *              There are many reasons for which an <code>IOException</code> may be thrown.
   *              Examples include:
   *              <ul>
   *              <li>a directory doesn't exist</li>
   *              <li>access to a file is denied</li>
   *              <li>a command is not found on the PATH</li>
   *              </ul>
   * @throws JavaGitException
   *              Thrown when there is an error executing git-branch.
   */
  public GitBranchResponse renameBranch(File repositoryPath, boolean forceRename, Ref oldName, 
      Ref newName) throws IOException, JavaGitException;
  
  /**
   * Creates a branch. Indicates to use the current working branch as the branch start point.
   *  
   * @param repositoryPath
   *          A <code>File</code> instance for the path to the repository. If null is passed, 
   *          a <code>NullPointerException</code> will be thrown.
   * @param branchName
   *          Name of the branch to create.
   * @return The result of the git branch with -m option.
   * @throws IOException
   *              There are many reasons for which an <code>IOException</code> may be thrown.
   *              Examples include:
   *              <ul>
   *              <li>a directory doesn't exist</li>
   *              <li>access to a file is denied</li>
   *              <li>a command is not found on the PATH</li>
   *              </ul>
   * @throws JavaGitException
   *              Thrown when there is an error executing git-branch.
   */
  public GitBranchResponse createBranch(File repositoryPath, Ref branchName) throws IOException, 
      JavaGitException;
  
  /**
   * Creates a branch according to given option. Indicate to use the current working branch as 
   * the branch start point.
   *  
   * @param repositoryPath
   *          A <code>File</code> instance for the path to the repository. If null is passed, 
   *          a <code>NullPointerException</code> will be thrown.
   * @param options
   *          The options for a git-branch command. If the value is null, a
   *          <code>NullPointerException</code> will be thrown.
   * @param branchName
   *          Name of the branch to create.
   * @return The result of the git branch with -m option.
   * @throws IOException
   *              There are many reasons for which an <code>IOException</code> may be thrown.
   *              Examples include:
   *              <ul>
   *              <li>a directory doesn't exist</li>
   *              <li>access to a file is denied</li>
   *              <li>a command is not found on the PATH</li>
   *              </ul>
   * @throws JavaGitException
   *              Thrown when there is an error executing git-branch.
   */
  public GitBranchResponse createBranch(File repositoryPath, GitBranchOptions options, 
      Ref branchName) throws IOException, JavaGitException;
  
  /**
   * Creates a branch. Uses the startPoint as the branch start point.
   *  
   * @param repositoryPath
   *          A <code>File</code> instance for the path to the repository. If null is passed, 
   *          a <code>NullPointerException</code> will be thrown.
   * @param branchName
   *          Name of the branch to create.
   * @param startPoint
   *          Start point of the newly created branch.
   * @return The result of the git branch with -m option.
   * @throws IOException
   *              There are many reasons for which an <code>IOException</code> may be thrown.
   *              Examples include:
   *              <ul>
   *              <li>a directory doesn't exist</li>
   *              <li>access to a file is denied</li>
   *              <li>a command is not found on the PATH</li>
   *              </ul>
   * @throws JavaGitException
   *              Thrown when there is an error executing git-branch.
   */
  public GitBranchResponse createBranch(File repositoryPath, Ref branchName, Ref startPoint) throws 
      IOException, JavaGitException;
  
  /**
   * Creates a branch according to given option. Allow the startPoint to be null to indicate to 
   * use the current working branch as the branch start point.
   *  
   * @param repositoryPath
   *          A <code>File</code> instance for the path to the repository. If null is passed, 
   *          a <code>NullPointerException</code> will be thrown.
   * @param options
   *          The options for a git-branch command. If the value is null, a
   *          <code>NullPointerException</code> will be thrown.
   * @param branchName
   *          Name of the branch to create.
   * @param startPoint
   *          Start point of the newly created branch.
   * @return The result of the git branch with -m option.
   * @throws IOException
   *              There are many reasons for which an <code>IOException</code> may be thrown.
   *              Examples include:
   *              <ul>
   *              <li>a directory doesn't exist</li>
   *              <li>access to a file is denied</li>
   *              <li>a command is not found on the PATH</li>
   *              </ul>
   * @throws JavaGitException
   *              Thrown when there is an error executing git-branch.
   */
  public GitBranchResponse createBranch(File repositoryPath, GitBranchOptions options, 
      Ref branchName, Ref startPoint) throws IOException, JavaGitException;
}

