#!groovy

import java.util.logging.Logger

import hudson.model.*
import jenkins.model.*
import hudson.security.*
import jenkins.security.*
import jenkins.security.apitoken.*
import jenkins.security.s2m.AdminWhitelistRule

def genpass = { n=10 ->
  def pool = ['a'..'z','#','@','%','A'..'Z','!','$','^',0..9,'_','&','*'].flatten()
  Random rand = new Random(System.currentTimeMillis())
  def passChars = (0..n).collect { pool[rand.nextInt(pool.size())] }
  passChars.join()
}

def ln = System.getProperty('line.separator')

Logger logger = Logger.getLogger("")

def userName = 'admin'
def tokenName = 'swarm-token'
def userPass = genpass(12)
def secretsPath = '/tmp/jenkins_shared'

def instance = Jenkins.getInstance()
if (User.get(userName, false) == null) {

  logger.info("User $userName does not exist. Creating...")
  def hudsonRealm = new HudsonPrivateSecurityRealm(false)
  hudsonRealm.createAccount(userName, userPass)
  instance.setSecurityRealm(hudsonRealm)

  def strategy = new FullControlOnceLoggedInAuthorizationStrategy()
  strategy.setAllowAnonymousRead(false)
  instance.setAuthorizationStrategy(strategy)
  instance.save()

  def passwordFile = new File("$secretsPath/jenkinsCredentials")
  passwordFile.newWriter().withWriter { w ->
    w << userName + ln
    w << userPass + ln
  }

} else {
  logger.info("--- User $userName exists. Skipping user creation.")
}

def tokenFile  = new File("$secretsPath/apiToken")

if (!tokenFile.exists()) {
  logger.info("--- Token file does not exist. Creating new token with name $tokenName ...")
  def user = User.get(userName, false)
  def apiTokenProperty = user.getProperty(ApiTokenProperty.class)
  def token = apiTokenProperty.tokenStore.generateNewToken(tokenName)
  user.save()

  tokenFile.newWriter().withWriter { w ->
    w << token.plainValue + ln
  }
} else {
  logger.info("--- Token file exists. Skipping token generation.")
}

Jenkins.instance.getInjector().getInstance(AdminWhitelistRule.class).setMasterKillSwitch(false)
