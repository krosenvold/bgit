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

import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.utilities.ExceptionMessageMap;

/**
 * A class to manage passing commit arguments to the <code>GitCommit</code> command.
 */
public class GitCommitOptions {

  // TODO (jhl388): change the author string below to be an object.
  // TODO (jhl388): Add a unit test for this class (GitCommitOptions).
  // TODO (jhl388): Add a reset() method that resets all values to their default state.

  /*
   * TODO (jhl388): check if the include and only options are mutually exclusive. If so, fix the
   * setters for those variables accordingly.
   */

  // The --author option; Override the author's name used in the commit.
  private String author = null;

  // The -a or --all option
  private boolean optAll = false;

  /*
   * The -i or --include option; Stage the contents of the paths included in the method call before
   * committing.
   */
  private boolean optInclude = false;

  // The --no-verify option; bypass the pre-commit and commit-msg hooks.
  private boolean optNoVerify = false;

  /*
   * The -o or --only option; Commit only from the paths specified in the method call. This
   * disregards any contents that have been staged so far.
   */
  private boolean optOnly = false;

  // The -s or --signoff option; Add a Signed-off-by line at the bottom of the commit message.
  private boolean optSignoff = false;

  /**
   * Get the value to use as the author name for a commit.
   * 
   * @return The value to use as the author name for a commit.
   */
  public String getAuthor() {
    return author;
  }

  /**
   * Is the --all option set?
   * 
   * @return True if the --all option is set, false if it is not set.
   */
  public boolean isOptAll() {
    return optAll;
  }

  /**
   * Is the --include option set?
   * 
   * @return True if the --include option is set, false if it is not set.
   */
  public boolean isOptInclude() {
    return optInclude;
  }

  /**
   * Is the --no-verify option set?
   * 
   * @return True if the --no-verify option is set, false if it is not set.
   */
  public boolean isOptNoVerify() {
    return optNoVerify;
  }

  /**
   * Is the --signoff option set?
   * 
   * @return True if the --signoff option is set, false if it is not set.
   */
  public boolean isOptSignoff() {
    return optSignoff;
  }

  /**
   * Is the --only option set?
   * 
   * @return True if the --only option is set, false if it is not set.
   */
  public boolean isOptOnly() {
    return optOnly;
  }

  /**
   * Set the value to use as the author name for a commit.
   * 
   * @param author
   *          The value to use as the author name for a commit.
   */
  public void setAuthor(String author) {
    this.author = author.replace('<', ' ').replace('>', ' ');
  }

  /**
   * Set the --all option. An <code>IllegalArgumentException</code> is thrown if the only or
   * include options are set.
   * 
   * @param optAll
   *          True to set the --all option, false to unset it.
   */
  public void setOptAll(boolean optAll) {
    if (optOnly || optInclude) {
      throw new IllegalArgumentException(ExceptionMessageMap.getMessage("000100")
          + "  The \"all\" option can not be set when the \"only\" or \"include\" option is set.");
    }
    this.optAll = optAll;
  }

  /**
   * Set the --include option. An <code>IllegalArgumentException</code> is thrown if the all
   * option is set.
   * 
   * @param optInclude
   *          True to set the --include option, false to unset it.
   */
  public void setOptInclude(boolean optInclude) {
    if (optAll) {
      throw new IllegalArgumentException(ExceptionMessageMap.getMessage("000100")
          + "  The \"include\" option can not be set when the \"all\" option is set.");
    }
    this.optInclude = optInclude;
  }

  /**
   * Set the --no-verify option.
   * 
   * @param optNoVerify
   *          True to set the --no-verify option, false to unset it.
   */
  public void setOptNoVerify(boolean optNoVerify) {
    this.optNoVerify = optNoVerify;
  }

  /**
   * Set the --only option. An <code>IllegalArgumentException</code> is thrown if the all option
   * is set.
   * 
   * @param optOnly
   *          True to set the --only option, false to unset it.
   */
  public void setOptOnly(boolean optOnly) {
    if (optAll) {
      throw new IllegalArgumentException(ExceptionMessageMap.getMessage("000100")
          + "  The \"only\" option can not be set when the \"all\" option is set.");
    }
    this.optOnly = optOnly;
  }

  /**
   * Set the --signoff option.
   * 
   * @param optSignoff
   *          True to set the --signoff option, false to unset it.
   */
  public void setOptSignoff(boolean optSignoff) {
    this.optSignoff = optSignoff;
  }

}
