package com.atlassian.labs.bamboo.git;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.atlassian.bamboo.repository.AbstractRepository;
import com.atlassian.bamboo.repository.CustomVariableProviderRepository;
import com.atlassian.bamboo.repository.MutableQuietPeriodAwareRepository;
import com.atlassian.bamboo.repository.QuietPeriodHelper;
import com.atlassian.bamboo.repository.Repository;
import com.atlassian.bamboo.repository.RepositoryException;
import com.atlassian.bamboo.template.TemplateRenderer;
import com.atlassian.bamboo.utils.error.ErrorCollection;
import com.atlassian.bamboo.v2.build.BuildChanges;
import com.atlassian.bamboo.v2.build.BuildContext;
import com.atlassian.bamboo.ww2.actions.build.admin.create.BuildConfiguration;
import com.atlassian.labs.bamboo.git.edu.nyu.cs.javagit.api.JavaGitException;
import com.google.common.collect.Maps;

/**
 * Bamboo GIT repository plugin implementation.
 * This is not Atlassian's implementation and it is not supported by their developers!
 */
public class GitRepositoryPlugin extends AbstractRepository implements MutableQuietPeriodAwareRepository,
        CustomVariableProviderRepository {

    private final Log log = LogFactory.getLog(GitRepositoryPlugin.class);

    private static final long serialVersionUID = -5031786714275269805L;

    private static final String NAME = "BGit (GitHub - dmatej)";

    /**
     * Plugin keys in conflict with the Atlassian GIT plugin, but may be used in saved configuration.
     */
    private static final String[] DEPRECATED_PLUGIN_SHORTKEYS = {"git", "github.git"};
    private static final String SHORTKEY = "github-git";

    private static final String REPOSITORY_PREFIX = "repository.";
    private static final String TEMPORARY_PREFIX = "temporary.";
    private static final String PLUGIN_PREFIX = REPOSITORY_PREFIX + SHORTKEY + ".";

    private static final String REPOSITORY_URL = "repositoryUrl";
    private static final String REMOTE_BRANCH = "remoteBranch";

    private static final String FULL_KEY_REPOSITORY_URL = PLUGIN_PREFIX + REPOSITORY_URL;
    private static final String FULL_KEY_REMOTE_BRANCH = PLUGIN_PREFIX + REMOTE_BRANCH;
    private static final String FULL_KEY_TEMPORARY_GIT_ADVANCED = TEMPORARY_PREFIX + SHORTKEY + ".advanced";

    // Quiet Period
    private final QuietPeriodHelper quietPeriodHelper = new QuietPeriodHelper(PLUGIN_PREFIX);

    private RepositorySettings settings = new RepositorySettings();

    /**
     * Used by central bamboo server to determine changes.
     */
    @NotNull
    public BuildChanges collectChangesSinceLastBuild(@NotNull String planKey,
            @NotNull String lastVcsRevisionKey) throws RepositoryException {
        log.trace("collectChangesSinceLastBuild(planKey=" + planKey + ", sinceKey=" + lastVcsRevisionKey + ")");

        GitRepository repo = getGitRepository(planKey);
        return repo.getChangesSinceLastBuild(planKey, lastVcsRevisionKey);
    }


    @SuppressWarnings("deprecation")
    @Deprecated
    @NotNull
    public String retrieveSourceCode(@NotNull String planKey, @Nullable String vcsRevisionKey)
        throws RepositoryException {
        log.debug("retrieving source code for planKey=" + planKey + " and revisionKey=" + vcsRevisionKey);
        try {
            GitRepository repo = getGitRepository(planKey);
            repo.cloneOrFetch(vcsRevisionKey);
            repo.submodule_update();
            return repo.getLastRevision(vcsRevisionKey);
        } catch (IOException e) {
            throw new RepositoryException("retrieveSourceCode", e);
        } catch (JavaGitException e) {
            throw new RepositoryException(e.getMessage(), e);
        }
    }


    @NotNull
    public String retrieveSourceCode(@NotNull BuildContext buildContext, @Nullable String vcsRevisionKey)
        throws RepositoryException {
        log.trace("retrieveSourceCode(buildContext, vcsRevisionKey)");
        return retrieveSourceCode(buildContext.getPlanKey(), vcsRevisionKey);
    }


    @NotNull
    @Override
    public File getSourceCodeDirectory(@NotNull String planKey) throws RepositoryException {
        log.debug("getSourceCodeDirectory(planKey=" + planKey + ")");
        File codeDirectory = super.getSourceCodeDirectory(planKey);
        return new File(codeDirectory, "checkout");
    }


    private File getCheckoutDirectory(String planKey) throws RepositoryException {
        log.trace("getCheckoutDirectory(planKey)");
        return getSourceCodeDirectory(planKey);
    }


    @Override
    public void addDefaultValues(@NotNull BuildConfiguration buildConfiguration) {
        log.trace("addDefaultValues(buildConfiguration)");
        super.addDefaultValues(buildConfiguration);
        quietPeriodHelper.addDefaultValues(buildConfiguration);
    }


    /**
     * Validates settings from an edit page
     */
    @Override
    @NotNull
    public ErrorCollection validate(@NotNull BuildConfiguration buildConfiguration) {
        log.info("validate(buildConfiguration)");
        if (log.isTraceEnabled() && buildConfiguration != null) {
            log.trace("buildConfiguration properties:\n" + toString(buildConfiguration));
        }

        ErrorCollection errorCollection = super.validate(buildConfiguration);

        String repoUrl = buildConfiguration.getString(FULL_KEY_REPOSITORY_URL);
        repoUrl = getVariableSubstitutionBean().substituteBambooVariables(repoUrl);
        if (StringUtils.isBlank(repoUrl)) {
            errorCollection.addError(FULL_KEY_REPOSITORY_URL, "Please specify the build's Git Repository");
        } else {
            // FIXME: do validation
        }

        String remoBranch = buildConfiguration.getString(FULL_KEY_REMOTE_BRANCH);
        if (StringUtils.isBlank(remoBranch)) {
            errorCollection.addError(FULL_KEY_REMOTE_BRANCH, "Please specify the remote branch that will be checked out");
        }

        this.quietPeriodHelper.validate(buildConfiguration, errorCollection);

        log.debug("validation results: " + errorCollection);
        return errorCollection;
    }


    public boolean isRepositoryDifferent(@NotNull Repository repository) {
        if (repository instanceof GitRepositoryPlugin) {
            GitRepositoryPlugin existing = (GitRepositoryPlugin) repository;
            return !new EqualsBuilder().append(this.getName(), existing.getName())
                    .append(this.getRepositoryUrl(), existing.getRepositoryUrl()).isEquals();
        }

        return true;
    }


    /**
     * Do any preprocessing work before validation occurs.
     *
     * @param buildConfiguration - @NotNull
     */
    @Override
    public void prepareConfigObject(@NotNull BuildConfiguration buildConfiguration) {
        log.debug("prepareConfigObject(buildConfiguration)");

        // 3.0.1 - does nothing
        super.prepareConfigObject(buildConfiguration);

        // Disabling advanced will clear all advanced
        boolean advanced = buildConfiguration.getBoolean(FULL_KEY_TEMPORARY_GIT_ADVANCED, false);

        if (!advanced) {
            this.quietPeriodHelper.clearFromBuildConfiguration(buildConfiguration);
        }
    }


    /**
     * Loads the configuration (persisted or changed via the plan configuration)
     */
    @Override
    public void populateFromConfig(@NotNull HierarchicalConfiguration config) {
        log.info("populateFromConfig(config)");
        if (log.isDebugEnabled()) {
            log.debug("config properties:\n" + toString(config));
        }

        super.populateFromConfig(config);

        String prefix = resolvePluginPrefixForPopulate(config);
        if (log.isDebugEnabled()) {
            log.debug("repository will load a configuration using prefix='" + prefix + "'");
        }

        if (PLUGIN_PREFIX.equals(prefix)) {
            setRepositoryUrl(config.getString(FULL_KEY_REPOSITORY_URL));
            setRemoteBranch(config.getString(FULL_KEY_REMOTE_BRANCH));
            this.quietPeriodHelper.populateFromConfig(config, this);
        } else {
            setRepositoryUrl(config.getString(prefix + REPOSITORY_URL));
            setRemoteBranch(config.getString(prefix + REMOTE_BRANCH));
            QuietPeriodHelper deprecHelper = new QuietPeriodHelper(prefix);
            deprecHelper.populateFromConfig(config, this);
        }
    }


    /**
     * Searches the config for a repository property prefixed with standard or some of the deprecated prefixes
     * @param config
     * @return a prefix
     */
    private String resolvePluginPrefixForPopulate(HierarchicalConfiguration config) {

        // look for the repo url property with the actual prefix
        String value = config.getString(FULL_KEY_REPOSITORY_URL);
        if (!StringUtils.isBlank(value)) {
            return PLUGIN_PREFIX;
        }

        // invalid or maybe deprecated key?
        StringBuilder key = new StringBuilder(32);
        for (int i = 0; i < DEPRECATED_PLUGIN_SHORTKEYS.length; i++) {
            key.setLength(0);
            key.append(REPOSITORY_PREFIX).append(DEPRECATED_PLUGIN_SHORTKEYS[i]).append('.').append(REPOSITORY_URL);
            value = StringUtils.trimToNull(config.getString(key.toString()));
            if (value != null) {
                break;
            }
        }

        if (value != null) {
            log.warn("Found property under an old deprecated key='" + key
                    + "', please open and save the configuration for a fix");
            key.delete(key.length() - REPOSITORY_URL.length(), key.length());
            return key.toString();
        }

        // nothing found
        return PLUGIN_PREFIX;
    }


    /**
     * Created the configuration (for persistance or viewing the configuration via browser)
     */
    @Override
    @NotNull
    public HierarchicalConfiguration toConfiguration() {
        log.trace("toConfiguration()");
        HierarchicalConfiguration configuration = super.toConfiguration();
        configuration.setProperty(FULL_KEY_REPOSITORY_URL, getRepositoryUrl());
        configuration.setProperty(FULL_KEY_REMOTE_BRANCH, getRemoteBranch());

        // Quiet period
        this.quietPeriodHelper.toConfiguration(configuration, this);
        return configuration;
    }


    /**
     * Overrides the plugin's key saved in atlassian-plugin.xml descriptor.
     * It is needed to avoid the properties cleanup when saving a configuration via GUI, because the Atlassian's plugin
     * uses the same shortkey.
     */
    @Override
    public String getShortKey() {
        return SHORTKEY;
    }


    /**
     * What's the name of the plugin - appears in the GUI dropdown
     *
     * @return The name
     */
    @NotNull
    public String getName() {
        return NAME;
    }


    /**
     * Specify the subversion repository we are using
     *
     * @param repositoryUrl The subversion repository
     */
    public void setRepositoryUrl(String repositoryUrl) {
        log.debug("setRepositoryUrl(repositoryUrl=" + repositoryUrl + ")");
        this.settings.setRepositoryUrl(repositoryUrl);
    }


    /**
     * Which repository URL are we using?
     *
     * @return The subversion repository
     */
    public String getRepositoryUrl() {
        return this.settings.getRepositoryUrl();
    }


    public void setRemoteBranch(String remoteBranch) {
        log.trace("setRemoteBranch(remoteBranch=" + remoteBranch + ")");
        this.settings.setRemoteBranch(remoteBranch);
    }


    public String getRemoteBranch() {
        return this.settings.getRemoteBranch();
    }


    public String getHost() {
        return "localhost";
    }


    public void setQuietPeriodEnabled(boolean quietPeriodEnabled) {
        log.trace("setQuietPeriodEnabled(quietPeriodEnabled=" + quietPeriodEnabled + ")");
        this.settings.setQuietPeriodEnabled(quietPeriodEnabled);
    }


    public boolean isQuietPeriodEnabled() {
        return this.settings.isQuietPeriodEnabled();
    }


    /**
     * @param buildConfiguration
     * @return true if the quietPeriod option is enabled
     */
    public boolean isAdvancedOptionEnabled(BuildConfiguration buildConfiguration) {
        return this.quietPeriodHelper.isEnabled(buildConfiguration);
    }


    public void setQuietPeriod(int quietPeriod) {
        log.trace("setQuietPeriod(quietPeriod=" + quietPeriod + ")");
        this.settings.setQuietPeriod(quietPeriod);
    }


    public int getQuietPeriod() {
        return this.settings.getQuietPeriod();
    }


    public void setMaxRetries(int maxRetries) {
        log.trace("setMaxRetries(maxRetries=" + maxRetries + ")");
        this.settings.setMaxRetries(maxRetries);
    }


    public int getMaxRetries() {
        return this.settings.getMaxRetries();
    }


    @Override
    public void setTemplateRenderer(TemplateRenderer templateRenderer) {
        log.trace("setTemplateRenderer(templateRenderer=" + templateRenderer + ")");
        super.setTemplateRenderer(templateRenderer);
    }


    @Override
    public int hashCode() {
        return new HashCodeBuilder(101, 11).append(getKey()).append(getRepositoryUrl()).append(getTriggerIpAddress())
                .toHashCode();
    }


    /**
     * @return true if the GIT repository has the same url and triggerIpAdress
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof GitRepositoryPlugin)) {
            return false;
        }
        GitRepositoryPlugin rhs = (GitRepositoryPlugin) o;
        return new EqualsBuilder().append(getRepositoryUrl(), rhs.getRepositoryUrl())
                .append(getTriggerIpAddress(), rhs.getTriggerIpAddress()).isEquals();
    }


    /**
     * @return empty map
     */
    public Map<String, String> getCustomVariables() {
        Map<String, String> variables = Maps.newHashMap();
        variables.put(FULL_KEY_REPOSITORY_URL, getRepositoryUrl());
        variables.put(FULL_KEY_REMOTE_BRANCH, getRemoteBranch());
        return variables;
    }


    private GitRepository getGitRepository(String planKey) throws RepositoryException {
        String url = this.settings.getRepositoryUrl();
        File dir = getCheckoutDirectory(planKey);
        return new GitRepository(url, dir, this.settings.getRemoteBranch());
    }


    private String toString(HierarchicalConfiguration config) {
        if (config == null) {
            return "<null>";
        }
        StringBuilder b = new StringBuilder(2048);
        for (Iterator<?> iterator = config.getKeys(); iterator.hasNext();) {
            String key = (String) iterator.next();
            b.append(key).append('=').append(config.getProperty(key));
            if (iterator.hasNext()) {
                b.append("  \n");
            }
        }
        return b.toString();
    }
}
