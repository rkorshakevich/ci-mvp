node {
    stage('git checkout') {
        git 'https://github.com/rkorshakevich/ci-mvp.git'
    }
    
    stage('tests') {
        sh 'JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.262.b10-0.el8_2.x86_64/jre ./gradlew clean test codenarcMain --info'
    }
    
    stage('Publish reports') {
        echo 'Publish Codenarc report'
        publishHTML(
                target: [
                        allowMissing         : false,
                        alwaysLinkToLastBuild: false,
                        keepAll              : true,
                        reportDir            : 'build/reports/codenarc',
                        reportFiles          : 'main.html',
                        reportName           : "Codenarc Report"
                ]
        )
                publishHTML(
                target: [
                        allowMissing         : false,
                        alwaysLinkToLastBuild: false,
                        keepAll              : true,
                        reportDir            : 'build/reports/tests/test',
                        reportFiles          : 'index.html',
                        reportName           : "Test report"
                ]
        )
    }
    
    stage('Build curl') {
    //    build job: 'Curl_build'
    }
}
