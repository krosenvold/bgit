[@ww.textfield labelKey='Repository' name='repository.git.repositoryUrl' required='true' /]
[@ww.textfield labelKey='Remote branch to track' name='repository.git.remoteBranch' required='true' /]


[@ui.bambooSection titleKey='repository.advanced.option']

[@ww.checkbox labelKey='repository.advanced.option.enable' toggle='true' name='temporary.git.advanced' value='${repository.isAdvancedOptionEnabled(buildConfiguration)?string}' /]
[@ui.bambooSection dependsOn='temporary.git.advanced' showOn='true']
    [@ww.checkbox labelKey='repository.common.quietPeriod.enabled' toggle='true' name='repository.git.quietPeriod.enabled' /]
    [@ui.bambooSection dependsOn='repository.git.quietPeriod.enabled' showOn='true']
        [@ww.textfield labelKey='repository.common.quietPeriod.period' name='repository.git.quietPeriod.period' required='true' /]
        [@ww.textfield labelKey='repository.common.quietPeriod.maxRetries' name='repository.git.quietPeriod.maxRetries' required='true' /]
    [/@ui.bambooSection]
[/@ui.bambooSection]
[/@ui.bambooSection]


