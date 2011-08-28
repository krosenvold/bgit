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
import java.util.StringTokenizer;

import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.JavaGitConfiguration;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.JavaGitException;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitAddOptions;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitAddResponse;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.GitAddResponseImpl;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.IGitAdd;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.utilities.CheckUtilities;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.utilities.ExceptionMessageMap;


/**
 * Command-line implementation of the <code>IGitAdd</code> interface.
 * 
 * TODO (gsd216) - to implement exception chaining.
 */
public class CliGitAdd implements IGitAdd {

  /**
   * Implementations of &lt;git-add&gt; with options and list of files provided.
   */
  public GitAddResponse add(File repositoryPath, GitAddOptions options, List<File> paths)
      throws JavaGitException, IOException {
    CheckUtilities.checkFileValidity(repositoryPath);
    GitAddParser parser = new GitAddParser();
    List<String> command = buildCommand(repositoryPath, options, paths);
    GitAddResponseImpl response = (GitAddResponseImpl) ProcessUtilities.runCommand(repositoryPath,
        command, parser);
    
    if (options != null) {
      addDryRun(options, response);
    }
    return (GitAddResponse) response;
  }

  /**
   * Adds a list of files with no GitAddOptions.
   */
  public GitAddResponse add(File repositoryPath, List<File> files) throws JavaGitException,
      IOException {
    GitAddOptions options = null;
    return add(repositoryPath, options, files);
  }

  /**
   * Adds one file to the index with no GitAddOptions.
   */
  public GitAddResponse add(File repositoryPath, File file) throws JavaGitException, IOException {
    List<File> filePaths = new ArrayList<File>();
    filePaths.add(file);
    GitAddOptions options = null;
    return add(repositoryPath, options, filePaths);
  }

  /**
   * Implementations of &lt;git-add&gt; with options and one file to be added to index.
   */
  public GitAddResponse add(File repositoryPath, GitAddOptions options, File file)
      throws JavaGitException, IOException {
    List<File> paths = new ArrayList<File>();
    paths.add(file);
    return add(repositoryPath, options, paths);
  }

  /**
   * Implementation of &lt;git-add&gt; dry run.
   */
  public GitAddResponse addDryRun(File repositoryPath, List<File> paths) throws JavaGitException,
      IOException {
    GitAddOptions options = new GitAddOptions();
    options.setDryRun(true);
    return add(repositoryPath, options, paths);
  }

  /**
   * Implementations of &lt;git-add&gt; in verbose mode.
   */
  public GitAddResponse addVerbose(File repositoryPath, List<File> paths) throws JavaGitException,
      IOException {
    GitAddOptions options = new GitAddOptions();
    options.setVerbose(true);
    return add(repositoryPath, options, paths);
  }

  /**
   * Implementations of &lt;git-add&gt; with force option set.
   */
  public GitAddResponse addWithForce(File repositoryPath, List<File> paths)
      throws JavaGitException, IOException {
    GitAddOptions options = new GitAddOptions();
    options.setForce(true);
    return add(repositoryPath, options, paths);
  }

  /**
   * if the dry run option was selected then set the flag in response.
   * 
   * @param options
   *          <code>GitAddOptions</code>
   * @param response
   *          <code>gitAddResponse</code>
   */
  private void addDryRun(GitAddOptions options, GitAddResponseImpl response) {
    if (options.dryRun()) {
      response.setDryRun(true);
    }
  }

  private List<String> buildCommand(File repositoryPath, GitAddOptions options, List<File> paths) {
    List<String> command = new ArrayList<String>();
    command.add(JavaGitConfiguration.getGitCommand());
    command.add("add");
    if (options != null) {
      if (options.dryRun()) {
        command.add("-n");
      }
      if (options.verbose()) {
        command.add("-v");
      }
      if (options.force()) {
        command.add("-f");
      }
      if (options.update()) {
        command.add("-u");
      }
      if (options.refresh()) {
        command.add("--refresh");
      }
      if (options.ignoreErrors()) {
        command.add("--ignore-errors");
      }
    }
    if (paths != null && paths.size() > 0) {
      for (File file : paths) {
        command.add(file.getPath());
      }
    }
    return command;
  }

