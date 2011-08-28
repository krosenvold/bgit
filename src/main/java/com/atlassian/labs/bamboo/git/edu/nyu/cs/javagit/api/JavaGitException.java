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
 * Base exception for git specific exceptions.
 */
public class JavaGitException extends Exception {

  // as per the Java spec, this is a required field for <code>Serializable</code>
	private static final long serialVersionUID = 1402053559415331074L;
	// The code for an exception instance.
  private int code;

  /**
   * Create an exception with a code and a message.
   * 
   * @param code
   *          The code for this exception.
   * @param message
   *          The message for this exception.
   */
  public JavaGitException(int code, String message) {
    super(message);
    this.code = code;
  }

  /**
   * Create an exception with a code, a message and a causal <code>Throwable</code>.
   * 
   * @param code
   *          The code for this exception.
   * @param message
   *          The message for this exception.
   * @param cause
   *          A <code>Throwable</code> that caused this exception.
   */
  public JavaGitException(int code, String message, Throwable cause) {
    super(message, cause);
    this.code = code;
  }

  /**
   * Get the code this exception is thrown with.
   * 
   * @return The code for this exception.
   */
  public int getCode() {
    return code;
  }

}
