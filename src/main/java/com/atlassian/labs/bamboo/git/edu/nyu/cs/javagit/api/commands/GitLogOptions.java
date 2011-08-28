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

public class GitLogOptions {

	// general options
	private boolean optBreakRewriteChanges = false;
	private boolean optDetectRenames = false;
	private boolean optFindCopies = false;
	private boolean optFindCopiesHarder = false;
	private boolean optFileDetails = false;
	private boolean optRelative = false;
	private String optRelativePath = null;

	// commit limiting options
	private boolean optLimitCommitMax = false;
	private int optLimitMax=0;
	private boolean optLimitCommitSkip = false;
	private int optLimitSkip=0;
	private boolean optLimitCommitSince = false;
	private String optLimitSince = null;
	private boolean optLimitCommitAfter = false;
	private String optLimitAfter = null;
    private boolean optLimitCommitRange = false;
    private String  optLimitRangeStart = null;
    private String  optLimitRangeEnd = null;
	private boolean optLimitComitUntil = false;
	private String optLimitUntil = null;
	private boolean optLimitCommitBefore = false;
	private String optLimitBefore = null;
	private boolean optLimitAuthor = false;
	private String optAuthor = null;
	private boolean optLimitCommitterPattern = false;
	private String optLimitPattern = null;
	private boolean optLimitGrep = false;
	private String optLimitGrepPattern = null;
	private boolean optLimitMatchIgnoreCase = false;
	private boolean optLimitEnableExtendedRegex = false;
	private boolean optLimitEnableFixedStrings = false;
	private boolean optLimitRemoveEmpty = false;
	private boolean optLimitFullHistory = false;
	private boolean optLimitNoMerges = false;
	private boolean optLimitFirstParent = false;
	private boolean optLimitAll = false;
	private boolean optLimiCherryPick = false;

	// commit ordering options
	private boolean optOrderingTopological = false;
	private boolean optOrderingDate = false;
	private boolean optOrderingReverse = false;

	public boolean isOptBreakRewriteChanges() {
		return optBreakRewriteChanges;
	}
	public void setOptBreakRewriteChanges(boolean optBreakRewriteChanges) {
		this.optBreakRewriteChanges = optBreakRewriteChanges;
	}
	
	/**
	 * 
	 * @return true If detect renames is enabled.
	 */
	public boolean isOptDetectRenames() {
		return optDetectRenames;
	}
	
	/**
	 * 
	 * @param optDetectRenames Sets rename detection to enable/disable
	 */
	public void setOptDetectRenames(boolean optDetectRenames) {
		this.optDetectRenames = optDetectRenames;
	}
	
	/**
	 * 
	 * @return	true If detect copies is enabled.
	 */
	public boolean isOptFindCopies() {
		return optFindCopies;
	}
	
	/**
	 * 
	 * @param optFindCopies	Sets copy detection enable/disable
	 */
	public void setOptFindCopies(boolean optFindCopies) {
		this.optFindCopies = optFindCopies;
	}
	
	/**
	 * 
	 * @return true If detect copies and renames is enabled.
	 */
	public boolean isOptFindCopiesHarder() {
		return optFindCopiesHarder;
	}
	
	/**
	 * 
	 * @param optFindCopiesHarder Sets complete copy detection enable/disable
	 */
	public void setOptFindCopiesHarder(boolean optFindCopiesHarder) {
		this.optFindCopiesHarder = optFindCopiesHarder;
	}
	
	/**
	 * 
	 * @return true If relative directory is enabled for logging.
	 */
	public boolean isOptRelative() {
		return optRelative;
	}
	
	/**
	 * 
	 * @param optRelative	Enables relative path for retrieving logs.
	 * @param optRelativePath	set the relative path to repository.
	 */
	public void setOptRelative(boolean optRelative,String optRelativePath) {
		this.optRelative = optRelative;
		setOptRelativePath(optRelativePath);
	}
	
	/**
	 * 
	 * @return 	true If log are retrieving additional file details.
	 */
	public boolean isOptFileDetails() {
		return optFileDetails;
	}
	
	/**
	 * 
	 * @param optFileDetails Enable/Disable logs to retrieve additional file details.
	 */
	public void setOptFileDetails(boolean optFileDetails) {
		this.optFileDetails = optFileDetails;
	}
	
	/**
	 * 
	 * @return	true If number of commits outputs are limited.
	 */
	public boolean isOptLimitCommitOutputs() {
		return optLimitCommitMax;
		
	}
	/**
	 * This function enables/disables limiting commit output.
	 * @param optLimitCommitOutputs Enable/Disable Commit outputs.
	 * @param optLimitMax Number of outputs to be displayed.
	 */
	public void setOptLimitCommitOutputs(boolean optLimitCommitOutputs,int optLimitMax) {
		this.optLimitCommitMax = optLimitCommitOutputs;
		setOptLimitMax(optLimitMax);
	}
	/**
	 * 
	 * @return true If commits are limited since a specified date.
	 */
	public boolean isOptLimitCommitSince() {
		return optLimitCommitSince;
	}
	/**
	 * 
	 * @param optLimitCommitSince Enable/Disable commit limiting since a date.
	 * @param optLimitSince	Date 
	 */
	public void setOptLimitCommitSince(boolean optLimitCommitSince, String optLimitSince) {
		this.optLimitCommitSince = optLimitCommitSince;
		setOptLimitSince(optLimitSince);
	}
	/**
	 * 
	 * @return true If commits are limited after a specified date.
	 */
	public boolean isOptLimitCommitAfter() {
		return optLimitCommitAfter;
	}
	/**
	 * 
	 * @param optLimitCommitAfter	Enable/Disable commit limiting after a date
	 * @param optLimitAfter Date
	 */
	public void setOptLimitCommitAfter(boolean optLimitCommitAfter, String optLimitAfter) {
		this.optLimitCommitAfter = optLimitCommitAfter;
		setOptLimitAfter(optLimitAfter);
	}

