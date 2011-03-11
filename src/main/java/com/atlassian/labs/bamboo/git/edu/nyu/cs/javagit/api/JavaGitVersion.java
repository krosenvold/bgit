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
 * <code>JavaGitVersion</code> exists solely to provide version information about the JavaGit
 * library.
 */
public final class JavaGitVersion {

  static final private int major = 0;

  static final private int minor = 2;

  static final private int revision = 0;

  static final private boolean isSnapshot = true;

  /**
   * Gets the major version number.
   * 
   * @return The major version number.
   */
  public static int getMajor() {
    return major;
  }

  /**
   * Gets the minor version number.
   * 
   * @return The minor version number.
   */
  public static int getMinor() {
    return minor;
  }

  /**
   * Gets the revision number.
   * 
   * @return The revision number.
   */
  public static int getRevision() {
    return revision;
  }

  /**
   * Indicates if this is a SNAPSHOT version. A SNAPSHOT version is a version under development that
   * is not an official release.
   * 
   * @return True if this is a snapshot version. False if it is a release version.
   */
  public static boolean isSnapshot() {
    return isSnapshot;
  }

  /**
   * Prints the version string for this version of JavaGit.
   * 
   * @return The version string in the format
   *         <code>&lt;MAJOR&gt;.&lt;MINOR&gt;.&lt;REVISION&gt;-SNAPSHOT</code> where
   *         &lt;MAJOR&gt; is the major version number, &lt;MINOR&gt; is the minor version number,
   *         &lt;REVISION&gt; is the revision number and -SNAPSHOT is the value inserted if this is
   *         a non-release version.
   */
  public String versionString() {
    return "" + major + "." + minor + "." + revision + ((isSnapshot) ? "-SNAPSHOT" : "");
  }

  /**
   * Prints the version string for this version of JavaGit.
   * 
   * @return The version string as formatted in the <code>versionString()</code> method.
   */
  public String toString() {
    return versionString();
  }

}
