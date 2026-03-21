pipeline {
    agent any

    tools {
        maven 'MAVEN_HOME'
    }

    environment {
        PROJECT_PATH = "D:\\Java Projects\\EmployeeManagementApplication"
        TOMCAT_HOME = "C:\\RahulSoftware\\apache-tomcat-9.0.86"
        TOMCAT_WEBAPPS = "${TOMCAT_HOME}\\webapps"
    }

    stages {

        stage('Build') {
            steps {
                dir("${PROJECT_PATH}") {
                    bat 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Stop Tomcat') {
            steps {
                bat '''
                for /f "tokens=5" %%a in ('netstat -aon ^| findstr :8080') do taskkill /F /PID %%a
                '''
            }
        }

        stage('Clean Old Deployment') {
            steps {
                bat """
                del /Q ${TOMCAT_WEBAPPS}\\*.war
                """
            }
        }

        stage('Deploy to Tomcat') {
            steps {
                bat """
                copy /Y "${PROJECT_PATH}\\target\\*.war" "${TOMCAT_WEBAPPS}"
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
    }
}