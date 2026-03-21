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

        stage('Stop Tomcat (If Running)') {
            steps {
                bat '''
                @echo off
                echo Checking if Tomcat is running...

                netstat -aon | findstr :8080 >nul
                if %errorlevel%==0 (
                    echo Tomcat is running. Stopping...
                    cd /d C:\\RahulSoftware\\apache-tomcat-9.0.86\\bin
                    call shutdown.bat >nul 2>&1
                    timeout /t 5 >nul
                ) else (
                    echo Tomcat not running. Skipping stop.
                )

                exit /b 0
                '''
            }
        }

        stage('Clean Old Deployment') {
            steps {
                bat '''
                echo Cleaning old deployment...
                del /Q C:\\RahulSoftware\\apache-tomcat-9.0.86\\webapps\\ROOT.war >nul 2>&1
                rmdir /S /Q C:\\RahulSoftware\\apache-tomcat-9.0.86\\webapps\\ROOT >nul 2>&1
                '''
            }
        }

        stage('Deploy WAR') {
            steps {
                bat '''
                echo Deploying WAR as ROOT...
                copy /Y target\\EmployeeManagement.war C:\\RahulSoftware\\apache-tomcat-9.0.86\\webapps\\ROOT.war
                '''
            }
        }

        stage('Start Tomcat') {
            steps {
                bat '''
                @echo off
                echo Starting Tomcat...

                cd /d C:\\RahulSoftware\\apache-tomcat-9.0.86\\bin

                REM Start Tomcat in background (IMPORTANT)
                start "" startup.bat

                echo Waiting for Tomcat to boot...
                ping 127.0.0.1 -n 10 >nul

                exit /b 0
                '''
            }
        }

        stage('Health Check') {
            steps {
                bat '''
                @echo off
                echo Performing health check...

                set retries=10

                :loop
                powershell -Command "(Invoke-WebRequest http://localhost:8080 -UseBasicParsing).StatusCode" >nul 2>&1

                if %errorlevel%==0 (
                    echo Application is UP!
                    exit /b 0
                )

                echo Waiting for application...
                ping 127.0.0.1 -n 5 >nul
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