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

import java.util.ArrayList;
import java.util.List;

import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.utilities.CheckUtilities;


/**
 * 	A response data object for the git log command.
 */
public class GitLogResponse implements CommandResponse {
 
	private List<Commit> commitList = new ArrayList<Commit>();
	protected List<ResponseString> errors = new ArrayList<ResponseString>();
	
	//local variables used to store parsed data until it is pushed into the object
	private String sha = null;
	private List<String> mergeDetails = null;
	private String dateString = null;
	private String message = null;
	private List<CommitFile> files = new ArrayList<CommitFile>();
	private String author = null;
	
	/**
	 * 
	 * @return true is the response object contain an error
	 */
	public boolean containsError() {
		return ( errors.size() > 0 );
	}



    public void addError(ResponseString error){
        errors.add ( error);
    }
	/**
	 * 
	 * @param sha This sets the sha extracted from each commit entry.
	 */
	public void setSha(String sha){
		this.sha = sha;
	}

	/**
	 * 
	 * @param mergeDetails This stored the merge details of a commit. eg. Merge: f859e80... 55a5e32...
	 */
	public void setMergeDetails(List<String> mergeDetails){
		this.mergeDetails = mergeDetails;
	}

	/**
	 * 
	 * @param string This returns the Date object for a particular commit.
	 */
	public void setDate(String string){
		this.dateString = string;
	}
	/**
	 * 
	 * @param author This sets the author for a commit.
	 */
	public void setAuthor(String author){
		this.author = author.replace('<', ' ').replace('>', ' ');
	}
	/**
	 * 
	 * @param message This set the message for a commmit.
	 */
	public void setMessage(String message){
		if (this.message == null){
			this.message = message;
		}
		else{
			this.message += message;
		}
		
	}
	/**
	 * 
	 * @param file This adds a file entry to the list of files modified for a single commit.
	 */
	public void addFile(CommitFile file){
		if (files==null){
			files = new ArrayList<CommitFile>();
		}
		this.files.add(file);
	}
	
	/**
	 * This add a newly created commit object to the list of commits for a log.
	 */
	public void addCommit(){
		if(this.sha!=null){
			Commit commit = new Commit(this.sha, this.mergeDetails, this.author, this.dateString,
					this.message, this.files);
			if (commitList == null){
				commitList = new ArrayList<Commit>();
			}

			this.commitList.add(commit);
			//reset variables for future commits.
			this.files = null;
			this.mergeDetails = null;
			this.message = null;
		}
	}
	/**
	 * This adds a file to the list of files affected by a particular commit.
	 * @param filename The filename affected by the commit.
	 * @param linesAdded	Number of lines added in the above file in that particular commit.
	 * @param linesDeleted	Number of lines deleted in the above file in that particular commit.
	 */
	public void addFile(String filename,int linesAdded, int linesDeleted){
		CommitFile commitFile = new CommitFile(filename, linesAdded, linesDeleted);
		if (files == null){
			files = new ArrayList<CommitFile>();
		}
		this.files.add(commitFile);
	}
	/**
	 * @param index 
	 *          Returns the index of error.
	 * @return The index of error.
	 */
	public ResponseString getError(int index) {
		CheckUtilities.checkIntInRange(index, 0, errors.size());
		return ( errors.get(index) );
	}
	
	/**
	 * 
	 * @return	This returns the commit list of the particular log instance. 
	 */
	public List<Commit> getLog() {

		return this.commitList;
	}
	
	
	/**
	 * 
	 * A data structure which  holds information about each commit.
	 *
	 */
	public static class Commit{
		
		String sha = null;
		List<String> mergeDetails = null; 
		String author = null;
		String date = null;

		String message = null;
		
		//Additional Commit details
		List<CommitFile> files = new ArrayList<CommitFile>();

		int filesChanged = 0;
		int linesInserted = 0;
		int linesDeleted = 0;

		/**
		 * Constructor for creating a commit data structure.
		 * @param sha	The SHA hash for a particular commit instance. 
		 * @param mergeDetails	The Merge details for a particular commit instance. Pass null is commit is not a merge
		 * @param author	The Author for a particular commit instance.
		 * @param date	The Date of a particular commit instance.
		 * @param message	The Message for a particular commit instance.
		 * @param files	The list of files affected by a particular commit instance.
		 */
		public Commit(String sha, List<String> mergeDetails, String author, String date,
				String message, List<CommitFile> files) {
			super();
			this.sha = sha;
			this.mergeDetails = mergeDetails;
			this.author = author;
			this.date = date;
			this.message = message;
			this.files = files;
			setLinesInsertionsDeletions();
			setFilesChanged();
		}


