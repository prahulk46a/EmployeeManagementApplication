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

                for /f "tokens=5" %%a in ('netstat -aon ^| findstr :8080') do (
                    echo Killing process %%a
                    taskkill /F /PID %%a >nul 2>&1
                )

                echo Stop step completed
                exit /b 0
                '''
            }
        }

        stage('Clean Old Deployment') {
            steps {
                bat """
                echo Cleaning old deployment...
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
                bat '''
                @echo off
                echo Starting Tomcat...

                cd /d C:\\RahulSoftware\\apache-tomcat-9.0.86\\bin

                REM Start Tomcat in separate window (stable for Jenkins)
                start "" cmd /c "catalina.bat run"

                echo Waiting for Tomcat to initialize...
                timeout /t 10 >nul

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
                curl --fail http://localhost:8080/ >nul 2>&1

                if %errorlevel%==0 (
                    echo Application is UP!
                    exit /b 0
                )

                echo Waiting for application...
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