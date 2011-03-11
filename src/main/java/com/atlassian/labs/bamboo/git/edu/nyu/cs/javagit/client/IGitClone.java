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

import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.JavaGitException;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitCloneOptions;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitCloneResponse;


/**
 * An interface to represent the git-clone command. Clones a repository into a new directory.
 */
public interface IGitClone {

    /**
   * Clones a git repository into given directory, without any option.
   * 
   * @param workingDirectoryPath
   *          A <code>File</code> instance for the path to the working directory. This argument
   *          must represent the absolute path to the desired directory as returned by the
   *          <code>File.getPath()</code> method. If null is passed, a
   *          <code>NullPointerException</code> will be thrown.
   * @param repository
   *          A <code>String</code> instance for the repository to be cloned. If null is passed, a
   *          <code>NullPointerException</code> will be thrown.
   * @param directory
   *          A <code>File</code> instance for the directory where the repository is to be cloned.
   *          If null is passed, a <code>NullPointerException</code> will be thrown.
   * @return The result of the git clone.
   * @throws IOException
   *           There are many reasons for which an <code>IOException</code> may be thrown.
   *           Examples include:
   *           <ul>
   *           <li>a directory doesn't exist</li>
   *           <li>a command is not found on the PATH</li>
   *           </ul>
   * @throws JavaGitException
   *           Thrown when there is an error executing git-clone.
   */
  public GitCloneResponse clone(File workingDirectoryPath, String repository, File directory)
      throws IOException, JavaGitException;

  /**
   * Clones a git repository into given directory, with the specified options.
   * 
   * @param workingDirectoryPath
   *          A <code>File</code> instance for the path to the working directory. This argument
   *          must represent the absolute path to the desired directory as returned by the
   *          <code>File.getPath()</code> method. If null is passed, a
   *          <code>NullPointerException</code> will be thrown.
   * @param options
   *          The options for the git-clone command. If the value is null, a
   *          <code>NullPointerException</code> will be thrown.
   * @param repository
   *          A <code>String</code> instance for the repository to be cloned. If null is passed, a
   *          <code>NullPointerException</code> will be thrown.
   * @param directory
   *          A <code>File</code> instance for the directory where the repository is to be cloned.
   *          If null is passed, a <code>NullPointerException</code> will be thrown.
   * @return The result of the git clone.
   * @throws IOException
   *           There are many reasons for which an <code>IOException</code> may be thrown.
   *           Examples include:
   *           <ul>
   *           <li>a directory doesn't exist</li>
   *           <li>a command is not found on the PATH</li>
   *           </ul>
   * @throws JavaGitException
   *           Thrown when there is an error executing git-clone.
   */
  public GitCloneResponse clone(File workingDirectoryPath, GitCloneOptions options, String repository,
      File directory) throws IOException, JavaGitException;
}
