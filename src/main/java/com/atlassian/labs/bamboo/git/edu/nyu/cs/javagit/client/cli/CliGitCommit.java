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
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.Ref;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitCommitOptions;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.GitCommitResponseImpl;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.IGitCommit;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.utilities.CheckUtilities;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.utilities.ExceptionMessageMap;


/**
 * Command-line implementation of the <code>IGitCommit</code> interface.
 */
public class CliGitCommit implements IGitCommit {

  public GitCommitResponseImpl commitAll(File repository, String message) throws IOException,
      JavaGitException {
    GitCommitOptions options = new GitCommitOptions();
    options.setOptAll(true);
    return commitProcessor(repository, options, message, null);
  }

  public GitCommitResponseImpl commit(File repository, GitCommitOptions options, String message)
      throws IOException, JavaGitException {
    return commitProcessor(repository, options, message, null);
  }

  public GitCommitResponseImpl commit(File repository, GitCommitOptions options, String message,
      List<File> paths) throws IOException, JavaGitException {
    return commitProcessor(repository, options, message, paths);
  }

  public GitCommitResponseImpl commit(File repository, String message) throws IOException,
      JavaGitException {
    return commitProcessor(repository, null, message, null);
  }

  public GitCommitResponseImpl commitOnly(File repository, String message, List<File> paths)
      throws IOException, JavaGitException {
    GitCommitOptions options = new GitCommitOptions();
    options.setOptOnly(true);
    return commitProcessor(repository, options, message, paths);
  }

  /**
   * Processes the commit.
   * 
   * @param repository
   *          A <code>File</code> instance for the path to the repository root (the parent
   *          directory of the .git directory) or a sub-directory in the working tree of the
   *          repository to commit against. This argument must represent the absolute path to the
   *          desired directory as returned by the <code>File.getPath()</code> method. If null is
   *          passed, a <code>NullPointerException</code> will be thrown.
   * @param options
   *          The options to commit with.
   * @param message
   *          The message to attach to the commit. A non-zero length argument is required for this
   *          parameter, otherwise a <code>NullPointerException</code> or
   *          <code>IllegalArgumentException</code> will be thrown.
   * @param paths
   *          A list of folders and/or files to commit. The paths specified in this list must all be
   *          relative to the path specified in the <code>repository</code> parameter as returned
   *          by <code>File.getPath()</code>.
   * @return The results from the commit.
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
  protected GitCommitResponseImpl commitProcessor(File repository, GitCommitOptions options,
      String message, List<File> paths) throws IOException, JavaGitException {
    CheckUtilities.checkNullArgument(repository, "repository");
    CheckUtilities.checkStringArgument(message, "message");

    List<String> commandLine = buildCommand(options, message, paths);
    GitCommitParser parser = new GitCommitParser(repository.getAbsolutePath());

    return (GitCommitResponseImpl) ProcessUtilities.runCommand(repository, commandLine, parser);
  }

  /**
   * Builds a list of command arguments to pass to <code>ProcessBuilder</code>.
   * 
   * @param options
   *          The options to include on the command line.
   * @param message
   *          The message for the commit.
   * @return A list of the individual arguments to pass to <code>ProcessBuilder</code>.
   */
  protected List<String> buildCommand(GitCommitOptions options, String message, List<File> paths) {

    // TODO (jhl388): Add a unit test for this method (CliGitCommit.buildCommand()).

    List<String> cmd = new ArrayList<String>();
    cmd.add(JavaGitConfiguration.getGitCommand());
    cmd.add("commit");

    if (null != options) {
      if (options.isOptAll()) {
        cmd.add("-a");
      }
      if (options.isOptInclude()) {
        cmd.add("-i");
      }
      if (options.isOptNoVerify()) {
        cmd.add("--no-verify");
      }
      if (options.isOptOnly()) {
        cmd.add("-o");
      }
      if (options.isOptSignoff()) {
        cmd.add("-s");
      }
      String author = options.getAuthor();
      if (null != author && author.length() > 0) {
        cmd.add("--author");
        cmd.add(options.getAuthor());
      }
    }

    cmd.add("-m");
    cmd.add(message);

    if (null != paths) {
      cmd.add("--");
      for (File f : paths) {
        cmd.add(f.getPath());
      }
    }

    return cmd;
  }

  public class GitCommitParser implements IParser {

    // Holding onto the error message to make part of an exception
    private StringBuffer errorMsg = null;

    // Track the number of lines parsed.
    private int numLinesParsed = 0;

    // The response object for a commit.
    private GitCommitResponseImpl response;

    // The working directory for the command that was run.
    private String workingDirectory;

    public GitCommitParser(String workingDirectory) {
      this.workingDirectory = workingDirectory;
    }

    public void parseLine(String line) {

      // TODO (jhl388): handle error messages in a better manner.

      if (null != errorMsg) {
        ++numLinesParsed;
        errorMsg.append(", line" + numLinesParsed + "=[" + line + "]");
        return;
      }

      switch (numLinesParsed) {
      case 0:
        parseLineOne(line);
        break;
      case 1:
        parseLineTwo(line);
        break;
      default:
        parseAllOtherLines(line);
      }
      ++numLinesParsed;
    }

    /**
     * Parses the first line of the commit response text.
     * 
     * @param line
     *          The line of text to process.
     */
    private void parseLineOne(String line) {
      if (line.startsWith("Created initial commit ") || line.startsWith("Created commit ")) {
        int locColon = line.indexOf(':');
        int locShortHash = line.lastIndexOf(' ', locColon);
        String shortHash = line.substring(locShortHash + 1, locColon);
        String shortComment = line.substring(locColon + 2);
        response = new GitCommitResponseImpl(Ref.createSha1Ref(shortHash), shortComment);
      } else {
        errorMsg = new StringBuffer();
        errorMsg.append("line1=[" + line + "]");
      }
    }

