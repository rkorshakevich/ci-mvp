#!groovy

import hudson.plugins.git.*
import hudson.plugins.git.extensions.*
import hudson.plugins.git.extensions.impl.*
import jenkins.model.Jenkins

def jobParameters = [
  name:          'Curl_build',
  description:   'Curl build',
  repository:    'https://github.com/rkorshakevich/ci-mvp.git',
  branch:        'master'
]

def branchConfig                =   [new BranchSpec(jobParameters.branch)]
def userConfig                  =   [new UserRemoteConfig(jobParameters.repository, null, null, null)]
def cleanBeforeCheckOutConfig   =   new CleanBeforeCheckout()
def sparseCheckoutPathConfig    =   new SparseCheckoutPaths([new SparseCheckoutPath("src/main/groovy/Jenkinsfile.groovy")])
def cloneConfig                 =   new CloneOption(true, true, null, 3)
def extensionsConfig            =   [cleanBeforeCheckOutConfig,sparseCheckoutPathConfig,cloneConfig]
def scm                         =   new GitSCM(userConfig, branchConfig, false, [], null, null, extensionsConfig)

def flowDefinition = new org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition(scm, "src/main/groovy/Jenkinsfile.groovy")

flowDefinition.setLightweight(true)

Jenkins jenkins = Jenkins.getInstance()

//def existingJob = Jenkins.instance.getAllItems(Job.class).find { 
//  it.name == jobParameters.name
//}

//if (!existingJob) {
  def job = new org.jenkinsci.plugins.workflow.job.WorkflowJob(jenkins,jobParameters.name)
  job.definition = flowDefinition
  job.setDescription(jobParameters.description)

  jenkins.save()
  jenkins.reload()
//}
