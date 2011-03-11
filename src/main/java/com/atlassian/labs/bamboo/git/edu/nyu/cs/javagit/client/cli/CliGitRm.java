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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.JavaGitConfiguration;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.JavaGitException;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitRmOptions;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitRmResponse;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.GitRmResponseImpl;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.IGitRm;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.utilities.ExceptionMessageMap;


/**
 * Command-line implementation of the <code>IGitRm</code> interface.
 */
public class CliGitRm implements IGitRm {

  // TODO (jhl388): Add test cases for this class.

  public GitRmResponse rm(File repository, File path) throws IOException, JavaGitException {
    return processRm(repository, null, path, null);
  }

  public GitRmResponse rm(java.io.File repository, GitRmOptions options, File path)
      throws IOException, JavaGitException {
    return processRm(repository, options, path, null);
  }

  public GitRmResponse rm(File repository, List<File> paths) throws IOException, JavaGitException {
    return processRm(repository, null, null, paths);
  }

  public GitRmResponse rm(File repository, GitRmOptions options, List<File> paths)
      throws IOException, JavaGitException {
    return processRm(repository, options, null, paths);
  }

  public GitRmResponse rmCached(File repository, List<File> paths) throws IOException,
      JavaGitException {
    GitRmOptions options = new GitRmOptions();
    options.setOptCached(true);
    return processRm(repository, options, null, paths);
  }

  public GitRmResponse rmRecursive(File repository, List<File> paths) throws IOException,
      JavaGitException {
    GitRmOptions options = new GitRmOptions();
    options.setOptR(true);
    return processRm(repository, options, null, paths);
  }

  /**
   * Processes an incoming <code>GitRm</code> request.
   * 
   * @param repository
   *          The path to the repository.
   * @param options
   *          The options to use in constructing the command line.
   * @param path
   *          A single file/directory to delete. This should be null if there is a list of paths to
   *          delete.
   * @param paths
   *          A list of files/paths to delete. This should be null if there is a single path to
   *          delete.
   * @return The response from running the command.
   * @exception IOException
   *              There are many reasons for which an <code>IOException</code> may be thrown.
   *              Examples include:
   *              <ul>
   *              <li>a directory doesn't exist</li>
   *              <li>access to a file is denied</li>
   *              <li>a command is not found on the PATH</li>
   *              </ul>
   * @exception JavaGitException
   *              Thrown when there is an error making the commit.
   */
  private GitRmResponse processRm(File repository, GitRmOptions options, File path, List<File> paths)
      throws IOException, JavaGitException {
    List<String> cmdline = buildCommandLine(options, path, paths);

    GitRmParser parser = new GitRmParser();
    return (GitRmResponse) ProcessUtilities.runCommand(repository, cmdline, parser);
  }

  /**
   * Builds the command line.
   * 
   * @param options
   *          The options to build with.
   * @param path
   *          If just a single path, this is it.
   * @param paths
   *          If there are multiple paths, these are they. <code>path</code> must be null for
   *          these paths to be used.
   * @return The list of arguments for the command line.
   */
  private List<String> buildCommandLine(GitRmOptions options, File path, List<File> paths) {
    List<String> cmdline = new ArrayList<String>();

    cmdline.add(JavaGitConfiguration.getGitCommand());
    cmdline.add("rm");

    if (null != options) {
      if (options.isOptCached()) {
        cmdline.add("--cached");
      }
      if (options.isOptF()) {
        cmdline.add("-f");
      }
      if (options.isOptN()) {
        cmdline.add("-n");
      }
      if (options.isOptQ()) {
        cmdline.add("-q");
      }
      if (options.isOptR()) {
        cmdline.add("-r");
      }
    }
    if (null != path) {
      cmdline.add(path.getPath());
    } else {
      for (File f : paths) {
        cmdline.add(f.getPath());
      }
    }
    return cmdline;
  }

  class GitRmParser implements IParser {

    // Holding onto the error message to make part of an exception
    private StringBuffer errorMsg = null;

    // Track the number of lines parsed.
    private int numLinesParsed = 0;

    // The response.
    private GitRmResponseImpl response = new GitRmResponseImpl();

    public void parseLine(String line) {

      // TODO (jhl388): handle error messages in a better manner.

      if (null != errorMsg) {
        ++numLinesParsed;
        errorMsg.append(", line" + numLinesParsed + "=[" + line + "]");
        return;
      }

      if (line.startsWith("rm '")) {
        int locQuote = line.indexOf('\'');
        int locLastQuote = line.lastIndexOf('\'');
        response.addFileToRemovedFilesList(new File(line.substring(locQuote + 1, locLastQuote)));
      } else {
        errorMsg = new StringBuffer();
        errorMsg.append("line1=[" + line + "]");
      }
    }

    public void processExitCode(int code) {
    }
    
   public GitRmResponse getResponse() throws JavaGitException {
      if (null != errorMsg) {
        throw new JavaGitException(434000, ExceptionMessageMap.getMessage("434000") + "  { "
            + errorMsg.toString() + " }");
      }
      return response;
    }
  }

}
