#!groovy
node {
   stage('Pull code') {
       echo 'Start fetch from github'
       git([url: 'https://github.com/EricomSoftwareLtd/SB.git', credentialsId: 'ozlevka-github'])
   }

   docker.withRegistry('https://hub.docker.com', 'beny-docker') {
        stage('Pull ubuntu Image') {
            def ubuntu = docker.image('ubuntu:latest')
            ubuntu.pull()
            echo 'Image pulled'
        }
    }
}