    /**
     * Parse the second line of the commit response text.
     * 
     * @param line
     *          The line of text to process.
     */
    private void parseLineTwo(String line) {
      int spaceOffset = line.indexOf(' ', 1);
      response.setFilesChanged(line.substring(1, spaceOffset));

      int commaOffset = line.indexOf(',', spaceOffset);
      spaceOffset = line.indexOf(' ', commaOffset + 2);
      response.setLinesInserted(line.substring(commaOffset + 2, spaceOffset));

      commaOffset = line.indexOf(',', spaceOffset);
      spaceOffset = line.indexOf(' ', commaOffset + 2);
      response.setLinesDeleted(line.substring(commaOffset + 2, spaceOffset));
    }

    /**
     * Parses the rest of the lines in the response from a successful commit.
     * 
     * @param line
     *          The line to parse.
     */
    private void parseAllOtherLines(String line) {
      if (line.startsWith(" create")) {
        parseAddDeleteLine(line, true);
      } else if (line.startsWith(" copy")) {
        parseCopyRenameLine(line, true);
      } else if (line.startsWith(" delete")) {
        parseAddDeleteLine(line, false);
      } else if (line.startsWith(" rename")) {
        parseCopyRenameLine(line, false);
      }
    }

    /**
     * Parses an add or delete line. The formats of the lines it parses are (without quotes):
     * <ul>
     * <li>
     * 
     * <pre>
     * &quot; create mode 100644 a/file/name.txt&quot;
     * </pre>
     * 
     * </li>
     * <li>
     * 
     * <pre>
     * &quot; delete mode 100644 a/file/name.txt&quot;
     * </pre>
     * 
     * </li>
     * </ul>
     * 
     * @param line
     *          The line of text to parse.
     * @param isAdd
     *          True if parsing an add (create) line, false if parsing a delete line.
     */
    private void parseAddDeleteLine(String line, boolean isAdd) {
      final int modeOffset = 13;
      final int endModeOffset = 19;
      final int startPathOffset = 20;
      String mode = line.substring(modeOffset, endModeOffset);
      String pathStr = line.substring(startPathOffset);
      File path = new File(workingDirectory + pathStr);
      if (isAdd) {
        response.addAddedFile(path, mode);
      } else {
        response.addDeletedFile(path, mode);
      }
    }

    /**
     * Parses a copy or rename line. The formats of the lines it parses are (without quotes):
     * <ul>
     * <li>
     * 
     * <pre>
     * &quot; copy path/to/{file.txt =&gt; newName.txt} (100%)&quot;
     * </pre>
     * 
     * </li>
     * <li>
     * 
     * <pre>
     * &quot; copy path/to/file.txt =&gt; different/path/newName.txt (5%)&quot;
     * </pre>
     * 
     * </li>
     * </ul>
     * 
     * @param line
     *          The line of text to parse.
     * @param isCopy
     *          True if parsing a copy line, false if parsing a rename line.
     */
    private void parseCopyRenameLine(String line, boolean isCopy) {
      final int PATH_START_COPY = 6;
      final int PATH_START_RENAME = 8;
      int pathStart = (isCopy ? PATH_START_COPY : PATH_START_RENAME);
      int openCurlyOffset = line.indexOf('{', pathStart);
      int openParenOffset = line.lastIndexOf('(');

      String fromPathStr = null;
      String toPathStr = null;

      if (-1 == openCurlyOffset) {
        int arrowOffset = line.indexOf("=>");
        fromPathStr = line.substring(pathStart, arrowOffset - 1);
        toPathStr = line.substring(arrowOffset + 3, openParenOffset - 1);
      } else {
        String base = line.substring(pathStart, openCurlyOffset);
        int arrowOffset = line.indexOf("=>", openCurlyOffset);
        fromPathStr = base + line.substring(openCurlyOffset + 1, arrowOffset - 1);
        int closeCurlyOffset = line.indexOf('}', arrowOffset + 3);
        toPathStr = base + line.substring(arrowOffset + 3, closeCurlyOffset);
      }

      File fromPath = new File(workingDirectory + fromPathStr);
      File toPath = new File(workingDirectory + toPathStr);

      int percentOffset = line.lastIndexOf('%');
      int percentage = 0;
      try {
        percentage = Integer.parseInt(line.substring(openParenOffset + 1, percentOffset));
      } catch (NumberFormatException e) {
        // TODO (jhl388): log this error somehow! Or, at least, deal with it in a better manner.
      }

      if (isCopy) {
        response.addCopiedFile(fromPath, toPath, percentage);
      } else {
        response.addRenamedFile(fromPath, toPath, percentage);
      }
    }

    public void processExitCode(int code) {
    }
    
    /**
     * Gets a <code>GitCommitResponse</code> object containing the information from the commit
     * response text parsed by this IParser instance.
     * 
     * @return The <code>GitCommitResponse</code> object containing the commit's response
     *         information.
     */
    public GitCommitResponseImpl getResponse() throws JavaGitException {
      if (null != errorMsg) {
        throw new JavaGitException(410000, ExceptionMessageMap.getMessage("410000")
            + "  The git-commit error message:  { " + errorMsg.toString() + " }");
      }
      return response;
    }

    /**
     * Gets the number of lines of response text parsed by this IParser.
     * 
     * @return The number of lines of response text parsed by this IParser.
     */
    public int getNumLinesParsed() {
      return numLinesParsed;
    }
  }

}
