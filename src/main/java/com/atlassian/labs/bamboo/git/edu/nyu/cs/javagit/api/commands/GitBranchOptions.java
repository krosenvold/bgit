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
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.utilities.ExceptionMessageMap;

/**
 * A class to manage passing branch options to the <code>GitBranch</code> command.
 */
public class GitBranchOptions {
  /*
   * Generic Options.
   */
  
  //The -v or --verbose option
  private boolean optVerbose = false;

  //The --track option
  private boolean optTrack = false;

  //The --no-track option. Ignore the branch.autosetupmerge configuration variable.
  private boolean optNoTrack = false;

  //The color option. Color branches to highlight current, local, and remote branches
  private boolean optColor = false;

  /*The --no-color option. Turn off branch colors, even when the configuration file gives the
   *default to color output.
   */
  private boolean optNoColor = false;

  //The -r (remote-tracking) option
  private boolean optR = false;

  //The --contains option. Prints only branches that contain the commit
  private Ref optContains;

  //The --abbrev option. --abbrev to set with default <n>.
  private boolean optAbbrev = false;

  //The default abbrev length.
  public static final int DEFAULT_ABBREV_LEN = 7;
  
  //The --abbrev option. --abbrev [<n>]   use <n> digits to display SHA-1s
  private int optAbbrevLen = DEFAULT_ABBREV_LEN; 

  //The -no-abbrev option. Displays the full SHA1s in output listing rather than abbreviating them.
  private boolean optNoAbbrev = false;

  /*
   * Non-generic options.
   */
  //The -a option. List both remote-tracking and local branches.
  private boolean optA = false;

  //The -l option. Create the branch's reflog.
  private boolean optL = false;

  //The -f option. Force creation (when already exists).
  private boolean optF = false;

  //The --merged option. List only branches merged with HEAD.
  private boolean optMerged = false;

  //The --no-merged option. List only branches not merged with HEAD.
  private boolean optNoMerged = false;
  
  //The -d option. Deletes a branch or the branch list.
  private boolean optDLower = false;
  
  //The -D option. Force deletes a branch or the branch list.
  private boolean optDUpper = false;
  
  //The -m option. Moves old branch to new branch.
  private boolean optMLower = false;

  //The -M option. Moves old branch to new branch.
  private boolean optMUpper = false;
  
  /**
   * Indicates if the verbose option should be used.
   *
   * @return True if the verbose option should be used, false otherwise. 
   */
  public boolean isOptVerbose() {
    return optVerbose;
  }

  /**
   * Checks whether the verbose option should be set and sets it. 
   *
   * @param optVerbose
   *        True if the verbose option should be used, false otherwise. 
   */
  public void setOptVerbose(boolean optVerbose) {
    checkCanSetNoArgOption("--verbose");
    if ((false == optVerbose) && (optAbbrev || optNoAbbrev)) {
      throw new IllegalArgumentException(ExceptionMessageMap.getMessage("000120")
          + "  --no-abbrev or --abbrev can only be used with --verbose.");
    }
    this.optVerbose = optVerbose;
  }

  /**
   * Indicates if the --abbrev option should be used.
   *
   * @return True if the --abbrev option should be used, false otherwise. 
   */
  public boolean isOptAbbrev() {
    return optAbbrev;
  }

  /**
   * Checks whether the --abbrev option should be set and sets it. 
   *
   * @param optAbbrev True if the --abbrev option should be used, false otherwise.
   */
  public void setOptAbbrev(boolean optAbbrev) {
    if (optVerbose) {
      if (optAbbrev && optNoAbbrev) {
        throw new IllegalArgumentException(ExceptionMessageMap.getMessage("000120")
            + "  --abbrev cannot be used with --no-abbrev.");
      }
      this.optAbbrev = optAbbrev;
    }
  }

  /**
   * Indicates if the length other than default length should be used for --abbrev option.
   *
   * @return An integer value for abbrev length if the --abbrev option should be used.
   */
  public int getOptAbbrevLen() {
    return optAbbrevLen;
  }

  /**
   * Sets the length to be used for --abbrev option to a value other than default length.
   * 
   * @param optAbbrevLen An integer value for abbrev length if the --abbrev option should be used.
   */
  public void setOptAbbrevLen(int optAbbrevLen) {
    if (isOptAbbrev()) {
      this.optAbbrevLen = optAbbrevLen;
    }
  }

