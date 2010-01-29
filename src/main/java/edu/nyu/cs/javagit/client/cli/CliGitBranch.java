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
import java.util.StringTokenizer;

import edu.nyu.cs.javagit.api.JavaGitConfiguration;
import edu.nyu.cs.javagit.api.JavaGitException;
import edu.nyu.cs.javagit.api.Ref;
import edu.nyu.cs.javagit.api.commands.GitBranchOptions;
import edu.nyu.cs.javagit.api.commands.GitBranchResponse;
import edu.nyu.cs.javagit.api.commands.GitBranchResponse.BranchRecord;
import edu.nyu.cs.javagit.api.commands.GitBranchResponse.responseType;
import edu.nyu.cs.javagit.client.GitBranchResponseImpl;
import edu.nyu.cs.javagit.client.IGitBranch;
import edu.nyu.cs.javagit.utilities.CheckUtilities;
import edu.nyu.cs.javagit.utilities.ExceptionMessageMap;

/**
 * Command-line implementation of the <code>IGitBranch</code> interface.
 */
public class CliGitBranch implements IGitBranch {
  public GitBranchResponse branch(File repoPath) throws IOException, JavaGitException {
    return branchProcess(repoPath, null, null, null, null);
  }

  public GitBranchResponseImpl branch(File repoPath, GitBranchOptions options) throws IOException,
      JavaGitException {
    return branchProcess(repoPath, options, null, null, null);
  }

  public GitBranchResponseImpl deleteBranch(File repoPath, boolean forceDelete, boolean remote,
      Ref branchName) throws IOException, JavaGitException {
    GitBranchOptions options = new GitBranchOptions();
    setDeleteOptions(options, forceDelete, remote);
    return branchProcess(repoPath, options, branchName, null, null);
  }

  public GitBranchResponseImpl deleteBranches(File repoPath, boolean forceDelete, boolean remote,
      List<Ref> branchList) throws IOException, JavaGitException {
    GitBranchOptions options = new GitBranchOptions();
    setDeleteOptions(options, forceDelete, remote);
    return branchProcess(repoPath, options, null, null, branchList);
  }

  public GitBranchResponseImpl renameBranch(File repoPath, boolean forceRename, Ref newName)
      throws IOException, JavaGitException {
    GitBranchOptions options = new GitBranchOptions();
    if (forceRename) {
      options.setOptMUpper(true);
    } else {
      options.setOptMLower(true);
    }
    return branchProcess(repoPath, options, newName, null, null);
  }

  public GitBranchResponseImpl renameBranch(File repoPath, boolean forceRename, Ref oldName, Ref newName)
      throws IOException, JavaGitException {
    GitBranchOptions options = new GitBranchOptions();
    if (forceRename) {
      options.setOptMUpper(true);
    } else {
      options.setOptMLower(true);
    }
    return branchProcess(repoPath, options, oldName, newName, null);
  }

  public GitBranchResponseImpl createBranch(File repoPath, Ref branchName) throws IOException,
      JavaGitException {
    return branchProcess(repoPath, null, branchName, null, null);
  }

  public GitBranchResponseImpl createBranch(File repoPath, GitBranchOptions options, Ref branchName)
      throws IOException, JavaGitException {
    return branchProcess(repoPath, options, branchName, null, null);
  }

  public GitBranchResponseImpl createBranch(File repoPath, Ref branchName, Ref startPoint)
      throws IOException, JavaGitException {
    return branchProcess(repoPath, null, branchName, startPoint, null);
  }

  public GitBranchResponseImpl createBranch(File repoPath, GitBranchOptions options, Ref branchName,
      Ref startPoint) throws IOException, JavaGitException {
    return branchProcess(repoPath, options, branchName, startPoint, null);
  }

