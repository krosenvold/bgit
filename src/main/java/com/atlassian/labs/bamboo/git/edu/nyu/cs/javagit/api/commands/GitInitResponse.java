package com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands;

public class GitInitResponse implements CommandResponse {

	public boolean initialized = false;
	public boolean reinitialized = false;
	
	public boolean isInitialized() {
		return initialized;
	}
	
	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}
	
	public boolean isReinitialized() {
		return reinitialized;
	}
	
	public void setReinitialized(boolean reinitialized) {
		this.reinitialized = reinitialized;
	}

	public boolean containsError() {
		// TODO Auto-generated method stub
		return false;
	}
}
