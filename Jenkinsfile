pipeline {
    agent {
        label 'devops'
    }

    environment {
        AWS_REGION = 'eu-west-3'  // Assuming your ECR repository is in this region
        ECR_REPO = '992906191722.dkr.ecr.eu-west-3.amazonaws.com/devopsdemorepo'
    }

    stages {
        stage('Check ECR Connection') {
            steps {
                script {
                    withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws-credentials', accessKeyVariable: 'AWS_ACCESS_KEY_ID', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']]) {
                        // Explicitly check for AWS CLI installation
                        sh 'which aws || (echo "AWS CLI not installed. Please install it." && exit 1)'

                        // Test AWS CLI authentication
                        def ecrCheck = sh(script: "aws ecr describe-repositories", returnStatus: true)
                        if (ecrCheck == 0) {
                            echo "Successfully authenticated with ECR using AWS CLI"
                        } else {
                            error "Failed to authenticate with ECR using AWS CLI"
                        }
                    }
                }
            }
        }

        // Add your other pipeline stages here, e.g., Checkout code, Build image, Push image, Deploy...
    }
}