  /**
   * Indicates if the --no-abbrev option should be used.
   *
   * @return True if the --no-abbrev option should be used, false otherwise. 
   */
  public boolean isOptNoAbbrev() {
    return optNoAbbrev;
  }

  /**
   * Checks whether the --no-abbrev option should be set and sets it. 
   *
   * @param optNoAbbrev True if the --no-abbrev option should be used, false otherwise.
   */
  public void setOptNoAbbrev(boolean optNoAbbrev) {
    if (optVerbose) {
      if (optAbbrev && optNoAbbrev) {
        throw new IllegalArgumentException(ExceptionMessageMap.getMessage("000120")
            + "  --no-abbrev cannot be used with --abbrev.");
      }
      this.optNoAbbrev = optNoAbbrev;
    }
  }
  
  /**
   * Indicates if the --track option should be used.
   *
   * @return True if the --track option should be used, false otherwise. 
   */
  public boolean isOptTrack() {
    return optTrack;
  }

  /**
   * Checks whether the --track option should be set and sets it. 
   *
   * @param optTrack True if the --track option should be used, false otherwise.
   */
  public void setOptTrack(boolean optTrack) {
    checkCanSetCreateOption("--track");
    if (optNoTrack && optTrack) {
      throw new IllegalArgumentException(ExceptionMessageMap.getMessage("000120")
          + "  --track cannot be used with --no-track.");
    } 
    this.optTrack = optTrack;
  }

  /**
   * Indicates if the --no-track option should be used.
   *
   * @return True if the --no-track option should be used, false otherwise. 
   */
  public boolean isOptNoTrack() {
    return optNoTrack;
  }

  /**
   * Checks whether the --no-track option should be set and sets it. 
   *
   * @param optNoTrack True if the --no-track option should be used, false otherwise.
   */
  public void setOptNoTrack(boolean optNoTrack) {
    checkCanSetNoArgOption("--no-track");
    if (optNoTrack && optTrack) {
      throw new IllegalArgumentException(ExceptionMessageMap.getMessage("000120")
          + "  --no-track cannot be used with --track.");
    } 
    this.optNoTrack = optNoTrack;
  }

  /**
   * Indicates if the --color option should be used.
   *
   * @return True if the --color option should be used, false otherwise. 
   */
  public boolean isOptColor() {
    return optColor;
  }

  /**
   * Checks whether the --color option should be set and sets it. 
   *
   * @param optColor True if the --color option should be used, false otherwise.
   */
  public void setOptColor(boolean optColor) {
    checkCanSetNoArgOption("--color");
    if (optNoColor && optColor) {
      throw new IllegalArgumentException(ExceptionMessageMap.getMessage("000120")
          + "  --color cannot be used with --no-color.");
    } 
    this.optColor = optColor;
  }

  /**
   * Indicates if the --no-color option should be used.
   *
   * @return True if the --no-color option should be used, false otherwise. 
   */
  public boolean isOptNoColor() {
    return optNoColor;
  }

  /**
   * Checks whether the --no-color option should be set and sets it. 
   *
   * @param optNoColor True if the --no-color option should be used, false otherwise.
   */
  public void setOptNoColor(boolean optNoColor) {
    checkCanSetNoArgOption("--no-color");
    if (optNoColor && optColor) {
      throw new IllegalArgumentException(ExceptionMessageMap.getMessage("000120")
          + "  --no-color cannot be used with --color.");
    } 
    this.optNoColor = optNoColor;
  }

  /**
   * Indicates if the -r option should be used.
   *
   * @return True if the -r option should be used, false otherwise. 
   */
  public boolean isOptR() {
    return optR;
  }

  /**
   * Checks whether the -r option should be set and sets it. 
   *
   * @param optR True if the -r option should be used, false otherwise.
   */
  public void setOptR(boolean optR) {
    if (true == optR) {
      checkCanSetNoArgOption("-r");
    }
    this.optR = optR;
  }

  /**
   * Indicates if the --contains option should be used. This is the commit value for --contains.
   *
   * @return Ref 
   *          Ref if the verbose option should be used, null otherwise. 
   */
  public Ref getOptContains() {
    return optContains;
  }

