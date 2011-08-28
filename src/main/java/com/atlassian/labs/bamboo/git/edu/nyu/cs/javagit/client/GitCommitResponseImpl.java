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
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitCommitResponse;


/**
 * Implementation of a <code>GitCommitResponse</code>. This class adds functionality to set
 * values in a <code>GitCommitResponse</code>.
 */
public class GitCommitResponseImpl extends GitCommitResponse {

  /**
   * Constructor.
   * 
   * @param shortHashName
   *          The short hash name
   * @param shortComment
   */
  public GitCommitResponseImpl(Ref shortHashName, String shortComment) {
    super(shortHashName, shortComment);
  }

  /**
   * Add the information about a newly added file in the repository for a given commit.
   * 
   * @param pathToFile
   *          The path to the file from the root of the repository.
   * @param mode
   *          The mode of the file.
   * @return False if the <code>pathToFile</code> is null or length zero. True otherwise.
   */
  public boolean addAddedFile(File pathToFile, String mode) {
    if (null == pathToFile) {
      return false;
    }

    return addedFiles.add(new AddedOrDeletedFile(pathToFile, mode));
  }

  /**
   * Add the information about a newly copied file in the repository for a given commit.
   * 
   * @param sourceFilePath
   *          The path to the source file.
   * @param destinationFilePath
   *          The path to the destination file.
   * @param percentage
   *          The percentage.
   * @return False if <code>sourceFilePath</code> or <code>destinationFilePath</code> is null or
   *         length zero. True otherwise.
   */
  public boolean addCopiedFile(File sourceFilePath, File destinationFilePath, int percentage) {
    if (null == sourceFilePath || null == destinationFilePath) {
      return false;
    }
    return copiedFiles.add(new CopiedOrMovedFile(sourceFilePath, destinationFilePath, percentage));
  }

  /**
   * Add the information about a file deleted from the repository for a given commit.
   * 
   * @param pathToFile
   *          The path to the file from the root of the repository.
   * @param mode
   *          The mode of the file.
   * @return False if the <code>pathToFile</code> is null or length zero. True otherwise.
   */
  public boolean addDeletedFile(File pathToFile, String mode) {
    if (null == pathToFile) {
      return false;
    }

    return deletedFiles.add(new AddedOrDeletedFile(pathToFile, mode));
  }

  /**
   * Add the information about a moved/renamed file in the repository for a given commit.
   * 
   * @param sourceFilePath
   *          The path to the source file.
   * @param destinationFilePath
   *          The path to the destination file.
   * @param percentage
   *          The percentage.
   * @return False if <code>sourceFilePath</code> or <code>destinationFilePath</code> is null or
   *         length zero. True otherwise.
   */
  public boolean addRenamedFile(File sourceFilePath, File destinationFilePath, int percentage) {
    if (null == sourceFilePath || null == destinationFilePath) {
      return false;
    }
    return renamedFiles.add(new CopiedOrMovedFile(sourceFilePath, destinationFilePath, percentage));
  }

  /**
   * Sets the number of files changed in the commit.
   * 
   * @param filesChanged
   *          The number of files changed in the commit.
   */
  public void setFilesChanged(int filesChanged) {
    this.filesChanged = filesChanged;
  }

  /**
   * Sets the number of files changed during a commit.
   * 
   * @param filesChangedStr
   *          The number of files changed in <code>String</code> format.
   * @return True if the <code>filesChangedStr</code> parameter is a <code>String</code>
   *         representing a number. False if the <code>String</code> does not contain a parsable
   *         integer.
   */
  public boolean setFilesChanged(String filesChangedStr) {
    try {
      this.filesChanged = Integer.parseInt(filesChangedStr);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  /**
   * Sets the number of lines deleted in the commit.
   * 
   * @param linesDeleted
   *          The number of lines deleted in the commit.
   */
  public void setLinesDeleted(int linesDeleted) {
    this.linesDeleted = linesDeleted;
  }

  /**
   * Sets the number of lines deleted in a commit.
   * 
   * @param linesDeletedStr
   *          The number of lines deleted in <code>String</code> format.
   * @return True if the <code>linesDeletedStr</code> parameter is a <code>String</code>
   *         representing a number. False if the <code>String</code> does not contain a parsable
   *         integer.
   */
  public boolean setLinesDeleted(String linesDeletedStr) {
    try {
      this.linesDeleted = Integer.parseInt(linesDeletedStr);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  /**
   * Sets the number of lines inserted in the commit.
   * 
   * @param linesInserted
   *          The number of lines inserted in the commit.
   */
  public void setLinesInserted(int linesInserted) {
    this.linesInserted = linesInserted;
  }

  /**
   * Sets the number of lines inserted in a commit.
   * 
   * @param linesInsertedStr
   *          The number of lines inserted in <code>String</code> format.
   * @return True if the <code>linesInsertedStr</code> parameter is a <code>String</code>
   *         representing a number. False if the <code>String</code> does not contain a parsable
   *         integer.
   */
  public boolean setLinesInserted(String linesInsertedStr) {
    try {
      this.linesInserted = Integer.parseInt(linesInsertedStr);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

}
