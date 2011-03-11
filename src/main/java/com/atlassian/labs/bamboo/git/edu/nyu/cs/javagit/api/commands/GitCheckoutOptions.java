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
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.Ref.RefType;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.utilities.CheckUtilities;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.utilities.ExceptionMessageMap;

/**
 * Class for managing options for &lt;git-checkout&gt; command.
 */
public class GitCheckoutOptions {

  /**
   * Option for creating a new branch with the name provided.
   */
  private Ref optB;

  /**
   * Quiet, suppress feedback messages.
   */
  private boolean optQ;

  /**
   * Proceed even if the index or the working tree differs from HEAD. This is used to throw away
   * local changes.
   */
  private boolean optF;

  /**
   * Track options for setting configuration so that &lt;git-pull&gt; retrieves data automatically
   * from starting point.
   */
  private boolean optTrack;
  /**
   * Set this option to ignore the branch.autosetupmerge.
   */
  private boolean optNoTrack;
  /**
   * Create the new branch's reflog. It also enables use of date based sha1 expressions.
   */
  private boolean optL;
  /**
   * Use this option, if merging need to be done between the current branch, working tree contents,
   * and the new branch. Also, brings you to the new branch.
   */
  private boolean optM;

  /**
   * Sets the name of the new branch that need to be created from the base branch.
   * 
   * @param newBranch
   *          New branch of type <code>Ref</code> that will be created by &lt;git-checkout&gt;.
   */
  public void setOptB(Ref newBranch) {
    CheckUtilities.validateArgumentRefType(newBranch, RefType.BRANCH, "New Branch Name");
    optB = newBranch;
  }

  /**
   * Gets the <code>Ref</code> of type branch that is created by &lt;git-checkout&gt; command.
   * 
   * @return Returns the new branch of type <code>Ref</code>.
   */
  public Ref getOptB() {
    return optB;
  }

  /**
   * Gets the value of Quiet option.
   * 
   * @return true if Quiet option is set, else false.
   */
  public boolean optQ() {
    return optQ;
  }

  /**
   * Sets the Quiet option
   * 
   * @param optQ
   *          True if quiet option should be set, false otherwise.
   */
  public void setOptQ(boolean optQ) {
    this.optQ = optQ;
  }

  /**
   * Gets the value of force option.
   * 
   * @return true 
   *          If force is set, or false.
   */
  public boolean optF() {
    return optF;
  }

  /**
   * Sets the value of force option
   * 
   * @param optF
   *          True if force should be set, false otherwise.
   */
  public void setOptF(boolean optF) {
    this.optF = optF;
  }

  /**
   * Gets the value value of track option.
   * 
   * @return true 
   *          If track option is set, else false.
   */
  public boolean optTrack() {
    return optTrack;
  }

  /**
   * Sets the track option.
   * 
   * @param optTrack
   *          True if the track option should be set, else false.
   */
  public void setOptTrack(boolean optTrack) {
    if ( optNoTrack ) {
      throw new IllegalArgumentException(ExceptionMessageMap.getMessage("000130")
          + "  The \"track\" option can not be set when the \"noTrack\" option is set.");
    }
    this.optTrack = optTrack;
  }

  /**
   * Gets the notrack option.
   * 
   * @return true if notrack option is set, else false.
   */
  public boolean optNoTrack() {
    return optNoTrack;
  }

  /**
   * Sets the noTrack option.
   * 
   * @param optNoTrack
   *          True if noTrack options need to be set, else false.
   */
  public void setOptNoTrack(boolean optNoTrack) {
    if ( optTrack ) {
      throw new IllegalArgumentException(ExceptionMessageMap.getMessage("000130")
          + "  The \"noTrack\" option can not be set when the \"Track\" option is set.");
    }
    this.optNoTrack = optNoTrack;
  }

  /**
   * Gets the reflog option for the new branch.
   * 
   * @return true
   *        If refLog option need to be set, else false.
   */
  public boolean optL() {
    return optL;
  }

  /**
   * Sets the reflog option for the newbranch.
   * 
   * @param optL
   *          True if reflog option should be set, else false.
   */
  public void setOptL(boolean optL) {
    this.optL = optL;
  }

  /**
   * Gets the merge option
   * 
   * @return true 
   *        If merge option to be used, else false
   */
  public boolean optM() {
    return optM;
  }

  /**
   * Sets the merge option.
   * 
   * @param optM
   *          True if merge need to be used, else false.
   */
  public void setOptM(boolean optM) {
    this.optM = optM;
  }

}
