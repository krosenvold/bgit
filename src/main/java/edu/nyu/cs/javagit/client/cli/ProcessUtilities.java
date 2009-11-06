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
package edu.nyu.cs.javagit.client.cli;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import edu.nyu.cs.javagit.api.JavaGitException;
import edu.nyu.cs.javagit.api.commands.CommandResponse;
import edu.nyu.cs.javagit.utilities.ExceptionMessageMap;

/**
 * <code>ProcessUtilities</code> contains methods to help managing processes.
 */
public class ProcessUtilities {

  // TODO (jhl): add unit tests for this class.

  /**
   * Start a process.
   * 
   * @param pb
   *          The <code>ProcessBuilder</code> to use to start the process.
   * @return The started process.
   * @exception IOException
   *              An <code>IOException</code> is thrown if there is trouble starting the
   *              sub-process.
   */
  public static Process startProcess(ProcessBuilder pb) throws IOException {
    try {
      return pb.start();
    } catch (IOException e) {
      IOException toThrow = new IOException(ExceptionMessageMap.getMessage("020100"));
      toThrow.initCause(e);
      throw toThrow;
    }
  }

  /**
   * Reads the output from the process and prints it to stdout.
   * 
   * @param p
   *          The process from which to read the output.
   * @exception IOException
   *              An <code>IOException</code> is thrown if there is trouble reading input from the
   *              sub-process.
   */
  public static void getProcessOutput(Process p, IParser parser) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
    while (true) {
      try {
        String str = br.readLine();
        if (null == str) {
          break;
        }
        parser.parseLine(str);
          System.out.println("--------  "+str);
      } catch (IOException e) {
        /*
         * TODO: add logging of any information already read from the InputStream. -- jhl388
         * 06.14.2008
         */
        IOException toThrow = new IOException(ExceptionMessageMap.getMessage("020101"));
        toThrow.initCause(e);
        throw toThrow;
      }
    }
  }

  /**
   * Waits for a process to terminate and then destroys it.
   * 
   * @param p
   *          The process to wait for and destroy.
   * @return The exit value of the process. By convention, 0 indicates normal termination.
   */
  public static int waitForAndDestroyProcess(Process p, IParser parser) {
    /*
     * I'm not sure this is the best way to handle waiting for a process to complete. -- jhl388
     * 06.14.2008
     */
    while (true) {
      try {
        int i = p.waitFor();
        parser.processExitCode(p.exitValue());
        p.destroy();
        return i;
      } catch (InterruptedException e) {
        // TODO: deal with this interrupted exception in a better manner. -- jhl388 06.14.2008
        continue;
      }
    }
  }

  // TODO (jhl388): Add a unit test for this method.
  /*
   * TODO (jhl388): The workingDirectory argument needs to be modified to take a File argument
   * instead of a String argument.
   */
  /**
   * Runs the command specified in the command line with the specified working directory. The
   * IParser is used to parse the response given by the command line.
   * 
   * @param workingDirectory
   *          The working directory in with which to start the process.
   * @param commandLine
   *          The command line to run.
   * @param parser
   *          The parser to use to parse the command line's response.
   * @return The command response from the <code>IParser</code>.
   * @throws IOException
   *           Thrown if there are problems with the subprocess.
   * @throws JavaGitException
   */
  public static CommandResponse runCommand(File workingDirectory, List<String> commandLine,
      IParser parser) throws IOException, JavaGitException {
    ProcessBuilder pb = new ProcessBuilder(commandLine);
     System.err.println("============ command:"+commandLine);
    if (workingDirectory != null) {
      pb.directory(workingDirectory);
    }

    pb.redirectErrorStream(true);

    Process p = startProcess(pb);
    getProcessOutput(p, parser);
    waitForAndDestroyProcess(p, parser);

    return parser.getResponse();
  }

}
