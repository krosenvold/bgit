package com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.CommandResponse;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.cli.IParser;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.cli.ProcessUtilities;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.utilities.CheckUtilities;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.utilities.ExceptionMessageMap;


/**
 * This class encapsulates our global API options that client code may want to get or set.
 * 
 * TODO (rs2705): Create a more generalized/modularized setup where variables and methods related to
 * specific implementation types are self-contained in their own separate classes, instead of
 * floating around in here.
 */
public final class JavaGitConfiguration {

  /*
   * The path to our git binaries. Default to null, which means that the git command is available
   * via the system PATH environment variable.
   */
  private static File gitPath = null;

  /*
   * The version string fpr the locally-installed git binaries.
   */
  private static String gitVersion = null;

  /*
   * The name for the git executable.
   */
  private static String gitExecutable = "git";

  static {
    if( System.getProperty("os.name").startsWith("Windows") ) {
      gitExecutable = "git.exe";
    }
  }

  /**
   * Constructor - private because this is an all-static class.
   */
  private JavaGitConfiguration() {

  }

  /**
   * This function gets called in one of two ways:
   * 
   * 1) When client code sets the path to the git binaries explicitly by calling {@link setGitPath},
   * this function is used to determine if the path is usable or not. In this case, the
   * <code>File</code> path argument will be the new path to the git binaries.
   * 
   * 2) When client code calls {@link getGitVersion} and the path has not been set explicitly, we
   * call this function to figure out the version. In this case, the <code>File</code> path
   * argument will be null.
   * 
   * 
   * @param path
   *          <code>File</code> object representing the directory containing the git binaries. If
   *          null, the previously-set path is used (if any). It must contain either an absolute
   *          path, or a valid path relative to the current working directory.
   * @return The git version string.
   * @throws JavaGitException
   *           Thrown if git is not found at the provided path.
   */
  private static String determineGitVersion(File path) throws JavaGitException {

    /*
     * If they already set the path explicitly, or are in the process of doing so (via the path
     * argument), make sure to prefix the git call with it. If they didn't, assume it's blank.
     */
    String gitPrefix = "";
    if (path != null) {
      // We got a path passed in as an argument.
      gitPrefix = path.getAbsolutePath() + File.separator;
    } else if (gitPath != null) {
      // They didn't pass in a path, but previously set it explicitly via setGitPath.
      gitPrefix = getGitCommandPrefix();
    }

    String gitCommand = gitPrefix + gitExecutable;
    if (!(gitPrefix.equals(""))) {
      // If we've got a full path to the git binary here, ensure it actually exists.
      if (!(new File(gitCommand).exists())) {
        throw new JavaGitException(100002, ExceptionMessageMap.getMessage("100002"));
      }
    }

    List<String> commandLine = new ArrayList<String>();
    commandLine.add(gitCommand);
    commandLine.add("--version");

    // Now run the actual git version command.
    GitVersionResponse response;
    try {
      // We're passing in a working directory of null, which is "don't care" to runCommand
      response = (GitVersionResponse) ProcessUtilities.runCommand(null, commandLine,
          new GitVersionParser());
    } catch (Exception e) {
      throw new JavaGitException(100001, ExceptionMessageMap.getMessage("100001"));
    }

    String version = response.getVersion();
    if (!(isValidVersionString(version))) {
      throw new JavaGitException(100001, ExceptionMessageMap.getMessage("100001"));
    }

    return version;
  }

  /**
   * Return the complete string necessary to invoke git on the command line. Could be an absolute
   * path to git, or if the path was never set explicitly, just "git".
   * 
   * @return <code>String</code> containing the path to git, ending with the git executable's name
   *         itself.
   */
  public static String getGitCommand() {
    return getGitCommandPrefix() + gitExecutable;
  }

  /**
   * Return an absolute path capable of being dropped in front of the command-line git invocation.
   * If the path hasn't been set explicitly, just return the empty string and assume git is in this
   * process' PATH environment variable.
   * 
   * @return The <code>String</code> that points to the git executable.
   */
  public static String getGitCommandPrefix() {
    return ((gitPath == null) ? "" : (gitPath.getAbsolutePath() + File.separator));
  }

  /**
   * Accessor method for the <code>File</code> object representing the path to git. If the git
   * path is never set explicitly, this will return null.
   * 
   * @return <code>File</code> object pointing at the directory containing git.
   */
  public static File getGitPath() {
    return gitPath;
  }

  /**
   * Returns the version number of the underlying git binaries. If this method is called and we
   * don't know the version yet, it tries to figure it out. (The version gets set if
   * {@link #setGitPath(File) setGitPath} was previously called.)
   * 
   * @return The git version <code>String</code>.
   */
  public static String getGitVersion() throws JavaGitException {
    // If the version hasn't been found yet, let's do some lazy initialization here.
    if (gitVersion == null) {
      gitVersion = determineGitVersion(gitPath);
    }

    return gitVersion;
  }

