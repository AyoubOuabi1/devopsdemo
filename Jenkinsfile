pipeline {
    agent any

    environment {
        ECR_REPOSITORY_URL = '992906191722.dkr.ecr.eu-west-3.amazonaws.com/devopsdemorepo'
        DOCKER_IMAGE_NAME = 'devopsdemoimage'
        DOCKER_IMAGE_TAG = 'latest'
        DOCKERFILE_PATH = 'Dockerfile'
        AWS_CREDENTIALS_ID = 'aws-ecr'
        AWS_REGION = 'eu-west-3'
        ECR_REPO = '992906191722.dkr.ecr.eu-west-3.amazonaws.com/devopsdemorepo'
    }


      stages {
                stage('Check ECR Connection') {
                    steps {
                        script {
                            // Authenticate with ECR
                            withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws-credentials', accessKeyVariable: 'AWS_ACCESS_KEY_ID', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']]) {
                                def ecrLogin = sh(script: "aws ecr get-login-password --region ${AWS_REGION}", returnStdout: true).trim()
                                sh "${ecrLogin}"

                                // Check if authentication was successful
                                def checkAuth = sh(script: "docker info | grep -q 'Authentication' && echo 'Success' || echo 'Failure'", returnStatus: true).trim()
                                if (checkAuth == 'Success') {
                                    echo "Successfully authenticated with ECR"
                                } else {
                                    error "Failed to authenticate with ECR"
                                }
                            }
                        }
                    }
                }
    }
}
