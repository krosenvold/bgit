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
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitStatusOptions;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitStatusResponse;


/**
 * An interface to represent the &lt;git-status&gt; command.
 */
public interface IGitStatus {

  /**
   * 
   * This method returns <code>GitStatusResponse</code> object after parsing the options and then
   * executing the &lt;git-status&gt; command.
   * 
   * @param repositoryPath
   *          Path to the root of the repository
   * @param options
   *          <code>GitStatusOptions</code> Options passed to &lt;git-status&gt; command
   * @param paths
   *          <code>List</code> of files or file-pattern
   * @return <code>GitStatusResponse</code> Response object returned by &lt;git-status&gt;
   *         command.
   * @throws <code>JavaGitException</code> Thrown when there is an error while running the
   *           &lt;git-status&gt; command.
   * @throws <code>IOException</code> There are many reasons for which an <code>IOException</code>
   *           may be thrown. Examples include:
   *           <ul>
   *           <li>a directory doesn't exist</li>
   *           <li>access to a file is denied</li>
   *           </ul>
   */
  public GitStatusResponse status(File repositoryPath, GitStatusOptions options, List<File> paths)
      throws JavaGitException, IOException;

  /**
   * 
   * This method returns <code>GitStatusResponse</code> object after parsing the options and then
   * executing the &lt;git-status&gt; command.
   * 
   * @param repositoryPath
   *          Path to the root of the repository
   * @param options
   *          <code>GitStatusOptions</code> Options passed to &lt;git-status&gt; command
   * @param file
   *          <code>File</code> whose status need to be checked.
   * @return <code>GitStatusResponse</code> Response object returned by &lt;git-status&gt;
   *         command.
   * @throws <code>JavaGitException</code> Thrown when there is an error while running the
   *           &lt;git-status&gt; command.
   * @throws <code>IOException</code> There are many reasons for which an <code>IOException</code>
   *           may be thrown. Examples include:
   *           <ul>
   *           <li>a directory doesn't exist</li>
   *           <li>access to a file is denied</li>
   *           </ul>
   */
  public GitStatusResponse status(File repositoryPath, GitStatusOptions options, File file)
      throws JavaGitException, IOException;

  /**
   * 
   * This method returns <code>GitStatusResponse</code> object after parsing the options and then
   * executing the &lt;git-status&gt; command.
   * 
   * @param repositoryPath
   *          Path to the root of the repository
   * @param options
   *          <code>GitStatusOptions</code> Options passed to &lt;git-status&gt; command
   * @return <code>GitStatusResponse</code> Response object returned by &lt;git-status&gt;
   *         command.
   * @throws <code>JavaGitException</code> Thrown when there is an error while running the
   *           &lt;git-status&gt; command.
   * @throws <code>IOException</code> There are many reasons for which an <code>IOException</code>
   *           may be thrown. Examples include:
   *           <ul>
   *           <li>a directory doesn't exist</li>
   *           <li>access to a file is denied</li>
   *           </ul>
   */
  public GitStatusResponse status(File repositoryPath, GitStatusOptions options)
      throws JavaGitException, IOException;

  /**
   * 
   * This method returns <code>GitStatusResponse</code> object after parsing the options and then
   * executing the &lt;git-status&gt; command.
   * 
   * @param repositoryPath
   *          Path to the root of the repository
   * @param paths
   *          <code>List</code> of files or file-pattern
   * @return <code>GitStatusResponse</code> Response object returned by &lt;git-status&gt;
   *         command.
   * @throws <code>JavaGitException</code> Thrown when there is an error while running the
   *           &lt;git-status&gt; command.
   * @throws <code>IOException</code> There are many reasons for which an <code>IOException</code>
   *           may be thrown. Examples include:
   *           <ul>
   *           <li>a directory doesn't exist</li>
   *           <li>access to a file is denied</li>
   *           </ul>
   */
  public GitStatusResponse status(File repositoryPath, List<File> paths) throws JavaGitException,
      IOException;
  
  /**
   * 
   * This method returns <code>GitStatusResponse</code> object with no options passed to
   * &lt;git-status&gt; command.
   * 
   * @param repositoryPath
   *          Path to the root of the repository
   * @return <code>GitStatusResponse</code> Response object returned by &lt;git-status&gt;
   *         command.
   * @throws <code>JavaGitException</code> Thrown when there is an error while running the
   *           &lt;git-status&gt; command.
   * @throws <code>IOException</code> There are many reasons for which an <code>IOException</code>
   *           may be thrown. Examples include:
   *           <ul>
   *           <li>a directory doesn't exist</li>
   *           <li>access to a directory/file is denied</li>
   *           </ul>
   */
  public GitStatusResponse status(File repositoryPath) throws JavaGitException, IOException;

  /**
   * 
   * This method returns <code>GitStatusResponse</code> object with options set to all(-a).
   * 
   * @param repositoryPath
   *          Path to the root of the repository
   * @return <code>GitStatusResponse</code> Response object returned by &lt;git-status&gt;
   *         command.
   * @throws <code>JavaGitException</code> Thrown when there is an error while running the
   *           &lt;git-status&gt; command.
   * @throws <code>IOException</code> There are many reasons for which an <code>IOException</code>
   *           may be thrown. Examples include:
   *           <ul>
   *           <li>a directory doesn't exist</li>
   *           <li>access to a directory/file is denied</li>
   *           </ul>
   */
  public GitStatusResponse statusAll(File repositoryPath) throws JavaGitException, IOException;
  

  /**
   * Return status for a single <code>File</code>
   * 
   * @param repositoryPath
   *          Directory path to the root of the repository.
   * @param options
   *          Options that are passed to &lt;git-status&gt; command.
   * @param file
   *          <code>File</code> instance 
   * @return <code>GitStatusResponse</code> object
   * @throws JavaGitException
   *           Exception thrown if the repositoryPath is null
   * @throws IOException
   *           Exception is thrown if any of the IO operations fail.
   */
  public GitStatusResponse getSingleFileStatus(File repositoryPath, GitStatusOptions options, File file) 
    throws JavaGitException, IOException;

}
