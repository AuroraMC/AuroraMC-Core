pipeline {
    agent any

    tools {
        maven 'Maven'
        jdk 'JDK'
    }

    stages {
        stage ('Initialize') {
            steps {
                sh '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                '''
            }
            }
        stage('Build') {
             steps {
                 sh 'mvn -Dmaven.test.failure.ignore=true clean package'
             }
             post {
                success {
                    archiveArtifacts artifacts: 'target/*.jar', followSymlinks: false
                }
             }
        }
    }
}