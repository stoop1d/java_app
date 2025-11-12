pipeline {
  agent any

  environment {
    KUBECONFIG = '/var/lib/jenkins/microk8s.kubeconfig'
    IMAGE_NAME = "stoop1dk4/java_app"
    IMAGE_TAG  = "latest" // можно заменить на "${env.BUILD_NUMBER}" для версионирования
  }

  stages {
    stage('Checkout') {
      steps {
        git branch: 'main',
            url: 'https://github.com/stoop1d/java_app.git',
            credentialsId: 'github-creds'
      }
    }

    stage('Build (Maven)') {
      steps {
        // workspace root is java_app repo root (pom.xml here)
        sh 'mvn -B -DskipTests package'
      }
    }

    stage('Build & Push Docker') {
      steps {
        script {
          // build from repo root (Dockerfile in repo root)
          docker.withRegistry('https://index.docker.io/v1/', 'dockerhub-creds') {
            def img = docker.build("${IMAGE_NAME}:${IMAGE_TAG}", ".")
            img.push()
          }
        }
      }
    }

    stage('Deploy to Kubernetes') {
      steps {
        // apply manifests located in ./k8s
        sh '''
          kubectl --kubeconfig="${KUBECONFIG}" apply -f k8s/deployment.yml
          kubectl --kubeconfig="${KUBECONFIG}" apply -f k8s/service.yml
          # ingress optional
          if [ -f k8s/ingress.yml ]; then
            kubectl --kubeconfig="${KUBECONFIG}" apply -f k8s/ingress.yml || true
          fi
          kubectl --kubeconfig="${KUBECONFIG}" -n apps get pods -l app=java-app -o wide
        '''
      }
    }
  }

  post {
    success { echo "Pipeline succeeded" }
    failure { echo "Pipeline failed" }
    always { echo "Pipeline finished" }
  }
}