  /**
   * Parser class that implements <code>IParser</code> for implementing a parser for
   * &lt;git-add&gt; output.
   */
  public static class GitAddParser implements IParser {

    private int lineNum;
    private GitAddResponseImpl response;
    private boolean error = false;
    private List<Error> errorList;

    public GitAddParser() {
      lineNum = 0;
      response = new GitAddResponseImpl();
    }

    public void parseLine(String line) {
      if (line == null || line.length() == 0) {
        return;
      }
      lineNum++;
      if (isError(line)) {
        error = true;
        errorList.add( new Error(lineNum, line) );
      }
      else if (isComment(line))
        response.setComment(lineNum, line);
      else
        processLine(line);
    }

    private boolean isError(String line) {
      if (line.trim().startsWith("fatal") || line.trim().startsWith("error")) {
        if ( errorList == null ) {
          errorList = new ArrayList<Error>();
        }
        return true;
      }
      return false;
    }

    private boolean isComment(String line) {
      if (line.startsWith("Nothing specified") || line.contains("nothing added")
          || line.contains("No changes") || line.contains("Maybe you wanted to say")
          || line.contains("usage")) {
        return true;
      }
      return false;
    }

    /**
     * Lines that start with "add" have the second token as the name of the file added by
     * &lt;git-add&gt.
     * 
     * @param line
     */
    private void processLine(String line) {
      if (line.startsWith("add")) {
        StringTokenizer st = new StringTokenizer(line);

        if (st.nextToken().equals("add") && st.hasMoreTokens()) {
          String extractedFileName = filterFileName(st.nextToken());
          if (extractedFileName != null && extractedFileName.length() > 0) {
            File file = new File(extractedFileName);
            response.add(file);
          }
        }
      } else {
        processSpaceDelimitedFilePaths(line);
      }
    }

    private void processSpaceDelimitedFilePaths(String line) {
      if (!line.startsWith("\\s+")) {
        StringTokenizer st = new StringTokenizer(line);
        while (st.hasMoreTokens()) {
          File file = new File(st.nextToken());
          response.add(file);
        }
      }
    }

    public String filterFileName(String token) {
      if (token.length() > 0 && enclosedWithSingleQuotes(token)) {
        int firstQuote = token.indexOf("'");
        int nextQuote = token.indexOf("'", firstQuote + 1);
        if (nextQuote > firstQuote) {
          return token.substring(firstQuote + 1, nextQuote);
        }
      }
      return null;
    }

    public boolean enclosedWithSingleQuotes(String token) {
      if (token.matches("'.*'")) {
        return true;
      }
      return false;
    }

    public void processExitCode(int code) {
    }

    /**
     * Gets a <code>GitAddResponse</code> object containing the info generated by &lt;git-add&gt; command.
     * If there was an error generated while running &lt;git-add&gt; then it throws an exception.
     * 
     * @return GitAddResponse object containing &lt;git-add&gt; response.
     * 
     * @throws JavaGitException If there are any errors generated by &lt;git-add&gt; command.
     */
    public GitAddResponse getResponse() throws JavaGitException {
      if (error) {
        throw new JavaGitException(401000, ExceptionMessageMap.getMessage("401000") + 
            " - git add error message: { " + getError() + " }");
      }
      return response;
    }
    
    /**
     * Retrieves all the errors in the error list and concatenate them together in one string.
     * @return
     */
    private String getError() {
      StringBuffer buf = new StringBuffer();
      for( int i=0; i < errorList.size(); i++ ) {
        buf.append("Line " + errorList.get(i).lineNum + ". " + errorList.get(i).getErrorString());
        if ( i < errorList.size() -1 ) {
          buf.append(" ");
        }
      }
      return buf.toString();
    }
    
    /**
     * Class for storing error details from the &lt;git-add&gt; output.
     *
     */
    private static class Error {
      final int lineNum;
      final String errorStr;
      Error(int lineNum, String errorStr) {
        this.lineNum = lineNum;
        this.errorStr = errorStr;
      }
      
      public String getErrorString() {
        return errorStr;
      }
    }
  }
}
