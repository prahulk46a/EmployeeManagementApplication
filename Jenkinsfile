pipeline {
    agent any

    tools {
        maven 'MAVEN_HOME'
        jdk 'Java_Home'
    }

    environment {
        TOMCAT_HOME = "C:\\RahulSoftware\\apache-tomcat-9.0.86"
        TOMCAT_WEBAPPS = "${TOMCAT_HOME}\\webapps"
        WAR_NAME = "EmployeeManagement.war"
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
                bat '''
                @echo off
                echo Checking if Tomcat is running...

                netstat -aon | findstr :8080 >nul
                if %errorlevel%==0 (
                    echo Tomcat running. Stopping...
                    cd /d C:\\RahulSoftware\\apache-tomcat-9.0.86\\bin
                    call shutdown.bat >nul 2>&1
                    echo Tomcat stopped.
                ) else (
                    echo Tomcat not running. Skipping.
                )

                exit /b 0
                '''
            }
        }

        stage('Clean Old Deployment') {
            steps {
                bat """
                echo Cleaning old deployments...
                del /Q ${TOMCAT_WEBAPPS}\\ROOT.war >nul 2>&1
                rmdir /S /Q ${TOMCAT_WEBAPPS}\\ROOT >nul 2>&1
                """
            }
        }

        stage('Deploy to Tomcat') {
            steps {
                bat """
                echo Deploying WAR as ROOT...
                copy /Y target\\${WAR_NAME} ${TOMCAT_WEBAPPS}\\ROOT.war
                """
            }
        }

        stage('Start Tomcat') {
            steps {
                bat """
                echo Starting Tomcat...
                cd /d ${TOMCAT_HOME}\\bin
                startup.bat
                """
            }
        }

        stage('Health Check') {
            steps {
                bat '''
                @echo off
                echo Waiting for application to start...

                set retries=6

                :loop
                curl http://localhost:8080/ >nul 2>&1
                if %errorlevel%==0 (
                    echo Application is UP!
                    exit /b 0
                )

                echo Waiting...
                timeout /t 5 >nul
                set /a retries-=1

                if %retries% GTR 0 goto loop

                echo Application failed to start
                exit /b 1
                '''
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