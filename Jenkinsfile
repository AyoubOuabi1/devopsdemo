pipeline {
    agent {
        label 'devops'
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
                    echo 'test are working bkk n'
                    def ecrRepositoryUrl = '992906191722.dkr.ecr.eu-west-3.amazonaws.com/devopsdemorepo'
                    def dockerImageName = 'devopsdemoimage'
                    def dockerImageTag = 'latest'
                    def dockerfilePath = 'Dockerfile'

                    // Build Docker image
                    sh "docker build -t ${ecrRepositoryUrl}/${dockerImageName}:${dockerImageTag} -f ${dockerfilePath} ."

                    // Push Docker image to AWS ECR using AWS credentials stored in Jenkins
                    withAws(credentials: 'aws-credentials', region: 'eu-west-3') {
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
