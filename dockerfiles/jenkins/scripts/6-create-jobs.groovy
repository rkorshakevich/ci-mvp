#!groovy

import hudson.plugins.git.*
import hudson.plugins.git.extensions.*
import hudson.plugins.git.extensions.impl.*
import jenkins.model.Jenkins

def jobParameters = [
  [
  name:          'Curl_build',
  description:   'Curl build',
  repository:    'https://github.com/rkorshakevich/ci-mvp.git',
  branch:        'master',
  jenkinsfile:	'src/main/groovy/Jenkinsfile.groovy'
  ],
  [
  name:         'Quality gate',
  description:  'Quality gate',
  repository:   'https://github.com/rkorshakevich/ci-mvp.git',
  branch:       'master',
  jenkinsfile:  'Jenkinsfile'
  ]
]

jobParameters.each { jobParameter ->
  def branchConfig                =   [new BranchSpec(jobParameter.branch)]
  def userConfig                  =   [new UserRemoteConfig(jobParameter.repository, null, null, null)]
  def cleanBeforeCheckOutConfig   =   new CleanBeforeCheckout()
  def sparseCheckoutPathConfig    =   new SparseCheckoutPaths([new SparseCheckoutPath(jobParameter.jenkinsfile)])
  def cloneConfig                 =   new CloneOption(true, true, null, 3)
  def extensionsConfig            =   [cleanBeforeCheckOutConfig,sparseCheckoutPathConfig,cloneConfig]
  def scm                         =   new GitSCM(userConfig, branchConfig, false, [], null, null, extensionsConfig)

  def flowDefinition = new org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition(scm, "jobParameter.jenkinsfile")

  flowDefinition.setLightweight(true)

  Jenkins jenkins = Jenkins.getInstance()

  def job = new org.jenkinsci.plugins.workflow.job.WorkflowJob(jenkins,jobParameter.name)
  job.definition = flowDefinition
  job.setDescription(jobParameter.description)

  jenkins.save()
  jenkins.reload()
}
