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

import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitRmResponse;


/**
 * Implementation of <code>GitRmResponse</code> that includes setter methods for all fields.
 */
public final class GitRmResponseImpl extends GitRmResponse {

  // TODO (jhl388): Add test cases for this class.

  /**
   * Adds the file to the removed files list.
   * 
   * @param file
   *          The file to add to the removed files list.
   */
  public void addFileToRemovedFilesList(File file) {
    removedFiles.add(file);
  }
  
}
