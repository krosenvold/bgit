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
import java.util.Scanner;
import java.util.StringTokenizer;

import edu.nyu.cs.javagit.api.JavaGitConfiguration;
import edu.nyu.cs.javagit.api.JavaGitException;
import edu.nyu.cs.javagit.api.Ref;
import edu.nyu.cs.javagit.api.commands.GitStatusOptions;
import edu.nyu.cs.javagit.api.commands.GitStatusResponse;
import edu.nyu.cs.javagit.client.GitStatusResponseImpl;
import edu.nyu.cs.javagit.client.IGitStatus;
import edu.nyu.cs.javagit.utilities.CheckUtilities;
import edu.nyu.cs.javagit.utilities.ExceptionMessageMap;

/**
 * Command-line implementation of the <code>IGitStatus</code> interface.
 * 
 * TODO - Need to parse -v option in a better way. Currently <code>GitStatusResponse</code>
 * does not save any output related to -v options such as lines containing diffs, or +++ etc.
 */
public class CliGitStatus implements IGitStatus {

  /**
   * Patterns for matching lines for deleted files, modified files, new files and empty lines.
   */
  public static enum Patterns {
    DELETED("^#\\s+deleted:\\s+.*"), 
    MODIFIED("^#\\s+modified:\\s+.*"), 
    NEW_FILE("^#\\s+new file:\\s+.*"), 
    EMPTY_HASH_LINE("^#\\s*$"),
    RENAMED("^#\\s+renamed:\\s+.*");

    String pattern;

    Patterns(String pattern) {
      this.pattern = pattern;
    }

    public boolean matches(String line) {
      return line.matches(this.pattern);
    }
  }

  private File inputFile = null;

  /**
   * Implementation of <code>IGitStatus</code> method for getting the status of a list of files
   * 
   */
  public GitStatusResponse status(File repositoryPath, GitStatusOptions options, List<File> paths)
      throws JavaGitException, IOException {
    CheckUtilities.checkNullArgument(repositoryPath, "RepositoryPath");
    CheckUtilities.checkFileValidity(repositoryPath);
    List<String> command = buildCommandLine(options, paths);
    GitStatusParser parser;
    if (inputFile != null) {
      parser = new GitStatusParser(repositoryPath.getPath() + File.separator, inputFile);
    } else {
      parser = new GitStatusParser(repositoryPath.getPath() + File.separator);
    }
    GitStatusResponse response = (GitStatusResponseImpl) ProcessUtilities.runCommand(repositoryPath,
        command, parser);
    return response;
  }
  
  /**
   * Implementation of <code>IGitStatus</code> method for getting the status of a file.
   */
  public GitStatusResponse status(File repositoryPath, GitStatusOptions options, File file)
  throws JavaGitException, IOException {
    List<File> paths = new ArrayList<File>();
    paths.add(file);
    return status(repositoryPath, options, paths);
  }
  
  /**
   * Implementation of <code>IGitStatus</code> method with only options passed to &lt;git-status&gt; command.
   */
  public GitStatusResponse status(File repositoryPath, GitStatusOptions options)
  throws JavaGitException, IOException {
    List<File> paths = null;
    return status( repositoryPath, options, paths);
  }
  
  /**
   * Implementation of <code>IGitStatus</code> method with file-paths passed to &lt;git-status&gt; command.
   */
  public GitStatusResponse status(File repositoryPath, List<File> paths) throws JavaGitException,
  IOException {
    return status(repositoryPath, null, paths);
  }
  
  /**
   * Implementation of <code>IGitStatus</code> method for getting the status of repository
   * with no options or files provided.
   */
  public GitStatusResponse status(File repositoryPath) throws JavaGitException, IOException {
    GitStatusOptions options = null;
    List<File> paths = null;
    return status(repositoryPath, options, paths);
  }
  
  /**
   * Implementation of <code>IGitStatus</code> method with options set to all(-a)
   */
  public GitStatusResponse statusAll(File repositoryPath) throws JavaGitException, IOException {
    GitStatusOptions options = new GitStatusOptions();
    return status(repositoryPath, options);
  }
  
