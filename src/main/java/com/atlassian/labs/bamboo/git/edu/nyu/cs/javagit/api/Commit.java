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
package com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api;

import java.util.List;

/**
 * <code>Commit</code> represents information about a commit to a git repository.
 * 
 * TODO: Build out the class
 */
public final class Commit {

  // The name of the commit.
  private Ref commitName;

  // commit comment
  private String comment;

  /**
   * Get a <code>Commit</code> instance for the specified HEAD commit offset.
   * 
   * @return The <code>Commit</code>.
   */
  public static Commit getHeadCommit() {
    return new Commit(Ref.createHeadRef(0));
  }

  /**
   * Get a <code>Commit</code> instance for the specified HEAD commit offset.
   * 
   * @param commitOffset
   *          See {@link com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.Ref} for information on acceptable values of
   *          <code>commitOffset</code>.
   * @return The <code>Commit</code>.
   */
  public static Commit getHeadCommit(int commitOffset) {
    return new Commit(Ref.createHeadRef(commitOffset));
  }

  /**
   * Get a <code>Commit</code> instance for the specified SHA1 name.
   * 
   * @param sha1Name
   *          See {@link com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.Ref} for information on acceptable values of
   *          <code>sha1Name</code>.
   * @return The <code>Commit</code>.
   */
  public static Commit getSha1Commit(String sha1Name) {
    return new Commit(Ref.createSha1Ref(sha1Name));
  }

  /**
   * Get a <code>Commit</code> instance for the specified commit name.
   * 
   * @param commitName
   *          The <code>CommitName</code> for this <code>Commit</code>.
   * @return The <code>Commit</code>.
   */
  public static Commit getCommit(Ref commitName) {
    return new Commit(commitName);
  }

  /**
   * The constructor.
   * 
   * @param commitName
   *          The name of this commit.
   */
  private Commit(Ref commitName) {
    this.commitName = commitName;
  }

  /**
   * Gets the name of this commit.
   * 
   * @return The name of this commit.
   */
  public Ref getCommitName() {
    return commitName;
  }

  /**
   * Gets the author's comment
   * 
   * @return The author's comment.
   */
  public String getComment() {
    return comment;
  }

  /**
   * Returns differences for this commit
   * 
   * 
   * @return The list of differences (one per each git object).
   */
  public List<Diff> diff() {
    // GitDiff.diff();
    return null;
  }

  /**
   * Diffs this commit with another commit
   * 
   * @param otherCommit
   *          The commit to compare current commit to
   * 
   * @return The list of differences (one per each git object).
   */
  public List<Diff> diff(Commit otherCommit) {
    // GitDiff.diff();
    return null;
  }

}