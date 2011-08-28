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
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitResetOptions;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitResetResponse;


/**
 * An interface to represent the git-reset command.
 */
public interface IGitReset {

  /**
   * Perform a reset on the repository. The results of this method are what one would get by running
   * &quote;git-reset&quote; against the repository.
   * 
   * @param repository
   *          A <code>File</code> instance for the path to the repository root (the parent
   *          directory of the .git directory) or a sub-directory in the working tree of the
   *          repository to perform reset against. This argument must represent the absolute path to
   *          the desired directory as returned by the <code>File.getPath()</code> method. If null
   *          is passed, a <code>NullPointerException</code> will be thrown.
   * @return The results of the reset.
   * @exception IOException
   *              There are many reasons for which an <code>IOException</code> may be thrown.
   *              Examples include:
   *              <ul>
   *              <li>a directory doesn't exist</li>
   *              <li>access to a file is denied</li>
   *              <li>a command is not found on the PATH</li>
   *              </ul>
   * @exception JavaGitException
   *              Thrown when there is an error making the commit.
   */
  public GitResetResponse gitReset(File repository) throws IOException, JavaGitException;

  /**
   * Perform a reset on the repository.
   * 
   * @param repository
   *          A <code>File</code> instance for the path to the repository root (the parent
   *          directory of the .git directory) or a sub-directory in the working tree of the
   *          repository to perform reset against. This argument must represent the absolute path to
   *          the desired directory as returned by the <code>File.getPath()</code> method. If null
   *          is passed, a <code>NullPointerException</code> will be thrown.
   * @param options
   *          The options for a git-reset command. If the value is null, a
   *          <code>NullPointerException</code> will be thrown.
   * @return The results of the reset.
   * @exception IOException
   *              There are many reasons for which an <code>IOException</code> may be thrown.
   *              Examples include:
   *              <ul>
   *              <li>a directory doesn't exist</li>
   *              <li>access to a file is denied</li>
   *              <li>a command is not found on the PATH</li>
   *              </ul>
   * @exception JavaGitException
   *              Thrown when there is an error making the commit.
   */
  public GitResetResponse gitReset(File repository, GitResetOptions options) throws IOException,
      JavaGitException;

  /**
   * Perform a reset on the repository.
   * 
   * @param repository
   *          A <code>File</code> instance for the path to the repository root (the parent
   *          directory of the .git directory) or a sub-directory in the working tree of the
   *          repository to perform reset against. This argument must represent the absolute path to
   *          the desired directory as returned by the <code>File.getPath()</code> method. If null
   *          is passed, a <code>NullPointerException</code> will be thrown.
   * @param commitName
   *          The name of the commit to reset to. If the value is null, a
   *          <code>NullPointerException</code> will be thrown.
   * @param paths
   *          A list of paths to folders or files to reset. The paths specified in this list must
   *          all be relative to the path specified in the <code>repository</code> parameter as
   *          returned by <code>File.getPath()</code>. A non-null and non-empty list is required
   *          for this parameter, otherwise a <code>NullPointerException</code> or
   *          <code>IllegalArgumentException</code> will be thrown.
   * @return The results of the reset.
   * @exception IOException
   *              There are many reasons for which an <code>IOException</code> may be thrown.
   *              Examples include:
   *              <ul>
   *              <li>a directory doesn't exist</li>
   *              <li>access to a file is denied</li>
   *              <li>a command is not found on the PATH</li>
   *              </ul>
   * @exception JavaGitException
   *              Thrown when there is an error making the commit.
   */
  public GitResetResponse gitReset(File repository, Ref commitName, List<File> paths)
      throws IOException, JavaGitException;

  /**
   * Perform a reset on the repository.
   * 
   * @param repository
   *          A <code>File</code> instance for the path to the repository root (the parent
   *          directory of the .git directory) or a sub-directory in the working tree of the
   *          repository to perform reset against. This argument must represent the absolute path to
   *          the desired directory as returned by the <code>File.getPath()</code> method. If null
   *          is passed, a <code>NullPointerException</code> will be thrown.
   * @param paths
   *          A list of paths to folders or files to reset. The paths specified in this list must
   *          all be relative to the path specified in the <code>repository</code> parameter as
   *          returned by <code>File.getPath()</code>. A non-null and non-empty list is required
   *          for this parameter, otherwise a <code>NullPointerException</code> or
   *          <code>IllegalArgumentException</code> will be thrown.
   * @return The results of the reset.
   * @exception IOException
   *              There are many reasons for which an <code>IOException</code> may be thrown.
   *              Examples include:
   *              <ul>
   *              <li>a directory doesn't exist</li>
   *              <li>access to a file is denied</li>
   *              <li>a command is not found on the PATH</li>
   *              </ul>
   * @exception JavaGitException
   *              Thrown when there is an error making the commit.
   */
  public GitResetResponse gitReset(File repository, List<File> paths) throws IOException,
      JavaGitException;

  /**
   * Perform a hard reset on the repository to the specified <code>CommitName</code>.
   * 
   * @param repository
   *          A <code>File</code> instance for the path to the repository root (the parent
   *          directory of the .git directory) or a sub-directory in the working tree of the
   *          repository to perform reset against. This argument must represent the absolute path to
   *          the desired directory as returned by the <code>File.getPath()</code> method. If null
   *          is passed, a <code>NullPointerException</code> will be thrown.
   * @param commitName
   *          The name of the commit to reset to. If the value is null, a
   *          <code>NullPointerException</code> will be thrown.
   * @return The results of the reset.
   * @exception IOException
   *              There are many reasons for which an <code>IOException</code> may be thrown.
   *              Examples include:
   *              <ul>
   *              <li>a directory doesn't exist</li>
   *              <li>access to a file is denied</li>
   *              <li>a command is not found on the PATH</li>
   *              </ul>
   * @exception JavaGitException
   *              Thrown when there is an error making the commit.
   */
  public GitResetResponse gitResetHard(File repository, Ref commitName) throws IOException,
      JavaGitException;

  /**
   * Perform a soft reset on the repository to the specified <code>CommitName</code>.
   * 
   * @param repository
   *          A <code>File</code> instance for the path to the repository root (the parent
   *          directory of the .git directory) or a sub-directory in the working tree of the
   *          repository to perform reset against. This argument must represent the absolute path to
   *          the desired directory as returned by the <code>File.getPath()</code> method. If null
   *          is passed, a <code>NullPointerException</code> will be thrown.
   * @param commitName
   *          The name of the commit to reset to. If the value is null, a
   *          <code>NullPointerException</code> will be thrown.
   * @return The results of the reset.
   * @exception IOException
   *              There are many reasons for which an <code>IOException</code> may be thrown.
   *              Examples include:
   *              <ul>
   *              <li>a directory doesn't exist</li>
   *              <li>access to a file is denied</li>
   *              <li>a command is not found on the PATH</li>
   *              </ul>
   * @exception JavaGitException
   *              Thrown when there is an error making the commit.
   */
  public GitResetResponse gitResetSoft(File repository, Ref commitName) throws IOException,
      JavaGitException;

}