  /**
   * Checks if the --contains option should be used and sets it to the commit Ref.
   * 
   * @param commit 
   *          Commit Ref if the --contains option should be used, null otherwise.
   */
  public void setOptContains(Ref commit) {
    if (null != commit) {
      checkCanSetNoArgOption("--contains");
    }
    this.optContains = commit;
  }

  /**
   * Indicates if the -a option should be used.
   *
   * @return True if the -a option should be used, false otherwise. 
   */
  public boolean isOptA() {
    return optA;
  }

  /**
   * Checks whether the -a option should be set and sets it. 
   *
   * @param optA True if the -a option should be used, false otherwise.
   */
  public void setOptA(boolean optA) {
    checkCanSetNoArgOption("-a");
    this.optA = optA;
  }

  /**
   * Indicates if the -l option should be used.
   *
   * @return True if the -l option should be used, false otherwise. 
   */
  public boolean isOptL() {
    return optL;
  }

  /**
   * Checks whether the -l option should be set and sets it. 
   *
   * @param optL True if the -l option should be used, false otherwise.
   */
  public void setOptL(boolean optL) {
    checkCanSetCreateOption("-l");
    this.optL = optL;
  }

  /**
   * Indicates if the -f option should be used.
   *
   * @return True if the -f option should be used, false otherwise. 
   */
  public boolean isOptF() {
    return optF;
  }

  /**
   * Checks whether the -f option should be set and sets it. 
   *
   * @param optF True if the -f option should be used, false otherwise.
   */
  public void setOptF(boolean optF) {
    checkCanSetCreateOption("-f");
    this.optF = optF;
  }

  /**
   * Indicates if the --merged option should be used.
   *
   * @return True if the --merged option should be used, false otherwise. 
   */
  public boolean isOptMerged() {
    return optMerged;
  }

  /**
   * Checks whether the --merged option should be set and sets it. 
   *
   * @param optMerged True if the --merged option should be used, false otherwise.
   */
  public void setOptMerged(boolean optMerged) {
    checkCanSetNoArgOption("--merged");
    if (optNoMerged && optMerged) {
      throw new IllegalArgumentException(ExceptionMessageMap.getMessage("000120")
          + "  --merged cannot be used with --no-merged.");
    } 
    this.optMerged = optMerged;
  }

  /**
   * Indicates if the --no-merged option should be used.
   *
   * @return True if the --no-merged option should be used, false otherwise. 
   */
  public boolean isOptNoMerged() {
    return optNoMerged;
  }

  /**
   * Checks whether the --no-merged option should be set and sets it. 
   *
   * @param optNoMerged True if the --no-merged option should be used, false otherwise.
   */
  public void setOptNoMerged(boolean optNoMerged) {
    checkCanSetNoArgOption("--no-merged");
    if (optNoMerged && optMerged) {
      throw new IllegalArgumentException(ExceptionMessageMap.getMessage("000120")
          + "  --no-merged cannot be used with --merged.");
    } 
    this.optNoMerged = optNoMerged;
  }
  
  /**
   * Indicates if the -d option should be used.
   *
   * @return True if the -d option should be used, false otherwise. 
   */
  public boolean isOptDLower() {
    return optDLower;
  }

  /**
   * Checks whether the -d option should be set and sets it. 
   *
   * @param optDLower True if the -d option should be used, false otherwise.
   */
  public void setOptDLower(boolean optDLower) {
    checkCanSetDeleteOption("-d");
    if (optDLower && optDUpper) {
      throw new IllegalArgumentException(ExceptionMessageMap.getMessage("000120")
          + "  -d cannot be used with -D.");
    } 
    this.optDLower = optDLower;
  }

  /**
   * Indicates if the -D option should be used.
   *
   * @return True if the -D option should be used, false otherwise. 
   */
  public boolean isOptDUpper() {
    return optDUpper;
  }

  /**
   * Checks whether the -D option should be set and sets it. 
   *
   * @param optDUpper True if the -D option should be used, false otherwise.
   */
  public void setOptDUpper(boolean optDUpper) {
    checkCanSetDeleteOption("-D");
    if (optDLower && optDUpper) {
      throw new IllegalArgumentException(ExceptionMessageMap.getMessage("000120")
          + "  -D cannot be used with -d.");
    } 
    this.optDUpper = optDUpper;
  }

