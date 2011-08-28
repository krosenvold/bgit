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
package com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api;

/**
 * <code>MergeOpions</code> represents merge options
 * 
 */
public class MergeOptions {
  private final String message;
  private Strategy strategy;

  public static enum Strategy {
    RESOLVE, RECURSIVE, OCTOPUS, OURS, SUBTREE
  }

  /**
   * The constructor.
   * 
   * @param s
   *          Merge strategy; see enum list
   */
  public MergeOptions(Strategy s) {
    this.strategy = s;
    //no message
    this.message = null;
  }

  /**
   * The constructor.
   * 
   * @param m
   *          Message for the commit
   * @param s
   *          Merge strategy; see enum list
   */
  public MergeOptions(Strategy s, String m) {
    this.strategy = s;
    this.message = m;
  }
  
  /**
   * Gets the commit message
   * 
   * @return The commit message
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets the merge strategy
   * 
   * @return The strategy for the merge (resolve, recursive, etc)
   */
  public Strategy getStrategy() {
    return strategy;
  }
}