  /**
   * Process the git-branch command, to show/delete/create/rename branches.
   * 
   * @param repoPath
   *          A <code>File</code> instance for the path to the repository. If null is passed, a
   *          <code>NullPointerException</code> will be thrown.
   * @param options
   *          The options to include on the command line.
   * @param arg1
   *          When renaming a branch to a different name, this is the old branch. When creating a
   *          branch this the branch name.
   * @param arg2
   *          When renaming a branch to a new branch name, this is the new branch name. When
   *          creating a branch, this is the head to start from.
   * @param branchList
   *          List of branches need to be deleted.
   * @return The result of the git branch.
   * @throws IOException
   *           There are many reasons for which an <code>IOException</code> may be thrown.
   *           Examples include:
   *           <ul>
   *           <li>a directory doesn't exist</li>
   *           <li>a command is not found on the PATH</li>
   *           </ul>
   * @throws JavaGitException
   *           Thrown when there is an error executing git-branch.
   */
  public GitBranchResponseImpl branchProcess(File repoPath, GitBranchOptions options, Ref arg1,
      Ref arg2, List<Ref> branchList) throws IOException, JavaGitException {
    CheckUtilities.checkNullArgument(repoPath, "repository path");
    List<String> commandLine = buildCommand(options, arg1, arg2, branchList);
    GitBranchParser parser = new GitBranchParser();

    return (GitBranchResponseImpl) ProcessUtilities.runCommand(repoPath, commandLine, parser);
  }

  /**
   * Builds a list of command arguments to pass to <code>ProcessBuilder</code>.
   * 
   * @param options
   *          The options to include on the command line.
   * @param arg1
   *          When renaming a branch to a different name, this is the old branch. When creating a
   *          branch this the branch name.
   * @param arg2
   *          When renaming a branch to a new branch name, this is the new branch name. When
   *          creating a branch, this is the head to start from.
   * @param branchList
   *          List of branches need to be deleted.
   * @return A list of the individual arguments to pass to <code>ProcessBuilder</code>.
   */
  protected List<String> buildCommand(GitBranchOptions options, Ref arg1, Ref arg2,
      List<Ref> branchList) {
    List<String> cmd = new ArrayList<String>();

    cmd.add(JavaGitConfiguration.getGitCommand());
    cmd.add("branch");

    if (null != options) {
      Ref commit = options.getOptContains();
      if (null != commit) {
        cmd.add("--contains");
        cmd.add(commit.getName());
      }
      if (options.isOptVerbose()) {
        cmd.add("--verbose");
      }
      if (options.isOptAbbrev()) {
        if (options.getOptAbbrevLen() != GitBranchOptions.DEFAULT_ABBREV_LEN) {
          cmd.add("--abbrev=" + Integer.toString(options.getOptAbbrevLen()));
        } else {
          cmd.add("--abbrev");
        }
      }
      if (options.isOptNoAbbrev()) {
        cmd.add("--no-abbrev");
      }
      if (options.isOptA()) {
        cmd.add("-a");
      }
      if (options.isOptDLower()) {
        cmd.add("-d");
      }
      if (options.isOptMLower()) {
        cmd.add("-m");
      }
      if (options.isOptDUpper()) {
        cmd.add("-D");
      }
      if (options.isOptMUpper()) {
        cmd.add("-M");
      }
      if (options.isOptColor()) {
        cmd.add("--color");
      }
      if (options.isOptNoColor()) {
        cmd.add("--no-color");
      }
      if (options.isOptF()) {
        cmd.add("-f");
      }
      if (options.isOptL()) {
        cmd.add("-l");
      }
      if (options.isOptMerged()) {
        cmd.add("--merged");
      }
      if (options.isOptNoMerged()) {
        cmd.add("--no-merged");
      }
      if (options.isOptR()) {
        cmd.add("-r");
      }
      if (options.isOptTrack()) {
        cmd.add("--track");
      }
      if (options.isOptNoTrack()) {
        cmd.add("--no-track");
      }
    }
    if (null != branchList) {
      if ((null != arg1) || (null != arg2)) {
        throw new IllegalArgumentException();
      }
      for (Ref branch : branchList) {
        cmd.add(branch.getName());
      }
    } else {
      if (null != arg1) {
        cmd.add(arg1.getName());
      }
      if (null != arg2) {
        cmd.add(arg2.getName());
      }
    }
    return cmd;
  }