  /**
   * Return status for a single <code>File</code>
   *
   * @param repositoryPath
   *          Directory path to the root of the repository.
   * @param options
   *          Options that are passed to &lt;git-status&gt; command.
   * @param file
   *          <code>File</code> instance
   * @return <code>GitStatusResponse</code> object
   * @throws JavaGitException
   *           Exception thrown if the repositoryPath is null
   * @throws IOException
   *           Exception is thrown if any of the IO operations fail.
   */
  public GitStatusResponse getSingleFileStatus(File repositoryPath, GitStatusOptions options, File file)
    throws JavaGitException, IOException {
    CheckUtilities.checkNullArgument(repositoryPath, "RepositoryPath");
    CheckUtilities.checkFileValidity(repositoryPath);
    List<String> command  = buildCommandLine(options, null);
    GitStatusParser parser = new GitStatusParser(repositoryPath.getPath() + File.separator,
        file);

    return (GitStatusResponseImpl) ProcessUtilities.runCommand(repositoryPath, command, parser);
  }
  
  /**
   * Parses options provided by the <code>GitStatusOptions</code> object and adds them to the
   * command.
   * 
   * @param options
   *          <code>GitStatusOptions</code> provided by &lt;gitclipse&gt;.
   * @param paths
   *          List of file paths.
   * @return command to be executed.
   */
  private List<String> buildCommandLine(GitStatusOptions options, List<File> paths) {
    List<String> command = new ArrayList<String>();

    command.add(JavaGitConfiguration.getGitCommand());
    command.add("status");

    if (options != null) {
      setOptions(command, options);
    }

    if (paths != null) {
      for (File file : paths) {
        command.add(file.getPath());
      }
    }
    return command;
  }

  private void setOptions(List<String> argsList, GitStatusOptions options) {
    if (options.isOptAll()) {
      argsList.add("-a");
    }
    if (options.isOptQuiet()) {
      argsList.add("-q");
    }
    if (options.isOptVerbose()) {
      argsList.add("-v");
    }
    if (options.isOptSignOff()) {
      argsList.add("-s");
    }
    if (options.isOptEdit()) {
      argsList.add("-e");
    }
    if (options.isOptInclude()) {
      argsList.add("-i");
    }
    if (options.isOptOnly()) {
      argsList.add("-o");
    }
    if (options.isOptNoVerify()) {
      argsList.add("-n");
    }
    if (options.isOptUntrackedFiles()) {
      argsList.add("--untracked-files");
    }
    if (options.isOptAllowEmpty()) {
      argsList.add("--allow-empty");
    }
    if (!options.isOptReadFromLogFileNull()) {
      argsList.add("-F");
      argsList.add(options.getOptReadFromLogFile().getPath());
    }
    if (!options.isAuthorNull()) {
      argsList.add("--author");
      argsList.add(options.getAuthor());
    }
  }

  public static class GitStatusParser implements IParser {

    private enum State {
      FILES_TO_COMMIT, NOT_UPDATED, UNTRACKED_FILES
    }

    private State outputState;
    private int lineNum;
    private GitStatusResponseImpl response;
    private File inputFile = null;
    
    // The working directory for the command that was run.
    private String workingDirectory;

    public GitStatusParser(String workingDirectory) {
      this.workingDirectory = workingDirectory;
      lineNum = 0;
      response = new GitStatusResponseImpl(workingDirectory);
    }

    public GitStatusParser(String workingDirectory, File in) {
      this.workingDirectory = workingDirectory;
      inputFile = in;
      lineNum = 0;
      response = new GitStatusResponseImpl(workingDirectory);
    }

    public void parseLine(String line) {
      //System.out.println(line);
      if (line == null || line.length() == 0) {
        return;
      }
      ++lineNum;
      if ( isError(line) ) {
        return;
      }
      if (lineNum == 1) {
        parseLineOne(line);
      } else {
        parseOtherLines(line);
      }
    }

    /*
     * Seems like a valid ( non-error ) line 1 always start
     * with a '#' and contains the branch name.
     */
    private void parseLineOne(String line) {
      if (!line.startsWith("#")) {
        return;
      }
      String branch = getBranch(line);
      if (branch != null) {
        String branchName = getBranch(line);
        response.setBranch(Ref.createBranchRef(branchName));
      }
    }