  /**
   * Indicates if the -m option should be used.
   *
   * @return True if the -m option should be used, false otherwise. 
   */
  public boolean isOptMLower() {
    return optMLower;
  }

  /**
   * Checks whether the -m option should be set and sets it. 
   *
   * @param optMLower True if the -m option should be used, false otherwise.
   */
  public void setOptMLower(boolean optMLower) {
    checkCanSetRenameOption("-m");
    if (optMLower && optMUpper) {
      throw new IllegalArgumentException(ExceptionMessageMap.getMessage("000120")
          + "  -m cannot be used with -M.");
    } 
    this.optMLower = optMLower;
  }

  /**
   * Indicates if the -M option should be used.
   *
   * @return True if the -M option should be used, false otherwise. 
   */
  public boolean isOptMUpper() {
    return optMUpper;
  }

  /**
   * Checks whether the -M option should be set and sets it. 
   *
   * @param optMUpper True if the -M option should be used, false otherwise.
   */
  public void setOptMUpper(boolean optMUpper) {
    checkCanSetRenameOption("-M");
    if (optMLower && optMUpper) {
      throw new IllegalArgumentException(ExceptionMessageMap.getMessage("000120")
          + "  -M cannot be used with -m.");
    } 
    this.optMUpper = optMUpper;
  }

  /**
   * Checks whether an option for displaying branches can be set. If not, throws an 
   * <code>IllegalArgumentException</code>.
   * 
   * @param option
   *        The name of the option being checked; for use in exception messages.
   */
  public void checkCanSetNoArgOption(String option) {
    if (isOptTrack() || isOptNoTrack() || isOptL() || isOptF() || isOptDLower() || isOptDUpper()
        || isOptMLower() || isOptMUpper()) {
      throw new IllegalArgumentException(ExceptionMessageMap.getMessage("000120")
          + option + " should be used without arguments, to display branches");
    } 
  }
  
  /**
   * Checks whether an option for creating a branch can be set. If not, throws an 
   * <code>IllegalArgumentException</code>.
   * 
   * @param option
   *        The name of the option being checked; for use in exception messages.
   */
  public void checkCanSetCreateOption(String option) {
    if (isOptColor() || isOptNoColor() || isOptR() || isOptA() || isOptVerbose() ||
        isOptMerged() || isOptNoMerged() || (null != getOptContains()) || isOptMLower() || 
        isOptMUpper() || isOptDLower() || isOptDUpper()) {
      throw new IllegalArgumentException(ExceptionMessageMap.getMessage("000120") + option + 
      " should be used with a branch name and optional start point, to create a branch");
    }
  }
  
  /**
   * Checks whether an option for deleting a branch or branch list can be set. If not, throws an 
   * <code>IllegalArgumentException</code>.
   * 
   * @param option
   *        The name of the option being checked; for use in exception messages.
   */
  public void checkCanSetDeleteOption(String option) {
    if (isOptColor() || isOptNoColor() || isOptA() || isOptVerbose() || isOptMerged() || 
        isOptNoMerged() || (null != getOptContains()) || isOptTrack() || isOptNoTrack() || 
        isOptL() || isOptF() || isOptMLower() || isOptMUpper()) {
      throw new IllegalArgumentException(ExceptionMessageMap.getMessage("000120") + option + 
      " should be used with branch(es), to delete the branch(es).");
    }
  }
  
  /**
   * Checks whether an option for renaming a branch can be set. If not, throws an 
   * <code>IllegalArgumentException</code>.
   * 
   * @param option
   *        The name of the option being checked; for use in exception messages.
   */
  public void checkCanSetRenameOption(String option) {
    if (isOptColor() || isOptNoColor() || isOptR() || isOptA() || isOptVerbose() ||
        isOptMerged() || isOptNoMerged() || (null != getOptContains()) || isOptTrack() || 
        isOptNoTrack() || isOptL() || isOptF() || isOptDLower() || isOptDUpper()) {
      throw new IllegalArgumentException(ExceptionMessageMap.getMessage("000120") + option + 
      " should be used with optional oldbranch and newbranch, to rename oldbranch/current branch" +
      "to newbranch.");
    }
  }
} 
