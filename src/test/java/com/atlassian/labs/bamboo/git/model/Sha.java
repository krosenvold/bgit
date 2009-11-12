package com.atlassian.labs.bamboo.git.model;

/**
* @author <a href="mailto:kristian@zenior.no">Kristian Rosenvold</a>
*/
public class Sha {
    private final String sha;

    public Sha(String sha) {
        this.sha = sha;
    }

    public String getSha() {
        return sha;
    }

    @Override
    public String toString() {
        return sha;
    }
}
