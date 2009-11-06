package edu.nyu.cs.javagit.client.cli;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.nyu.cs.javagit.api.JavaGitConfiguration;
import edu.nyu.cs.javagit.api.JavaGitException;
import edu.nyu.cs.javagit.api.commands.CommandResponse;
import edu.nyu.cs.javagit.client.IGitSubmodule;
import edu.nyu.cs.javagit.utilities.CheckUtilities;

public class CliGitSubmodule implements IGitSubmodule
{

    public void init(File repoDirectory)
            throws JavaGitException, IOException
    {
        CheckUtilities.checkFileValidity(repoDirectory);
        GitSubmoduleParser parser = new GitSubmoduleParser();
        List<String> command = buildInitCommand(repoDirectory);
        ProcessUtilities.runCommand(repoDirectory,
                command, parser);
    }

    public void update(File repoDirectory)
            throws JavaGitException, IOException
    {
        CheckUtilities.checkFileValidity(repoDirectory);
        GitSubmoduleParser parser = new GitSubmoduleParser();
        List<String> command = buildUpdateCommand(repoDirectory);
        ProcessUtilities.runCommand(repoDirectory,
                command, parser);
    }

    private List<String> buildCommand(File repoDirectory)
    {
        List<String> command = new ArrayList<String>();
        command.add(JavaGitConfiguration.getGitCommand());
        command.add("submodule");
        return command;
    }

    private List<String> buildInitCommand(File repoDirectory)
    {
        List<String> command = buildCommand(repoDirectory);
        command.add("init");
        return command;
    }

    private List<String> buildUpdateCommand(File repoDirectory)
    {
        List<String> command = buildCommand(repoDirectory);
        command.add("update");
        return command;
    }

    public class GitSubmoduleParser implements IParser
    {

        public void parseLine(String line)
        {
        }

        public void processExitCode(int code)
        {
        }

        public CommandResponse getResponse() throws JavaGitException
        {
            return null; // To change body of implemented methods use File | Settings | File Templates.
        }
    }
}