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
 * A response data object for the <code>git-reset</code> command.
 */
public abstract class GitResetResponse implements CommandResponse {

  /*
   * The list of files left in a dirty state (different than what is in the new HEAD commit) in the
   * working tree.
   */
  protected List<File> filesNeedingUpdate = new ArrayList<File>();

  // If the --hard option was given, this is the SHA1 of the new head.
  protected Ref newHeadSha1 = null;

  // If the --hard option was given, this is the short message for the commit at the new head.
  protected String newHeadShortMessage = null;

  /**
   * Default constructor.
   */
  protected GitResetResponse() {
  }

  /**
   * Constructor that sets the SHA1 and short message of the new head commit.
   * 
   * @param newHeadSha1
   *          The SHA1 of the new head commit.
   * @param newHeadShortMessage
   *          The short message of the new head commit.
   */
  protected GitResetResponse(Ref newHeadSha1, String newHeadShortMessage) {
    this.newHeadSha1 = newHeadSha1;
    this.newHeadShortMessage = newHeadShortMessage;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof GitResetResponse)) {
      return false;
    }
  
    GitResetResponse grr = (GitResetResponse) o;
  
    if (!CheckUtilities.checkObjectsEqual(newHeadSha1, grr.getNewHeadSha1())) {
      return false;
    }
  
    if (!CheckUtilities.checkObjectsEqual(newHeadShortMessage, grr.getNewHeadShortMessage())) {
      return false;
    }
  
    if (!CheckUtilities.checkListsEqual(filesNeedingUpdate, grr.filesNeedingUpdate)) {
      return false;
    }
  
    return true;
  }

  /**
   * Gets the file at the specified index from the list of files needing update.
   * 
   * @param index
   *          The index of the file to get. It must fall in the range:
   *          <code>0 &lt;= index &lt; getRemovedFilesSize()</code>.
   * @return The file at the specified index.
   */
  public File getFileNeedingUpdate(int index) {
    CheckUtilities.checkIntIndexInListRange(filesNeedingUpdate, index);
    return filesNeedingUpdate.get(index);
  }

  /**
   * Gets an <code>Iterator</code> over the list of files needing update.
   * 
   * @return An <code>Iterator<code> over the list of files needing update.
   */
  public Iterator<File> getFilesNeedingUpdateIterator() {
    return (new ArrayList<File>(filesNeedingUpdate)).iterator();
  }

  /**
   * Gets the SHA1 of the new head commit. Only returned when the <code>--hard</code> option is
   * used.
   * 
   * @return The SHA1 of the new head commit.
   */
  public Ref getNewHeadSha1() {
    return newHeadSha1;
  }

  /**
   * Gets the short message of the new head commit. Only returned when the <code>--hard</code>
   * option is used.
   * 
   * @return The short message of the new head commit.
   */
  public String getNewHeadShortMessage() {
    return newHeadShortMessage;
  }

  /**
   * Gets the number of files needing update (provided that the quiet option was not used).
   * 
   * @return The number of files needing update. If the quiet option was used, zero (0) will be
   *         returned.
   */
  public int getRemovedFilesSize() {
    return filesNeedingUpdate.size();
  }

  @Override
  public int hashCode() {
    int ret = (null == newHeadSha1) ? 0 : newHeadSha1.hashCode();
    ret += (null == newHeadShortMessage) ? 0 : newHeadShortMessage.hashCode();
    for (File f : filesNeedingUpdate) {
      ret += f.hashCode();
    }
    return ret;
  }

  @Override
  public String toString() {
    StringBuffer buf = new StringBuffer();
    if (null != newHeadSha1) {
      buf.append("HEAD: ");
      buf.append(newHeadSha1);
      buf.append(" ");
      buf.append(newHeadShortMessage);
    }
    if (filesNeedingUpdate.size() > 0) {
      buf.append(filesNeedingUpdate);
    }
    return buf.toString();
  }

}
