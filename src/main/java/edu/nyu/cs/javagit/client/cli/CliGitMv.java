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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.nyu.cs.javagit.api.JavaGitConfiguration;
import edu.nyu.cs.javagit.api.JavaGitException;
import edu.nyu.cs.javagit.api.commands.GitMvOptions;
import edu.nyu.cs.javagit.api.commands.GitMvResponse;
import edu.nyu.cs.javagit.client.GitMvResponseImpl;
import edu.nyu.cs.javagit.client.IGitMv;
import edu.nyu.cs.javagit.utilities.ExceptionMessageMap;

/**
 * Command-line implementation of the <code>IGitMv</code> interface.
 */
public class CliGitMv implements IGitMv {
  // Variable, which if set the fatal messages are not considered for throwing exceptions.
  private boolean dryRun;

  public GitMvResponseImpl mv(File repoPath, File source, File destination) throws IOException,
      JavaGitException {
    List<File> sources = new ArrayList<File>();
    sources.add(source);
    return mvProcess(repoPath, null, sources, destination);
  }

  public GitMvResponseImpl mv(File repoPath, GitMvOptions options, File source, File destination)
      throws IOException, JavaGitException {
    List<File> sources = new ArrayList<File>();
    sources.add(source);
    return mvProcess(repoPath, options, sources, destination);
  }

  public GitMvResponseImpl mv(File repoPath, List<File> sources, File destination) throws IOException,
      JavaGitException {
    return mvProcess(repoPath, null, sources, destination);
  }

  public GitMvResponseImpl mv(File repoPath, GitMvOptions options, List<File> sources, File destination)
      throws IOException, JavaGitException {
    return mvProcess(repoPath, options, sources, destination);
  }

  /**
   * Exec of git-mv command
   * 
   * @param repoPath
   *          A <code>File</code> instance for the path to the repository root (the parent
   *          directory of the .git directory) or a sub-directory in the working tree of the
   *          repository to move/rename against. This argument must represent the absolute path to 
   *          the desired directory as returned by the <code>File.getPath()</code> method. If null 
   *          is passed, a <code>NullPointerException</code> will be thrown.
   * @param options
   *          The options to git-mv command.
   * @param source
   *          The <code>List</code> of source file/folder/symlink which are to be moved to a 
   *          different location. The paths specified in this list must all be relative to the 
   *          path specified in the <code>repository</code> parameter as returned by 
   *          <code>File.getPath()</code>. A non-zero length argument is required for this 
   *          parameter, otherwise a <code>NullPointerException</code> or 
   *          <code>IllegalArgumentException</code> will be thrown.
   * @param destination
   *          The destination file/folder/symlink which the source is renamed or moved to. It 
   *          should be relative to the path specified in the <code>repository</code> 
   *          parameter as returned by <code>File.getPath()</code>. A non-zero length argument is 
   *          required for this parameter, otherwise a <code>NullPointerException</code> or 
   *          <code>IllegalArgumentException</code> will be thrown.
   * @return The results from the git-mv. 
   *           It is expected that GitMv does not notify when a move was successful. This follows 
   *           the response that git-mv itself gives. If the move/rename fails for any reason, 
   *           proper exception messages are generated and thrown.
   * @exception IOException
   *              There are many reasons for which an <code>IOException</code> may be thrown.
   *              Examples include:
   *              <ul>
   *              <li>access to a file is denied</li>
   *              <li>a command is not found on the PATH</li>
   *              </ul>
   * @exception JavaGitException
   *              Thrown when there is an error executing git-mv.
   */
  public GitMvResponseImpl mvProcess(File repoPath, GitMvOptions options, List<File> source,
      File destination) throws IOException, JavaGitException {

    List<String> commandLine = buildCommand(options, source, destination);
    GitMvParser parser = new GitMvParser();

    return (GitMvResponseImpl) ProcessUtilities.runCommand(repoPath, commandLine, parser);
  }

