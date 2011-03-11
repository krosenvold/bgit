package com.atlassian.labs.bamboo.git;

import org.junit.Test;

import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.JavaGitException;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.Ref;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.commands.GitBranchResponse;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.client.cli.CliGitBranch;

import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * The branch output format exists in several generations
 * @author <a href="mailto:kristian@zenior no">Kristian Rosenvold</a>
 */
public class GitBranchParserTest {
    @Test
    public void testGit1633() throws JavaGitException {
        String branchOutput = "* (no branch)\n" +
                "  feature1\n" +
                "  featureDefault\n" +
                "  remotes/origin/HEAD -> origin/featureDefault\n" +
                "  remotes/origin/aBranch\n" +
                "  remotes/origin/feature1\n" +
                "  remotes/origin/feature2\n" +
                "  remotes/origin/featureDefault\n" +
                "  remotes/origin/master";


        verifyBranchParse( branchOutput, 8);

    }
    
    @Test
    public void testGit166() throws JavaGitException {
        String branchOutput = "* featureDefault\n" +
                "  remotes/origin/HEAD -> origin/featureDefault\n" +
                "  remotes/origin/aBranch\n" +
                "  remotes/origin/feature1\n" +
                "  remotes/origin/feature2\n" +
                "  remotes/origin/featureDefault\n" +
                "  remotes/origin/master";
        verifyBranchParse(branchOutput, 7);
    }

    private void verifyBranchParse(String branchOutput, int expected) throws JavaGitException {
        CliGitBranch.GitBranchParser parser = new CliGitBranch.GitBranchParser();
        for (String msg : branchOutput.split("\n")){
            parser.parseLine(msg);
        }
        final GitBranchResponse response = parser.getResponse();
        final List<Ref> record = response.getBranchList();
        assertEquals(expected, record.size());
    }

}