    private void parseOtherLines(String line) {
      if (!(line.charAt(0) == '#')) {
        response.setStatusOutputComment(line);
        return;
      }
      if (line.contains("Changes to be committed")) {
        outputState = State.FILES_TO_COMMIT;
        return;
      } else if (line.contains("Changed but not updated")) {
        outputState = State.NOT_UPDATED;
        return;
      } else {
        if (line.contains("Untracked files")) {
          outputState = State.UNTRACKED_FILES;
          return;
        }
      }
      if (ignoreOutput(line)) {
        return;
      }
      if (Patterns.DELETED.matches(line)) {
        String deletedFile = getFilename(line);
        if ((inputFile != null) && (!deletedFile.matches(inputFile.getName())))
          return;
        addDeletedFile(deletedFile);
        return;
      }
      if (Patterns.MODIFIED.matches(line)) {
        String modifiedFile = getFilename(line);
        if ((inputFile != null) && (!modifiedFile.matches(inputFile.getName())))
          return;
        addModifiedFile(modifiedFile);
        return;
      }
      if (Patterns.NEW_FILE.matches(line)) {
        String newFile = getFilename(line);
        if ((inputFile != null) && (!newFile.matches(inputFile.getName())))
          return;
        addNewFile(newFile);
        return;
      }
      if (outputState == State.UNTRACKED_FILES) {
        String untrackedFile = getFilename(line);
        if ((inputFile != null) && (!untrackedFile.matches(inputFile.getName())))
          return;
        addUntrackedFile(untrackedFile);
      }
      if ( Patterns.RENAMED.matches(line)) {
    	String renamedFile = getFilename(line);
    	if ((inputFile != null) && (!renamedFile.matches(inputFile.getName())))
          return;
    	addRenamedFileToCommit(renamedFile);
      }
    }

    private boolean isError(String line) {
      if (line.startsWith("fatal") || line.startsWith("Error") || line.startsWith("error")) {
        response.setError(lineNum, line);
        return true;
      }
      return false;
    }

    private void addNewFile(String filename) {
      response.addToNewFilesToCommit(new File(workingDirectory + filename));
    }

    private void addDeletedFile(String filename) {
      File file = new File(workingDirectory + filename);
      switch (outputState) {
      case FILES_TO_COMMIT:
        response.addToDeletedFilesToCommit(file);
        break;

      case NOT_UPDATED:
        response.addToDeletedFilesNotUpdated(file);
        break;
      }
    }

    private void addModifiedFile(String filename) {
      File file = new File(workingDirectory + filename);
      switch (outputState) {
      case FILES_TO_COMMIT:
        response.addToModifiedFilesToCommit(file);
        break;

      case NOT_UPDATED:
        response.addToModifiedFilesNotUpdated(file);
        break;
      }
    }

    private void addRenamedFileToCommit(String renamedFile) {
      response.addToRenamedFilesToCommit(new File(workingDirectory + renamedFile));	
    }
    
    private void addUntrackedFile(String filename) {
      response.addToUntrackedFiles(new File(workingDirectory + filename));
    }

    private String getBranch(String line) {
      StringTokenizer st = new StringTokenizer(line);
      String last = null;
      while (st.hasMoreTokens()) {
        last = st.nextToken();
      }
      return last;
    }

    //

    public String getFilename(String line) {
      String filename = null;
      Scanner scanner = new Scanner(line);
      while (scanner.hasNext()) {
        filename = scanner.next();
      }
      return filename;
    }

    private boolean ignoreOutput(String line) {
      if (line.contains("(use \"git reset")) {
        return true;
      }
      if (line.contains("(use \"git add ")) {
        return true;
      }
      if (line.contains("(use \"git add/rm")) {
        return true;
      }
      if (Patterns.EMPTY_HASH_LINE.matches(line)) {
        return true;
      }
      return false;
    }

    public void processExitCode(int code) {
    }
    
    public GitStatusResponse getResponse() throws JavaGitException {
      if( response.errorState() ) {
        throw new JavaGitException(438000, ExceptionMessageMap.getMessage("438000") + 
            " - git status error message: { " + response.getError() + " }");
      }
      return response;
    }
  }
}
