pipeline {
    agent any

    tools {
        maven 'MAVEN_HOME'
        jdk 'Java_Home' // Ensure this is configured in Jenkins
    }

    environment {
        TOMCAT_HOME = "C:\\RahulSoftware\\apache-tomcat-9.0.86"
        TOMCAT_WEBAPPS = "${TOMCAT_HOME}\\webapps"
        APP_NAME = "EmployeeManagement"
    }

    stages {

        stage('Checkout Code') {
            steps {
                git(
                    url: 'https://github.com/prahulk46a/EmployeeManagementApplication',
                    branch: 'main',
                    credentialsId: 'github-creds'
                )
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Stop Tomcat') {
            steps {
                bat """
                cd /d ${TOMCAT_HOME}\\bin
                shutdown.bat || echo Tomcat already stopped
                """
            }
        }

        stage('Clean Old Deployment') {
            steps {
                bat """
                del /Q ${TOMCAT_WEBAPPS}\\*.war || echo No old WAR files
                """
            }
        }

        stage('Deploy to Tomcat') {
            steps {
                bat """
                copy /Y target\\*.war ${TOMCAT_WEBAPPS}
                """
            }
        }

        stage('Start Tomcat') {
            steps {
                bat """
                cd /d ${TOMCAT_HOME}\\bin
                startup.bat
                """
            }
        }

        stage('Health Check') {
            steps {
                bat """
                timeout /t 10
                curl http://localhost:8080/ || echo App might still be starting
                """
            }
        }
    }

    post {
        success {
            echo '✅ Deployment Successful'
        }
        failure {
            echo '❌ Pipeline Failed - Check logs'
        }
    }
}