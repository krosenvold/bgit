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

import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitAddResponse;


/**
 * Class implementing <code>GitAddResponse</code> for setting values in it.
 */
public class GitAddResponseImpl extends GitAddResponse {

  /**
   * Sets the value of no output flag.
   * 
   * @param noOutput
   *          true if there was no output generated. This is a helper flag that tells the consumer
   *          of <code>GitAddResponse</code> that there was no resulting output for executed
   *          &lt;git-add&gt; command.
   */
  public void setNoOutput(boolean noOutput) {
    this.noOutput = noOutput;
  }

  /**
   * Sets the flag if dry run flag need to be used
   * 
   * @param dryRun
   *          true if dry run should be used, otherwise false.
   */
  public void setDryRun(boolean dryRun) {
    this.dryRun = dryRun;
  }

  /**
   * Sets the non-error message generated in the output of the &lt;git-add&gt; command.
   * 
   * @param lineNumber
   *          line number at which the message appeared in output.
   * @param commentString
   *          message itself.
   */
  public void setComment(int lineNumber, String commentString) {
    ResponseString comment = new ResponseString(lineNumber, commentString);
    comments.add(comment);
  }

  /**
   * Adds a file and action to the list.
   * 
   * @param file
   *          File to be added to the <code>List</code>.
   */
  public void add(File file) {
    filePathsList.add(file);
  }

}
