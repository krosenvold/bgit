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
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.GitCommitResponseImpl;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.IClient;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.IGitCommit;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.utilities.CheckUtilities;


/**
 * <code>GitCommit</code> provides an API to commit changes to a git repository.
 */
public class GitCommit {

  /**
   * Automatically stage all tracked files that have been changed and then commit all files staged
   * in the git repository's index.
   * 
   * @param repository
   *          A <code>File</code> instance for the path to the repository root (the parent
   *          directory of the .git directory) or a sub-directory in the working tree of the
   *          repository to commit against. This argument must represent the absolute path to the
   *          desired directory as returned by the <code>File.getPath()</code> method. If null is
   *          passed, a <code>NullPointerException</code> will be thrown.
   * @param message
   *          The message to attach to the commit. A non-zero length argument is required for this
   *          parameter, otherwise a <code>NullPointerException</code> or
   *          <code>IllegalArgumentException</code> will be thrown.
   * @return The results from the commit.
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
  public GitCommitResponseImpl commitAll(File repository, String message) throws IOException,
      JavaGitException {
    CheckUtilities.checkNullArgument(repository, "repository");
    CheckUtilities.checkStringArgument(message, "message");

    IClient client = ClientManager.getInstance().getPreferredClient();
    IGitCommit gitCommit = client.getGitCommitInstance();
    return gitCommit.commitAll(repository, message);
  }

  /**
   * Commits staged changes into the specified repository. The specific files that are committed
   * depends on the options specified.
   * 
   * @param repository
   *          A <code>File</code> instance for the path to the repository root (the parent
   *          directory of the .git directory) or a sub-directory in the working tree of the
   *          repository to commit against. This argument must represent the absolute path to the
   *          desired directory as returned by the <code>File.getPath()</code> method. If null is
   *          passed, a <code>NullPointerException</code> will be thrown.
   * @param options
   *          The options to commit with. If null is passed, a <code>NullPointerException</code>
   *          will be thrown.
   * @param message
   *          The message to attach to the commit. A non-zero length argument is required for this
   *          parameter, otherwise a <code>NullPointerException</code> or
   *          <code>IllegalArgumentException</code> will be thrown.
   * @return The results from the commit.
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
  public GitCommitResponse commit(File repository, GitCommitOptions options, String message)
      throws IOException, JavaGitException {

    CheckUtilities.checkNullArgument(repository, "repository");
    CheckUtilities.checkStringArgument(message, "message");
    CheckUtilities.checkNullArgument(options, "options");

    IClient client = ClientManager.getInstance().getPreferredClient();
    IGitCommit gitCommit = client.getGitCommitInstance();
    return gitCommit.commit(repository, options, message);
  }

  /**
   * Commits staged changes into the specified repository. The specific files that are committed
   * depends on the options and paths specified.
   * 
   * @param repository
   *          A <code>File</code> instance for the path to the repository root (the parent
   *          directory of the .git directory) or a sub-directory in the working tree of the
   *          repository to commit against. This argument must represent the absolute path to the
   *          desired directory as returned by the <code>File.getPath()</code> method. If null is
   *          passed, a <code>NullPointerException</code> will be thrown.
   * @param options
   *          The options to commit with. If null is passed, a <code>NullPointerException</code>
   *          will be thrown.
   * @param message
   *          The message to attach to the commit. A non-zero length argument is required for this
   *          parameter, otherwise a <code>NullPointerException</code> or
   *          <code>IllegalArgumentException</code> will be thrown.
   * @param paths
   *          A list of folders and/or files to commit. The paths specified in this list must all be
   *          relative to the path specified in the <code>repository</code> parameter as returned
   *          by <code>File.getPath()</code>. A non-null and non-empty list is required for this
   *          parameter, otherwise a <code>NullPointerException</code> or
   *          <code>IllegalArgumentException</code> will be thrown.
   * @return The results from the commit.
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
  public GitCommitResponse commit(File repository, GitCommitOptions options, String message,
      List<File> paths) throws IOException, JavaGitException {
    CheckUtilities.checkNullArgument(repository, "repository");
    CheckUtilities.checkStringArgument(message, "message");
    CheckUtilities.checkNullArgument(options, "options");
    CheckUtilities.checkNullListArgument(paths, "paths");

    IClient client = ClientManager.getInstance().getPreferredClient();
    IGitCommit gitCommit = client.getGitCommitInstance();
    return gitCommit.commit(repository, options, message, paths);
  }

  /**
   * Commits all staged changes (changes already defined in the index) into the specified
   * repository.
   * 
   * @param repository
   *          A <code>File</code> instance for the path to the repository root (the parent
   *          directory of the .git directory) or a sub-directory in the working tree of the
   *          repository to commit against. This argument must represent the absolute path to the
   *          desired directory as returned by the <code>File.getPath()</code> method. If null is
   *          passed, a <code>NullPointerException</code> will be thrown.
   * @param message
   *          The message to attach to the commit. A non-zero length argument is required for this
   *          parameter, otherwise a <code>NullPointerException</code> or
   *          <code>IllegalArgumentException</code> will be thrown.
   * @return The results from the commit.
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
  public GitCommitResponse commit(File repository, String message) throws IOException,
      JavaGitException {
    CheckUtilities.checkNullArgument(repository, "repository");
    CheckUtilities.checkStringArgument(message, "message");

    IClient client = ClientManager.getInstance().getPreferredClient();
    IGitCommit gitCommit = client.getGitCommitInstance();
    return gitCommit.commit(repository, message);
  }

  /**
   * Commits only the changes in the specified paths into the specified repository. Any staged
   * changes (changes already defined in the index) are ignored.
   * 
   * @param repository
   *          A <code>File</code> instance for the path to the repository root (the parent
   *          directory of the .git directory) or a sub-directory in the working tree of the
   *          repository to commit against. This argument must represent the absolute path to the
   *          desired directory as returned by the <code>File.getPath()</code> method. If null is
   *          passed, a <code>NullPointerException</code> will be thrown.
   * @param message
   *          The message to attach to the commit. A non-zero length argument is required for this
   *          parameter, otherwise a <code>NullPointerException</code> or
   *          <code>IllegalArgumentException</code> will be thrown.
   * @param paths
   *          A list of folders and/or files to commit. The paths specified in this list must all be
   *          relative to the path specified in the <code>repository</code> parameter as returned
   *          by <code>File.getPath()</code>. A non-null and non-empty list is required for this
   *          parameter, otherwise a <code>NullPointerException</code> or
   *          <code>IllegalArgumentException</code> will be thrown.
   * @return The results from the commit.
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
  public GitCommitResponse commitOnly(File repository, String message, List<File> paths)
      throws IOException, JavaGitException {
    CheckUtilities.checkNullArgument(repository, "repository");
    CheckUtilities.checkStringArgument(message, "message");
    CheckUtilities.checkNullListArgument(paths, "paths");

    IClient client = ClientManager.getInstance().getPreferredClient();
    IGitCommit gitCommit = client.getGitCommitInstance();
    return gitCommit.commitOnly(repository, message, paths);
  }

}
