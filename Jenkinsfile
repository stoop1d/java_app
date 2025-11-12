pipeline {
  agent any

  environment {
    KUBECONFIG = '/var/lib/jenkins/microk8s.kubeconfig'   // уже на сервере
    IMAGE_NAME = "stoop1dk4/java_app"
    IMAGE_TAG = "latest" // можно заменить на ${env.BUILD_NUMBER}
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
        dir('') {
          sh 'mvn -B -DskipTests package'
        }
      }
    }

    stage('Build Docker & Push') {
      steps {
        script {
          // build in project dir, tag as latest
          docker.withRegistry('https://index.docker.io/v1/', 'dockerhub-creds') {
            def built = docker.build("${IMAGE_NAME}:${IMAGE_TAG}", ".")
            built.push()
          }
        }
      }
    }

    stage('Deploy to microk8s') {
      steps {
        sh '''
          # apply k8s in namespace apps
          kubectl --kubeconfig="${KUBECONFIG}" apply -f deployment.yml
          # wait a bit and show pods
          kubectl --kubeconfig="${KUBECONFIG}" -n apps get pods -l app=java-app -o wide
        '''
      }
    }
  }

  post {
    success { echo "Pipeline succeeded" }
    failure { echo "Pipeline failed" }
    always { echo "Done" }
  }
}

