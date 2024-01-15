pipeline {
    agent any

    environment {
        ECR_REPOSITORY_URL = '992906191722.dkr.ecr.eu-west-3.amazonaws.com/devopsdemorepo'
        DOCKER_IMAGE_NAME = 'devopsdemoimage'
        DOCKER_IMAGE_TAG = 'latest'
        DOCKERFILE_PATH = 'Dockerfile'
        AWS_CREDENTIALS_ID = 'aws-ecr'
        AWS_REGION = 'eu-west-3'  // Assuming your ECR repository is in this region
        CUSTOM_TAG = "${env.BUILD_ID}_${new Date().format('yyyyMMddHHmmss')}"

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
                        sh "aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${ECR_REPOSITORY_URL}"
                        sh "docker build -t ${DOCKER_IMAGE_NAME}:${CUSTOM_TAG} ."
                        sh "docker tag ${DOCKER_IMAGE_NAME}:${CUSTOM_TAG} ${ECR_REPOSITORY_URL}:${CUSTOM_TAG}"
                        sh "docker push ${ECR_REPOSITORY_URL}:${CUSTOM_TAG}"
                   }
                }
            }
        }
        stage('Update Task Definition') {
            steps {
                script {
                    // Read the task definition file
                   // def taskDefinition = readJSON file: 'task_definition.json'
                    def taskDefinition = readJSON file: 'task_definition.json'
                    echo "Original task definition JSON: ${taskDefinition}"

                    // Update the image tag
                    taskDefinition.containerDefinitions[0].image = "${ECR_REPOSITORY_URL}:${CUSTOM_TAG}"

                    // Write the updated task definition back to the file
                    writeJSON file: 'task_definition.json', json: taskDefinition
                    echo "Updated task definition JSON: ${readFile('task_definition.json')}"

                }
            }
        }
        stage('Deploy to ECS') {
            steps {
                script {
                    withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws-credentials', accessKeyVariable: 'AWS_ACCESS_KEY_ID', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']]) {
                        def ecsCluster = 'devopsdemo'

                        // Register the new task definition and capture its revision number
                        def registerOutput = sh(script: "aws ecs register-task-definition --cli-input-json file://task_definition.json", returnStdout: true).trim()
                        def newRevision = (registerOutput =~ /"revision": (\d+),/)[0][1]

                        // Update the ECS service with the new revision
                        sh "aws ecs update-service --region ${AWS_REGION} --cluster ${ecsCluster} --service dev_service --task-definition ayoub_task_def:${newRevision}"

                        // Optionally, wait for the service to stabilize
                        // sh "aws ecs wait services-stable --region ${AWS_REGION} --cluster ${ecsCluster} --services your-service-name"
                    }
                }
            }
        }


        // Add your other pipeline stages here, e.g., Deploy...
    }
}
