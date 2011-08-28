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

import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.IClient;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.IGitAdd;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.IGitBranch;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.IGitCheckout;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.IGitClone;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.IGitCommit;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.IGitDiff;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.IGitGrep;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.IGitInit;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.IGitLog;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.IGitMerge;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.IGitMv;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.IGitRemote;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.IGitReset;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.IGitRevert;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.IGitRm;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.IGitShow;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.IGitStatus;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.IGitSubmodule;

/**
 * Command-line implementation of the <code>IClient</code> interface.
 */
public class CliClient implements IClient {

  public IGitAdd getGitAddInstance() {
    return new CliGitAdd();
  }

  public IGitCommit getGitCommitInstance() {
    return new CliGitCommit();
  }

  public IGitDiff getGitDiffInstance() {
    return new CliGitDiff();
  }

  public IGitGrep getGitGrepInstance() {
    return new CliGitGrep();
  }

  public IGitLog getGitLogInstance() {
    return new CliGitLog();
  }

  public IGitMv getGitMvInstance() {
    return new CliGitMv();
  }

  public IGitReset getGitResetInstance() {
    return new CliGitReset();
  }

  public IGitRevert getGitRevertInstance() {
    return new CliGitRevert();
  }

  public IGitRm getGitRmInstance() {
    return new CliGitRm();
  }

  public IGitShow getGitShowInstance() {
    return new CliGitShow();
  }

  public IGitStatus getGitStatusInstance() {
    return new CliGitStatus();
  }

  public IGitBranch getGitBranchInstance() {
    return new CliGitBranch();
  }

  public IGitCheckout getGitCheckoutInstance() {
    return new CliGitCheckout();
  }

  public IGitInit getGitInitInstance() {
  	return new CliGitInit();
  }

  public IGitClone getGitCloneInstance() {
    return new CliGitClone();
  }

  public IGitSubmodule getGitSubmoduleInstance() {
    return new CliGitSubmodule();
  }

    public IGitMerge getGitMergeInstance()
    {
        return new CliGitMerge();
    }

    public IGitRemote getGitRemoteInstance()
    {
        return new CliGitRemote();
    }

}
