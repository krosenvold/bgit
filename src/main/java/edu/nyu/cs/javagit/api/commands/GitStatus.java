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
package edu.nyu.cs.javagit.api.commands;

import java.io.File;
import java.io.IOException;
import java.util.List;

import edu.nyu.cs.javagit.api.GitFileSystemObject;
import edu.nyu.cs.javagit.api.JavaGitException;
import edu.nyu.cs.javagit.client.ClientManager;
import edu.nyu.cs.javagit.client.IClient;
import edu.nyu.cs.javagit.client.IGitStatus;
import edu.nyu.cs.javagit.utilities.CheckUtilities;

/**
 * <code>GitStatus</code> provides an API to status of a git repository.
 */
public final class GitStatus {

  /**
   * It returns a <code>GitStatusResponse</code> object that contains all the details of the
   * output of &lt;git-status&gt; command.
   * 
   * @param repositoryPath
   *          Directory pointing to the root of the repository.
   * @param options
   *          Options that are passed to &lt;git-status&gt; command.
   * @param paths
   *          List of paths or patterns
   * @return <code>GitStatusResponse</code> object
   * @throws JavaGitException
   *           Exception thrown if the repositoryPath is null or if options or paths are invalid.
   * @throws IOException
   *           Exception is thrown if any of the IO operations fail.
   */
  public GitStatusResponse status(File repositoryPath, GitStatusOptions options, List<File> paths)
      throws JavaGitException, IOException {
    CheckUtilities.checkFileValidity(repositoryPath);
    IClient client = ClientManager.getInstance().getPreferredClient();
    IGitStatus gitStatus = client.getGitStatusInstance();
    return gitStatus.status(repositoryPath, options, paths);
  }

  /**
   * It returns a <code>GitStatusResonse</code> object that contains all the details of the output
   * of &lt;git-status&gt; command. It has no options passed as parameter to the method.
   * 
   * @param repositoryPath
   *          Directory path to the root of the repository.
   * @param paths
   *          List of paths or file patterns.
   * @return <code>GitStatusResponse</code> object
   * @throws JavaGitException
   *           Exception thrown if the repositoryPath is null or if options or paths are invalid.
   * @throws IOException
   *           Exception is thrown if any of the IO operations fail.
   */
  public GitStatusResponse status(File repositoryPath, List<File> paths) throws JavaGitException,
      IOException {
    CheckUtilities.checkFileValidity(repositoryPath);
    IClient client = ClientManager.getInstance().getPreferredClient();
    IGitStatus gitStatus = client.getGitStatusInstance();
    return gitStatus.status(repositoryPath, paths);
  }

  /**
   * It returns a <code>GitStatusResonse</code> object that contains all the details of the output
   * of &lt;git-status&gt; command. It has no path passed as parameter to the method.
   * 
   * @param repositoryPath
   *          Directory path to the root of the repository.
   * @param options
   *          Options that are passed to &lt;git-status&gt; command.
   * @return <code>GitStatusResponse</code> object
   * @throws JavaGitException
   *           Exception thrown if the repositoryPath is null or if options or paths are invalid.
   * @throws IOException
   *           Exception is thrown if any of the IO operations fail.
   */
  public GitStatusResponse status(File repositoryPath, GitStatusOptions options)
      throws JavaGitException, IOException {
    CheckUtilities.checkFileValidity(repositoryPath);
    IClient client = ClientManager.getInstance().getPreferredClient();
    IGitStatus gitStatus = client.getGitStatusInstance();
    return gitStatus.status(repositoryPath, options);
  }

  /**
   * It returns a <code>GitStatusResonse</code> object that contains all the details of the output
   * of &lt;git-status&gt; command. Instead of passing a list of paths, this method takes a
   * <code>String</code> argument to a file-path.
   * 
   * @param repositoryPath
   *          Directory path to the root of the repository.
   * @param options
   *          Options that are passed to &lt;git-status&gt; command.
   * @param file
   *          Filename or directory
   * @return <code>GitStatusResponse</code> object
   * @throws JavaGitException
   *           Exception thrown if the repositoryPath is null or if options or paths are invalid.
   * @throws IOException
   *           Exception is thrown if any of the IO operations fail.
   */
  public GitStatusResponse status(File repositoryPath, GitStatusOptions options, File file)
      throws JavaGitException, IOException {
    CheckUtilities.checkFileValidity(repositoryPath);
    CheckUtilities.checkFileValidity(file);
    IClient client = ClientManager.getInstance().getPreferredClient();
    IGitStatus gitStatus = client.getGitStatusInstance();
    return gitStatus.status(repositoryPath, options, file);
  }

