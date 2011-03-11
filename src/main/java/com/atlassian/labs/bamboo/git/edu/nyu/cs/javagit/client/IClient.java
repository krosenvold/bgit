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

/**
 * An interface to represent a git client type, such as a command-line client.
 */
public interface IClient {

  /**
   * Gets an instance of <code>IGitAdd</code>.
   * 
   * @return An instance of <code>IGitAdd</code>.
   */
  public IGitAdd getGitAddInstance();

  /**
   * Gets an instance of <code>IGitCommit</code>.
   * 
   * @return An instance of <code>IGitCommit</code>.
   */
  public IGitCommit getGitCommitInstance();

  /**
   * Gets an instance of <code>IGitDiff</code>.
   * 
   * @return An instance of <code>IGitDiff</code>.
   */
  public IGitDiff getGitDiffInstance();

  /**
   * Gets an instance of <code>IGitGrep</code>.
   * 
   * @return An instance of <code>IGitGrep</code>.
   */
  public IGitGrep getGitGrepInstance();

  /**
   * Gets an instance of <code>IGitLog</code>.
   * 
   * @return An instance of <code>IGitLog</code>.
   */
  public IGitLog getGitLogInstance();

  /**
   * Gets an instance of <code>IGitMv</code>.
   * 
   * @return An instance of <code>IGitMv</code>.
   */
  public IGitMv getGitMvInstance();

  /**
   * Gets an instance if <code>IGitReset</code>.
   * 
   * @return An instance of <code>IGitReset</code>
   */
  public IGitReset getGitResetInstance();

  /**
   * Gets an instance of <code>IGitRevert</code>.
   * 
   * @return An instance of <code>IGitRevert</code>.
   */
  public IGitRevert getGitRevertInstance();

  /**
   * Gets an instance of <code>IGitRm</code>.
   * 
   * @return An instance of <code>IGitRm</code>.
   */
  public IGitRm getGitRmInstance();

  /**
   * Gets an instance of <code>IGitShow</code>.
   * 
   * @return An instance of <code>IGitShow</code>.
   */
  public IGitShow getGitShowInstance();

  /**
   * Gets an instance of <code>IGitStatus</code>.
   * 
   * @return An instance of <code>IGitStatus</code>.
   */
  public IGitStatus getGitStatusInstance();

  /**
   * Gets an instance of <code>IGitBranch</code>
   * 
   * @return An instance of <code>IGitBranch</code>
   */
  public IGitBranch getGitBranchInstance();

  /**
   * Gets an instance of <code>IGitCheckout</code>
   * 
   * @return An instance of <code>IGitCheckout</code>
   */
  public IGitCheckout getGitCheckoutInstance();
  
  /**
   * Gets an instance of <code>IGitInit</code>
   * 
   * @return An instance of <code>IGitInit</code>
   */
  public IGitInit getGitInitInstance();
  
  /**
   * Gets an instance of <code>IGitClone</code>
   * 
   * @return An instance of <code>IGitClone</code>
   */
  public IGitClone getGitCloneInstance();

  /**
   * Gets an instance of <code>IGitSubmodule</code>
   * 
   * @return An instance of <code>IGitSubmodule</code>
   */
  public IGitSubmodule getGitSubmoduleInstance();

    IGitMerge getGitMergeInstance();

    IGitRemote getGitRemoteInstance();
}
