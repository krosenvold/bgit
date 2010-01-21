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

import edu.nyu.cs.javagit.utilities.CheckUtilities;

/**
 * This class represents the name of a ref. The types of <code>Ref</code>s, specified in the
 * <code>RefType</code> enumeration, are:
 * 
 * <ul>
 * <li> <code>BRANCH</code> </li>
 * <li> <code>HEAD</code> </li>
 * <li> <code>REMOTE</code> </li>
 * <li> <code>SHA1</code> </li>
 * <li> <code>TAG</code> </li>
 * </ul>
 * 
 * If the <code>RefType</code> of a <code>Ref</code> instance is <code>BRANCH</code>, then
 * the branch name is maintained by the instance. The branch name is simply a <code>String</code>
 * representing the name of the branch. Examples:
 * 
 * <ul>
 * <li> master </li>
 * <li> testing </li>
 * <li> crazyidea </li>
 * </ul>
 * 
 * If the <code>RefType</code> of a <code>Ref</code> instance is <code>HEAD</code>, then a
 * commit offset value is maintained as an <code>int</code>. This commit offset indicates the
 * commit back from the HEAD commit on the branch checked out in the working tree. Values greater
 * than or equal to zero are acceptable and correspond to:
 * 
 * <ul>
 * <li> 0 - The current HEAD commit</li>
 * <li> 1 - The commit prior to the current HEAD commit</li>
 * <li> 2 - The second commit prior to the current HEAD commit</li>
 * <li> etc...</li>
 * </ul>
 * 
 * If the <code>RefType</code> of a <code>Ref</code> instance is <code>REMOTE</code>, then
 * the remote repository name and remote branch name are maintained as a <code>String</code>s by
 * the instance.<br>
 * <br>
 * 
 * Note: It is possible for the remote repository name to be non-existent. In such cases, the
 * repository value is <code>null</code>. <br>
 * <br>
 * 
 * Examples:
 * 
 * <ul>
 * <li> remote repository name: origin, remote branch name: master </li>
 * <li> remote repository name: davidsrepo, remote branch name: testing </li>
 * <li> remote repository name: <no_repo_name>, remote branch name: crazyidea </li>
 * </ul>
 * 
 * If the <code>RefType</code> of a <code>Ref</code> instance is <code>SHA1</code>, then the
 * SHA1 value is maintained as a <code>String</code> value. This string value can be:
 * 
 * <ul>
 * <li> the full SHA1 </li>
 * <li> a SHA1 truncated to the first n characters </li>
 * </ul>
 * 
 * If the <code>RefType</code> of a <code>Ref</code> instance is <code>TAG</code>, then the
 * name of the tag is maintained as a <code>String</code> value. Examples:
 * 
 * <ul>
 * <li> Release1.0.8 </li>
 * <li> 6.3-RC1 </li>
 * </ul>
 */
public class Ref {

  /*
   * TODO (jhl388): add the field "isCurrentBranch" to indicate if the ref is the current working
   * branch. Refs of the current working branch and all HEAD refs should have the value "true". All
   * other refs should have the value "false".
   */

  /**
   * An enumeration of the types of refs.
   */
  public static enum RefType {
    BRANCH, HEAD, REMOTE, SHA1, TAG
  }

  /** The HEAD commit. */
  public static final Ref HEAD;

  /** The prior HEAD commit. */
  public static final Ref HEAD_1;

  static {
    HEAD = new Ref();
    HEAD.refType = RefType.HEAD;
    HEAD.headOffset = 0;

    HEAD_1 = new Ref();
    HEAD_1.refType = RefType.HEAD;
    HEAD_1.headOffset = 1;
  }

  // The type of this ref.
  private Ref.RefType refType;

  /*
   * If the ref's type is HEAD, this is the number of commits back from the head of the current
   * working branch.
   */
  private int headOffset = -1;

  // If the ref's type is BRANCH, REMOTE, SHA1, or TAG, this is the name of the ref.
  private String name = null;

  /*
   * If the ref's type is REMOTE, this is the name of the remote repository iff a repository name is
   * associated with the remote name.
   */
  private String repositoryName = null;

  /**
   * Creates a <code>Ref</code> of type <code>BRANCH</code>.
   * 
   * @param name
   *          The branch name of this ref. If the value is null, a <code>NullPointerException</code>
   *          is thrown. If the value has length zero, an <code>IllegalArgumentException</code> is
   *          thrown.
   * @return A <code>Ref</code> instance of type <code>BRANCH</code>.
   */
  public static Ref createBranchRef(String name) {
    CheckUtilities.checkStringArgument(name, "name");

    Ref cn = new Ref();
    cn.refType = RefType.BRANCH;
    cn.name = name;
    return cn;
  }

  /**
   * Creates a <code>Ref</code> of type <code>HEAD</code>.
   * 
   * @param headOffset
   *          The offset of the commit back from the HEAD commit on the current working branch. If
   *          the value is less than zero, an <code>IllegalArgumentException</code> is thrown.
   * @return A <code>Ref</code> instance of type <code>HEAD</code>.
   */
  public static Ref createHeadRef(int headOffset) {
    CheckUtilities.checkIntArgumentGreaterThan(headOffset, -1, "headOffset");

    if (0 == headOffset) {
      return HEAD;
    } else if (1 == headOffset) {
      return HEAD_1;
    }

    Ref cn = new Ref();
    cn.refType = RefType.HEAD;
    cn.headOffset = headOffset;
    return cn;
  }

