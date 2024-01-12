pipeline {
    agent {
        label 'devops'
    }

    environment {
        ECR_REPOSITORY_URL = '992906191722.dkr.ecr.eu-west-3.amazonaws.com/devopsdemorepo'
        DOCKER_IMAGE_NAME = 'devopsdemoimage'
        DOCKER_IMAGE_TAG = 'latest'
        DOCKERFILE_PATH = 'Dockerfile'
        AWS_CREDENTIALS_ID = 'aws-ecr'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build and Push Docker Image') {
            steps {
                script {
                    echo 'Tests are working. Building and pushing Docker image.'

                    // Build Docker image
                    sh "docker build -t ${ECR_REPOSITORY_URL}/${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG} -f ${DOCKERFILE_PATH} ."

                    // Get ECR login password
                    def ecrLoginCmd = sh(script: "aws ecr get-login-password --region eu-west-3", returnStdout: true).trim()

                    // Extract the authentication token from the ECR login command
                    def authToken = sh(script: "${ecrLoginCmd} | awk '{print \\\$6}'", returnStdout: true).trim()

                    // Authenticate Docker with AWS ECR using the obtained token
                    sh "docker login -u AWS -p ${authToken} ${ECR_REPOSITORY_URL}"

                    // Push Docker image to AWS ECR
                    sh "docker push ${ECR_REPOSITORY_URL}/${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}"
                }
            }
        }
    }
}
