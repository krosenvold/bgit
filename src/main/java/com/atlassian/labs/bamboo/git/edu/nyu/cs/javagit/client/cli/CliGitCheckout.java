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
import java.util.Scanner;

import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.JavaGitConfiguration;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.JavaGitException;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.Ref;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.Ref.RefType;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitCheckoutOptions;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitCheckoutResponse;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.GitCheckoutResponseImpl;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.IGitCheckout;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.utilities.CheckUtilities;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.utilities.ExceptionMessageMap;


/**
 * Command-line implementation of the <code>IGitCheckout</code> interface.
 */
public class CliGitCheckout implements IGitCheckout {

  /**
   * String pattern for matching files with modified, deleted, added words in the output.
   */
  private enum Pattern {
    MODIFIED("^M\\s+\\w+"), DELETED("^D\\s+\\w+"), ADDED("^A\\s+\\w+");

    String pattern;

    private Pattern(String pattern) {
      this.pattern = pattern;
    }

    public boolean matches(String line) {
      return line.matches(pattern);
    }
  }

  /**
   * Git checkout with options and base branch information provided to &lt;git-checkout&gt; command.
   */
  public GitCheckoutResponse checkout(File repositoryPath, GitCheckoutOptions options, Ref ref)
      throws JavaGitException, IOException {
    CheckUtilities.checkFileValidity(repositoryPath);
    checkRefAgainstRefType(ref, RefType.HEAD);
    List<String> command = buildCommand(options, ref);
    GitCheckoutParser parser = new GitCheckoutParser();
    GitCheckoutResponse response = (GitCheckoutResponse) ProcessUtilities.runCommand(repositoryPath,
        command, parser);
    return response;
  }

  /**
   * Git checkout without any options and branch information provided. Just a basic checkout
   * command.
   */
  public GitCheckoutResponse checkout(File repositoryPath) throws JavaGitException, IOException {
    GitCheckoutOptions options = null;
    return checkout(repositoryPath, options, null);
  }

  /**
   * Checks out a branch from the git repository with a given branch name.
   */
  public GitCheckoutResponse checkout(File repositoryPath, Ref branch) throws JavaGitException,
      IOException {
    return checkout(repositoryPath, null, branch);
  }

  /**
   * Checks out a list of files from repository, no checkout options provided.
   */
  public GitCheckoutResponse checkout(File repositoryPath, List<File> paths)
      throws JavaGitException, IOException {
    CheckUtilities.checkFileValidity(repositoryPath);
    CheckUtilities.checkNullListArgument(paths, "list of file paths");
    GitCheckoutParser parser = new GitCheckoutParser();
    List<String> command = buildCommand(null, null, paths);
    GitCheckoutResponse response = (GitCheckoutResponse) ProcessUtilities.runCommand(repositoryPath,
        command, parser);
    return response;
  }

  /**
   * Checks out a list of file from repository, with &lt;tree-ish&gt; options provided.
   */
  public GitCheckoutResponse checkout(File repositoryPath, GitCheckoutOptions options, Ref ref,
      List<File> paths) throws JavaGitException, IOException {
    CheckUtilities.checkFileValidity(repositoryPath);
    if ( ref != null && ref.getRefType() == RefType.HEAD ) {
        throw new IllegalArgumentException("Invalid ref type passed as argument to checkout");
    }
    GitCheckoutParser parser = new GitCheckoutParser();
    List<String> command = buildCommand(options, ref, paths);
    return (GitCheckoutResponse) ProcessUtilities.runCommand(repositoryPath, command, parser);
  }

  /**
   * Checks out a file from repository from a particular branch
   */
  public GitCheckoutResponse checkout(File repositoryPath, GitCheckoutOptions options, Ref branch,
      File path) throws JavaGitException, IOException {
    CheckUtilities.checkFileValidity(repositoryPath);
    GitCheckoutParser parser = new GitCheckoutParser();
    List<File> paths = new ArrayList<File>();
    paths.add(path);
    List<String> command = buildCommand(options, branch, paths);
    GitCheckoutResponse response = (GitCheckoutResponse) ProcessUtilities.runCommand(repositoryPath,
        command, parser);
    return response;
  }

  /**
   * Checks out a list of files from a given branch
   */
  public GitCheckoutResponse checkout(File repositoryPath, Ref branch, List<File> paths)
      throws JavaGitException, IOException {
    CheckUtilities.checkFileValidity(repositoryPath);
    GitCheckoutParser parser = new GitCheckoutParser();
    List<String> command = buildCommand(null, branch, paths);
    GitCheckoutResponse response = (GitCheckoutResponse) ProcessUtilities.runCommand(repositoryPath,
        command, parser);
    return response;
  }
  
  /**
   * This is just a test method for verifying that a given ref is not of refType provided as one of the parameters.
   * @param ref
   *          This could be branch, sha1 etc.
   * @param refType
   *          This is the <code>RefType</code> which the ref should not match.
   */
  private void checkRefAgainstRefType( Ref ref, RefType refType ) {
    if ( ref != null && ref.getRefType() == refType ) {
      throw new IllegalArgumentException("Invalid ref type passed as argument to checkout");
    }
  }

