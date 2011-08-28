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

import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.utilities.CheckUtilities;


/**
 * <code>GitRmResponse</code> holds the response information returned by the <code>GitRm</code>
 * class.
 */
public abstract class GitRmResponse implements CommandResponse {

  // The list of removed files.
  protected List<File> removedFiles = new ArrayList<File>();

  /**
   * Gets the file at the specified index from the removed file list.
   * 
   * @param index
   *          The index of the file to get. It must fall in the range:
   *          <code>0 &lt;= index &lt; getRemovedFilesSize()</code>.
   * @return The file at the specified index.
   */
  public File getRemovedFile(int index) {
    CheckUtilities.checkIntIndexInListRange(removedFiles, index);
    return removedFiles.get(index);
  }

  /**
   * Gets an <code>Iterator</code> over the list of removed files.
   * 
   * @return An <code>Iterator<code> over the list of removed files.
   */
  public Iterator<File> getRemovedFilesIterator() {
    return (new ArrayList<File>(removedFiles)).iterator();
  }

  /**
   * Gets the number of removed files (provided that the quiet option was not used).
   * 
   * @return The number of removed files. If the quiet option was used, zero (0) will be returned.
   */
  public int getRemovedFilesSize() {
    return removedFiles.size();
  }

}
