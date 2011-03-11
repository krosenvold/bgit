[@ww.label label='Repository URL' value='${plan.buildDefinition.repository.repositoryUrl}' /]
[@ww.label label='Remote branch' value='${plan.buildDefinition.repository.remoteBranch}' /]

[#if plan.buildDefinition.repository.quietPeriodEnabled]
    [@ww.label labelKey='repository.common.quietPeriod.period' value='${plan.buildDefinition.repository.quietPeriod}' hideOnNull='true' /]
    [@ww.label labelKey='repository.common.quietPeriod.maxRetries' value='${plan.buildDefinition.repository.maxRetries}' hideOnNull='true' /]
[/#if]