  /**
   * It returns a <code>GitStatusResonse</code> object that contains all the details of the output
   * of &lt;git-status&gt; command. It does not pass any options or file paths. This is the most
   * generic execution of &lt;git-status&gt; command.
   * 
   * @param repositoryPath
   *          Directory path to the root of the repository.
   * @return <code>GitStatusResponse</code> object
   * @throws JavaGitException
   *           Exception thrown if the repositoryPath is null or if options or paths are invalid.
   * @throws IOException
   *           Exception is thrown if any of the IO operations fail.
   */
  public GitStatusResponse status(File repositoryPath) throws JavaGitException, IOException {
    CheckUtilities.checkFileValidity(repositoryPath);
    IClient client = ClientManager.getInstance().getPreferredClient();
    IGitStatus gitStatus = client.getGitStatusInstance();
    return gitStatus.status(repositoryPath);
  }

  /**
   * It returns a <code>GitStatusResonse</code> object that contains all the details of the output
   * of &lt;git-status&gt; command. It is equivalent of executing &lt;git-status&gt; command with
   * '-a' option.
   * 
   * @param repositoryPath
   *          Directory path to the root of the repository.
   * @return <code>GitStatusResponse</code> object
   * @throws JavaGitException
   *           Exception thrown if the repositoryPath is null or if options or paths are invalid.
   * @throws IOException
   *           Exception is thrown if any of the IO operations fail.
   */
  public GitStatusResponse statusAll(File repositoryPath) throws JavaGitException, IOException {
    CheckUtilities.checkFileValidity(repositoryPath);
    IClient client = ClientManager.getInstance().getPreferredClient();
    IGitStatus gitStatus = client.getGitStatusInstance();
    return gitStatus.statusAll(repositoryPath);
  }
  
  /**
   * It returns a <code>GitStatusResonse</code> object that contains all the details of the output
   * of &lt;git-status&gt; command, but filters output for a single file
   *
   * @param repositoryPath
   *          Directory path to the root of the repository.
   * @param path
   * @return <code>GitStatusResponse</code> object
   * @throws JavaGitException
   *           Exception thrown if the repositoryPath is null
   * @throws IOException
   *           Exception is thrown if any of the IO operations fail.
   */
  public GitStatusResponse getSingleFileStatus(File repositoryPath, File path) throws JavaGitException, IOException {
    CheckUtilities.checkFileValidity(repositoryPath);
    IClient client = ClientManager.getInstance().getPreferredClient();
    IGitStatus gitStatus = client.getGitStatusInstance();
    return gitStatus.getSingleFileStatus(repositoryPath, null, path);
  }

  /**
   * It returns a <code>GitFileSystemObject.Status</code> for a single file
   *
   * @param repositoryPath
   *          Directory path to the root of the repository.
   * @param path
   * @return <code>GitFileSystemObject.Status</code>
   * @throws JavaGitException
   *           Exception thrown if the repositoryPath is null
   * @throws IOException
   *           Exception is thrown if any of the IO operations fail.
   */
  public GitFileSystemObject.Status getFileStatus(File repositoryPath, File path) throws JavaGitException, IOException {
    //check validity of a repository path
    CheckUtilities.checkFileValidity(repositoryPath);

    //run git status
    IClient client = ClientManager.getInstance().getPreferredClient();
    IGitStatus gitStatus = client.getGitStatusInstance();
    GitStatusResponse response = gitStatus.status(repositoryPath);

    return response.getFileStatus(path);
    /*
     * TODO: quote from Michael Schidlowsky: "this block of if statements is a little smelly... I'd
     * prefer to see something like return response.asStatus()...
     */
    /*
    if (response.getUntrackedFilesSize() > 0) {
      return Status.UNTRACKED;
    }

    if (response.getNewFilesToCommitSize() > 0) {
      return Status.NEW_TO_COMMIT;
    }

    if (response.getDeletedFilesNotUpdatedSize() > 0) {
      return Status.DELETED;
    }

    if (response.getDeletedFilesToCommitSize() > 0) {
      return Status.DELETED_TO_COMMIT;
    }

    if (response.getModifiedFilesNotUpdatedSize() > 0) {
      return Status.MODIFIED;
    }

    if (response.getModifiedFilesToCommitSize() > 0) {
      return Status.MODIFIED_TO_COMMIT;
    }

    // default
    return Status.IN_REPOSITORY;
    */
  }
}
