node {
    stage('Checkout SCM') {
        git 'https://github.com/curl/curl.git'
    }

    stage('Build') {
        sh './buildconf'
        sh './configure --disable-shared --enable-debug'
        sh 'make'
    }

    stage('Run Unit Tests') {
        // sh 'make test'
    }

    stage('Push to Artifactory') {
        def buildInfo = Artifactory.newBuildInfo()
        // buildInfo.env.capture = true
        def server = Artifactory.server 'artifactory'
        def uploadSpec = """{
          "files": [
            {
              "pattern": "src/curl",
              "target": "example-repo-local/curl/"
            }
          ]
        }"""
        
        def uploadInfo = server.upload spec: uploadSpec
        buildInfo.append uploadInfo
        server.publishBuildInfo buildInfo
    }
}
