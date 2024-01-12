pipeline {
    agent {
        label 'devops-agent'
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
                    echo 'test are working  '
                    def ecrRepositoryUrl = '992906191722.dkr.ecr.eu-west-3.amazonaws.com/devopsdemorepo'
                    def dockerImageName = 'devopsdemoimage'
                    def dockerImageTag = 'latest'
                    def dockerfilePath = 'Dockerfile'

                    // Build Docker image
                    sh "docker build -t ${ecrRepositoryUrl}/${dockerImageName}:${dockerImageTag} -f ${dockerfilePath} ."

                    // Push Docker image to AWS ECR using AWS credentials stored in Jenkins
                    withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', accessKeyVariable: 'AWS_ACCESS_KEY_ID', credentialsId: 'aws-credentials', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']]) {
                        // Tag Docker image for AWS ECR
                        sh "docker tag ${ecrRepositoryUrl}/${dockerImageName}:${dockerImageTag} ${ecrRepositoryUrl}/${dockerImageName}:${dockerImageTag}"

                        // Push Docker image to AWS ECR
                        sh "docker push ${ecrRepositoryUrl}/${dockerImageName}:${dockerImageTag}"
                    }
                }
            }
        }
    }
}
