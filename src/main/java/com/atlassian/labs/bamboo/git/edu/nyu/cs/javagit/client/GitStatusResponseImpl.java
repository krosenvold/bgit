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

import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.Ref;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.GitFileSystemObject.Status;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitStatusResponse;


/**
 * Implementation of a <code>GitStatusResponse</code>. This class adds functionality to set
 * values in a <code>GitStatusResponse</code>
 */
public class GitStatusResponseImpl extends GitStatusResponse {

  /**
   * Constructor
   */
  public GitStatusResponseImpl(String repositoryPath) {
    super(repositoryPath);
  }

  /**
   * Sets the branch name in <code>GitStatusResponse</code>. <code>NullPointerException</code>
   * is thrown if branch is null;
   * 
   * @param branch
   *          <code>Ref</code> of <code>RefType.BRANCH</code>.
   */
  public void setBranch(Ref branch) {
    this.branch = branch;
  }

  /**
   * Sets the error message in the response object.
   * 
   * @param lineNumber
   *          Output line number where the error message appeared.
   * @param errorMsg
   *          <code>String</code> Error Message
   */
  public void setError(int lineNumber, String errorMsg) {
    errors.add(new ErrorDetails(lineNumber, errorMsg));
  }

  /**
   * Sets the <code>String</code> message in <code>GitStatusResponse</code>.
   * <code>NullPointerException</code> is thrown if message is null.
   * 
   * @param message
   *          Output message saved from the &lt;git-status&gt; command.
   */
  public void setStatusOutputComment(String message) {
    this.message = message;
  }

  /**
   * Adds a file to list of files that are deleted and will be committed next time
   * &lt;git-commit&gt; is run.
   * 
   * @param file
   *          <code>File</code> to be added to the list.
   */
  public void addToDeletedFilesToCommit(File file) {
    deletedFilesToCommit.add(file);
    fileToStatus.put(file, Status.DELETED_TO_COMMIT);
  }

  /**
   * Adds a file to the list of files that are deleted locally but not yet deleted from index using
   * &lt;git-rm&gt; command.
   * 
   * @param file
   *          <code>File</code> to be added to the list.
   */
  public void addToDeletedFilesNotUpdated(File file) {
    deletedFilesNotUpdated.add(file);
    fileToStatus.put(file, Status.DELETED);
  }

  /**
   * Adds a file to list of files that are modified and will be committed next time
   * &lt;git-commit&gt; is run.
   * 
   * @param file
   *          <code>File</code> to be added to the list.
   */
  public void addToModifiedFilesToCommit(File file) {
    modifiedFilesToCommit.add(file);
    fileToStatus.put(file, Status.MODIFIED_TO_COMMIT);
  }

  /**
   * Adds a file to the list of files that are modified files but not yet updated.
   * 
   * @param file
   *          <code>File</code> to be added to the list.
   */
  public void addToModifiedFilesNotUpdated(File file) {
    modifiedFilesNotUpdated.add(file);
    fileToStatus.put(file, Status.MODIFIED);
  }

  /**
   * Adds a file to the list of new files that are ready to be committed next time &lt;git-commit&gt;
   * command is run.
   * 
   * @param file
   *          <code>File</code> to be added to the list.
   */
  public void addToNewFilesToCommit(File file) {
    newFilesToCommit.add(file);
    fileToStatus.put(file, Status.NEW_TO_COMMIT);
  }
  
  /**
   * Adds a file to the list of renamed files that are ready to be committed next time &lt;git-commit&gt;
   * command is run.
   * 
   * @param file
   *          <code>File</code> to be added to the list.
   */
  public void addToRenamedFilesToCommit(File file) {
    renamedFilesToCommit.add(file);
    fileToStatus.put(file, Status.RENAMED_TO_COMMIT);
  }  

  /**
   * Adds a file to list of files that have been added locally but not yet added to the index.
   * 
   * @param file
   *          <code>File</code> to be added to the list.
   */
  public void addToUntrackedFiles(File file) {
    untrackedFiles.add(file);
    fileToStatus.put(file, Status.UNTRACKED);
  }
}
