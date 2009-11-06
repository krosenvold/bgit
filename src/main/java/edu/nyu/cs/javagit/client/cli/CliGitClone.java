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
import edu.nyu.cs.javagit.api.commands.CommandResponse;
import edu.nyu.cs.javagit.api.commands.GitCloneOptions;
import edu.nyu.cs.javagit.client.GitCloneResponseImpl;
import edu.nyu.cs.javagit.client.IGitClone;

/**
 * Command-line implementation of the <code>IGitClone</code> interface.
 */
public class CliGitClone implements IGitClone {

  public GitCloneResponseImpl clone(File workingDirectoryPath, String repository) throws IOException,
      JavaGitException {
    return cloneProcess(workingDirectoryPath, null, repository, null);
  }

  public GitCloneResponseImpl clone(File workingDirectoryPath, GitCloneOptions options, String repository)
      throws IOException, JavaGitException {
    return cloneProcess(workingDirectoryPath, options, repository, null);
  }

  public GitCloneResponseImpl clone(File workingDirectoryPath, String repository, File directory)
      throws IOException, JavaGitException {
    return cloneProcess(workingDirectoryPath, null, repository, directory);
  }

  public GitCloneResponseImpl clone(File workingDirectoryPath, GitCloneOptions options, String repository,
      File directory) throws IOException, JavaGitException {
    return cloneProcess(workingDirectoryPath, options, repository, directory);
  }
  
  /**
   * Process the git-clone command, to make a clone of the git repository.
   * 
   * @param workingDirectoryPath
   *          A <code>File</code> instance for the path to the working directory. This argument
   *          must represent the absolute path to the desired directory as returned by the
   *          <code>File.getPath()</code> method. If null is passed, a
   *          <code>NullPointerException</code> will be thrown.
   * @param options
   *          The options for the git-clone command. If the value is null, a
   *          <code>NullPointerException</code> will be thrown.
   * @param repository
   *          A <code>URL</code> instance for the repository to be cloned. If null is passed, a
   *          <code>NullPointerException</code> will be thrown.
   * @param directory
   *          A <code>File</code> instance for the directory where the repository is to be cloned.
   *          If null is passed, a <code>NullPointerException</code> will be thrown.
   * @return The result of the git clone.
   * @throws IOException
   *           There are many reasons for which an <code>IOException</code> may be thrown.
   *           Examples include:
   *           <ul>
   *           <li>a directory doesn't exist</li>
   *           <li>a command is not found on the PATH</li>
   *           </ul>
   * @throws JavaGitException
   *           Thrown when there is an error executing git-clone.
   */
  public GitCloneResponseImpl cloneProcess(File workingDirectoryPath, GitCloneOptions options,
      String repository, File directory) throws IOException, JavaGitException {
    List<String> commandLine = buildCommand(options, repository, directory);
    GitCloneParser parser = new GitCloneParser();

    return (GitCloneResponseImpl) ProcessUtilities.runCommand(workingDirectoryPath, commandLine, parser);
  }
  
  /**
   * Builds a list of command arguments to pass to <code>ProcessBuilder</code>.
   * @param options
   *          The options for the git-clone command. If the value is null, a
   *          <code>NullPointerException</code> will be thrown.
   * @param repository
   *          A <code>URL</code> instance for the repository to be cloned. If null is passed, a
   *          <code>NullPointerException</code> will be thrown.
   * @param directory
   *          A <code>File</code> instance for the directory where the repository is to be cloned.
   *          If null is passed, a <code>NullPointerException</code> will be thrown.
   * @return The result of the git clone.
   */
  protected List<String> buildCommand(GitCloneOptions options, String repository, File directory) {
    List<String> cmd = new ArrayList<String>();

    cmd.add(JavaGitConfiguration.getGitCommand());
    cmd.add("clone");
    cmd.add(repository);
    cmd.add("checkout");
    return cmd;
  }
  
  /**
   * Implementation of the <code>IParser</code> interface in GitCloneParser class.
   */
  public class GitCloneParser implements IParser {

    public CommandResponse getResponse() throws JavaGitException {
      // TODO Auto-generated method stub
      return null;
    }

    public void parseLine(String line) {
      // TODO Auto-generated method stub
      
    }

    public void processExitCode(int code) {
      // TODO Auto-generated method stub
      
    }
  }
}
