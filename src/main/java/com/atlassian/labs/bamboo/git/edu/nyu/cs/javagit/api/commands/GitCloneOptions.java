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

/**
 * A class to manage passing options to the <code>GitClone</code> command.
 */
public class GitCloneOptions {
    /**
     * Make mirror copy.
     */
    private boolean mirror;

    /**
     * Do not use hardlinks when cloning in local file system.
     */
    private boolean nohardlinks;

    private boolean bare;


    public GitCloneOptions() {
    }

    public GitCloneOptions(boolean mirror, boolean nohardlinks, boolean bare) {
        this.mirror = mirror;
        this.nohardlinks = nohardlinks;
        this.bare = bare;
    }

    public boolean isMirror() {
        return mirror;
    }

    public void setMirror(boolean mirror) {
        this.mirror = mirror;
    }

    public boolean isNohardlinks() {
        return nohardlinks;
    }

    public void setNohardlinks(boolean nohardlinks) {
        this.nohardlinks = nohardlinks;
    }

    public boolean isBare() {
        return bare;
    }

    public void setBare(boolean bare) {
        this.bare = bare;
    }
}
