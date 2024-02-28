pipeline {
    agent any

    environment {
        ECR_REPOSITORY_URL = '992906191722.dkr.ecr.eu-west-3.amazonaws.com/devopsdemorepo'
        DOCKER_IMAGE_NAME = 'devopsdemoimage'
        DOCKER_IMAGE_TAG = 'latest'
        DOCKERFILE_PATH = 'Dockerfile'
        AWS_CREDENTIALS_ID = 'aws-ecr'
        AWS_REGION = 'eu-west-3' 
        CUSTOM_TAG = "${env.BUILD_ID}"
        ECS_CLUSTER = 'devopsdemo'
        ECS_SERVICE = 'demo_service'
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
                    sh "docker build -t ${DOCKER_IMAGE_NAME}:${CUSTOM_TAG} -f ${DOCKERFILE_PATH} ."

                    withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws-credentials', accessKeyVariable: 'AWS_ACCESS_KEY_ID', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']]) {
                        sh "aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${ECR_REPOSITORY_URL}"
                         // Tagging the image with custom tag
                         sh "docker tag ${DOCKER_IMAGE_NAME}:${CUSTOM_TAG} ${ECR_REPOSITORY_URL}:${CUSTOM_TAG}"

                            // Pushing the image with custom tag
                         sh "docker push ${ECR_REPOSITORY_URL}:${CUSTOM_TAG}"

                            // Tagging the image with latest
                         sh "docker tag ${DOCKER_IMAGE_NAME}:${CUSTOM_TAG} ${ECR_REPOSITORY_URL}:latest"

                            // Pushing the image with latest tag
                         sh "docker push ${ECR_REPOSITORY_URL}:latest"
                    }
                }
            }
        }
       

       stage('Deploy to ECS') {
                   steps {
                       script {
                           withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws-credentials', accessKeyVariable: 'AWS_ACCESS_KEY_ID', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']]) {

                                def ret = sh (script : "aws ecs update-service --region ${AWS_REGION} --cluster ${ECS_CLUSTER} --service ${ECS_SERVICE} --task-definition demo_task_def")


                           }
                       }
                   }
       }
    }
}
