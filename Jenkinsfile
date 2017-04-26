#!groovy
node {
   stage('Pull code') {
       echo 'Start fetch from github'
       git([url: 'https://github.com/EricomSoftwareLtd/SB.git', credentialsId: 'ozlevka-github'])
   }

   withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'beny-docker',
                            usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
            sh 'docker logout && docker login -u $USERNAME -p $PASSWORD && docker pull securebrowsing/secure-remote-browser-ubuntu-base'
    }
}