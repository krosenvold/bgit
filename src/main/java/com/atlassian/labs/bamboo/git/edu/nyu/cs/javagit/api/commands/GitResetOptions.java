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

import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.Ref;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.utilities.CheckUtilities;

/**
 * A class to manage passing arguments to the <code>GitReset</code> command.
 * <code>GitReset</code> resets the HEAD of the current checked out branch to the state of a
 * previous commit. <br>
 * <br>
 * 
 * Default values:
 * <ul>
 * <li> resetType - <code>ResetType.MIXED</code></li>
 * <li> commitName - <code>CommitName.HEAD</code></li>
 * <li> quiet - false</li>
 * </ul>
 */
public class GitResetOptions {

  /**
   * An enumeration of the types of resets that can be performed.
   * 
   * <ul>
   * <li> MIXED - resets the checked out branch and the index but not the working tree</li>
   * <li> SOFT - resets only the checked out branch</li>
   * <li> HARD - resets the checked out branch, the index and the working tree</li>
   * </ul>
   */
  public static enum ResetType {
    MIXED("mixed"), SOFT("soft"), HARD("hard");

    private String cliOpt;

    private ResetType(String cliOpt) {
      this.cliOpt = cliOpt;
    }

    public String toString() {
      return "--" + cliOpt;
    }
  }

  // The name of the commit to reset to.
  private Ref commitName;

  // The type of reset to perform.
  private ResetType resetType;

  // Suppress feedback.
  private boolean quiet = false;

  /**
   * Construct a <code>GitResetOptions</code> instance with the default values.
   */
  public GitResetOptions() {
    setup(ResetType.MIXED, Ref.HEAD);
  }

  /**
   * Construct a <code>GitResetOptions</code> instance with the specified <code>CommitName</code>.
   * 
   * @param commitName
   *          The name of the commit to reset to.
   */
  public GitResetOptions(Ref commitName) {
    setup(ResetType.MIXED, commitName);
  }

  /**
   * Construct a <code>GitResetOptions</code> instance with the specified <code>ResetType</code>.
   * 
   * @param resetType
   *          The type of reset to perform.
   */
  public GitResetOptions(ResetType resetType) {
    setup(resetType, Ref.HEAD);
  }

  /**
   * Construct a <code>GitResetOptions</code> instance with the specified <code>ResetType</code>
   * and <code>CommitName</code>.
   * 
   * @param resetType
   *          The type of reset to perform.
   * @param commitName
   *          The name of the commit to reset to.
   */
  public GitResetOptions(ResetType resetType, Ref commitName) {
    setup(resetType, commitName);
  }

  /**
   * Central instance construction setup method.
   * 
   * @param resetType
   *          The type of reset to perform.
   * @param commitName
   *          The name of the commit to reset to.
   */
  private void setup(ResetType resetType, Ref commitName) {
    CheckUtilities.checkNullArgument(resetType, "resetType");
    CheckUtilities.checkNullArgument(commitName, "commitName");

    this.resetType = resetType;
    this.commitName = commitName;
  }

  /**
   * Get the name of the commit to reset to.
   * 
   * @return The name of the commit this instance says to reset to.
   */
  public Ref getCommitName() {
    return commitName;
  }

  /**
   * Gets the type of reset for this instance.
   * 
   * @return The type of reset for this instance.
   */
  public ResetType getResetType() {
    return resetType;
  }

  /**
   * Is the reset to perform quietly?
   * 
   * @return True if the reset is to perform quietly. False if the reset is to perform as normal.
   */
  public boolean isQuiet() {
    return quiet;
  }

  /**
   * Set the name of the commit to reset to.
   * 
   * @param commitName
   *          The name of the commit to reset to.
   */
  public void setCommitName(Ref commitName) {
    this.commitName = commitName;
  }

  /**
   * Set the reset to perform quietly or with normal progress information.
   * 
   * @param quiet
   *          True if the reset is to perform quietly. False if the reset is to perform as normal.
   */
  public void setQuiet(boolean quiet) {
    this.quiet = quiet;
  }

  /**
   * Set the type of reset to perform.
   * 
   * @param resetType
   *          The type of reset to perform.
   */
  public void setResetType(ResetType resetType) {
    this.resetType = resetType;
  }

  public String toString() {
    StringBuffer buf = new StringBuffer();

    buf.append(resetType.toString());
    buf.append(" ");

    if (quiet) {
      buf.append("-q ");
    }

    buf.append(commitName.toString());
    return buf.toString();
  }

  public boolean equals(Object o) {
    if (!(o instanceof GitResetOptions)) {
      return false;
    }

    GitResetOptions grOpts = (GitResetOptions) o;

    if (!CheckUtilities.checkObjectsEqual(commitName, grOpts.getCommitName())) {
      return false;
    }

    if (!CheckUtilities.checkObjectsEqual(resetType, grOpts.getResetType())) {
      return false;
    }

    if (grOpts.isQuiet() != quiet) {
      return false;
    }

    return true;
  }

  public int hashCode() {
    int ret = resetType.hashCode() + commitName.hashCode();
    return (quiet) ? ret + 1 : ret;
  }

}
