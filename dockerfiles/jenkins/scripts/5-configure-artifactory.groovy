#!groovy

import jenkins.model.Jenkins
import com.cloudbees.plugins.credentials.domains.Domain
import com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl
import com.cloudbees.plugins.credentials.CredentialsScope
import org.jfrog.* 
import org.jfrog.hudson.* 
import org.jfrog.hudson.util.Credentials;

instance = Jenkins.instance
domain = Domain.global()
store = instance.getExtensionList(
  "com.cloudbees.plugins.credentials.SystemCredentialsProvider")[0].getStore()

usernameAndPassword = new UsernamePasswordCredentialsImpl(
  CredentialsScope.GLOBAL,
  "artifactory-admin",
  "Artifactory admin credentials",
  "admin",
  "password"
)

store.addCredentials(domain, usernameAndPassword)


def desc = instance.getDescriptor("org.jfrog.hudson.ArtifactoryBuilder")
def deployerCredentials = new CredentialsConfig("","", "artifactory-admin", false)

def sinst = [new ArtifactoryServer( "artifactory", "http://artifactory:8081/artifactory",  deployerCredentials, null, 300, true ,1,1)]
desc.setArtifactoryServers(sinst)
desc.setUseCredentialsPlugin(true)
desc.save()
