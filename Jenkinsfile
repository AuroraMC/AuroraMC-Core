pipeline {
    agent any

    environment {
        MVN_CREDS = credentials('maven-creds')
    }

    tools {
        maven 'Maven'
        jdk 'JDK17'
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
                 sh 'mvn -Dmaven.test.failure.ignore=true -s $MVN_CREDS clean package'
             }
             post {
                success {
                    archiveArtifacts artifacts: 'proxy/target/proxy-**.jar,server/target/server-**.jar,server/src/main/resources/config.yml,server/src/main/resources/spigot.yml,server/src/main/resources/eula.txt,proxy/src/main/resources/config.yml,proxy/src/main/resources/proxy-config.yml,proxy/src/main/resources/normal.png,proxy/src/main/resources/maintenance.png,smp/target/smp-**.jar', followSymlinks: false
                }
             }
        }
    }
}