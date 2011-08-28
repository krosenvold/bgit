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


/**
 * A response data object for the git-mv command. For information about the contents of 
 * GitMvResponse instances returned by a given method, please see the JavaDoc for the method
 * in question.
 */
public class GitMvResponse implements CommandResponse {

  // Variable to store the source file/folder/symlink of the response.
  protected File source;

  // Variable to store the destination file/folder/symlink of the response.
  protected File destination;

  // String Buffer to store the comment message after execution of git-mv.
  protected StringBuffer message = new StringBuffer();

  /**
   * Gets the comments, if received, upon successful execution of the git-mv command, from the 
   * message buffer.
   * 
   * @return message
   *           The comments, if received, upon successful execution of the git-mv command, from the
   * message buffer.
   */
  public String getComment() {
    return message.toString();
  }

  /**
   * Gets the destination file/folder
   * 
   * @return the destination
   */
  public File getDestination() {
    return destination;
  }

  /**
   * Gets the source file/folder/symlink
   * 
   * @return the source
   */
  public File getSource() {
    return source;
  }
  
  @Override
  public boolean equals(Object o) {
    if (!(o instanceof GitMvResponse)) {
      return false;
    }

    GitMvResponse g = (GitMvResponse) o;

    if (!CheckUtilities.checkObjectsEqual(getSource(), g.getSource())) {
      return false;
    }

    if (!CheckUtilities.checkObjectsEqual(getDestination(), g.getDestination())) {
      return false;
    }

    if (!CheckUtilities.checkObjectsEqual(getComment(), g.getComment())) {
      return false;
    }
    
    return true;
  }
  
  @Override
  public int hashCode() {
    return source.hashCode() + destination.hashCode() + message.hashCode();
  }
  
  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    if (null != source) {
      buffer.append("Source: ");
      buffer.append(source.getName());
      buffer.append(" ");
    }
    
    if (null != destination) {
      buffer.append("Destination: ");
      buffer.append(destination.getName());
      buffer.append(" ");
    }
    
    if ((message.length()!=0)) {
      buffer.append("Message: ");
      buffer.append(message.toString());
    }
    return buffer.toString();
  }
}