  /**
   * Sets the options for delete.
   * 
   * @param options
   *          The <code>GitBranchOptions</code> object.
   * @param forceDelete
   *          The forceDelete boolean flag. True if branch to be force deleted, false otherwise. 
   * @param remote
   *          The remote boolean flag. True if a remote branch is being deleted, false otherwise.
   */
  public void setDeleteOptions(GitBranchOptions options, boolean forceDelete, boolean remote) {
    if (forceDelete) {
      options.setOptDUpper(true);
    } else {
      options.setOptDLower(true);
    }
    if (remote) {
      options.setOptR(true);
    }
  }

  /**
   * Implementation of the <code>IParser</code> interface in GitBranchParser class.
   */
  public static class GitBranchParser implements IParser {
    // The response object for a branch operation.
    private GitBranchResponseImpl response;

    // While handling the error cases this buffer will have the error messages.
    private StringBuffer errorMessage = null;

    // Track the number of lines parsed.
    private int numLinesParsed = 0;

    /**
     * Parses the line from the git-branch response text.
     * 
     * @param line
     *          The line of text to process.
     */
    public void parseLine(String line) {
      ++numLinesParsed;
      if (null != errorMessage) {
        errorMessage.append(", line" + numLinesParsed + "=[" + line + "]");
        return;
      }
      if (line.contains("fatal:") || line.contains("error:")) {
        if (null == errorMessage) {
          errorMessage = new StringBuffer();
        }
        errorMessage.append("line1=[" + line + "]");
      } else {
        if (null == response) {
          response = new GitBranchResponseImpl();
        }

        if (line.startsWith("Deleted branch")) {
          int indexOfBranch = line.indexOf("branch");
          String branchName = line.substring(indexOfBranch + 7, line.length()-1);
          response.setResponseType(responseType.MESSAGE);
          if (1 == numLinesParsed) {
            response.addMessages(line.substring(0, indexOfBranch + 6));
          }
          response.addIntoBranchList(Ref.createBranchRef(branchName));
        } else if (null == line) {
          response.setResponseType(responseType.EMPTY);
        } else {
          handleBranchDisplay(line);
        }
      }
    }

    /**
     * Parses the output of git-branch with different options and without any argumen.
     * 
     * @param line
     *          The line of text to be parsed.
     */
    public void handleBranchDisplay(String line) {
      String nextWord;
      if (line.startsWith("* (no branch)")) {
          return;
      }
      boolean isCurrentBranch = false;
      StringTokenizer st = new StringTokenizer(line);

      nextWord = st.nextToken();
      response.setResponseType(responseType.BRANCH_LIST);

      if ('*' == nextWord.charAt(0)) {
        isCurrentBranch = true;
        nextWord = st.nextToken();
        response.setCurrentBranch(Ref.createBranchRef(nextWord));
      }
      response.addIntoBranchList(Ref.createBranchRef(nextWord));

      if (st.hasMoreTokens()) {
        Ref branch = Ref.createBranchRef(nextWord);
        nextWord = st.nextToken();
        Ref sha1 = Ref.createSha1Ref(nextWord);
        int indexOfSha = line.indexOf(nextWord);
        String comment = line.substring(indexOfSha+nextWord.length()+1);
        BranchRecord record = new BranchRecord(branch, sha1, comment, isCurrentBranch);
        response.addIntoListOfBranchRecord(record);
      }
    }

    public void processExitCode(int code) {
    }
    
    /**
     * Throws appropriate <code>JavaGitException</code> for an error case or returns the 
     * <code>GitBranchResponse</code> object to the upper layer.
     * 
     * @return GitBranchResponse object.
     * @throws JavaGitException
     *           Thrown when there is an error executing git-branch.
     */
    public GitBranchResponse getResponse() throws JavaGitException {
      if (null != errorMessage) {
        throw new JavaGitException(404000, ExceptionMessageMap.getMessage("404000")
            + "  The git-branch error message:  { " + errorMessage.toString() + " }");
      }
      return response;
    }
  }
}
