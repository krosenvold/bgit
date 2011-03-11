package com.atlassian.labs.bamboo.git.model;

import com.atlassian.bamboo.commit.Commit;
import com.atlassian.bamboo.commit.CommitFile;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.Ref;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author <a href="mailto:kristian@zenior.no">Kristian Rosenvold</a>
 */
public class CommitDescriptor implements Comparable<CommitDescriptor> {

    // Fri Oct 9 15:37:45 2009 +0200
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.US);
    private final Sha sha;
    private final String expectedFile;
    private final Calendar authorDate;
    private final Calendar commitDate;
    private final List<CommitDescriptor> parents;


    public CommitDescriptor(Sha sha, String expectedFile, String date, CommitDescriptor... parents) {
        this(sha, expectedFile, date, null, parents);
    }


    public CommitDescriptor(Sha sha, String expectedFile, String date, String commitDate, CommitDescriptor... parents) {
        this.sha = sha;
        this.expectedFile = expectedFile;
        try {
            final Date date1 = simpleDateFormat.parse(date);
            this.authorDate = Calendar.getInstance();
            this.authorDate.setTime(date1);
            final Date cdate = simpleDateFormat.parse(commitDate);
            this.commitDate = Calendar.getInstance();
            this.commitDate.setTime(cdate);

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        this.parents = Arrays.asList(parents);
    }


    public Calendar getCommitDate() {
        return commitDate;
    }


    public Sha getSha() {
        return sha;
    }


    public Ref getShaRef() {
        return Ref.createSha1Ref(getSha().getSha());
    }


    public boolean isThis(Sha sha) {
        return (this.sha.equals(sha));
    }


    public boolean historyContains(Sha sha) {
        if (isThis(sha))
            return true;
        for (CommitDescriptor commitDescriptor : parents) {
            if (commitDescriptor.historyContains(sha))
                return true;
        }
        return false;
    }


    public CommitDescriptor getAncestor(Sha sha) {
        if (isThis(sha))
            return this;
        for (CommitDescriptor commitDescriptor : parents) {
            final CommitDescriptor commitDescriptor1 = commitDescriptor.getAncestor(sha);
            if (commitDescriptor1 != null)
                return commitDescriptor1;
        }
        return null;
    }


    /**
     * Returns true if and only if this node is a true descendant of sha.
     *
     * @param sha The sha of the parent
     * @return true if it is a descendant
     */
    public boolean isDescendantOf(Sha sha) {
        return (historyContains(sha) && !isThis(sha));
    }


    /**
     * Collects all nodes up to (but not including) the supplied sha
     *
     * @param sha collects nodes
     * @return The commit descriptos
     */
    public List<CommitDescriptor> collectNodes(Sha sha) {
        Set<CommitDescriptor> result = new HashSet<CommitDescriptor>();
        collectionNodes(sha, result);
        return new ArrayList<CommitDescriptor>(result);
    }


    public List<CommitDescriptor> collectNodesByDate(Calendar targetCommitDate, Sha sha) {
        Set<CommitDescriptor> result = new HashSet<CommitDescriptor>();
        collectNodes(targetCommitDate, sha, result);
        return new ArrayList<CommitDescriptor>(result);
    }


    public CommitList collectNodesInRealGitLogOrder(Sha sha) {
        final CommitDescriptor descriptor = getAncestor(sha);
        final Calendar targetCommitDate = descriptor.getCommitDate();
        final List<CommitDescriptor> commitDescriptors1 = collectNodesByDate(targetCommitDate, sha);
        SortedSet<CommitDescriptor> sorted = new TreeSet<CommitDescriptor>(commitDescriptors1);
        final ArrayList<CommitDescriptor> result = new ArrayList<CommitDescriptor>(sorted);
        Collections.reverse(result);
        return new CommitList(result);

    }


    void collectionNodes(Sha sha, Set<CommitDescriptor> result) {
        if (isDescendantOf(sha))
            result.add(this);
        if (!isThis(sha)) {
            for (CommitDescriptor commitDescriptor : parents) {
                commitDescriptor.collectionNodes(sha, result);
            }
        }
    }


    void collectNodes(Calendar date, Sha sha, Set<CommitDescriptor> result) {
        if (this.getCommitDate().compareTo(date) >= 0) {
            if (!isThis(sha))
                result.add(this);
            for (CommitDescriptor commitDescriptor : parents) {
                commitDescriptor.collectNodes(date, sha, result);
            }
        }
    }


    public void assertMatch(com.atlassian.bamboo.commit.Commit commit) {
        final CommitFile commitFile = getAnyFileInCommit(commit);
        assertEquals("Sha match not ", sha.getSha(), commitFile.getRevision());
    }


    private CommitFile getAnyFileInCommit(Commit commit) {
        if (commit.getFiles().size() > 0)
            return commit.getFiles().get(0);
        throw new IllegalStateException("No files in commit");
    }


    public int compareTo(CommitDescriptor o) {
        int i = commitDate.compareTo(o.commitDate);
        if (i == 0) {
            return (this.isDescendantOf(o.getSha())) ? +1 : -1;
        }
        return i;
    }


    public void assertHistoryMatch(List<com.atlassian.bamboo.commit.Commit> commits, Sha until) {
        final CommitList expectedCollection = collectNodesInRealGitLogOrder(until);
        Iterator<CommitDescriptor> expectedHistory = expectedCollection.iterator();
        Iterator<com.atlassian.bamboo.commit.Commit> bambooCommits = commits.iterator();

        if (expectedCollection.size() != commits.size()) {
            fail("The expected list is not the same size as the commit list");
        }

        while (expectedHistory.hasNext()) {
            final CommitDescriptor expected = expectedHistory.next();
            final Commit commit = bambooCommits.next();
            expected.assertMatch(commit);
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        CommitDescriptor that = (CommitDescriptor) o;

        if (expectedFile != null ? !expectedFile.equals(that.expectedFile) : that.expectedFile != null)
            return false;
        if (sha != null ? !sha.equals(that.sha) : that.sha != null)
            return false;

        return true;
    }


    @Override
    public int hashCode() {
        int result = sha != null ? sha.hashCode() : 0;
        result = 31 * result + (expectedFile != null ? expectedFile.hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        return "CommitDescriptor{" + "sha=" + sha + '}';
    }
}
