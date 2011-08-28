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
package com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.JavaGitException;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.Ref;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitCheckoutOptions;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitCheckoutResponse;


/**
 * An interface to represent the &lt;git-checkout&gt; command.
 */
public interface IGitCheckout {

  /**
   * Checks out either an existing branch or new branch from the repository.
   * 
   * @param repositoryPath
   *          Path to the root of the repository
   * @param options
   *          <code>GitCheckoutOptions</code> object used for passing options to
   *          &lt;git-checkout&gt;
   * @param branch
   *          Name of the base branch that need to be checked out or if the new branch is being
   *          checkout based on this base branch.
   * @param paths
   *          <code>List</code> of files that are specifically to be checked out.
   * @return GitCheckoutResponse object
   * @throws JavaGitException thrown if -
   *           <ul>
   *           <li>if options passed are not correct.</li>
   *           <li>if the output for &lt;git-checkout&gt; command generated an error.</li>
   *           <li>if processBuilder not able to run the command.</li>
   *           </ul>
   * @throws IOException thrown if -
   *           <ul>
   *           <li>paths given do not have proper permissions.</li>
   *           <li>paths given do not exist at all.</li>
   *           </ul>
   */
  public GitCheckoutResponse checkout(File repositoryPath, GitCheckoutOptions options, Ref branch,
      List<File> paths) throws JavaGitException, IOException;

  /**
   * Checks out either an existing branch or new branch from the repository.
   * 
   * @param repositoryPath
   *          Path to the root of the repository
   * @param options
   *          <code>GitCheckoutOptions</code> object used for passing options to
   *          &lt;git-checkout&gt;
   * @param branch
   *          Name of the base branch that need to be checked out or if the new branch is being
   *          checkout based on this base branch.
   * @param file
   *          Single file that need to be checked out from the git repository
   * @return GitCheckoutResponse object
   * @throws JavaGitException thrown if -
   *           <ul>
   *           <li>if options passed are not correct.</li>
   *           <li>if the output for &lt;git-checkout&gt; command generated an error.</li>
   *           <li>if processBuilder not able to run the command.</li>
   *           </ul>
   * @throws IOException thrown if -
   *           <ul>
   *           <li>paths given do not have proper permissions.</li>
   *           <li>paths given do not exist at all.</li>
   *           </ul>
   */
  public GitCheckoutResponse checkout(File repositoryPath, GitCheckoutOptions options, Ref branch,
      File file) throws JavaGitException, IOException;

  /**
   * Checks out either an existing branch or new branch from the repository.
   * 
   * @param repositoryPath
   *          Path to the root of the repository
   * @param options
   *          <code>GitCheckoutOptions</code> object used for passing options to
   *          &lt;git-checkout&gt;
   * @param branch
   *          Name of the base branch that need to be checked out or if the new branch is being
   *          checkout based on this base branch.
   * @return GitCheckoutResponse< object
   * @throws JavaGitException thrown if -
   *           <ul>
   *           <li>if options passed are not correct.</li>
   *           <li>if the output for &lt;git-checkout&gt; command generated an error.</li>
   *           <li>if processBuilder not able to run the command.</li>
   *           </ul>
   * @throws IOException thrown if -
   *           <ul>
   *           <li>paths given do not have proper permissions.</li>
   *           <li>paths given do not exist at all.</li>
   *           </ul>
   */
  public GitCheckoutResponse checkout(File repositoryPath, GitCheckoutOptions options, Ref branch)
      throws JavaGitException, IOException;

  /**
   * Vanilla version of &lt;git-checkout&gt; where no options and no branch info is passed to it and
   * files are checked out from current branch.
   * 
   * @param repositoryPath
   *          path to the root of the repository
   * @throws JavaGitException thrown if -
   *           <ul>
   *           <li>if options passed are not correct.</li>
   *           <li>if the output for &lt;git-checkout&gt; command generated an error.</li>
   *           <li>if processBuilder not able to run the command.</li>
   *           </ul>
   * @throws IOException thrown if -
   *           <ul>
   *           <li>paths given do not have proper permissions.</li>
   *           <li>paths given do not exist at all.</li>
   *           </ul>
   */
  public GitCheckoutResponse checkout(File repositoryPath) throws JavaGitException, IOException;

  /**
   * Checks out an existing branch with no options provided.
   * 
   * @param repositoryPath
   *          path to the root of the repository
   * @param branch
   *          name of the base branch that need to be checked out
   * @return GitCheckoutResponse object
   * @throws JavaGitException thrown if -
   *           <ul>
   *           <li>if the output for &lt;git-checkout&gt; command generated an error.</li>
   *           <li>if processBuilder not able to run the command.</li>
   *           </ul>
   * @throws IOException thrown if -
   *           <ul>
   *           <li>paths given do not have proper permissions.</li>
   *           <li>paths given do not exist at all.</li>
   *           </ul>
   */
  public GitCheckoutResponse checkout(File repositoryPath, Ref branch) throws JavaGitException,
      IOException;

  /**
   * &lt;git-checkout&gt; where a list of files is given to be checked out from current branch of
   * the repository.
   * 
   * @param repositoryPath
   *          path to the root of the repository
   * @param paths
   *          list of file paths or directory that need to be checked out from git repository.
   * @return GitCheckoutResponse object
   * @throws JavaGitException thrown if -
   *           <ul>
   *           <li>if the output for &lt;git-checkout&gt; command generated an error.</li>
   *           <li>if processBuilder not able to run the command.</li>
   *           </ul>
   * @throws IOException thrown if -
   *           <ul>
   *           <li>paths given do not have proper permissions.</li>
   *           <li>paths given do not exist at all.</li>
   *           </ul>
   */
  public GitCheckoutResponse checkout(File repositoryPath, List<File> paths)
      throws JavaGitException, IOException;

  /**
   * &lt;git-checkout&gt; where a list of files is given to be checked out with tree-ish option set.
   * 
   * @param repositoryPath
   *          path to the root of the repository
   * @param treeIsh
   *          RefType object
   * @param paths
   *          List of files to be checked out.
   * @return GitCheckoutResponse object
   * @throws JavaGitException thrown if -
   *           <li>if the output for &lt;git-checkout&gt; command generated an error.</li>
   *           <li>if processBuilder not able to run the command.</li>
   *           </ul>
   * @throws IOException thrown if -
   *           <ul>
   *           <li>paths given do not have proper permissions.</li>
   *           <li>paths given do not exist at all.</li>
   *           </ul>
   */
  public GitCheckoutResponse checkout(File repositoryPath, Ref treeIsh, List<File> paths)
      throws JavaGitException, IOException;
}
