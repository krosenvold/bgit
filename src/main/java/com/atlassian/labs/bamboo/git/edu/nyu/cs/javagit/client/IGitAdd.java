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
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitAddOptions;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitAddResponse;


/**
 * An interface to represent the git-add command.
 */
public interface IGitAdd {

  /**
   * Adds list of files to the index.
   * 
   * @param repositoryPath
   *          File path pointing to the root of the repository
   * @param options
   *          Object containing all the options that need to be passed to &lt;git-add&gt; command.
   * @param paths
   *          List of file paths that are going to be added to the index.
   * @return GitAddResponse object.
   * @throws JavaGitException Thrown when there is an error while running the add command.
   * @throws IOException There are many reasons for which an <code>IOException</code>
   *           may be thrown. Examples include:
   *           <ul>
   *           <li>a directory doesn't exist</li>
   *           <li>access to a file is denied</li>
   *           <li>a command is not found on the PATH</li>
   *           </ul>
   */
  public GitAddResponse add(File repositoryPath, GitAddOptions options, List<File> paths)
      throws JavaGitException, IOException;

  /**
   * Adds a file to the index.
   * 
   * @param repositoryPath
   *          File path pointing to the root of the repository
   * @param options
   *          Object containing all the options that need to be passed to &lt;git-add&gt; command.
   * @param file
   *          File to be added to the index.
   * @return GitAddResponse object.
   * @throws JavaGitException Thrown when there is an error while running the add command.
   * @throws IOException
   *           There are many reasons for which an <code>IOException</code> may be thrown.
   *           Examples include:
   *           <ul>
   *           <li>a directory doesn't exist</li>
   *           <li>access to a file is denied</li>
   *           <li>a command is not found on the PATH</li>
   *           </ul>
   */
  public GitAddResponse add(File repositoryPath, GitAddOptions options, File file)
      throws JavaGitException, IOException;

  /**
   * Dry run for adding a list of files to the index.
   * 
   * @param repositoryPath
   *          File path pointing to the root of the repository
   * @param paths
   *          List of file paths that are going to be added to the index.
   * @return GitAddResponse object.
   * @throws JavaGitException
   *           Thrown when there is an error while running the add command.
   * @throws IOException
   *           There are many reasons for which an <code>IOException</code> may be thrown.
   *           Examples include:
   *           <ul>
   *           <li>a directory doesn't exist</li>
   *           <li>access to a file is denied</li>
   *           <li>a command is not found on the PATH</li>
   *           </ul>
   */
  public GitAddResponse addDryRun(File repositoryPath, List<File> paths) throws JavaGitException,
      IOException;

  /**
   * Adds list of files to the index in verbose mode.
   * 
   * @param repositoryPath
   *          File path pointing to the root of the repository
   * @param paths
   *          List of file paths that are going to be added to the index.
   * @return GitAddResponse object.
   * @throws JavaGitException
   *           Thrown when there is an error while running the add command.
   * @throws IOException
   *           There are many reasons for which an <code>IOException</code> may be thrown.
   *           Examples include:
   *           <ul>
   *           <li>a directory doesn't exist</li>
   *           <li>access to a file is denied</li>
   *           <li>a command is not found on the PATH</li>
   *           </ul>
   */
  public GitAddResponse addVerbose(File repositoryPath, List<File> paths) throws JavaGitException,
      IOException;

  /**
   * Adds list of files to the index with Force option set.
   * 
   * @param repositoryPath
   *          File path pointing to the root of the repository
   * @param paths
   *          List of file paths that are going to be added to the index.
   * @return GitAddResponse object.
   * @throws JavaGitException Thrown when there is an error while running the add command.
   * @throws IOException There are many reasons for which an <code>IOException</code>
   *           may be thrown. Examples include:
   *           <ul>
   *           <li>a directory doesn't exist</li>
   *           <li>access to a file is denied</li>
   *           <li>a command is not found on the PATH</li>
   *           </ul>
   */
  public GitAddResponse addWithForce(File repositoryPath, List<File> paths)
      throws JavaGitException, IOException;

  /**
   * Adds a file to the index with no options set.
   * 
   * @param repositoryPath
   *          File path pointing to the root of the repository.
   * @param file
   *          File will added to the index.
   * @return GitAddResponse object.
   * @throws JavaGitException Thrown when there is an error while running the add command.
   * @throws IOException There are many reasons for which an <code>IOException</code>
   *           may be thrown. Examples include:
   *           <ul>
   *           <li>a directory doesn't exist</li>
   *           <li>access to a file is denied</li>
   *           <li>a command is not found on the PATH</li>
   *           </ul>
   */
  public GitAddResponse add(File repositoryPath, File file) throws JavaGitException, IOException;

  /**
   * Adds list of files to the index with no options set.
   * 
   * @param repositoryPath
   *          File path pointing to the root of the repository
   * @param paths
   *          List of file paths that are going to be added to the index.
   * @return GitAddResponse object.
   * @throws JavaGitException Thrown when there is an error while running the add command.
   * @throws IOException\ There are many reasons for which an <code>IOException</code>
   *           may be thrown. Examples include:
   *           <ul>
   *           <li>a directory doesn't exist</li>
   *           <li>access to a file is denied</li>
   *           <li>a command is not found on the PATH</li>
   *           </ul>
   */
  public GitAddResponse add(File repositoryPath, List<File> paths) throws JavaGitException,
      IOException;

}
