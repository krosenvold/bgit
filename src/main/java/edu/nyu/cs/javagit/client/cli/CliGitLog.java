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
package edu.nyu.cs.javagit.client.cli;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import edu.nyu.cs.javagit.api.JavaGitConfiguration;
import edu.nyu.cs.javagit.api.JavaGitException;
import edu.nyu.cs.javagit.api.Ref;
import edu.nyu.cs.javagit.api.commands.CommandResponse;
import edu.nyu.cs.javagit.api.commands.GitLogOptions;
import edu.nyu.cs.javagit.api.commands.GitLogResponse;
import edu.nyu.cs.javagit.api.commands.GitLogResponse.Commit;
import edu.nyu.cs.javagit.client.IGitLog;
import edu.nyu.cs.javagit.utilities.CheckUtilities;

/**
 * Command-line implementation of the <code>IGitLog</code> interface.
 */
public class CliGitLog implements IGitLog{

    /**
	 * Implementations of &lt;git log&gt; with options and one file to be added to index.
	 */
	public List<Commit> log(File repositoryPath, GitLogOptions options, Ref branch)
	throws JavaGitException, IOException {
		CheckUtilities.checkFileValidity(repositoryPath);
		GitLogParser parser = new GitLogParser();
		List<String> command = buildCommand(repositoryPath, branch, options);
		GitLogResponse response =  (GitLogResponse) ProcessUtilities.runCommand(repositoryPath,
				command, parser);
		if (response.containsError()) {
			int line = response.getError(0).getLineNumber();
			String error = response.getError(0).error();
			throw new JavaGitException(420001, "Line " + line + ", " + error);
		}
		return response.getLog();
	}

    /**
	 * Implementations of &lt;git log&gt; with options and one file to be added to index.
	 */
	public List<Commit> log(File repositoryPath, GitLogOptions options)
	throws JavaGitException, IOException {
		return (log(repositoryPath, options, null));
	}

	public List<Commit> log(File repositoryPath) throws JavaGitException,
	IOException {
		CheckUtilities.checkFileValidity(repositoryPath);
		GitLogParser parser = new GitLogParser();
		List<String> command = buildCommand(repositoryPath,null, null);
		GitLogResponse response =  (GitLogResponse) ProcessUtilities.runCommand(repositoryPath,
				command, parser);
		if (response.containsError()) {
			int line = response.getError(0).getLineNumber();
			String error = response.getError(0).error();
			throw new JavaGitException(420001, "Line " + line + ", " + error);
		}
		return response.getLog();
	}

	/**
	 * This function builds the git log commands with necessary options as specified by the user.
	 * @param repositoryPath Root of the repository
	 * @param options	Options supplied to the git log command using <code>GitLogOptions</code>.
	 * @return Returns a List of command argument to be applied to git log.
	 */
	private List<String> buildCommand(File repositoryPath, Ref branch, GitLogOptions options) {
		List<String> command = new ArrayList<String>();
		command.add(JavaGitConfiguration.getGitCommand());
		command.add("log");
		if(options!=null){
			//General Options
			/**
			 * Breaks rewrite changes in to pairs of delete and create.
			 */
			if (options.isOptBreakRewriteChanges()) {
				command.add("-B");
			}
			/**
			 * Detects renames
			 */
			if (options.isOptDetectRenames()) {
				command.add("-M");
			}
			/**
			 * Detects copies and renames, of original files
			 */
			if (options.isOptFindCopies()) {
				command.add("-C");
			}
			/**
			 * 	Detects copies and renames , very expensive operation.
			 */
			if (options.isOptFindCopiesHarder()) {
				command.add("--find-copies-harder");
			}
			/**
			 *  List details about lines modified and files affected in a commit.
			 */
			if (options.isOptFileDetails()) {
				command.add("--numstat");
			}

			/**
			 * 	List all logs on the relative path.
			 */
			if (options.isOptRelative()) {
				command.add("--relative="+options.getOptRelativePath());
			}

			/**
			 * 	List all logs since specified date.
			 */
			if (options.isOptLimitCommitSince()) {
				command.add("--since=\""+ options.getOptLimitSince()+"\"");
			}

			/**
			 * 	List all logs after specified date.
			 */
			if (options.isOptLimitCommitAfter()) {
				command.add("--after=\""+ options.getOptLimitAfter()+"\"");
			}

			/**
			 * 	List all logs after specified date.
			 */
			if (options.isOptLimitCommitUntil()) {
				command.add("--until="+ options.getOptLimitUntil());
			}

            /**
			 * 	List all logs within a range of commits.
			 */
			if (options.isOptLimitCommitRange()) {
				command.add(options.getOptLimitRangeStart() + ".." + options.getOptLimitRangeEnd());
			}

			/**
			 * 	List all logs before specified date.
			 */
			if (options.isOptLimitCommitBefore()) {
				command.add("--before="+ options.getOptLimitBefore());
			}

			/**
			 * 	List all logs by an author
			 */
			if (options.isOptLimitAuthor()) {
				command.add("--author="+ options.getOptAuthor());
			}

			/**
			 * 	List all logs by an author/committer header pattern.
			 */
			if (options.isOptLimitCommitterPattern()) {
				command.add("--committer="+ options.getOptLimitPattern());
			}

			/**
			 * 	List all logs by matching to a grep pattern.
			 */
			if (options.isOptLimitGrep()) {
				command.add("--grep="+ options.getOptLimitGrepPattern().toString());
			}
			/**
			 * 	Match regular expressions with out  regard to letters case.
			 */
			if (options.isOptLimitMatchIgnoreCase()) {
				command.add("-i");
			}

			/**
			 * 	Match extended regular expressions.
			 */
			if (options.isOptLimitEnableExtendedRegex()) {
				command.add("-E");
			}

			/**
			 * 	Match patterns as fixed strings and not regular expressions.
			 */
			if (options.isOptLimitMatchIgnoreCase()) {
				command.add("-F");
			}

			/**
			 * 	Stop when a path dissapears from the tree.
			 */
			if (options.isOptLimitRemoveEmpty()) {
				command.add("--remove-empty");
			}

			/**
			 * 	Match parts of history irrelevant to the current path.
			 */
			if (options.isOptLimitFullHistory()) {
				command.add("--full-history");
			}

			/**
			 * 	Do not print commits with more than one merges.
			 */
			if (options.isOptLimitNoMerges()) {
				command.add("--no-merges");
			}

			/**
			 * 	Follow only first parent on seeing a merge.
			 */
			if (options.isOptLimitFirstParent()) {
				command.add("--first-parent");
			}

			/**
			 * 	Order commits topologically.
			 */
			if (options.isOptOrderingTopological()) {
				command.add("--topo-order");
			}

			/**
			 * 	Order commits in reverse
			 */
			if (options.isOptOrderingReverse()) {
				command.add("--reverse");
			}
			
			/**
			 * 	Limits the number of commits to retrieve.
			 */
			if (options.isOptLimitCommitMax()) {
				command.add("-n");
				command.add(String.valueOf(options.getOptLimitMax()));
			}

            if (branch != null) {
                command.add(branch.getName());
            }
        }

		return command;

	}

