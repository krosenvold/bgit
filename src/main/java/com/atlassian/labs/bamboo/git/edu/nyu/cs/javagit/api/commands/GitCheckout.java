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
package com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.JavaGitException;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.Ref;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.Ref.RefType;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.ClientManager;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.IClient;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.IGitCheckout;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.utilities.CheckUtilities;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.utilities.ExceptionMessageMap;


/**
 * <code>GitCheckout</code> provides an interface for checking out files from a particular branch
 * in the repository or creating a new branch and switching to it.
 */
public final class GitCheckout {

  /**
   * For checking out a branch or creating a new branch. The <new-branch> is the value of the option
   * '-b' passed in <code>GitCheckoutOptions</code> starting at <branch-name> provided as the last
   * argument to the checkout method.
   * 
   * @param repositoryPath
   *          Path to the Git repository.
   * @param options
   *          <code>GitCheckoutOptions</code> passed.
   * @param branch
   *          name of the branch that will be checked out OR if creating a new branch then the
   *          starting for the new branch will be this branch.
   * @return <code>GitCheckoutResponse</code> object
   * @throws JavaGitException thrown if -
   *           <li>if the output for &lt;git-checkout&gt; command generated an error.</li>
   *           <li>if options provided are incorrect.</li>
   *           <li>if processBuilder not able to run the command.</li>
   *           </ul>
   * @throws IOException 
   *          thrown if -
   *           <ul>
   *           <li>paths given do not have proper permissions.</li>
   *           <li>paths given do not exist at all.</li>
   *           </ul>
   */
  public GitCheckoutResponse checkout(File repositoryPath, GitCheckoutOptions options, Ref branch)
      throws IOException, JavaGitException {
    CheckUtilities.checkFileValidity(repositoryPath);
    CheckUtilities.validateArgumentRefType(branch, RefType.BRANCH, "Branch name");
    IClient client = ClientManager.getInstance().getPreferredClient();
    IGitCheckout gitCheckout = client.getGitCheckoutInstance();
    return gitCheckout.checkout(repositoryPath, options, branch);
  }

  /**
   * For checking a file or list of files from a branch.
   * 
   * @param repositoryPath
   *          path to the Git repository.
   * @param paths
   *          List of file paths that are to be checked out.
   * @return GitCheckoutResponse object
   * @throws JavaGitException thrown if -
   *           <li>if the output for &lt;git-checkout&gt; command generated an error.</li>
   *           <li>if processBuilder not able to run the command.</li>
   *           </ul>
   * @throws IOException 
   *           thrown if -
   *           <ul>
   *           <li>paths given do not have proper permissions.</li>
   *           <li>paths given do not exist at all.</li>
   *           </ul>
   */
  public GitCheckoutResponse checkout(File repositoryPath, List<File> paths) throws IOException,
      JavaGitException {
    CheckUtilities.checkFileValidity(repositoryPath);
    CheckUtilities.checkNullListArgument(paths, "List of Paths");
    IClient client = ClientManager.getInstance().getPreferredClient();
    IGitCheckout gitCheckout = client.getGitCheckoutInstance();
    return gitCheckout.checkout(repositoryPath, paths);
  }

  /**
   * 
   * For checking out a file from some other branch into the current branch.
   * 
   * @param repositoryPath
   *          path to the Git repository.
   * @param options
   *          <code>GitCheckoutOptions</code> that are passed to &lt;git-checkout&gt; command
   * @param branch
   *          <code>Ref</code> branch-name from where the file will be checked out
   * @param file
   *          <code>File</code> to be checked out.
   * @return <code>GitCheckoutResponse</code> object
   * @throws <code>JavaGitException</code> thrown if -
   *           <li>if the output for &lt;git-checkout&gt; command generated an error.</li>
   *           <li>if options provided are incorrect</li>
   *           <li>if processBuilder not able to run the command.</li>
   *           </ul>
   * @throws </code>
   *           IOException</code> thrown if -
   *           <ul>
   *           <li>paths given do not have proper permissions.</li>
   *           <li>paths given do not exist at all.</li>
   *           </ul>
   */
  public GitCheckoutResponse checkout(File repositoryPath, GitCheckoutOptions options, Ref branch,
      File file) throws IOException, JavaGitException {
    CheckUtilities.checkFileValidity(repositoryPath);
    CheckUtilities.validateArgumentRefType(branch, RefType.BRANCH, "Branch name");
    IClient client = ClientManager.getInstance().getPreferredClient();
    IGitCheckout gitCheckout = client.getGitCheckoutInstance();
    return gitCheckout.checkout(repositoryPath, options, branch, file);
  }

  /**
   * 
   * Checks out a <code>List</code> of files from some other branch into the current branch.
   * 
   * @param repositoryPath
   *          path to the Git repository.
   * @param options
   *          <code>GitCheckoutOptions</code> that are passed to &lt;git-checkout&gt; command
   * @param branch
   *          <code>Ref</code> branch-name from where the file will be checked out
   * @param paths
   *          <code>List</code> of file paths that are to be checked out.
   * @return GitCheckoutResponse object
   * @throws JavaGitException
   *           thrown if -
   *           <li>if the output for &lt;git-checkout&gt; command generated an error.</li>
   *           <li>if options provided are incorrect</li>
   *           <li>if processBuilder not able to run the command.</li>
   *           </ul>
   * @throws IOException
   *           Thrown if -
   *           <ul>
   *           <li>paths given do not have proper permissions.</li>
   *           <li>paths given do not exist at all.</li>
   *           </ul>
   */
  public GitCheckoutResponse checkout(File repositoryPath, GitCheckoutOptions options, Ref branch,
      List<File> paths) throws IOException, JavaGitException {
    CheckUtilities.checkFileValidity(repositoryPath);
    CheckUtilities.checkNullListArgument(paths, "List of files");
    CheckUtilities.validateArgumentRefType(branch, RefType.BRANCH, "Branch name");
    IClient client = ClientManager.getInstance().getPreferredClient();
    IGitCheckout gitCheckout = client.getGitCheckoutInstance();
    return gitCheckout.checkout(repositoryPath, options, branch, paths);
  }

  /**
   * Checks out files from the repository when a tree-ish object is given as the reference.
   * 
   * @param repositoryPath
   *          Path to the Git repository.
   * @param ref
   *          A branch, or sha1 object.
   * @param paths
   *          <code>List</code> of file paths that are to be checked out.
   * @return <code>GitCheckoutResponse</code> object
   * @throws JavaGitException 
   *           Thrown if -
   *           <li>the output for &lt;git-checkout&gt; command generated an error.</li>
   *           <li>processBuilder not able to run the command.</li>
   *           </ul>
   * @throws IOException
   *           Thrown if -
   *           <ul>
   *           <li>paths given do not have proper permissions.</li>
   *           <li>paths given do not exist at all.</li>
   *           </ul>
   */
  public GitCheckoutResponse checkout(File repositoryPath, Ref ref, List<File> paths)
      throws JavaGitException, IOException {
    CheckUtilities.checkFileValidity(repositoryPath);
    if ( ref != null && ( ref.getRefType() != RefType.BRANCH && ref.getRefType() != RefType.SHA1) ) {
      throw new JavaGitException(100000, ExceptionMessageMap.getMessage("100000")
          + " RefType passed: " + ref.getRefType());
    }
    CheckUtilities.checkNullListArgument(paths, "List of files");
    IClient client = ClientManager.getInstance().getPreferredClient();
    IGitCheckout gitCheckout = client.getGitCheckoutInstance();
    return gitCheckout.checkout(repositoryPath, ref, paths);
  }
}
