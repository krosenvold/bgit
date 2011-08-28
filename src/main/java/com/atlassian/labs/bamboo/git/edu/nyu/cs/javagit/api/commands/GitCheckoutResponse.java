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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.Ref;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.utilities.CheckUtilities;


/**
 * Response data object for &lt;git-checkout&gt; command.
 */
public abstract class GitCheckoutResponse implements CommandResponse {

  /**
   * List of files that have been modified but not committed.
   */
  protected final List<File> modifiedFiles;
  /**
   * List of files added by &lt;git-add&gt; but not committed.
   */
  protected final List<File> addedFiles;
  /**
   * List of files deleted but not committed.
   */
  protected final List<File> deletedFiles;

  /**
   * The new branch that is created and switched to by -b option provided by &lt;git-checkout&gt;.
   */
  protected Ref newBranch;
  /**
   * The name of the branch to which &lt;git-checkout&gt; switches.
   */
  protected Ref branch;

  /**
   * Constructor
   */
  protected GitCheckoutResponse() {
    modifiedFiles = new ArrayList<File>();
    addedFiles = new ArrayList<File>();
    deletedFiles = new ArrayList<File>();
  }

  /**
   * Returns the newly created branch by -b option by &lt;git-checkout&gt;.
   * 
   * @return Name of the new branch
   */
  public Ref getNewBranch() {
    return newBranch;
  }

  /**
   * Returns the branch to which &lt;git-checkout&gt; switches to.
   * 
   * @return Name of the branch
   */
  public Ref getBranch() {
    return branch;
  }

  /**
   * Returns iterator to the modified files list
   * 
   * @return iterator to the list.
   */
  public Iterator<File> getModifiedFilesIterator() {
    return (new ArrayList<File>(modifiedFiles).iterator());
  }

  /**
   * Gets iterator to the copy of addedFiles list.
   * 
   * @return iterator to the list.
   * 
   */
  public Iterator<File> getAddedFilesIterator() {
    return (new ArrayList<File>(addedFiles).iterator());
  }

  /**
   * Gets iterator to the copy of deletedFiles list.
   * 
   * @return iterator to the list.
   */
  public Iterator<File> getDeletedFilesIterator() {
    return (new ArrayList<File>(deletedFiles).iterator());
  }

  /**
   * Returns the file at a given location in the addedFiles list
   * 
   * @param index
   *          in the list and should be positive and less than no. of files added.
   * @return added file at the index in addedFiles list.
   */
  public File getAddedFile(int index) {
    CheckUtilities.checkIntIndexInListRange(addedFiles, index);
    return addedFiles.get(index);
  }

  /**
   * Returns the file at a given location in the deletedFiles list
   * 
   * @param index
   *          in the list and should be positive and less than no. of files deleted.
   * @return deleted file at the index in deleteFiles list.
   */
  public File getDeletedFile(int index) {
    CheckUtilities.checkIntIndexInListRange(deletedFiles, index);
    return deletedFiles.get(index);
  }

  /**
   * Returns the file at a given location in the modifiedFiles list.
   * 
   * @param index
   *          in the list and it should be positive and less than total no. of files modified.
   * @return modified file at the index in modifiedFiles list.
   */
  public File getModifiedFile(int index) {
    CheckUtilities.checkIntIndexInListRange(modifiedFiles, index);
    return modifiedFiles.get(index);
  }

  /**
   * Gets the total no. of files in addedFiles list.
   * 
   * @return no. of files
   */
  public int getNumberOfAddedFiles() {
    return addedFiles.size();
  }

  /**
   * Gets total no. of files in modifiedFiles list.
   * 
   * @return no. of files.
   */
  public int getNumberOfModifiedFiles() {
    return modifiedFiles.size();
  }

  /**
   * Gets total no. o files in addedFiles List.
   * 
   * @return no. of files.
   */
  public int getNumberOfDeletedFiles() {
    return deletedFiles.size();
  }
}