		/**
		 * 
		 * @return This returns the SHA for each commit.
		 */
		public String getSha() {
			return sha;
		}
		
		/**
		 * 
		 * @return This returns the merge details for each commit. If the commit was not a merge it returns null.
		 */
		public List<String> getMergeDetails() {
			return mergeDetails;
		}
		/**
		 * 
		 * @return This return the name of the author of the commit.
		 */
		public String getAuthor() {
			return author.replace('<', ' ').replace('>', ' ');
		}
		
		/**
		 * 
		 * @return This return the Date object for a particular commmit.
		 */
		public String getDateString() {
			return date;
		}
		/**
		 * 
		 * @return	This returns the message of a commit.
		 */
		public String getMessage() {
			return message;
		}
		/**
		 *  This returns the list of files affected by a particular commit.
		 */
		public List<CommitFile> getFiles() {
			return files;
		}
		
		/**
		 * 
		 * @param sha sets the SHA for a commit.
		 */
		public void setSha(String sha) {
			this.sha = sha;
		}
		/**
		 * 
		 * @param mergeDetails This set the merge details for a particular commit.
		 */
		public void setMergeDetails(List<String> mergeDetails) {
			this.mergeDetails = mergeDetails;
		}
		/**
		 * 
		 * @param author This sets the Author of a particular commit.
		 */
		public void setAuthor(String author) {
			this.author = author.replace('<', ' ').replace('>', ' '); 
		}
		/**
		 * 
		 * @param date	This sets the Date object for a particular commit.
		 */
		public void setDateString(String date) {
			this.date = date;
		}
		/**
		 * 
		 * @param message This sets the Message for a particular commit.
		 */
		public void setMessage(String message) {
			this.message = message;
		}
		/**
		 * 
		 * @param files This sets the list of files affected by a particular commit.
		 */
		public void setFiles(List<CommitFile> files) {
			this.files = files;
		}
		
		/**
		 * This methods calculated the total number of files affected by a particular commit.
		 */
		void setFilesChanged() {
			if(this.files != null){
				this.filesChanged = this.files.size();
			}
			else{
				this.filesChanged = 0;
			}
			
		}
		/**
		 * This calculated the Total number of lines Added and Deleted for a particular commit. 
		 */
		void setLinesInsertionsDeletions(){
			if(this.files != null){
				for(int i = 0; i< this.files.size();i++){
					this.linesInserted += files.get(i).linesAdded;
					this.linesDeleted += files.get(i).linesDeleted;
				}
				
			}
			else{
				this.linesInserted = 0;
				this.linesDeleted = 0;
			}
		}
		
		/**
		 * 
		 * @return The number of lines deleted for a paticular commit.
		 */
		public int getLinesDeleted() {
			return this.linesDeleted;
		}
		
		/**
		 * 
		 * @return @return The number of lines inserted for a particular commit.
		 */
		public int getLinesInserted() {
			return this.linesInserted;
		}
		
		/**
		 * 
		 * @return The number of files changed in a particular commit.
		 */
		public int getFilesChanged() {
			return this.filesChanged;
		}
	}

	/**
	 * 
	 * This class hold information about a file affected by a commit 
	 *
	 */
	public static  class CommitFile{

		String filename = null;
		int linesAdded = 0;
		int linesDeleted = 0;
		public CommitFile(String name, int linesAdded, int linesDeleted) {
			super();
			this.filename = name;
			this.linesAdded = linesAdded;
			this.linesDeleted = linesDeleted;
		}
		public String getName() {
			return filename;
		}

		public int getLinesAdded() {
			return linesAdded;
		}

		public int getLinesDeleted() {
			return linesDeleted;
		}
	}
	
  public static class ResponseString {
    final String error;
         final int lineNumber;
     
    public ResponseString(int lineNumber, String error) {
      this.lineNumber = lineNumber;
      this.error = error;
    }
     
     public int getLineNumber() {
       return lineNumber;
     }
     
    public String error() {
      return error;
    }
  }
}