  /**
   * Builds a list of command arguments to pass to <code>ProcessBuilder</code>.
   * 
   * @param options
   *          The options to include on the command line.
   * @param source
   *          The source file/directory/symlink to rename/move.
   * @param destination
   *          The destination file/directory/symlink to rename/move to.
   * @return A list of the individual arguments to pass to <code>ProcessBuilder</code>.
   */
  protected List<String> buildCommand(GitMvOptions options, List<File> source, File destination) {
    List<String> cmd = new ArrayList<String>();

    cmd.add(JavaGitConfiguration.getGitCommand());
    cmd.add("mv");

    if (null != options) {
      if (options.isOptF()) {
        cmd.add("-f");
      }
      if (options.isOptK()) {
        cmd.add("-k");
      }
      if (options.isOptN()) {
        cmd.add("-n");
        setDryRun(true);
      }
    }
    for (File file : source) {
      cmd.add(file.getPath());
    }
    cmd.add(destination.getPath());
    return cmd;
  }

  /**
   * Implementation of the <code>IParser</code> interface in GitMvParser class.
   */
  public class GitMvParser implements IParser {

    // The response object for an mv operation.
    private GitMvResponseImpl response = null;

    // While handling the error cases this buffer will have the error messages.
    private StringBuffer errorMessage = null;
    
    // Track the number of lines parsed.
    private int numLinesParsed = 0;

    /**
     * Parses the line from the git-mv response text.
     * 
     * @param line
     *          The line of text to process.
     * 
     */
    public void parseLine(String line) {
      ++numLinesParsed;
      if (null != errorMessage) {
        errorMessage.append(", line" + numLinesParsed + "=[" + line + "]");
        return;
      }
      if (line.startsWith("error") || line.startsWith("fatal")) {
        if (null == errorMessage) {
          errorMessage = new StringBuffer();
        }
        errorMessage.append("line1=[" + line + "]");
      } else {
        if (null == response) {
          response = new GitMvResponseImpl();
        }
        // This is to parse the output when -n or -f options were given
        parseLineForSuccess(line);
      }
    }

    /**
     * Parses the line for successful execution.
     * 
     * @param line
     *          The line of text to process.
     */
    public void parseLineForSuccess(String line) {
      if (line.contains("Warning:")) {
        response.addComment(line);
      }
      if (line.contains("Adding") || line.contains("Changed")) {
        response.setDestination(new File(line.substring(11)));
      }
      if (line.contains("Deleting")) {
        response.setSource(new File(line.substring(11)));
      }
    }

    public void processExitCode(int code) {
    }
    
    /**
     * Gets a <code>GitMvResponse</code> object containing the information from the git-mv
     * response text parsed by this IParser instance. It is expected that GitMv does not notify 
     * when a move was successful. This follows the response that git-mv itself gives. If the 
     * move/rename fails for any reason, proper exception messages are generated and thrown.
     * 
     * @return The <code>GitMvResponse</code> object containing the git-mv's response
     *         information. It is expected that GitMv does not notify when a move was successful. 
     *         This follows the response that git-mv itself gives. If the move/rename fails for 
     *         any reason, proper exception messages are generated and thrown.
     * @throws <code>JavaGitException</code> if there is an error executing git-mv.
     */
    public GitMvResponse getResponse() throws JavaGitException {
      if (null != errorMessage) {
        if (isDryRun()) {
          throw new JavaGitException(424001, ExceptionMessageMap.getMessage("424001")
              + "  The git-mv dry-run error message:  { " + errorMessage.toString() + " }");
        } else {
          throw new JavaGitException(424000, ExceptionMessageMap.getMessage("424000")
              + "  The git-mv error message:  { " + errorMessage.toString() + " }");
        }
      }
      return response;
    }
  }

  /**
   * @return the dryRun
   */
  public boolean isDryRun() {
    return dryRun;
  }

  /**
   * @param dryRun
   *          the dryRun to set
   */
  public void setDryRun(boolean dryRun) {
    this.dryRun = dryRun;
  }
}
