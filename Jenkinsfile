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
        /* stage('Describe Task Definition') {
            steps {
                script {
                     withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws-credentials', accessKeyVariable: 'AWS_ACCESS_KEY_ID', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']]) {
                        def taskDefinition = sh(script: "aws ecs describe-task-definition --task-definition ${ECS_SERVICE}", returnStdout: true).trim()
                        echo "Task Definition Details: ${taskDefinition}"
                     }

                }
            }
        } */
        stage('Deploy to ECS') {
            steps {
                script {
                    withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws-credentials', accessKeyVariable: 'AWS_ACCESS_KEY_ID', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']]) {
                        // Register the new task definition and capture its revision number
                       // def registerOutput = sh(script: "aws ecs register-task-definition --cli-input-json file://task_definition.json", returnStdout: true).trim()

                       // def newRevision = (registerOutput =~ /"revision": (\d+),/)[0][1]
                        // echo "task revision ${newRevision}";
                        //def TASK_REVISION=sh(script : "aws ecs describe-task-definition --task-definition ${ECS_SERVICE} | egrep "revision" | tr "/" " " | awk '{print $2}' | sed 's/"$//'")
                         // def TASK_REVISION = sh(script: "aws ecs describe-task-definition --task-definition ${ECS_SERVICE} --query 'taskDefinition.revision' --output text", returnStdout: true).trim()


                        // Update the ECS service with the new revision
                        def ret = sh (script : "aws ecs update-service --region ${AWS_REGION} --cluster ${ECS_CLUSTER} --service ${ECS_SERVICE} --task-definition ayoub_task_def")


                    }
                }
            }
        }

        // Add your other pipeline stages here, e.g., Deploy...
    }
}