    /**
     *
     * @return true If commits are limited by range.
     */
    public boolean isOptLimitCommitRange() {
        return optLimitCommitRange;
    }

    /**
     *
     * @param optLimitCommitRange	Enable/Disable commit limiting by a range
     * @param optLimitRangeStart    First commit in range
     * @param optLimitRangeEnd      Last commit in range
     */
    public void setOptLimitCommitRange(boolean optLimitCommitRange, String optLimitRangeStart, String optLimitRangeEnd) {
        this.optLimitCommitRange = optLimitCommitRange;
        setOptLimitRangeStart(optLimitRangeStart);
        setOptLimitRangeEnd(optLimitRangeEnd);
    }

	/**
	 * 
	 * @return	true If commits are limited until a specified date.
	 */
	public boolean isOptLimitCommitUntil() {
		return optLimitComitUntil;
	}
	
	/**
	 * 
	 * @param optLimitComitUntil Enable/Disable commit limiting until a date
	 * @param optLimitUntil	Date
	 */
	public void setOptLimitComitUntil(boolean optLimitComitUntil,String optLimitUntil) {
		this.optLimitComitUntil = optLimitComitUntil;
		setOptLimitUntil(optLimitUntil);
	}
	/**
	 * 
	 * @return 	true If commits are limited before a specified date.
	 */
	public boolean isOptLimitCommitBefore() {
		return optLimitCommitBefore;
	}
	/**
	 * 
	 * @param optLimitCommitBefore	Enable/Disable commit limiting before a date
	 * @param optLimitBefore	Date
	 */
	public void setOptLimitCommitBefore(boolean optLimitCommitBefore, String optLimitBefore) {
		this.optLimitCommitBefore = optLimitCommitBefore;
		setOptLimitBefore(optLimitBefore);
	}
	/**
	 * 
	 * @return true If commits are limited based on author name.
	 */
	public boolean isOptLimitAuthor() {
		return optLimitAuthor;
	}
	
	/**
	 * 
	 * @param optLimitAuthor Enable/Disable commit limiting based on author name.
	 * @param optAuthor	Author name
	 */
	public void setOptLimitAuthor(boolean optLimitAuthor,String optAuthor) {
		this.optLimitAuthor = optLimitAuthor;
		setOptAuthor(optAuthor);
	}
	
	/**
	 * 
	 * @param optAuthor 	sets the author name;
	 */
	private void setOptAuthor(String optAuthor) {
		this.optAuthor = optAuthor;
		
	}
	
	public String getOptAuthor(){
		return this.optAuthor;
		
	}
	
	/**
	 * 
	 * @return true If log output is limited to a pattern specifying author/committer header
	 */
	public boolean isOptLimitCommitterPattern() {
		return optLimitCommitterPattern;
	}
	
	/**
	 * 
	 * @param optLimitCommitterPattern Enable/Disable commit limiting based on author/committer header pattern.
	 * @param optLimitPattern	pattern
	 */
	public void setOptLimitCommitterPattern(boolean optLimitCommitterPattern,String optLimitPattern) {
		this.optLimitCommitterPattern = optLimitCommitterPattern;
		setOptLimitPattern(optLimitPattern);
	}
	
	public boolean isOptLimitGrep() {
		return optLimitGrep;
	}
	/**
	 * 
	 * @param optLimitGrep	Enable/Disable commit limiting on based on a grep pattern.
	 * @param optLimitGrepPattern Pattern.
	 */
	public void setOptLimitGrep(boolean optLimitGrep,String optLimitGrepPattern) {
		this.optLimitGrep = optLimitGrep;
		setOptLimitGrepPattern(optLimitGrepPattern);
	}
	
	//TODO (apj221) comment the rest
	public boolean isOptLimitMatchIgnoreCase() {
		return optLimitMatchIgnoreCase;
	}
	
