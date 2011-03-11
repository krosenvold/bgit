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
package com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.utilities;

import java.util.HashMap;
import java.util.Map;

/**
 * Maps exception error codes to exception messages. <br>
 * <br>
 * 
 * The code structure is as follows:
 * 
 * <ul>
 * <li>The error code is a six digit number</li>
 * <li>The first two digits represent the error type</li>
 * <li>The last four digits represent the specific error in that error type</li>
 * </ul>
 * 
 * Note: the last four digits may be divided into subtypes. In such case, the explanation of the
 * format will be included in the error type class descriptions below.<br>
 * <br>
 * 
 * The error types are:
 * 
 * <ul>
 * <li>00 -- Standard Java exceptions caused by malformed or invalid method parameters</li>
 * <li>02 -- All other standard Java exceptions</li>
 * <li>10 -- General JavaGit exceptions</li>
 * <li>40-60 -- Command Specific JavaGit exceptions
 * <ul>
 * <li>400-401 -- git-add specific JavaGit exceptions</li>
 * <li>402-403 -- git-bisect specific JavaGit exceptions</li>
 * <li>404-405 -- git-branch specific JavaGit exceptions</li>
 * <li>406-407 -- git-checkout specific JavaGit exceptions</li>
 * <li>408-409 -- git-clone specific JavaGit exceptions</li>
 * <li>410-411 -- git-commit specific JavaGit exceptions</li>
 * <li>412-413 -- git-diff specific JavaGit exceptions</li>
 * <li>414-415 -- git-fetch specific JavaGit exceptions</li>
 * <li>416-417 -- git-grep specific JavaGit exceptions</li>
 * <li>418-419 -- git-init specific JavaGit exceptions</li>
 * <li>420-421 -- git-log specific JavaGit exceptions</li>
 * <li>422-423 -- git-merge specific JavaGit exceptions</li>
 * <li>424-425 -- git-mv specific JavaGit exceptions</li>
 * <li>426-427 -- git-pull specific JavaGit exceptions</li>
 * <li>428-429 -- git-push specific JavaGit exceptions</li>
 * <li>430-431 -- git-rebase specific JavaGit exceptions</li>
 * <li>432-433 -- git-reset specific JavaGit exceptions</li>
 * <li>434-435 -- git-rm specific JavaGit exceptions</li>
 * <li>436-437 -- git-show specific JavaGit exceptions</li>
 * <li>438-439 -- git-status specific JavaGit exceptions</li>
 * <li>440-441 -- git-tag specific JavaGit exceptions</li>
 * </ul>
 * </li>
 * </ul>
 * 
 * How to use the codes and messages:
 * 
 * <ul>
 * <li>All exceptions thrown from within javagit should contain a code and a message, even
 * exceptions thrown using standard Java exceptions.</li>
 * <li>When creating a new exception code and message, start the message with the code like so:
 * "000001: "</li>
 * <li>When throwing a <code>JavaGitException</code>, the code and the message must be supplied
 * to the constructor.</li>
 * <li>When throwing a standard Java exception (ex. <code>NullPointerException</code>), just
 * supply the message since the code is already included in the message.</li>
 * <li>The message retrieved from the <code>ExceptionMessageMap</code> can be treated like a base
 * message. If additional information is required in the message, append that information to the
 * base message.</li>
 * <li>If the value of a variable must be included in the message, list the variable names and
 * their values at the end of the message in the following format: "{ varName1=[varVal1],
 * varName2=[varVal2], ... }"</li>
 * </ul>
 * 
 * TODO (jhl388): Load the exception message mapping from a properties bundle.
 */
public class ExceptionMessageMap {

  private static Map<String, String> MESSAGE_MAP;

  static {
    MESSAGE_MAP = new HashMap<String, String>();

    MESSAGE_MAP.put("000001", "000001: A String argument was not specified but is required.");
    MESSAGE_MAP.put("000002", "000002: A List<String> argument was not specified but is required.");
    MESSAGE_MAP.put("000003", "000003: An Object argument was not specified but is required.");
    MESSAGE_MAP.put("000004",
        "000004: The int argument is not greater than the lower bound (lowerBound < toCheck).");
    MESSAGE_MAP.put("000005",
        "000005: An List<?> argument was not specified or is empty but is required.");
    MESSAGE_MAP.put("000006",
        "000006: The int argument is outside the allowable range (start <= index < end).");
    MESSAGE_MAP.put("000007","000007: The argument should be a directory.");

    MESSAGE_MAP.put("000100", "000100: Invalid option combination for git-commit command.");
    MESSAGE_MAP.put("000110", "000110: Invalid option combination for git-add command.");
    MESSAGE_MAP.put("000120", "000120: Invalid option combination for git-branch command.");
    MESSAGE_MAP.put("000130", "000130: Invalid option combination for git-checkout command.");
    
    MESSAGE_MAP.put("020001", "020001: File or path does not exist.");
    MESSAGE_MAP.put("020002", "020002: File or path is not a directory.");

    MESSAGE_MAP.put("020100", "020100: Unable to start sub-process.");
    MESSAGE_MAP.put("020101", "020101: Error reading input from the sub-process.");

    MESSAGE_MAP.put("100000", "100000: Incorrect refType type.");
    MESSAGE_MAP.put("100001", "100001: Error retrieving git version.");
    MESSAGE_MAP.put("100002", "100002: Invalid path to git specified.");

    MESSAGE_MAP.put("401000", "401000: Error calling git-add.");
    MESSAGE_MAP.put("401001", "401001: Error fatal pathspec error while executing git-add.");

    MESSAGE_MAP.put("410000", "410000: Error calling git-commit.");

    MESSAGE_MAP.put("404000", "404000: Error calling git-branch. ");

    MESSAGE_MAP.put("424000", "424000: Error calling git-mv. ");
    
    MESSAGE_MAP.put("424001", "424001: Error calling git-mv for dry-run. ");
    
    MESSAGE_MAP.put("420001", "420001: Error calling git log");

    MESSAGE_MAP.put("432000", "432000: Error calling git-reset.");
    
    MESSAGE_MAP.put("418000", "418000: Error calling git init.");

    MESSAGE_MAP.put("434000", "434000: Error calling git-rm.");

    MESSAGE_MAP.put("406000", "406000: Error calling git-checkout");
    MESSAGE_MAP.put("406001", "406001: Error not a treeIsh RefType");

    MESSAGE_MAP.put("438000", "438000: Error calling git-status");
  }

  /**
   * Gets the error message for the specified code.
   * 
   * @param code
   *          The error code for which to get the associated error message.
   * @return The error message for the specified code.
   */
  public static String getMessage(String code) {
    String str = MESSAGE_MAP.get(code);
    if (null == str) {
      return "NO MESSAGE FOR ERROR CODE. { code=[" + code + "] }";
    }
    return str;
  }

}
