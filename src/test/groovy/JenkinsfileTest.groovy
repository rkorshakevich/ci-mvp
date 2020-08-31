import com.lesfurets.jenkins.unit.BasePipelineTest
import groovy.json.JsonSlurper
import org.junit.Before
import org.junit.Test

class Artifactory {
    String[] serverId;

    Artifactory() {}
    Artifactory(String[] serverId) {
        this.serverId = serverId
    }

    static server(String[] serverId) {
        return new Artifactory(serverId)
    }

    static newBuildInfo() {
        return new Artifactory()
    }

    void upload(LinkedHashMap uploadSpec) {
        def slurper = new JsonSlurper()
        String spec = uploadSpec.spec
        HashMap result = slurper.parseText spec
        HashMap files = result.files
        if (
            files.target != 'example-repo-local/curl/'  ||
            files.pattern != 'build/*') {
            throw new Exception('Wrong parameters passed in artifactory uploadSpec')
        }
    }

    void publishBuildInfo(x) {}
    void append(x) {}
}

class JenkinsfileTest extends BasePipelineTest {

    @Override
    @Before
    void setUp() throws Exception {
        super.setUp()
        binding.setVariable('scm', [:])
        helper.registerAllowedMethod("sh", [Map.class], null)
        helper.registerAllowedMethod("git", [String.class], null)
    }

    @Test
    void "Should execute without errors"() throws Exception {
        runScript("src/main/groovy/Jenkinsfile.groovy")
        printCallStack()
        assertJobStatusSuccess()
    }

    @Test
    void "Check that all expected stages are executed"() throws Exception {
        runScript("src/main/groovy/Jenkinsfile.groovy")
        String[] stages = [
                'Checkout SCM',
                'Build',
                'Run Unit Tests',
                'Push to Artifactory'
        ]

        stages.each {
            assertCallStackContains("Jenkinsfile.stage($it, groovy.lang.Closure)")
        }
    }

    @Test
    void "Check Artifactory upload arguments"() throws Exception {
        runScript("src/main/groovy/Jenkinsfile.groovy")
    }


}
