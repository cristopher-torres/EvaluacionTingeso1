pipeline {
    agent any
    tools {
        maven "maven"
    }
    environment {
        DOCKER_CONTEXT = 'default' 
    }
    stages {
        stage("Build JAR File") {
            steps {
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/cristopher-torres/EvaluacionTingeso1']])
                dir("ToolRent_BACKEND") {
                    bat "mvn clean install"
                }
            }
        }
        stage("Test") {
            steps {
                dir("ToolRent_BACKEND") {
                    bat "mvn test"
                }
            }
        }
        stage("Build and Push Docker Image") {
            steps {
                dir("ToolRent_BACKEND") {
                    script {
                        withDockerRegistry(credentialsId: 'docker-credentials') {
                            bat "docker build -t crisdocker10/toolrent-backend:latest . "
                            bat "docker push crisdocker10/toolrent-backend:latest"
                        }
                    }
                }
            }
        }
    }
}