pipeline {
    agent any

    environment {
        ECR_REPOSITORY_URL = '992906191722.dkr.ecr.eu-west-3.amazonaws.com/devopsdemorepo'
        DOCKER_IMAGE_NAME = 'devopsdemoimage'
        DOCKER_IMAGE_TAG = 'latest'
        DOCKERFILE_PATH = 'Dockerfile'
        AWS_CREDENTIALS_ID = 'aws-ecr'
        AWS_REGION = 'eu-west-3'  // Assuming your ECR repository is in this region
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

                    // Authenticate with ECR and push Docker image
                    withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws-credentials', accessKeyVariable: 'AWS_ACCESS_KEY_ID', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']]) {
                        sh "aws ecr get-login-password --region eu-west-3 | docker login --username AWS --password-stdin 992906191722.dkr.ecr.eu-west-3.amazonaws.com"
                        sh "docker build -t devopsdemorepo ."
                        sh "docker tag devopsdemorepo:latest 992906191722.dkr.ecr.eu-west-3.amazonaws.com/devopsdemorepo:latest"
                        sh "docker push 992906191722.dkr.ecr.eu-west-3.amazonaws.com/devopsdemorepo:latest"
                    }
                }
            }
        }
        stage('Deploy to ECS') {
            steps {
                script {
                    withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws-credentials', accessKeyVariable: 'AWS_ACCESS_KEY_ID', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']]) {
                        // Assume your ECS cluster name is 'your-ecs-cluster'
                        def ecsCluster = 'devopsdemo'

                        // Assume your task definition file is named 'task_definition.json'
                        def taskDefinitionFile = 'task_definition.json'


                        // Create or update ECS service with security group(s)
                        sh "aws ecs update-service --region ${AWS_REGION} --cluster ${ecsCluster} --service dev_service --task-definition ${taskDefinitionFile}"

                        // Optionally, wait for the service to stabilize
                        // sh "aws ecs wait services-stable --region ${AWS_REGION} --cluster ${ecsCluster} --services your-service-name"
                    }
                }
            }
        }

        // Add your other pipeline stages here, e.g., Deploy...
    }
}
