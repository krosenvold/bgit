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
package edu.nyu.cs.javagit.api.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.nyu.cs.javagit.api.Ref;
import edu.nyu.cs.javagit.utilities.CheckUtilities;

/**
 * A response data object for the git-branch command.
 */
abstract public class GitBranchResponse implements CommandResponse {
  /**
   * An enumeration of the types of response. In normal case a list of branches, otherwise some
   * message such as "Deleted branch".
   */
  public static enum responseType {
    BRANCH_LIST, MESSAGE, EMPTY
  }

  // The list of branches in the response of git-branch.
  protected List<Ref> branchList;
  
  // The list of branch records, in response of git-branch with verbose option.
  protected List<BranchRecord> listOfBranchRecord;
  
  // String Buffer to store the message after execution of git-branch command.
  protected StringBuffer messages = new StringBuffer();

  // Variable to store the current branch.
  protected Ref currentBranch;

  // The type of this response.
  protected responseType responseType;

  /**
   * Constructor.
   */
  public GitBranchResponse() {
    branchList = new ArrayList<Ref>();
    listOfBranchRecord = new ArrayList<BranchRecord>();
  }

    public List<Ref> getBranchList() {
        return branchList;
    }

    public boolean containsBranch(Ref branch){
        for (Ref foundbranch : branchList ){
            if (foundbranch.isThisBranch( branch)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsExactBranchMatch(Ref branch){
         for (Ref foundbranch : branchList ){
             if (foundbranch.isExactMatch( branch)) {
                 return true;
             }
         }
         return false;
     }

  public boolean equals(Object o) {
    if (!(o instanceof GitBranchResponse)) {
      return false;
    }

    GitBranchResponse g = (GitBranchResponse) o;

    if (!CheckUtilities.checkObjectsEqual(getResponseType(), g.getResponseType())) {
      System.out.println("Not Equal getResponseType");
      return false;
    }

    if (!CheckUtilities.checkObjectsEqual(getMessages(), g.getMessages())) {
      System.out.println("Not Equal getMessages");
      return false;
    }

    if (!CheckUtilities.checkObjectsEqual(getCurrentBranch(), g.getCurrentBranch())) {
      return false;
    }

    if (!CheckUtilities.checkUnorderedListsEqual(branchList, g.branchList)) {
      System.out.println("Not Equal branchList");
      return false;
    }

    if (!CheckUtilities.checkUnorderedListsEqual(listOfBranchRecord, g.listOfBranchRecord)) {
      System.out.println("Not Equal listOfBranchRecord");
      return false;
    }

    return true;
  }

  /**
   * Get an <code>Iterator</code> with which to iterate over the branch list.
   * 
   * @return An <code>Iterator</code> with which to iterate over the branch list.
   */
  public Iterator<Ref> getBranchListIterator() {
    return (new ArrayList<Ref>(branchList).iterator());
  }

  /**
   * Get an <code>Iterator</code> with which to iterate over the branch record list.
   * 
   * @return An <code>Iterator</code> with which to iterate over the branch record list.
   */
  public Iterator<BranchRecord> getListOfBranchRecordIterator() {
    return (new ArrayList<BranchRecord>(listOfBranchRecord).iterator());
  }

  /**
   * Gets the type of the response. Branch list, message or empty.
   * 
   * @return The responseType.
   */
  public responseType getResponseType() {
    return responseType;
  }

  /**
   * Gets a message about the git-branch operation that was run.
   * 
   * @return A message about the git-branch operation that was run.
   */
  public String getMessages() {
    return messages.toString();
  }

  /**
   * Gets the current branch from the list of branches displayed by git-branch operation.
   * 
   * @return The current branch from the list of branches displayed by git-branch operation.
   */
  public Ref getCurrentBranch() {
    return currentBranch;
  }

  public int hashCode() {
    return branchList.hashCode() + listOfBranchRecord.hashCode();
  }


    public List<BranchRecord> getListOfBranchRecord() {
        return listOfBranchRecord;
    }

    /**
   * A record containing branch, its head SHA1, and the comment of the commit on head. A list of
   * this object is returned as a part of <code>BranchResponse</code> object when verbose option 
   * is set.
   */
  public static class BranchRecord {
    private Ref branch;

    // The SHA Refs of a branch in the response of git-branch with -v option.
    private Ref sha1;

    // String Buffer to store the comment after execution of git-branch command with -v option.
    private String comment;

    // Variable to store the current branch.
    private boolean isCurrentBranch;

    public BranchRecord(Ref branch, Ref sha1, String comment, boolean isCurrentBranch) {
      this.branch = branch;
      this.sha1 = sha1;
      this.comment = comment;
      this.isCurrentBranch = isCurrentBranch;
    }

    public boolean equals(Object o) {
      if (!(o instanceof BranchRecord)) {
        return false;
      }

      BranchRecord c = (BranchRecord) o;

      if (!CheckUtilities.checkObjectsEqual(getBranch(), c.getBranch())) {
        return false;
      }

      if (!CheckUtilities.checkObjectsEqual(getSha1(), c.getSha1())) {

        return false;
      }

      if (!CheckUtilities.checkObjectsEqual(getComment(), c.getComment())) {
        return false;
      }

      if (isCurrentBranch() != c.isCurrentBranch()) {
        return false;
      }

      return true;
    }

    /**
     * Gets the branch from the record.
     * 
     * @return The branch from the record.
     */
    public Ref getBranch() {
      return branch;
    }

    /**
     * Gets the SHA1 from the record.
     * 
     * @return The SHA1 from the record.
     */
    public Ref getSha1() {
      return sha1;
    }

    /**
     * Gets the comment of the last commit on a branch or the last commit on the branch it has
     * originated from. Displayed when git-branch is run with -v option.
     * 
     * @return The comment of the recent commit on a branch.
     */
    public String getComment() {
      return comment;
    }

    public int hashCode() {
      return branch.hashCode() + sha1.hashCode() + comment.hashCode();
    }

    /**
     * Gets the current branch from the list of branches displayed by git-branch operation.
     * 
     * @return The current branch from the list of branches displayed by git-branch operation.
     */
    public boolean isCurrentBranch() {
      return isCurrentBranch;
    }


  }
}
