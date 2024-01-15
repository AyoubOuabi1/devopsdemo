pipeline {
    agent any

    environment {
        ECR_REPOSITORY_URL = '992906191722.dkr.ecr.eu-west-3.amazonaws.com/devopsdemorepo'
        DOCKER_IMAGE_NAME = 'devopsdemoimage'
        DOCKER_IMAGE_TAG = 'latest'
        DOCKERFILE_PATH = 'Dockerfile'
        AWS_CREDENTIALS_ID = 'aws-ecr'
        AWS_REGION = 'eu-west-3'  // Assuming your ECR repository is in this region
        CUSTOM_TAG = 'latest' //"${env.BUILD_ID}_${new Date().format('yyyyMMddHHmmss')}"
        ECS_CLUSTER = 'devopsdemo'
        ECS_SERVICE = 'dev_service'
    }

    stages {
        stage('Check ECR Connection') {
            steps {
                script {
                    withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws-credentials', accessKeyVariable: 'AWS_ACCESS_KEY_ID', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']]) {
                        // Explicitly check for AWS CLI installation
                        sh 'which aws || (echo "AWS CLI not installed. Please install it." && exit 1)'

                        // Test AWS CLI authentication
                        def ecrCheck = sh(script: "aws ecr describe-repositories", returnStatus: true, failOnError: false)
                        if (ecrCheck == 0) {
                            echo "Successfully authenticated with ECR using AWS CLI"
                        } else {
                            error "Failed to authenticate with ECR using AWS CLI"
                        }
                    }
                }
            }
        }

        stage('Build and Push Docker Image') {
            steps {
                script {
                    // Build Docker image
                    sh "docker build -t ${DOCKER_IMAGE_NAME}:${CUSTOM_TAG} -f ${DOCKERFILE_PATH} ."

                    // Authenticate with ECR and push Docker image
                    withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws-credentials', accessKeyVariable: 'AWS_ACCESS_KEY_ID', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']]) {
                        sh "aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${ECR_REPOSITORY_URL}"
                        sh "docker tag ${DOCKER_IMAGE_NAME}:${CUSTOM_TAG} ${ECR_REPOSITORY_URL}:${CUSTOM_TAG}"
                        sh "docker push ${ECR_REPOSITORY_URL}:${CUSTOM_TAG}"
                    }
                }
            }
        }

        stage('Deploy to ECS') {
                    steps {
                        script {
                            withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws-credentials', accessKeyVariable: 'AWS_ACCESS_KEY_ID', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']]) {
                                // Register the new task definition and capture its revision number
                                def newRevision = sh(script: "aws ecs register-task-definition --family ${ECS_SERVICE} --container-definitions '[
                                    {
                                        \"name\": \"devopsimage\",
                                        \"image\": \"${ECR_REPOSITORY_URL}:${CUSTOM_TAG}\",
                                        \"essential\": true
                                    }
                                ]' --output json", returnStdout: true).trim()

                                // Extract the new revision number
                                newRevision = (newRevision =~ /"revision": (\d+),/)[0][1]

                                // Update the ECS service with the new revision
                                sh "aws ecs update-service --region ${AWS_REGION} --cluster ${ECS_CLUSTER} --service ${ECS_SERVICE} --task-definition ${ECS_SERVICE}:${newRevision}"

                                // Optionally, wait for the service to stabilize
                                // sh "aws ecs wait services-stable --region ${AWS_REGION} --cluster ${ECS_CLUSTER} --services ${ECS_SERVICE}"
                            }
                        }
                    }
                }

        // Add your other pipeline stages here, e.g., Deploy...
    }
}