	/**
	 * Parser class to parse the output generated by git log; and return a
	 * <code>GitLogResponse</code> object.
	 */
	public class GitLogParser implements IParser {

		private int linesAdded = 0;
		private int linesDeleted = 0;
		private boolean canCommit = false;
		private String filename = null;
		private String []tmp ;
		private GitLogResponse response = new GitLogResponse();
		
		/**
		 * Add the final parsed commit. and returns the response of git log execution.
		 */
		public CommandResponse getResponse() throws JavaGitException {
			response.addCommit();
			return this.response;
		}


    /**
		 *  Parses a line at a time from the commandline execution output of git log
		 */    		
		public void parseLine(String line) {
			if(line.length() == 0){
				return;
			}

			//commit
			if (line.startsWith("commit")){
				if(canCommit){
					response.addCommit();

				}
				canCommit = true;
				tmp = line.split(" ");
				response.setSha(line.substring(tmp[0].length()).trim());
			}
			//merge (optional)
			else if (line.startsWith("Merge")){
				List<String> mergeDetails = new ArrayList<String>();
				tmp = line.split(" ");
				StringTokenizer st = new StringTokenizer(line.substring(tmp[0].length()));
				while(st.hasMoreTokens()){
					mergeDetails.add(st.nextToken().trim());
				}

				response.setMergeDetails(mergeDetails);
				mergeDetails = null;
			}
			//Author
			else if (line.startsWith("Author")){
				tmp = line.split(" ");
				response.setAuthor(line.substring(tmp[0].length()).trim());
			}
			//Date
			else if (line.startsWith("Date")){
				tmp = line.split(" ");
				response.setDate(line.substring(tmp[0].length()).trim());
			}
			//message or fileDetails (always starts with an int)
			else{
				StringTokenizer st = new StringTokenizer(line);
				try{
					linesAdded = Integer.parseInt(st.nextToken());
					linesDeleted = Integer.parseInt(st.nextToken());
					filename = st.nextToken().toString();
					response.addFile(filename,linesAdded,linesDeleted);
				}
				catch(NumberFormatException nfe){
					try{
						if (st.nextToken().equals("-")){
							linesAdded = 0;
							linesDeleted = 0;
							filename = st.nextToken().toString();
							response.addFile(filename,linesAdded,linesDeleted);
						}
						else{
							response.setMessage(line);
						}
					}
					catch(NoSuchElementException nsee){
						response.setMessage(line);
					}
				}
				catch(NoSuchElementException nsee){
					response.setMessage(line);	
				}
				catch(Exception e){
					response.setMessage(line);
				}
			}
		}


	public void processExitCode(int code) {
		// TODO Auto-generated method stub
		
	}
	}
}
