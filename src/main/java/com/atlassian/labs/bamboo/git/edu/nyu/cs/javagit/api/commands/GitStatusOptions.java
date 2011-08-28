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

import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.utilities.CheckUtilities;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.utilities.ExceptionMessageMap;


/**
 * A class for managing options and passing these options to &lt;git-status&gt; command.
 * 
 * The &lt;git-status&gt; command takes the same set of options as &lt;git-commit&gt; it shows what
 * would be committed if the same options are given to git-commit.
 */

public final class GitStatusOptions {

  private boolean optQuiet = false;
  private boolean optVerbose = false;
  private boolean optSignOff = false;
  private boolean optEdit = false;
  private boolean optAll = false;
  private boolean optInclude = false;
  private boolean optOnly = false;
  private boolean optNoVerify = false;
  private boolean optUntrackedFiles = false;
  private boolean optAllowEmpty = false;
  private File optReadFromLogFile = null;
  private String author = null;

  /**
   * Returns true if the quiet (-q) option is set.
   * 
   * @return optQuite
   */
  public boolean isOptQuiet() {
    return optQuiet;
  }

  /**
   * Sets the quiet(-q) option.
   * 
   * @param optQuiet
   */
  public void setOptQuiet(boolean optQuiet) {
    this.optQuiet = optQuiet;
  }

  /**
   * Returns true if verbose(-v) option is passed to &lt;git-status&gt; command.
   * 
   * @return
   *           True if verbose(-v) option is passed to &lt;git-status&gt; command.
   */
  public boolean isOptVerbose() {
    return optVerbose;
  }

  /**
   * Sets the verbose option for &lt;git-status&gt;.
   * 
   * @param optVerbose
   */
  public void setOptVerbose(boolean optVerbose) {
    this.optVerbose = optVerbose;
  }

  /**
   * Returns true if read from log file(-F) is null.
   * 
   * @return
   *          True if read from log file(-F) is null.
   */
  public boolean isOptReadFromLogFileNull() {
    return optReadFromLogFile == null;
  }

  /**
   * Sets the readFromLogFile option, if we are reading logs from a file.
   * 
   * @param optReadFromLogFile
   *          <code>File</code> for reading log.
   */
  public void setOptReadFromLogFile(File optReadFromLogFile) {
    this.optReadFromLogFile = optReadFromLogFile;
  }

  /**
   * Returns the file from where log is to be read.
   * 
   * @return log file.
   */
  public File getOptReadFromLogFile() {
    return optReadFromLogFile;
  }

  /**
   * Returns true if sign-off(-s) options is set.
   * 
   * @return value of sign-off option
   */
  public boolean isOptSignOff() {
    return optSignOff;
  }

  /**
   * Sets the signOff(-s) option to &lt;git-status&gt; command.
   * 
   * @param signOff
   */
  public void setOptSignOff(boolean signOff) {
    this.optSignOff = signOff;
  }

  /**
   * Returns true if edit(-e) option is set.
   * 
   * @return true if edit is set
   */
  public boolean isOptEdit() {
    return optEdit;
  }

  /**
   * Sets the edit(-e) option to &lt;git-status&gt; command
   * 
   * @param edit
   */
  public void setOptEdit(boolean edit) {
    this.optEdit = edit;
  }

  /**
   * optAll tells the command to automatically stage files that have been modified and deleted, but
   * new files you have not told git about are not affected.
   * 
   * Returns true if all(-a) option need to be used for &lt;git-status&gt; command.
   * 
   * @return value of optAll
   */
  public boolean isOptAll() {
    return optAll;
  }

  /**
   * Sets the '-a' options to &lt;git-status&gt; command. If any one of the optOnly or OptInclude
   * options are set then optAll should not be set. While setting optAll to true, if optOnly or
   * optInclude is set set then <code>IllegalArgumentException</code> is thrown.
   * 
   * @param all
   */
  public void setOptAll(boolean all) {
    if (all && (optOnly || optInclude)) {
      throw new IllegalArgumentException(ExceptionMessageMap.getMessage("000100")
          + "  The \"all\" option can not be set when the \"only\" or \"include\" option is set.");
    }
    this.optAll = all;
  }

  /**
   * Before making a commit out of staged contents so far, stage the contents of paths given on the
   * command line as well. This is normally not used.
   * 
   * Returns true if include(-i) option is set for &lt;git-status&gt; command.
   * 
   * @return optInclude
   */
  public boolean isOptInclude() {
    return optInclude;
  }

  /**
   * Sets the value of optInclude to &lt;git-status&gt; command.
   * 
   * @param include
   */
  public void setOptInclude(boolean include) {
    if (include && optAll) {
      throw new IllegalArgumentException(ExceptionMessageMap.getMessage("000100")
          + "  The \"include\" option can not be set when the \"all\" option is set.");
    }
    this.optInclude = include;
  }

  /**
   * Returns true if only(-o) option is set.
   * 
   * @return optOnly
   */
  public boolean isOptOnly() {
    return optOnly;
  }

  /**
   * Sets the optOnly option for &lt;git-status&gt; command. optOnly should not be set if optAll(-a)
   * option is set. <code>IllegalArgumentException</code> is thrown if both '-a' and 'o' are set.
   * 
   * @param only
   */
  public void setOptOnly(boolean only) {
    if (only && optAll) {
      throw new IllegalArgumentException(ExceptionMessageMap.getMessage("000100")
          + "  The \"only\" option can not be set when the \"all\" option is set.");
    }
    this.optOnly = only;
  }

  /**
   * Returns true if no-verify(-n) options is set.
   * 
   * @return optNoVerify
   */
  public boolean isOptNoVerify() {
    return optNoVerify;
  }

  /**
   * Set the value to true if no verify(-n) option is set.
   * 
   * @param noVerify
   */
  public void setOptNoVerify(boolean noVerify) {
    this.optNoVerify = noVerify;
  }

  /**
   * Returns true if --untracked-files options is set.
   * 
   * @return optUntrackedFiles.
   */
  public boolean isOptUntrackedFiles() {
    return optUntrackedFiles;
  }

  /**
   * Sets the value to true if untracked (--untracked-files) option need to be set.
   * 
   * @param untrackedFiles
   */
  public void setOptUntrackedFiles(boolean untrackedFiles) {
    this.optUntrackedFiles = untrackedFiles;
  }

  /**
   * Returns true if allow empty options is set. This option should be set if it's okay to record 
   * an empty change.
   * 
   * @return
   *           True if allow empty options is set. This option should be set if it's okay to record
   *           an empty change.
   */
  public boolean isOptAllowEmpty() {
    return optAllowEmpty;
  }

  /**
   * Sets the value of allow empty option to true if we would like to record an empty change.
   * 
   * @param allowEmpty
   */
  public void setOptAllowEmpty(boolean allowEmpty) {
    this.optAllowEmpty = allowEmpty;
  }

  /**
   * Returns the author. This option should be used if we would like to override the value in
   * commit.
   * 
   * @return author
   */
  public String getAuthor() {
    return author;
  }

  /**
   * Sets the author name.
   * 
   * @param author
   */
  public void setAuthor(String author) {
    CheckUtilities.checkStringArgument(author, "Author name for override");
    this.author = author.replace('<', ' ').replace('>', ' ');
  }

  /**
   * Returns true if author name is null.
   * 
   * @return true if author name for override is set.
   */
  public boolean isAuthorNull() {
    return author == null;
  }

}