  /**
   * Judge the validity of a given git version string. This can be difficult to do, as there seems
   * to be no deliberately-defined git version format. So, here we do a minimal sanity check for two
   * things: 1. The first character in the version is a number. 2. There's at least one period in
   * the version string.
   * 
   * @param version
   * @return
   */
  private static boolean isValidVersionString(String version) {
    /*
     * Git version strings can vary, so let's do a minimal sanity check for two things: 1. The first
     * character in the version is a number. 2. There's at least one period in the version string.
     * 
     * TODO (rs2705): Make this more sophisticated by parsing out a major/minor version number, and
     * ensuring it's >= some minimally-required version.
     */
    try {
      Integer.parseInt(version.substring(0, 1));
    } catch (NumberFormatException e) {
      // First character in the version string was not a valid number!
      return false;
    }

    if (version.indexOf(".") == -1) {
      // The version string doesn't contain a period!
      return false;
    }

    return true;
  }

  /**
   * Called when client code wants to explicitly tell us where to find git on their filesystem. If
   * never called, we assume that git is in a directory in the PATH environment variable for this
   * process. Passing null as the path argument will unset an explicitly-set path and revert to
   * looking for git in the PATH.
   * 
   * @param path
   *          <code>File</code> object representing the directory containing the git binaries. It
   *          must contain either an absolute path, or a valid path relative to the current working
   *          directory.
   * @throws IOException
   *           Thrown if the provided path does not exist.
   * @throws JavaGitException
   *           Thrown if git does not exist at the provided path, or the provided path is not a
   *           directory.
   */
  public static void setGitPath(File path) throws IOException, JavaGitException {
    if (path != null) {
      CheckUtilities.checkFileValidity(path);

      if (!(path.isDirectory())) {
        throw new JavaGitException(020002, ExceptionMessageMap.getMessage("020002") + " { path=["
            + path.getPath() + "] }");
      }
    }

    try {
      gitVersion = determineGitVersion(path);
    } catch (Exception e) {
      // The path that was passed in doesn't work. Catch any errors and throw this one instead.
      throw new JavaGitException(100002, ExceptionMessageMap.getMessage("100002") + " { path=["
          + path.getPath() + "] }", e);
    }

    // Make sure we're hanging onto an absolute path.
    gitPath = (path != null) ? path.getAbsoluteFile() : null;
  }

  /**
   * Convenience method for setting the path with a <code>String</code> instead of a
   * <code>File</code>.
   * 
   * TODO (rs2705): Enforce the requirement below that the path be absolute. Is there a simple way
   * to do this in an OS-independent fashion?
   * 
   * @param path
   *          Absolute path to git binaries installed on the system. The path must be absolute since
   *          it needs to persist for the life of the client code, even if the working directory
   *          changes. Throws a NullPointerException if path is null, or an IllegalArgumentException
   *          if it's the empty string.
   * @throws IOException
   *           Thrown if the provided path does not exist.
   * @throws JavaGitException
   *           Thrown if we cannot find git at the path provided.
   */
  public static void setGitPath(String path) throws IOException, JavaGitException {
    CheckUtilities.checkStringArgument(path, "path");
    setGitPath(new File(path));
  }

  /*
   * <code>GitVersionParser</code> parses the output of the <code>git --version</code> command.
   * It is also used to determine if the git binaries are accessible via the command line.
   * 
   * TODO (rs2705): Write unit tests for this class.
   */
  private static class GitVersionParser implements IParser {
    // The version of git that we parse out.
    private String version = "";

    // Whether or not we saw a valid version string.
    private boolean parsedCorrectly = true;

    // We only care about parsing the first line of our input - watch that here.
    private boolean sawLine = false;

    /**
     * Returns the <code>GitVersionResponse</code> object that's essentially just a wrapper around
     * the git version number.
     * 
     * @return The response object containing the version number.
     */
    public CommandResponse getResponse() throws JavaGitException {
      if (!(parsedCorrectly)) {
        throw new JavaGitException(100001, ExceptionMessageMap.getMessage("100001"));
      }
      return new GitVersionResponse(version);
    }

    /**
     * Parses the output of <code>git --version</code>. Expects to see: "git version XYZ".
     * 
     * @param line
     *          <code>String</code> containing the line to be parsed.
     */
    public void parseLine(String line) {
      if (!(sawLine)) {
        sawLine = true;
        parsedCorrectly = (line.trim().indexOf("git version ") == 0);
        if (parsedCorrectly) {
          version = line.replaceAll("git version ", "");
        }
      }
    }

    public void processExitCode(int code) {
    }
    
  }

  /*
   * The response object to wrap around the output of <code>git --version</code>.
   * 
   * TODO (rs2705): Write unit tests for this class.
   */
  private static class GitVersionResponse implements CommandResponse {
    // The version of git that gets passed into our constructor.
    private String version;

    /**
     * Constructor. Simply takes a version string and stores it.
     * 
     * @param version
     *          The version of git being used.
     */
    public GitVersionResponse(String version) {
      this.version = version;
    }

    /**
     * Gets the version of git being used in <code>String</code> format.
     * 
     * @return The git version string.
     */
    public String getVersion() {
      return version;
    }
  }
}