	public void setOptLimitMatchIgnoreCase(boolean optLimitMatchIgnoreCase) {
		this.optLimitMatchIgnoreCase = optLimitMatchIgnoreCase;
	}
	public boolean isOptLimitEnableExtendedRegex() {
		return optLimitEnableExtendedRegex;
	}
	public void setOptLimitEnableExtendedRegex(boolean optLimitEnableExtendedRegex) {
		this.optLimitEnableExtendedRegex = optLimitEnableExtendedRegex;
	}
	public boolean isOptLimitEnableFixedStrings() {
		return optLimitEnableFixedStrings;
	}
	public void setOptLimitEnableFixedStrings(boolean optLimitEnableFixedStrings) {
		this.optLimitEnableFixedStrings = optLimitEnableFixedStrings;
	}
	public boolean isOptLimitRemoveEmpty() {
		return optLimitRemoveEmpty;
	}
	public void setOptLimitRemoveEmpty(boolean optLimitRemoveEmpty) {
		this.optLimitRemoveEmpty = optLimitRemoveEmpty;
	}
	public boolean isOptLimitFullHistory() {
		return optLimitFullHistory;
	}
	public void setOptLimitFullHistory(boolean optLimitFullHistory) {
		this.optLimitFullHistory = optLimitFullHistory;
	}
	public boolean isOptLimitNoMerges() {
		return optLimitNoMerges;
	}
	public void setOptLimitNoMerges(boolean optLimitNoMerges) {
		this.optLimitNoMerges = optLimitNoMerges;
	}
	public boolean isOptLimitFirstParent() {
		return optLimitFirstParent;
	}
	public void setOptLimitFirstParent(boolean optLimitFirstParent) {
		this.optLimitFirstParent = optLimitFirstParent;
	}
	public boolean isOptLimitAll() {
		return optLimitAll;
	}
	public void setOptLimitAll(boolean optLimitAll) {
		this.optLimitAll = optLimitAll;
	}
	public boolean isOptLimiCherryPick() {
		return optLimiCherryPick;
	}
	public void setOptLimiCherryPick(boolean optLimiCherryPick) {
		this.optLimiCherryPick = optLimiCherryPick;
	}
	public boolean isOptOrderingTopological() {
		return optOrderingTopological;
	}
	public void setOptOrderingTopological(boolean optOrderingTopological) {
		this.optOrderingTopological = optOrderingTopological;
	}
	public boolean isOptOrderingDate() {
		return optOrderingDate;
	}
	public void setOptOrderingDate(boolean optOrderingDate) {
		this.optOrderingDate = optOrderingDate;
	}
	public boolean isOptOrderingReverse() {
		return optOrderingReverse;
	}
	public void setOptOrderingReverse(boolean optOrderingReverse) {
		this.optOrderingReverse = optOrderingReverse;
	}
	public boolean isOptLimitCommitSkip() {
		return optLimitCommitSkip;
	}
	public void setOptLimitCommitSkip(boolean optLimitCommitSkip,int optLimitSkip) {
		this.optLimitCommitSkip = optLimitCommitSkip;
		setOptLimitSkip(optLimitSkip);
	}
	public String getOptRelativePath() {
		return optRelativePath;
	}
	private void setOptRelativePath(String optRelativePath) {
		this.optRelativePath = optRelativePath;
	}
	public int getOptLimitMax() {
		return optLimitMax;
	}
	private void setOptLimitMax(int optLimitMax) {
		this.optLimitMax = optLimitMax;
	}
	public boolean isOptLimitCommitMax() {
		return optLimitCommitMax;
	}
	public void setOptLimitCommitMax(boolean optLimitCommitMax,int optLimitMax) {
		this.optLimitCommitMax = optLimitCommitMax;
		setOptLimitMax(optLimitMax);
	}
	public int getOptLimitSkip() {
		return optLimitSkip;
	}
	private void setOptLimitSkip(int optLimitSkip) {
		this.optLimitSkip = optLimitSkip;
	}
	public String getOptLimitSince() {
		return optLimitSince;
	}
	private void setOptLimitSince(String optLimitSince) {
		this.optLimitSince = optLimitSince;
	}
	public String getOptLimitAfter() {
		return optLimitAfter;
	}
	private void setOptLimitAfter(String optLimitAfter) {
		this.optLimitAfter = optLimitAfter;
	}
    public String getOptLimitRangeStart() {
        return optLimitRangeStart;
    }
    public String getOptLimitRangeEnd() {
        return optLimitRangeEnd;
    }
    private void setOptLimitRangeStart(String optLimitRangeStart) {
        this.optLimitRangeStart = optLimitRangeStart;
	}
    private void setOptLimitRangeEnd(String optLimitRangeEnd) {
        this.optLimitRangeEnd = optLimitRangeEnd;
	}
	public String getOptLimitUntil() {
		return optLimitUntil;
	}
	private void setOptLimitUntil(String optLimitUntil) {
		this.optLimitUntil = optLimitUntil;
	}
	public String getOptLimitBefore() {
		return optLimitBefore;
	}
	private void setOptLimitBefore(String optLimitBefore) {
		this.optLimitBefore = optLimitBefore;
	}
	public String getOptLimitPattern() {
		return optLimitPattern;
	}
	private void setOptLimitPattern(String optLimitPattern) {
		this.optLimitPattern = optLimitPattern;
	}
	public String getOptLimitGrepPattern() {
		return optLimitGrepPattern;
	}
	private void setOptLimitGrepPattern(String optLimitGrepPattern) {
		this.optLimitGrepPattern = optLimitGrepPattern;
	}

}