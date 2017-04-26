#!groovy
node {
   stage('Pull code') {
       echo 'Start fetch from github'
       git([url: 'https://github.com/EricomSoftwareLtd/SB.git', credentialsId: 'ozlevka-github'])
   }
   
   stage("Buid Images") {
        docker.withRegistry('https://hub.docker.com', 'beny-docker') {
            stage('Pull ubuntu Image') {
                def ubuntu = docker.image('securebrowsing/secure-remote-browser-ubuntu-base:latest')
                ubuntu.pull()
                echo 'Ubuntu image arrive'
            }
        }
   }
}