  /**
   * Creates a <code>Ref</code> of type <code>REMOTE</code>.
   * 
   * @param repositoryName
   *          The remote repository name of this ref. If the value is blank or null, the value will
   *          be maintained as null.
   * @param name
   *          The remote branch name of this ref. If the value is null, a
   *          <code>NullPointerException</code> is thrown. If the value has length zero, an
   *          <code>IllegalArgumentException</code> is thrown.
   * @return A <code>Ref</code> instance of type <code>REMOTE</code>.
   */
  public static Ref createRemoteRef(String repositoryName, String name) {
    CheckUtilities.checkStringArgument(name, "name");

    Ref cn = new Ref();
    cn.refType = RefType.REMOTE;
    cn.name = name;

    if (null != repositoryName && repositoryName.length() > 0) {
      cn.repositoryName = repositoryName;
    }

    return cn;
  }

  /**
   * Creates a <code>Ref</code> of type <code>SHA1</code>.
   * 
   * @param name
   *          The SHA1 name of this ref. The value can be a short name or the full SHA1 value. If
   *          the value is null, a <code>NullPointerException</code> is thrown. If the value has
   *          length zero, an <code>IllegalArgumentException</code> is thrown.
   * @return A <code>Ref</code> instance of type <code>SHA1</code>.
   */
  public static Ref createSha1Ref(String name) {
    CheckUtilities.checkStringArgument(name, "name");

    Ref cn = new Ref();
    cn.refType = RefType.SHA1;
    cn.name = name;
    return cn;
  }

  /**
   * Creates a <code>Ref</code> of type <code>TAG</code>.
   * 
   * @param name
   *          The tag name of this ref. If the value is null, a <code>NullPointerException</code>
   *          is thrown. If the value has length zero, an <code>IllegalArgumentException</code> is
   *          thrown.
   * @return A <code>Ref</code> instance of type <code>TAG</code>.
   */
  public static Ref createTagRef(String name) {
    CheckUtilities.checkStringArgument(name, "name");

    Ref cn = new Ref();
    cn.refType = RefType.TAG;
    cn.name = name;
    return cn;
  }

  /**
   * Gets the type of the <code>Ref</code> instance.
   * 
   * @return The type of the <code>Ref<code> instance.
   */
  public Ref.RefType getRefType() {
    return refType;
  }

  /**
   * Gets the offset of the commit back from the HEAD commit on the current working branch for this
   * <code>Ref</code> instance.
   * 
   * @return If the type of this <code>Ref</code> is not <code>HEAD</code>, then -1 is
   *         returned. Otherwise, the offset of the commit back from the HEAD commit on the current
   *         working branch is returned.
   */
  public int getHeadOffset() {
    return headOffset;
  }

  /**
   * Gets the name of this ref.
   * 
   * @return If the type of this <code>Ref</code> is not <code>BRANCH</code>,
   *         <code>REMOTE</code>, <code>SHA1</code> or <code>TAG</code>, then null is
   *         returned. Otherwise, the name of this ref is returned.
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the repository name of this ref.
   * 
   * @return If the type of this <code>Ref</code> is not <code>REMOTE</code>, then null is
   *         returned. If the type of this <code>Ref</code> is <code>REMOTE</code> and there is
   *         no associated repository name, then null is returned. Otherwise, the repository name of
   *         this ref is returned.
   */
  public String getRepositoryName() {
    return repositoryName;
  }

  @Override
  public String toString() {
    if (RefType.HEAD == refType) {
      if (0 == headOffset) {
        return "HEAD";
      } else if (1 == headOffset) {
        return "HEAD^1";
      } else {
        return "HEAD~" + Integer.toString(headOffset);
      }
    } else if (RefType.BRANCH == refType || RefType.SHA1 == refType || RefType.TAG == refType) {
      return name;
    } else if (RefType.REMOTE == refType) {
      if (null != repositoryName) {
        return repositoryName + "/" + name;
      } else {
        return name;
      }
    } else {
      return "";
    }
  }

    public boolean isThisBranch(Ref other) {

      if (!CheckUtilities.checkObjectsEqual(refType, other.getRefType())) {
        return false;
      }

      if (! name.endsWith( other.getName())){
        return false;
      }
      return true;
    }

    public boolean isExactMatch(Ref other) {

      if (!CheckUtilities.checkObjectsEqual(refType, other.getRefType())) {
        return false;
      }

      return  name.equals( other.getName());
    }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Ref)) {
      return false;
    }

    Ref cn = (Ref) o;

    if (!CheckUtilities.checkObjectsEqual(refType, cn.getRefType())) {
      return false;
    }
    if (!CheckUtilities.checkObjectsEqual(name, cn.getName())) {
      return false;
    }
    if (!CheckUtilities.checkObjectsEqual(repositoryName, cn.getRepositoryName())) {
      return false;
    }
    if (cn.getHeadOffset() != headOffset) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int ret = refType.hashCode() + headOffset;
    ret += (null == name) ? 0 : name.hashCode();
    ret += (null == repositoryName) ? 0 : repositoryName.hashCode();
    return ret;
  }

}