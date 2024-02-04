pipeline {
    agent any

    environment {
        ECR_REPOSITORY_URL = '992906191722.dkr.ecr.eu-west-3.amazonaws.com/devopsdemorepo'
        DOCKER_IMAGE_NAME = 'devopsdemoimage'
        DOCKER_IMAGE_TAG = 'latest'
        DOCKERFILE_PATH = 'Dockerfile'
        AWS_CREDENTIALS_ID = 'aws-ecr'
        AWS_REGION = 'eu-west-3' 
        CUSTOM_TAG = "${env.BUILD_ID}_${new Date().format('yyyyMMddHHmmss')}"
        ECS_CLUSTER = 'devopsdemo'
        ECS_SERVICE = 'demo_service'
    }


    stages {
       /* stage('SonarQube Analysis') {
           steps {
            script{
                def mavenHome = tool 'Maven'
               withSonarQubeEnv('sonarQube') {
                        sh "${mavenHome}/bin/mvn clean verify sonar:sonar -Dsonar.projectKey=devopsdemo -Dsonar.projectName='devopsdemo'"

                    }
               }
          }
        }
          stage('Quality Gate') {
            steps {
                timeout(time: 1, unit: 'HOURS') {
                    script {
                        def qg = waitForQualityGate()
                        if (qg.status != 'OK') {
                            error "Pipeline aborted due to Quality Gate failure: ${qg.status}"
                        } else {
                            echo "Quality Gate passed - continuing with the pipeline"
                        }
                    }
                }
            }
       }*/

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
       
        /* stage('Deploy to ECS') {
            steps {
                script {
                    withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws-credentials', accessKeyVariable: 'AWS_ACCESS_KEY_ID', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']]) {
                       
                        def ret = sh (script : "aws ecs update-service --region ${AWS_REGION} --cluster ${ECS_CLUSTER} --service ${ECS_SERVICE} --task-definition ayoub_task_def")


                    }
                }
            }
        } */
        stage('Deploy to ECS') {
            steps {
                script {
                    withCredentials([
                        [$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws-credentials', accessKeyVariable: 'AWS_ACCESS_KEY_ID', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']
                    ]) {
                        // Read the existing task definition JSON from file
                        def taskDefinitionFile = 'task-def.json'
                        def existingTaskDefinition = readFile(taskDefinitionFile)
                        def taskDefJson = evaluateJson(existingTaskDefinition)

                        // Update the image field with the new image from Jenkins
                        taskDefJson.containerDefinitions[0].image = "${ECR_REPOSITORY_URL}:${CUSTOM_TAG}"

                        // Save the updated task definition JSON to a file
                        def updatedTaskDefinitionFile = 'updated_task_definition.json'
                        writeFile file: updatedTaskDefinitionFile, text: JsonOutput.toJson(taskDefJson)

                        // Register the updated task definition
                        def registerTaskDefinitionCmd = "aws ecs register-task-definition --region ${AWS_REGION} --family ayoub_task_def --container-definitions file://${updatedTaskDefinitionFile}"
                        def registerTaskDefinitionOutput = sh(script: registerTaskDefinitionCmd, returnStdout: true).trim()
                        echo "Registered updated task definition: ${registerTaskDefinitionOutput}"

                        // Update the ECS service with the new task definition
                        def updateServiceCmd = "aws ecs update-service --region ${AWS_REGION} --cluster ${ECS_CLUSTER} --service ${ECS_SERVICE} --task-definition ayoub_task_def"
                        def updateServiceOutput = sh(script: updateServiceCmd, returnStdout: true).trim()
                        echo "Updated ECS service: ${updateServiceOutput}"

                        // Clean up: Delete the temporary updated task definition file
                        sh "rm -f ${updatedTaskDefinitionFile}"
                    }
                }
            }
       }
    }
}
