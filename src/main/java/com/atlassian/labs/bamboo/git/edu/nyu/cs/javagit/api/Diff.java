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
 * <code>Diff</code> represents a diff for one object in git repository
 * 
 * TODO: Build out the class
 */
public class Diff {
  private String name;

  /**
   * The constructor.
   * 
   * @param name
   *          The name of the git object Diff refers to
   */
  public Diff(String name) {
    this.name = name;
  }

  /**
   * Gets the name of the git object Diff refers to
   * 
   * @return The name of the git object Diff refers to
   */
  public String getName() {
    return name;
  }
}