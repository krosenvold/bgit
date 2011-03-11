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
package com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.cli;

import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.JavaGitException;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.CommandResponse;

/**
 * <code>IParser</code> is an interface for parsing a git command line output stream. See how
 * implementations of <code>IParser</code> are used in <code>ProcessUtilities</code>.
 * 
 * @see ProcessUtilities
 */
public interface IParser {

  /**
   * Parses a line of output from a git command line output stream.
   * 
   * @param line
   *          The line to parse.
   */
  public void parseLine(String line);

  /**
   * Takes the process exit code so it can provide the most information possible to the user about
   * how the process ends.
   * 
   * @param code
   *          The exit code of the process that was run.
   */
  public void processExitCode(int code);

  /**
   * Gets the response for the command for which the parser is implemented.
   * 
   * @return The response for the command for which the parser is implemented.
   * @throws JavaGitException
   *           Thrown when there are problems creating the response, if there was an error running
   *           the command, ....
   */
  public CommandResponse getResponse() throws JavaGitException;

}
