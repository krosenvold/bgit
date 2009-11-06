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
package edu.nyu.cs.javagit.api;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.nyu.cs.javagit.api.commands.GitAdd;
import edu.nyu.cs.javagit.api.commands.GitAddResponse;
import edu.nyu.cs.javagit.api.commands.GitCommit;
import edu.nyu.cs.javagit.api.commands.GitCommitResponse;
import edu.nyu.cs.javagit.api.commands.GitMv;
import edu.nyu.cs.javagit.api.commands.GitMvResponse;
import edu.nyu.cs.javagit.api.commands.GitRm;
import edu.nyu.cs.javagit.api.commands.GitRmResponse;
import edu.nyu.cs.javagit.utilities.CheckUtilities;

/**
 * <code>GitFileSystemObject</code> provides some implementation shared by files and directories
 */

// TODO (rs2705): Alphabetize the methods in this class
public abstract class GitFileSystemObject {

  public static enum Status {
    // untracked (created but not added to the repository)
    UNTRACKED,
    // new, waiting to commit
    NEW_TO_COMMIT,
    // in repository and deleted locally
    DELETED,
    // deleted, waiting to commit
    DELETED_TO_COMMIT,
    // changed locally, but not updated
    MODIFIED,
    // changed and added to the index
    MODIFIED_TO_COMMIT,
    // renamed, waiting to commit
    RENAMED_TO_COMMIT,
    // in repository
    IN_REPOSITORY
  }

  /**
   * file path, as specified by the user
   */
  protected File file;
  /**
   * path, relative to working directory; used to pass around data to command API
   */
  protected File relativePath;

  protected WorkingTree workingTree;

  /**
   * The constructor.
   * 
   * @param file
   *          underlying <code>java.io.File</code> object
   */
  protected GitFileSystemObject(File file, WorkingTree workingTree) throws JavaGitException {
    this.workingTree = workingTree;
    this.file = file;
    this.relativePath = getRelativePath(file, workingTree.getPath());
  }

  /**
   * Returns a file, with path relative to git working tree
   * 
   * @param in
   *          input <code>File</code> object
   *        repositoryPath
   *          path to git repository
   * @return
   *        <code>File</code> object with relative path 
   * @throws JavaGitException
   *         input file does not belong to git working tree/ repo
   */
  public static File getRelativePath(File in, File repositoryPath) throws JavaGitException {
    String path = in.getPath();
    String absolutePath = in.getAbsolutePath();

    //check if the path is relative or absolute
    if(path.equals(absolutePath)) {
      //if absolute, make sure it belongs to working tree
      String workingTreePath = repositoryPath.getAbsolutePath();
      if(!path.startsWith(workingTreePath)) {
        throw new JavaGitException(999, "Invalid path :" + path 
            + ". Does not belong to the git working tree/ repository: " + workingTreePath);
      }

      //make path relative
      if(!path.equals(workingTreePath)) {
        path = path.substring(workingTreePath.length()+1);
      }
    }
    
    return new File(path);
  }
  

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof GitFileSystemObject)) {
      return false;
    }

    GitFileSystemObject gitObj = (GitFileSystemObject) obj;
    return CheckUtilities.checkObjectsEqual(gitObj.getFile(), file);
  }

  /**
   * Gets the name of the file system object
   * 
   * @return The name
   */
  public String getName() {
    return file.getName();
  }

  /**
   * Gets the underlying <code>java.io.File</code> object
   * 
   * @return <code>java.io.File</code> object
   */
  public File getFile() {
    return file;
  }

  /**
   * Gets parent directory of this <code>GitFileSystemObject</code> object
   * 
   * @return parent directory (null if invalid)
   * 
   */
  public GitDirectory getParent() {
    // NOTE: file.getParentFile() returns null if there is no parent.
    if(file.getParentFile() == null) {
      return null;
    }
    
    try {
      return new GitDirectory(file.getParentFile(), workingTree);
    }
    catch(JavaGitException e) {
      //invalid git parent
      return null;
    }
  }

  /**
   * Adds the object to the git index
   * 
   * @return response from git add
   */
  public GitAddResponse add() throws IOException, JavaGitException {
    GitAdd gitAdd = new GitAdd();

    // create a list of filenames and add yourself to it
    List<File> list = new ArrayList<File>();
    File relativeFilePath;
    if(relativePath.isDirectory()){
    	for(File f : relativePath.listFiles()){
    		if(!f.isHidden() && !f.getName().startsWith(".")){
    			relativeFilePath = this.getRelativePath(f, this.getWorkingTree().getPath());
    			list.add(relativeFilePath );
    		}
    	}
    }
    else{
    	
    	list.add(relativePath);
    }
    // run git-add command
    return gitAdd.add(workingTree.getPath(), null, list);
  }

  /**
   * Commits the file system object
   * 
   * @param comment
   *          Developer's comment
   * 
   * @return response from git commit
   */
  public GitCommitResponse commit(String comment) throws IOException, JavaGitException {
    // first add the file
    add();

    // create a list of filenames and add yourself to it
    List<File> list = new ArrayList<File>();
    list.add(relativePath);

    GitCommit gitCommit = new GitCommit();
    return gitCommit.commitOnly(workingTree.getPath(), comment, list);
  }

  /**
   * Moves or renames the object
   * 
   * @param dest
   *          destination path (relative to the Git Repository)
   * 
   * @return response from git mv
   */
  public GitMvResponse mv(File dest) throws IOException, JavaGitException {
    // source; current location (relative)
    File source = relativePath;
    //get relative path for destination
    File relativeDest = getRelativePath(dest, workingTree.getPath());

    // perform git-mv
    GitMv gitMv = new GitMv();
    GitMvResponse response = gitMv.mv(workingTree.getPath(), source, relativeDest);

    // file has changed; update
    file = dest;
    relativePath = relativeDest;

    return response;
  }

  /**
   * Removes the file system object from the working tree and the index
   * 
   * @return response from git rm
   */
  public GitRmResponse rm() throws IOException, JavaGitException {
    GitRm gitRm = new GitRm();

    // run git rm command
    return gitRm.rm(workingTree.getPath(), relativePath);
  }

  /**
   * Checks out some earlier version of the object
   * 
   * @param sha1
   *          Commit id
   */
  public void checkout(String sha1) throws JavaGitException {
    // GitCheckout.checkout(path, sha1);
  }

  /**
   * Show differences between current file system object and index version of it
   * 
   * @return diff between working directory and git index
   */
  public Diff diff() throws JavaGitException {
    // GitLog.log(path);
    return null;
  }

  /**
   * Show differences between current file system object and some commit
   * 
   * @param commit
   *          Git commit to compare with
   * 
   * @return diff between working directory and a given git commit
   */
  public Diff diff(Commit commit) throws JavaGitException {
    // GitLog.log(path);
    return null;
  }

  
  /**
   * Return the <code>WorkingTree</code> this object is in
   * 
   * @return working tree
   */
  public WorkingTree getWorkingTree() {
    return workingTree;
  }
}
