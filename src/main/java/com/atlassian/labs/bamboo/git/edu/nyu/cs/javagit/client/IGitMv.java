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
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitMvOptions;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitMvResponse;


/**
 * An interface to represent the git-mv command.
 */
public interface IGitMv {
  /**
   * Moves the specified source file/symlink/directory to the destination file/symlink/directory. If
   * destination is non-existant then same as rename.
   * 
   * @param repositoryPath
   *          A <code>File</code> instance for the path to the repository root (the parent
   *          directory of the .git directory) or a sub-directory in the working tree of the
   *          repository to move/rename against. This argument must represent the absolute path to 
   *          the desired directory as returned by the <code>File.getPath()</code> method. If null 
   *          is passed, a <code>NullPointerException</code> will be thrown.
   * @param source
   *          The source file/folder/symlink which is to be renamed or moved to a different
   *          location. It should be relative to the path specified in the <code>repository</code> 
   *          parameter as returned by <code>File.getPath()</code>. A non-zero length argument is 
   *          required for this parameter, otherwise a <code>NullPointerException</code> or 
   *          <code>IllegalArgumentException</code> will be thrown.
   * @param destination
   *          The destination file/folder/symlink which the source is renamed or moved to. It 
   *          should be relative to the path specified in the <code>repository</code> 
   *          parameter as returned by <code>File.getPath()</code>. A non-zero length argument is 
   *          required for this parameter, otherwise a <code>NullPointerException</code> or 
   *          <code>IllegalArgumentException</code> will be thrown.
   * @return The results from the git-mv. 
   *           It is expected that GitMv does not notify when a move was successful. This follows 
   *           the response that git-mv itself gives. If the move/rename fails for any reason, 
   *           proper exception messages are generated and thrown.
   * @exception IOException
   *              There are many reasons for which an <code>IOException</code> may be thrown.
   *              Examples include:
   *              <ul>
   *              <li>access to a file is denied</li>
   *              <li>a command is not found on the PATH</li>
   *              </ul>
   * @exception JavaGitException
   *              Thrown when there is an error excecuting git-mv.
   */
  public GitMvResponse mv(File repositoryPath, File source, File destination)
      throws IOException, JavaGitException;
  
  /**
   * Moves the specified source file/symlink/directory to the destination file/symlink/directory. If
   * destination is non-existant then same as rename.
   * 
   * @param repositoryPath
   *          A <code>File</code> instance for the path to the repository root (the parent
   *          directory of the .git directory) or a sub-directory in the working tree of the
   *          repository to move/rename against. This argument must represent the absolute path to 
   *          the desired directory as returned by the <code>File.getPath()</code> method. If null 
   *          is passed, a <code>NullPointerException</code> will be thrown.
   * @param options
   *          The options to git-mv command.
   * @param source
   *          The source file/folder/symlink which is to be renamed or moved to a different
   *          location. It should be relative to the path specified in the <code>repository</code> 
   *          parameter as returned by <code>File.getPath()</code>. A non-zero length argument is 
   *          required for this parameter, otherwise a <code>NullPointerException</code> or 
   *          <code>IllegalArgumentException</code> will be thrown.
   * @param destination
   *          The destination file/folder/symlink which the source is renamed or moved to. It 
   *          should be relative to the path specified in the <code>repository</code> 
   *          parameter as returned by <code>File.getPath()</code>. A non-zero length argument is 
   *          required for this parameter, otherwise a <code>NullPointerException</code> or 
   *          <code>IllegalArgumentException</code> will be thrown.
   * @return The results from the git-mv. 
   *           It is expected that GitMv does not notify when a move was successful. This follows 
   *           the response that git-mv itself gives. If the move/rename fails for any reason, 
   *           proper exception messages are generated and thrown.
   * @exception IOException
   *              There are many reasons for which an <code>IOException</code> may be thrown.
   *              Examples include:
   *              <ul>
   *              <li>access to a file is denied</li>
   *              <li>a command is not found on the PATH</li>
   *              </ul>
   * @exception JavaGitException
   *              Thrown when there is an error excecuting git-mv.
   */
  public GitMvResponse mv(File repositoryPath, GitMvOptions options, File source,
      File destination) throws IOException, JavaGitException;

