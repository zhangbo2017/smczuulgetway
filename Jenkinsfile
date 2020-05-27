pipeline {
  agent none
  environment {
    DOCKHUB_USERNAME = '920018225'
  }
  stages {
    stage('maven Build src') {
      agent {
        docker {
          image 'maven:3-alpine'
          args '-v /root/.m2:/root/.m2'
        }
      }
      steps {
      // maven build src to get the jar file in target folder
        sh 'mvn -B -DskipTests clean package'
      }
    }

    stage('docker build & push image on build docker/build server') {
      agent any
      steps {
        // docker stop/rm older containers: remove only there are containers found
        script {
          def REMOVE_FLAG = sh(returnStdout: true, script: "docker container ls -aq --filter name=.*smc-zuul-ctn.*") != ""
          echo "REMOVE_FLAG: ${REMOVE_FLAG}"
          if(REMOVE_FLAG){
            // sh 'docker container ls -aq --format {{.ID}} --filter name=smc-zuul-ctn | xargs docker container rm -f'
            // sh 'docker container rm -f $(docker container ls -aq --format {{.ID}} --filter name=.*smc-zuul-ctn.*)'
            sh 'docker container rm -f $(docker container ls -aq --filter name=.*smc-zuul-ctn.*)'
          }
        }

        // docker rmi old images before build: remove only there are images found
        script {
          def REMOVE_FLAG = sh(returnStdout: true, script: "docker image ls -q *${DOCKHUB_USERNAME}/smc-zuul-srv*") != ""
          echo "REMOVE_FLAG: ${REMOVE_FLAG}"
          if(REMOVE_FLAG){
            // sh 'docker image ls --format {{.ID}} *${DOCKHUB_USERNAME}/smc-zuul-srv* | xargs docker image rm -f'
            // sh 'docker image rm -f $(docker image ls --format {{.ID}} *${DOCKHUB_USERNAME}/smc-zuul-srv*)'
            sh 'docker image rm -f $(docker image ls -q *${DOCKHUB_USERNAME}/smc-zuul-srv*)'
          }
        }

        // solution 1: can login successfully, but id/pw will be exposed
        // script {
          // docker.withRegistry(registry-server, Credentials_ID)
          // docker.withRegistry('https://index.docker.io/v1/', 'DockerHub_${DOCKHUB_USERNAME}') {
          //   def customImage = docker.build("${DOCKHUB_USERNAME}/smc-zuul-srv:latest", '-f Dockerfile .')
          //   /* Push the image to the docker hub Registry */
          //   customImage.push('latest')
          // }
        // }
        // soution 2: it's good to use without any expose..
        withCredentials([usernamePassword(credentialsId: "DockerHub_${DOCKHUB_USERNAME}", usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
          sh 'docker login -u $USERNAME -p $PASSWORD'
          sh 'docker image build -t ${DOCKHUB_USERNAME}/smc-zuul-srv:latest .'
          sh 'docker push ${DOCKHUB_USERNAME}/smc-zuul-srv:latest'
        }
      }
    }

    stage('clean workspace') {
      agent any
      steps {
        // clean workspace after job finished
        cleanWs()
      }
    }
  }
}