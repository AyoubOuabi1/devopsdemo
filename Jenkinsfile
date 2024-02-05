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
                        sh "docker tag ${DOCKER_IMAGE_NAME}:${CUSTOM_TAG} ${ECR_REPOSITORY_URL}:${CUSTOM_TAG}"
                        sh "docker push ${ECR_REPOSITORY_URL}:${CUSTOM_TAG}"
                    }
                }
            }
        }
       

       stage('Register & Deploy to ECS') {
           steps {
               script {
                   withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws-credentials', accessKeyVariable: 'AWS_ACCESS_KEY_ID', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']]) {
                       def taskDefJson = readFile file: 'task_definition.json'
                       //def taskDefJson = load('task_definition.json')
                       echo "start task definition ${taskDefJson}"
                      //  def jsonSlurper = new JsonSlurper()
                       taskDefJson = JsonSlurper().parseText(taskDefJson)

                          taskDefJson.containerDefinitions.each { containerDef ->
                                containerDef.image = "${ECR_REPOSITORY_URL}:${env.CUSTOM_TAG}"
                        }
                       echo "end writing image"

                       writeFile file: 'updated_task_definition.json', text: JsonOutput.toJson(taskDefJson)

                        echo "start saving task def"
                       def registerOutput = sh(script: "aws ecs register-task-definition --region ${AWS_REGION} --family ayoub_task_def --container-definitions  --cli-input-json file://updated_task_definition.json", returnStdout: true).trim()
                       echo "Registered updated task definition: ${registerOutput}"
                       def updateOutput = sh(script: "aws ecs update-service --region ${AWS_REGION} --cluster ${ECS_CLUSTER} --service ${ECS_SERVICE} --task-definition ${registerOutput}", returnStdout: true).trim()
                       echo "Updated ECS service: ${updateOutput}"
                       sh 'rm -f updated_task_definition.json'
                   }
               }
           }
       }
    }
}
