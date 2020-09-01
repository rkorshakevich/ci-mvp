#!groovy

import hudson.model.*
import jenkins.model.Jenkins
import jenkins.model.JenkinsLocationConfiguration

System.setProperty("hudson.model.DirectoryBrowserSupport.CSP", "")

def jenkinsParameters = [
  email:  'Jenkins Admin <admin@jenkins.com>',
  url:    'http://localhost:8080/'
]

def jenkinsLocationConfiguration = JenkinsLocationConfiguration.get()
jenkinsLocationConfiguration.setUrl(jenkinsParameters.url)
jenkinsLocationConfiguration.setAdminAddress(jenkinsParameters.email)
jenkinsLocationConfiguration.save()
