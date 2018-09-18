pipeline {
    agent any

    stages {
        stage ('Build') {
            steps {
                sh '''
                    export JAVA_HOME=/usr/lib/jvm/jdk-10.0.1
                    ./gradlew -Penv=jenkins -b build.gradle clean build --info --stacktrace --parallel -x test -x checkstyleMain -x checkstyleTest -x spotbugsMain -x spotbugsTest
                '''
            }
        }
        stage ('Spotbugs Test') {
            steps {
                sh '''
                    export JAVA_HOME=/usr/lib/jvm/jdk-10.0.1
                    ./gradlew -Penv=jenkins -b build.gradle check -x checkstyleMain -x checkstyleTest -x spotbugsMain -x test --no-daemon --stacktrace
                '''
            }
        }
    }
}