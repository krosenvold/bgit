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
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitResetResponse;


/**
 * Implementation of a <code>GitResetResponse</code> abstract class. This class adds functionality
 * to set values in a <code>GitResetResponse</code>.
 */
public class GitResetResponseImpl extends GitResetResponse {

  // TODO (jhl388): Add test cases for this class.

  /**
   * Default constructor.
   */
  public GitResetResponseImpl() {
    super();
  }

  public GitResetResponseImpl(Ref newHeadSha1, String newHeadShortMessage) {
    super(newHeadSha1, newHeadShortMessage);
  }

  /**
   * Adds the file to the files needing update list.
   * 
   * @param file
   *          The file to add to the files needing update list.
   */
  public void addFileToFilesNeedingUpdateList(File file) {
    filesNeedingUpdate.add(file);
  }

}
