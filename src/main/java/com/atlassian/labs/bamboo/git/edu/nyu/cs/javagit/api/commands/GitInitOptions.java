package com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands;

public class GitInitOptions {

	private boolean optBare = false;
	private boolean optTemplate = false;
	private String optTemplateDirecory = null;
	private boolean optSharedUmask = false;
	private boolean optSharedGroup = false;
	private String optSharedGroupName = null;
	private boolean optSharedAll = false;
	private boolean optSharedOctal = false;
	private int optSharedOctalValue = 0;
	
	/**
	 * @return the optBare
	 */
	public boolean isOptBare() {
		return optBare;
	}
	/**
	 * @param optBare the optBare to set
	 */
	public void setOptBare(boolean optBare) {
		this.optBare = optBare;
	}
	/**
	 * @return the optTemplate
	 */
	public boolean isOptTemplate() {
		return optTemplate;
	}
	/**
	 * @param optTemplate the optTemplate to set
	 */
	public void setOptTemplate(boolean optTemplate) {
		this.optTemplate = optTemplate;
	}
	/**
	 * @return the optTemplateDirecory
	 */
	public String getOptTemplateDirecory() {
		return optTemplateDirecory;
	}
	/**
	 * @param optTemplateDirecory the optTemplateDirecory to set
	 */
	public void setOptTemplateDirecory(String optTemplateDirecory) {
		this.optTemplateDirecory = optTemplateDirecory;
	}
	/**
	 * @return the optSharedUmask
	 */
	public boolean isOptSharedUmask() {
		return optSharedUmask;
	}
	/**
	 * @param optSharedUmask the optSharedUmask to set
	 */
	public void setOptSharedUmask(boolean optSharedUmask) {
		this.optSharedUmask = optSharedUmask;
	}
	/**
	 * @return the optSharedGroup
	 */
	public boolean isOptSharedGroup() {
		return optSharedGroup;
	}
	/**
	 * @param optSharedGroup the optSharedGroup to set
	 */
	public void setOptSharedGroup(boolean optSharedGroup) {
		this.optSharedGroup = optSharedGroup;
	}
	/**
	 * @return the optSharedGroupName
	 */
	public String getOptSharedGroupName() {
		return optSharedGroupName;
	}
	/**
	 * @param optSharedGroupName the optSharedGroupName to set
	 */
	public void setOptSharedGroupName(String optSharedGroupName) {
		this.optSharedGroupName = optSharedGroupName;
	}
	/**
	 * @return the optSharedAll
	 */
	public boolean isOptSharedAll() {
		return optSharedAll;
	}
	/**
	 * @param optSharedAll the optSharedAll to set
	 */
	public void setOptSharedAll(boolean optSharedAll) {
		this.optSharedAll = optSharedAll;
	}
	/**
	 * @return the optSharedOctal
	 */
	public boolean isOptSharedOctal() {
		return optSharedOctal;
	}
	/**
	 * @param optSharedOctal the optSharedOctal to set
	 */
	public void setOptSharedOctal(boolean optSharedOctal) {
		this.optSharedOctal = optSharedOctal;
	}
	/**
	 * @return the optSharedOctalValue
	 */
	public int getOptSharedOctalValue() {
		return optSharedOctalValue;
	}
	/**
	 * @param optSharedOctalValue the optSharedOctalValue to set
	 */
	public void setOptSharedOctalValue(int optSharedOctalValue) {
		this.optSharedOctalValue = optSharedOctalValue;
	}
	
	
 }