  /**
   * Moves the specified source files/symlinks/directories to the destination directory. 
   * 
   * @param repositoryPath
   *          A <code>File</code> instance for the path to the repository root (the parent
   *          directory of the .git directory) or a sub-directory in the working tree of the
   *          repository to move/rename against. This argument must represent the absolute path to 
   *          the desired directory as returned by the <code>File.getPath()</code> method. If null 
   *          is passed, a <code>NullPointerException</code> will be thrown.
   * @param sources
   *          The <code>List</code> of source file/folder/symlink which are to be moved to a 
   *          different location. The paths specified in this list must all be relative to the 
   *          path specified in the <code>repository</code> parameter as returned by 
   *          <code>File.getPath()</code>. A non-zero length argument is required for this 
   *          parameter, otherwise a <code>NullPointerException</code> or 
   *          <code>IllegalArgumentException</code> will be thrown.
   * @param destination
   *          The destination file/folder/symlink which the source is renamed or moved to. It 
   *          should be relative to the path specified in the <code>repository</code> 
   *          parameter as returned by <code>File.getPath()</code>. A non-zero length argument is 
   *          required for this parameter, otherwise a <code>NullPointerException</code> or 
   *          <code>IllegalArgumentException</code> will be thrown.
   * @return The results from the git-mv. 
   *           It is expected that GitMv does not notify when a move was successful. This follows 
   *           the response that git-mv itself gives. If the move/rename fails for any reason, 
   *           proper exception messages are generated and thrown.
   * @exception IOException
   *              There are many reasons for which an <code>IOException</code> may be thrown.
   *              Examples include:
   *              <ul>
   *              <li>access to a file is denied</li>
   *              <li>a command is not found on the PATH</li>
   *              </ul>
   * @exception JavaGitException
   *              Thrown when there is an error executing git-mv.
   */
  public GitMvResponse mv(File repositoryPath, List<File> sources, File destination) 
      throws IOException, JavaGitException;
  
  /**
   * Moves the specified source files/symlinks/directories to the destination directory. 
   * 
   * @param repositoryPath
   *          A <code>File</code> instance for the path to the repository root (the parent
   *          directory of the .git directory) or a sub-directory in the working tree of the
   *          repository to move/rename against. This argument must represent the absolute path to 
   *          the desired directory as returned by the <code>File.getPath()</code> method. If null 
   *          is passed, a <code>NullPointerException</code> will be thrown.
   * @param options
   *          The options to git-mv command.
   * @param sources
   *          The <code>List</code> of source file/folder/symlink which are to be moved to a 
   *          different location. The paths specified in this list must all be relative to the 
   *          path specified in the <code>repository</code> parameter as returned by 
   *          <code>File.getPath()</code>. A non-zero length argument is required for this 
   *          parameter, otherwise a <code>NullPointerException</code> or 
   *          <code>IllegalArgumentException</code> will be thrown.
   * @param destination
   *          The destination file/folder/symlink which the source is renamed or moved to. It 
   *          should be relative to the path specified in the <code>repository</code> 
   *          parameter as returned by <code>File.getPath()</code>. A non-zero length argument is 
   *          required for this parameter, otherwise a <code>NullPointerException</code> or 
   *          <code>IllegalArgumentException</code> will be thrown.
   * @return The results from the git-mv. 
   *           It is expected that GitMv does not notify when a move was successful. This follows 
   *           the response that git-mv itself gives. If the move/rename fails for any reason, 
   *           proper exception messages are generated and thrown.
   * @exception IOException
   *              There are many reasons for which an <code>IOException</code> may be thrown.
   *              Examples include:
   *              <ul>
   *              <li>access to a file is denied</li>
   *              <li>a command is not found on the PATH</li>
   *              </ul>
   * @exception JavaGitException
   *              Thrown when there is an error executing git-mv.
   */
  public GitMvResponse mv(File repositoryPath, GitMvOptions options, List<File> sources,
      File destination) throws IOException, JavaGitException;
}
