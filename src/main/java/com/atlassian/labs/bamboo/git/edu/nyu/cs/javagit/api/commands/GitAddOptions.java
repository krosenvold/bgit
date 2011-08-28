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

/**
 * A class to manage passing add options to the <code>GitAdd</code> command.
 */
public class GitAddOptions {

  // We are intentionally ignoring interactive(-i) and patching(-p) options currently.

  /**
   * Don't actually add the file(s), just show if they exist.
   */
  private boolean dryRun;
  /**
   * Be verbose.
   */
  private boolean verbose;
  /**
   * Allow adding otherwise ignored files.
   */
  private boolean force;
  /**
   * Update only files that git already knows about, staging modified content for commit and marking
   * deleted files for removal.
   */
  private boolean update;
  /**
   * Don't add the file(s), but only refresh their stat() information in the index.
   */
  private boolean refresh;
  /**
   * If some files could not be added because of errors indexing them, do not abort the operation,
   * but continue adding the others.
   */
  private boolean ignoreErrors;

  /**
   * Gets the value of dryRun flag in &lt;GitAdd&gt; command
   * 
   * @return true if dryRun is set else false.
   */
  public boolean dryRun() {
    return dryRun;
  }

  /**
   * Sets the value of dryRun flag in &lt;GitAdd&gt; command
   * 
   * @param dryRun
   *          set to true if the flag need to be set.
   */
  public void setDryRun(boolean dryRun) {
    this.dryRun = dryRun;
  }

  /**
   * Gets the value of verbose flag in &lt;GitAdd&gt; command
   * 
   * @return returns true if the flag is set
   */
  public boolean verbose() {
    return verbose;
  }

  /**
   * Sets the value of verbose flag in &lt;GitAdd&gt; command
   * 
   * @param verbose
   *          true if the flag should be set, else false.
   */
  public void setVerbose(boolean verbose) {
    this.verbose = verbose;
  }

  /**
   * Gets the value of force flag in &lt;GitAdd&gt; command
   * 
   * @return true if the force flag is set else false.
   */
  public boolean force() {
    return force;
  }

  /**
   * Sets the value of force flag in &lt;GitAdd&gt; command
   * 
   * @param force
   *          true if the flag should be set, else false.
   */
  public void setForce(boolean force) {
    this.force = force;
  }

  /**
   * Gets the value of update flag in &lt;GitAdd&gt; command
   * 
   * @return returns true if update flag is set, else false.
   */
  public boolean update() {
    return update;
  }

  /**
   * Sets the value of update flag in &lt;GitAdd&gt; command
   * 
   * @param update
   *          should be set to true if the flag should be set, else false.
   */
  public void setUpdate(boolean update) {
    this.update = update;
  }

  /**
   * Gets the value of refresh flag in &lt;GitAdd&gt; command.
   * 
   * @return true if the flag is set, else false.
   */
  public boolean refresh() {
    return refresh;
  }

  /**
   * Sets the value of refresh flag in &lt;GitAdd&gt; command.
   * 
   * @param refresh
   *          should be set to true if the flag need to be set, else false.
   */
  public void setRefresh(boolean refresh) {
    this.refresh = refresh;
  }

  /**
   * Gets the value of ignoreErrors flag &lt;GitAdd&gt; command.
   * 
   * @return true if the errors should be ignored, else false.
   */
  public boolean ignoreErrors() {
    return ignoreErrors;
  }

  /**
   * Sets the value of ignoreError flag in &lt;GitAdd&gt; command.
   * 
   * @param ignoreErrors
   *          should be set to true if ignoreError flag need to be set, else false.
   */
  public void setIgnoreErrors(boolean ignoreErrors) {
    this.ignoreErrors = ignoreErrors;
  }

}