  /**
   * builds a &lt;git-checkout&gt; command in sth <code>List<String></code> format.
   * 
   * @param options
   *          <code>GitCheckoutOptions</code> options passed to the &lt;git-checkout&gt; command.
   * @param treeIsh
   *          either a branch type or sha1 type object
   * @param paths
   *          List of files that are to be checked out
   * @return
   * @throws JavaGitException
   */
  private List<String> buildCommand(GitCheckoutOptions options, Ref treeIsh, List<File> paths)
      throws JavaGitException {
    List<String> command = new ArrayList<String>();
    command.add(JavaGitConfiguration.getGitCommand());
    command.add("checkout");
    // Process options
    if (options != null) {
      processOptions(command, options);
    }
    // Process tree-ish
    if (treeIsh != null) {
      command.add(treeIsh.getName());
    }
    // return if no file-paths are provided
    if (paths == null) {
      return command;
    }
    command.add("--");
    for (File file : paths) {
      command.add(file.getName());
    }
    return command;
  }

  private List<String> buildCommand(GitCheckoutOptions options, Ref branch) throws JavaGitException {
    List<String> command = new ArrayList<String>();
    command.add(JavaGitConfiguration.getGitCommand());
    command.add("checkout");
    if (options != null) {
      processOptions(command, options);
    }
    if (branch != null && branch.getName().length() > 0) {
      command.add(branch.getName());
    }
    return command;
  }

  private void processOptions(List<String> command, GitCheckoutOptions options)
      throws JavaGitException {
    if (options.optQ()) {
      command.add("-q");
    }
    if (options.optF()) {
      command.add("-f");
    }
    // --track and --no-track options are valid only with -b option
    Ref newBranch;
    if ((newBranch = options.getOptB()) != null) {
      if (options.optNoTrack() && options.optTrack()) {
        throw new JavaGitException(120, "Both --notrack and --track options are set");
      }
      if (options.optTrack()) {
        command.add("--track");
      }
      if (options.optNoTrack()) {
        command.add("--no-track");
      }
      command.add("-b");
      command.add(newBranch.getName());
    }
    if (options.optL()) {
      command.add("-l");
    }
    if (options.optM()) {
      command.add("-m");
    }
  }

  /**
   * Parser class to parse the output generated by &lt;git-checkout&gt; and return a
   * <code>GitCheckoutResponse</code> object.
   */
  public static class GitCheckoutParser implements IParser {

    private int lineNum;
    private GitCheckoutResponseImpl response;
    private List<ErrorDetails> errors;

    public GitCheckoutParser() {
      lineNum = 0;
      response = new GitCheckoutResponseImpl();
      errors = new ArrayList<ErrorDetails>();
    }

    public void parseLine(String line) {
      if (line == null || line.length() == 0) {
        return;
      }
      ++lineNum;
      if (!isErrorLine(line)) {
        parseSwitchedToBranchLine(line);
        parseFilesInfo(line);
      }
    }

    private boolean isErrorLine(String line) {
      if (line.startsWith("error") || line.startsWith("fatal")) {
        setError(lineNum, line);
        return true;
      }
      return false;
    }

    public void parseSwitchedToBranchLine(String line) {
      if (line.startsWith("Switched to branch")) {
        getSwitchedToBranch(line);
      } else if (line.startsWith("Switched to a new branch")) {
        getSwitchedToNewBranch(line);
      }
    }

    private void getSwitchedToBranch(String line) {
      String branchName = extractBranchName(line);
      Ref branch = Ref.createBranchRef(branchName);
      response.setBranch(branch);
    }

    private void getSwitchedToNewBranch(String line) {
      String newBranchName = extractBranchName(line);
      Ref newBranch = Ref.createBranchRef(newBranchName);
      response.setNewBranch(newBranch);
    }

    private String extractBranchName(String line) {
      int startIndex = Math.max( line.indexOf('"'), line.indexOf('\''));
      int endIndex = Math.max(line.indexOf('"', startIndex + 1), line.indexOf('\'', startIndex + 1));
      return line.substring(startIndex, endIndex + 1);
    }

    private void parseFilesInfo(String line) {
      if (Pattern.MODIFIED.matches(line)) {
        File file = new File(extractFileName(line));
        response.addModifiedFile(file);
        return;
      }
      if (Pattern.DELETED.matches(line)) {
        File file = new File(extractFileName(line));
        response.addDeletedFile(file);
        return;
      }
      if (Pattern.ADDED.matches(line)) {
        File file = new File(extractFileName(line));
        response.addAddedFile(file);
      }
    }

    private String extractFileName(String line) {
      String filename = null;
      Scanner scanner = new Scanner(line);
      while (scanner.hasNext()) {
        filename = scanner.next();
      }
      return filename;
    }

    public void processExitCode(int code) {
    }
    
    public GitCheckoutResponse getResponse() throws JavaGitException {
      if (errors.size() > 0) {
        throw new JavaGitException(406000, ExceptionMessageMap.getMessage("406000") + 
            " - git checkout error message: { " + getError() + " }");
      }
      return response;
    }
   
    private String getError() {
      StringBuilder buffer = new StringBuilder();
      for(int i=0; i < errors.size(); i++ ) {
        buffer.append(errors.get(i) + " ");
      }
      return buffer.toString();
    }
    
    private void setError(int lineNumber, String error) {
      ErrorDetails errorDetails = new ErrorDetails( lineNumber, error );
      errors.add(errorDetails);
    }
    
    private static class ErrorDetails {
      final int lineNumber;
      final String error;
      public ErrorDetails(int lineNumber, String error) {
        this.lineNumber = lineNumber;
        this.error = error;
      }
    }
  }

}
