pipeline {
    agent any

    environment {
        // kubeconfig не как credential, а просто как файл на сервере
        KUBECONFIG = '/var/lib/jenkins/microk8s.kubeconfig'
        IMAGE_NAME = "stoop1dk4/java_app"
        IMAGE_TAG  = "latest"
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/stoop1d/java_app.git',
                    credentialsId: 'github-creds'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh '''
                    echo "Building Docker image..."
                    docker build -t $IMAGE_NAME:$IMAGE_TAG .
                '''
            }
        }

        stage('Docker Login') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-creds',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh '''
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                    '''
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                sh '''
                    docker push $IMAGE_NAME:$IMAGE_TAG
                '''
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                sh '''
                    echo "Applying Kubernetes manifests..."
                    microk8s.kubectl --kubeconfig=$KUBECONFIG apply -f k8s/deployment.yml
                    microk8s.kubectl --kubeconfig=$KUBECONFIG apply -f k8s/service.yml
                '''
            }
        }
    }

    post {
        always {
            sh 'docker logout || true'
        }
    }
}

