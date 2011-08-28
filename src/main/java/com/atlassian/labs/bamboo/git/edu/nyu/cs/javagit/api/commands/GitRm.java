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
package com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.JavaGitException;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.ClientManager;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.IClient;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.IGitRm;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.utilities.CheckUtilities;


/**
 * <code>GitRm</code> provides an interface to remove files from a git repository.
 */

public class GitRm {

  // TODO (jhl388): Add test cases for this class.

  /**
   * Remove files relative to the path within the repository.
   * 
   * @param repository
   *          A <code>File</code> instance for the path to the repository root (the parent
   *          directory of the .git directory) or a sub-directory in the working tree of the
   *          repository to run rm against. This argument must represent the absolute path to the
   *          desired directory as returned by the <code>File.getPath()</code> method. If null is
   *          passed, a <code>NullPointerException</code> will be thrown.
   * @param path
   *          A single file to remove. The specified path must be relative to the path specified in
   *          the <code>repository</code> parameter as returned by <code>File.getPath()</code>.
   *          If null is passed, a <code>NullPointerException</code> will be thrown.
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
  public GitRmResponse rm(File repository, File path) throws IOException, JavaGitException {
    CheckUtilities.checkNullArgument(repository, "repository");
    CheckUtilities.checkNullArgument(path, "path");

    IClient client = ClientManager.getInstance().getPreferredClient();
    IGitRm GitRm = client.getGitRmInstance();
    return GitRm.rm(repository, path);
  }

  /**
   * Remove files relative to the path within the repository.
   * 
   * @param repository
   *          A <code>File</code> instance for the path to the repository root (the parent
   *          directory of the .git directory) or a sub-directory in the working tree of the
   *          repository to run rm against. This argument must represent the absolute path to the
   *          desired directory as returned by the <code>File.getPath()</code> method. If null is
   *          passed, a <code>NullPointerException</code> will be thrown.
   * @param options
   *          The options to run rm with. If null is passed, a <code>NullPointerException</code>
   *          will be thrown.
   * @param path
   *          A single file to remove. The specified path must be relative to the path specified in
   *          the <code>repository</code> parameter as returned by <code>File.getPath()</code>.
   *          If null is passed, a <code>NullPointerException</code> will be thrown.
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
  public GitRmResponse rm(java.io.File repository, GitRmOptions options, File path)
      throws IOException, JavaGitException {
    CheckUtilities.checkNullArgument(repository, "repository");
    CheckUtilities.checkNullArgument(options, "options");
    CheckUtilities.checkNullArgument(path, "path");

    IClient client = ClientManager.getInstance().getPreferredClient();
    IGitRm GitRm = client.getGitRmInstance();
    return GitRm.rm(repository, options, path);
  }

  /**
   * Removes files from the specified repository.
   * 
   * @param repository
   *          A <code>File</code> instance for the path to the repository root (the parent
   *          directory of the .git directory) or a sub-directory in the working tree of the
   *          repository to run rm against. This argument must represent the absolute path to the
   *          desired directory as returned by the <code>File.getPath()</code> method. If null is
   *          passed, a <code>NullPointerException</code> will be thrown.
   * @param paths
   *          A list of files to remove. The paths specified in this list must all be relative to
   *          the path specified in the <code>repository</code> parameter as returned by
   *          <code>File.getPath()</code>. A non-null and non-empty list is required for this
   *          parameter, otherwise a <code>NullPointerException</code> or
   *          <code>IllegalArgumentException</code> will be thrown.
   * @return The results from the rm.
   * @exception IOException
   *              There are many reasons for which an <code>IOException</code> may be thrown.
   *              Examples include:
   *              <ul>
   *              <li>a directory doesn't exist</li>
   *              <li>access to a file is denied</li>
   *              <li>a command is not found on the PATH</li>
   *              </ul>
   */
  public GitRmResponse rm(File repository, List<File> paths) throws IOException, JavaGitException {
    CheckUtilities.checkNullArgument(repository, "repository");
    CheckUtilities.checkNullListArgument(paths, "paths");

    IClient client = ClientManager.getInstance().getPreferredClient();
    IGitRm GitRm = client.getGitRmInstance();
    return GitRm.rm(repository, paths);
  }

