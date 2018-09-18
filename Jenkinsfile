pipeline {
    agent any

    stages {
        stage ('Integration Test') {
            steps {
                sh '''
                    export JAVA_HOME=/usr/lib/jvm/jdk-10.0.1
                    cd integration-test && make without-build && make revert-db
                '''
            }
        }
    }
}