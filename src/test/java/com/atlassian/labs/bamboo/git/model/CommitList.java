package com.atlassian.labs.bamboo.git.model;

import java.util.*;

/**
 * @author <a href="mailto:kristian@zenior.no">Kristian Rosenvold</a>
 */
public class CommitList {

    private final List<CommitDescriptor> commits;


    public CommitList(List<CommitDescriptor> commits) {
        this.commits = commits;
    }


    public CommitList(CommitDescriptor... commits) {
        this.commits = new ArrayList<CommitDescriptor>(Arrays.asList(commits));
    }


    public Iterator<CommitDescriptor> iterator() {
        return commits.iterator();
    }


    public List<CommitDescriptor> getCommits() {
        return commits;
    }


    public CommitList getDateOrdered() {
        List<CommitDescriptor> result = new ArrayList<CommitDescriptor>(commits);
        SortedSet<CommitDescriptor> sl = new TreeSet<CommitDescriptor>();
        sl.addAll(result);
        return new CommitList(new ArrayList<CommitDescriptor>(sl));
    }


    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (CommitDescriptor commitDescriptor : commits) {
            result.append(commitDescriptor.getSha().getSha());
            result.append("\n");
        }
        return result.toString();
    }


    public int size() {
        return commits.size();
    }
}
