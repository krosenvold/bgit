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
import edu.nyu.cs.javagit.api.Ref;
import edu.nyu.cs.javagit.api.commands.GitResetOptions;
import edu.nyu.cs.javagit.api.commands.GitResetResponse;
import edu.nyu.cs.javagit.api.commands.GitResetOptions.ResetType;
import edu.nyu.cs.javagit.client.GitResetResponseImpl;
import edu.nyu.cs.javagit.client.IGitReset;
import edu.nyu.cs.javagit.utilities.CheckUtilities;
import edu.nyu.cs.javagit.utilities.ExceptionMessageMap;

/**
 * Command-line implementation of the <code>IGitReset</code> interface.
 */
public class CliGitReset implements IGitReset {

  public GitResetResponse gitReset(File repository) throws IOException, JavaGitException {
    return null;
  }

  public GitResetResponse gitReset(File repository, GitResetOptions options) throws IOException,
      JavaGitException {
    return resetProcessor(repository, options, null);
  }

  public GitResetResponse gitReset(File repository, Ref commitName, List<File> paths)
      throws IOException, JavaGitException {
    return resetProcessor(repository, new GitResetOptions(commitName), paths);
  }

  public GitResetResponse gitReset(File repository, List<File> paths) throws IOException,
      JavaGitException {
    return resetProcessor(repository, new GitResetOptions(), paths);
  }

  public GitResetResponse gitResetHard(File repository, Ref commitName) throws IOException,
      JavaGitException {
    return resetProcessor(repository, new GitResetOptions(ResetType.HARD, commitName), null);
  }

  public GitResetResponse gitResetSoft(File repository, Ref commitName) throws IOException,
      JavaGitException {
    return resetProcessor(repository, new GitResetOptions(ResetType.SOFT, commitName), null);
  }

  protected GitResetResponseImpl resetProcessor(File repository, GitResetOptions options,
      List<File> paths) throws IOException, JavaGitException {
    CheckUtilities.checkNullArgument(repository, "repository");

    List<String> commandLine = buildCommand(options, paths);
    GitResetParser parser = new GitResetParser(repository.getPath());

    return (GitResetResponseImpl) ProcessUtilities.runCommand(repository, commandLine, parser);
  }

  protected List<String> buildCommand(GitResetOptions options, List<File> paths) {

    // TODO (jhl388): Add a unit test for this method.

    List<String> cmd = new ArrayList<String>();
    cmd.add(JavaGitConfiguration.getGitCommand());
    cmd.add("reset");

    if (null != options) {
      if (null == paths) {
        // Only include the reset type if there are no paths. -- jhl388 2008.07.04
        cmd.add(options.getResetType().toString());
      }

      if (options.isQuiet()) {
        cmd.add("-q");
      }

      cmd.add(options.getCommitName().toString());
    }

    if (null != paths) {
      cmd.add("--");
      for (File f : paths) {
        cmd.add(f.getPath());
      }
    }

    return cmd;
  }

  public class GitResetParser implements IParser {

    // TODO (jhl388): Create test case for this class.
    // TODO (jhl388): Finish implementing the GitResetParser.

    // The index of the start of the short SHA1 in the HEAD record. Result of the --hard option
    private final int HEAD_RECORD_SHA1_START = 15;

    /*
     * The working directory path set for the command line. Used to generate the correct paths to
     * the files needing update.
     */
    private String workingDirectoryPath;

    // Holding onto the error message to make part of an exception
    private StringBuffer errorMsg = null;

    // Track the number of lines parsed.
    private int numLinesParsed = 0;

    // The response object for a reset.
    private GitResetResponseImpl response;

    /**
     * Constructor for <code>GitResetParser</code>
     * 
     * @param workingDirectoryPath
     *          The working directory path set for the command line.
     */
    public GitResetParser(String workingDirectoryPath) {
      this.workingDirectoryPath = workingDirectoryPath;
    }

    public void parseLine(String line) {

      // TODO (jhl388): handle error messages in a better manner.

      if (null != errorMsg) {
        ++numLinesParsed;
        errorMsg.append(", line" + numLinesParsed + "=[" + line + "]");
        return;
      }

      if (line.startsWith("HEAD ")) {
        // A record indicating the new HEAD commit resulting from using the --hard option.
        int sha1End = line.indexOf(' ', HEAD_RECORD_SHA1_START);
        Ref sha1 = Ref.createSha1Ref(line.substring(HEAD_RECORD_SHA1_START, sha1End));
        response = new GitResetResponseImpl(sha1, line.substring(sha1End + 1));
      } else if (numLinesParsed > 0 && response.getNewHeadSha1() != null) {
        // No line is expected after getting a HEAD record. Doing nothing for now. Must revisit.

        // TODO (jhl388): Figure out what to do if a line is received after a HEAD record.
      } else if (line.endsWith(": needs update")) {
        // A file needs update record.
        int lastColon = line.lastIndexOf(":");
        File f = new File(workingDirectoryPath + line.substring(0, lastColon));
        response.addFileToFilesNeedingUpdateList(f);
      } else if (numLinesParsed > 0) {
        errorMsg = new StringBuffer();
        errorMsg.append("Unexpected results.  line" + (numLinesParsed + 1) + "=[" + line + "]");
      } else {
        errorMsg = new StringBuffer();
        errorMsg.append("line1=[" + line + "]");
      }

      ++numLinesParsed;
    }

    public void processExitCode(int code) {
    }
    
   /**
     * Gets a <code>GitResetResponseImpl</code> object containing the information from the reset
     * response text parsed by this IParser instance.
     * 
     * @return The <code>GitResetResponseImpl</code> object containing the reset's response
     *         information.
     */
    public GitResetResponseImpl getResponse() throws JavaGitException {
      if (null != errorMsg) {
        throw new JavaGitException(432000, ExceptionMessageMap.getMessage("432000")
            + "  The git-reset error message:  { " + errorMsg.toString() + " }");
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
