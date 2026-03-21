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
                    bat '''
                    @echo off
                    echo Checking if Tomcat is running on port 8080...

                    netstat -aon | findstr :8080 >nul
                    if %errorlevel%==0 (
                        echo Tomcat is running. Stopping now...
                        cd /d C:\\RahulSoftware\\apache-tomcat-9.0.86\\bin
                        call shutdown.bat >nul 2>&1
                        echo Tomcat stopped.
                    ) else (
                        echo Tomcat not running. Skipping stop.
                    )

                    exit /b 0
                    '''
                }
            }

            stage('Clean Old Deployment') {
                steps {
                    bat """
                    del /Q ${TOMCAT_WEBAPPS}\\*.war >nul 2>&1 || echo No old WAR files
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
                    timeout /t 10 >nul
                    curl http://localhost:8080/ || echo App still starting
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