  /**
   * Remove files relative to the path within the repository.
   * 
   * @param repository
   *          A <code>File</code> instance for the path to the repository root (the parent
   *          directory of the .git directory) or a sub-directory in the working tree of the
   *          repository to run rm against. This argument must represent the absolute path to the
   *          desired directory as returned by the <code>File.getPath()</code> method. If null is
   *          passed, a <code>NullPointerException</code> will be thrown.
   * @param options
   *          The options to run rm with. If null is passed, a <code>NullPointerException</code>
   *          will be thrown.
   * @param paths
   *          A list of files or folders to remove. The paths specified in this list must all be
   *          relative to the path specified in the <code>repository</code> parameter as returned
   *          by <code>File.getPath()</code>. A non-null and non-empty list is required for this
   *          parameter, otherwise a <code>NullPointerException</code> or
   *          <code>IllegalArgumentException</code> will be thrown.
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
  public GitRmResponse rm(File repository, GitRmOptions options, List<File> paths)
      throws IOException, JavaGitException {
    CheckUtilities.checkNullArgument(repository, "repository");
    CheckUtilities.checkNullArgument(options, "options");
    CheckUtilities.checkNullListArgument(paths, "paths");

    IClient client = ClientManager.getInstance().getPreferredClient();
    IGitRm GitRm = client.getGitRmInstance();
    return GitRm.rm(repository, options, paths);
  }

  /**
   * Remove files relative to the path within the repository but only effect the index.
   * 
   * @param repository
   *          A <code>File</code> instance for the path to the repository root (the parent
   *          directory of the .git directory) or a sub-directory in the working tree of the
   *          repository to run rm against. This argument must represent the absolute path to the
   *          desired directory as returned by the <code>File.getPath()</code> method. If null is
   *          passed, a <code>NullPointerException</code> will be thrown.
   * @param paths
   *          A list of files to remove. The paths specified in this list must all be relative to
   *          the path specified in the <code>repository</code> parameter as returned by
   *          <code>File.getPath()</code>. A non-null and non-empty list is required for this
   *          parameter, otherwise a <code>NullPointerException</code> or
   *          <code>IllegalArgumentException</code> will be thrown.
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
  public GitRmResponse rmCached(File repository, List<File> paths) throws IOException,
      JavaGitException {
    CheckUtilities.checkNullArgument(repository, "repository");
    CheckUtilities.checkNullListArgument(paths, "paths");

    IClient client = ClientManager.getInstance().getPreferredClient();
    IGitRm GitRm = client.getGitRmInstance();
    return GitRm.rmCached(repository, paths);
  }

  /**
   * Recursively remove files relative to the path within the repository.
   * 
   * @param repository
   *          A <code>File</code> instance for the path to the repository root (the parent
   *          directory of the .git directory) or a sub-directory in the working tree of the
   *          repository to run rm against. This argument must represent the absolute path to the
   *          desired directory as returned by the <code>File.getPath()</code> method. If null is
   *          passed, a <code>NullPointerException</code> will be thrown.
   * @param paths
   *          A list of files or folders to remove. The paths specified in this list must all be
   *          relative to the path specified in the <code>repository</code> parameter as returned
   *          by <code>File.getPath()</code>. A non-null and non-empty list is required for this
   *          parameter, otherwise a <code>NullPointerException</code> or
   *          <code>IllegalArgumentException</code> will be thrown.
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
  public GitRmResponse rmRecursive(File repository, List<File> paths) throws IOException,
      JavaGitException {
    CheckUtilities.checkNullArgument(repository, "repository");
    CheckUtilities.checkNullListArgument(paths, "paths");

    IClient client = ClientManager.getInstance().getPreferredClient();
    IGitRm GitRm = client.getGitRmInstance();
    return GitRm.rmRecursive(repository, paths);
  }

}
