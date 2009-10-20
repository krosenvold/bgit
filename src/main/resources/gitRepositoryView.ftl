[@ww.label labelKey='repository.git.repository' name='build.buildDefinition.repository.repositoryUrl' /]
[@ww.label labelKey='repository.git.remoteBranch' name='build.buildDefinition.repository.remoteBranch' /]

[#if repository.quietPeriodEnabled]
    [@ww.label labelKey='repository.common.quietPeriod.period' name='build.buildDefinition.repository.quietPeriod' hideOnNull='true' /]
    [@ww.label labelKey='repository.common.quietPeriod.maxRetries' name='build.buildDefinition.repository.maxRetries' hideOnNull='true' /]
[/#if]
