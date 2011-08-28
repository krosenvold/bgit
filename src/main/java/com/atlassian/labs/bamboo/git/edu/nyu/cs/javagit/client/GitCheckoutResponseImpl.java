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
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitCheckoutResponse;


/**
 * This class implements <code>GitCheckoutResponse</code> by setting values in
 * <code>GitCheckoutResponse</code>.
 */
public class GitCheckoutResponseImpl extends GitCheckoutResponse {

  /**
   * Sets the new branch name that is created by &lt;git-checkout&gt using -b option
   * 
   * @param newBranch
   *          Name of the new branch created
   */
  public void setNewBranch(Ref newBranch) {
    this.newBranch = newBranch;
  }

  /**
   * Sets the branch to the branch, to which the &lt;git-checkout&gt switched the repository to.
   * This branch should already be existing in the repository. To create a new branch and switch to
   * it, use the -b option while running &lt;git-checkout&gt.
   * 
   * @param branch
   */
  public void setBranch(Ref branch) {
    this.branch = branch;
  }

  /**
   * Adds the modified file to the list of modifiedFiles. When a file is modified locally but has
   * not been committed to the repository and if we try to switch the branch to another branch, the
   * &lt;git-checkout&gt fails and outputs the list of modified files that are not yet committed
   * unless -f option is used by &lt;git-checkout&gt.
   * 
   * @param file
   */
  public void addModifiedFile(File file) {
    modifiedFiles.add(file);
  }

  /**
   * Adds the newly added file to the list of addedFiles. A newly added file is the one that is
   * added by &lt;git-add&gt; command but had not been committed.
   * 
   * @param file
   */
  public void addAddedFile(File file) {
    addedFiles.add(file);
  }

  /**
   * Adds the locally deleted file to the list of deletedFiles. A locally deleted file is one that
   * has been removed but has not been removed from repository using &lt;git-rm&gt; command.
   * 
   * @param file
   */
  public void addDeletedFile(File file) {
    deletedFiles.add(file);
  }

}
