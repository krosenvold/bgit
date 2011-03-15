/**
 *
 */
package com.atlassian.labs.bamboo.git;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.atlassian.bamboo.repository.QuietPeriodHelper;

/**
 * Repository settings are persisted in the Bamboo's database
 *
 * @author David Matějček
 */
public class RepositorySettings implements Serializable {

    private static final long serialVersionUID = -4196806263339529193L;

    private String repositoryUrl;
    private String webRepositoryUrl;
    private String remoteBranch;
    private boolean hideAuthorEmail = true;

    private boolean quietPeriodEnabled = false;
    private int quietPeriod = QuietPeriodHelper.DEFAULT_QUIET_PERIOD;
    private int maxRetries = QuietPeriodHelper.DEFAULT_MAX_RETRIES;


    public String getRepositoryUrl() {
        return this.repositoryUrl;
    }


    public void setRepositoryUrl(String repositoryUrl) {
        this.repositoryUrl = StringUtils.trimToNull(repositoryUrl);
    }


    public String getWebRepositoryUrl() {
        return this.webRepositoryUrl;
    }


    public void setWebRepositoryUrl(String webRepositoryUrl) {
        this.webRepositoryUrl = webRepositoryUrl;
    }


    /**
     * @return true if the webRepositoryUrl is not blank
     */
    public boolean hasWebBasedRepositoryAccess() {
        return this.webRepositoryUrl != null;
    }


    public String getRemoteBranch() {
        return this.remoteBranch;
    }


    public void setRemoteBranch(String remoteBranch) {
        this.remoteBranch = StringUtils.trimToNull(remoteBranch);
    }


    public boolean isRemoteBranchSpecified() {
        return this.remoteBranch != null;
    }


    public boolean isHideAuthorEmail() {
        return this.hideAuthorEmail;
    }


    public void setHideAuthorEmail(boolean hideAuthorEmail) {
        this.hideAuthorEmail = hideAuthorEmail;
    }


    public boolean isQuietPeriodEnabled() {
        return this.quietPeriodEnabled;
    }


    public void setQuietPeriodEnabled(boolean quietPeriodEnabled) {
        this.quietPeriodEnabled = quietPeriodEnabled;
    }


    public int getQuietPeriod() {
        return this.quietPeriod;
    }


    public void setQuietPeriod(int quietPeriod) {
        this.quietPeriod = quietPeriod;
    }


    public int getMaxRetries() {
        return this.maxRetries;
    }


    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

}
