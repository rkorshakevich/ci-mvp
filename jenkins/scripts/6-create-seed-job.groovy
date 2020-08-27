#!groovy

import hudson.plugins.git.*
import hudson.plugins.git.extensions.*
import hudson.plugins.git.extensions.impl.*
import jenkins.model.Jenkins

def jobParameters = [
  name:          'Curl build',
  description:   'Curl build',
  repository:    'https://github.com/rkorshakevich/ci-mvp.git',
  branch:        'master'
]

def branchConfig                =   [new BranchSpec(jobParameters.branch)]
def userConfig                  =   [new UserRemoteConfig(jobParameters.repository, null, null, null)]
def cleanBeforeCheckOutConfig   =   new CleanBeforeCheckout()
def sparseCheckoutPathConfig    =   new SparseCheckoutPaths([new SparseCheckoutPath("Jenkinsfile")])
def cloneConfig                 =   new CloneOption(true, true, null, 3)
def extensionsConfig            =   [cleanBeforeCheckOutConfig,sparseCheckoutPathConfig,cloneConfig]
def scm                         =   new GitSCM(userConfig, branchConfig, false, [], null, null, extensionsConfig)

def flowDefinition = new org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition(scm, "Jenkinsfile")

flowDefinition.setLightweight(true)

Jenkins jenkins = Jenkins.getInstance()

def job = new org.jenkinsci.plugins.workflow.job.WorkflowJob(jenkins,jobParameters.name)
job.definition = flowDefinition
job.setDescription(jobParameters.description)

jenkins.save()
jenkins.reload()
