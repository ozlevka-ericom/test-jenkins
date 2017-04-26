#!groovy
node {
   stage('Pull code') {
       echo 'Start fetch from github'
       git([url: 'https://github.com/EricomSoftwareLtd/SB.git', credentialsId: 'ozlevka-github'])
   }


   stage('Test Variable') {
       env.HELLOWORLD = 'Hello world'
       sh 'echo $HELLOWORLD'
   }

//    withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'beny-docker',
//                             usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
//             stage('Build Images') {
//                 sh 'docker logout && docker login -u $USERNAME -p $PASSWORD && docker pull securebrowsing/secure-remote-browser-ubuntu-base'
//                 echo 'Fetch ubuntu image success'
//                 sh 'docker build -t securebrowsing/secure-remote-browser-ubuntu-nodejs-xdummy Containers/Docker/secure-remote-browser-ubuntu-nodejs-xdummy'
//                 echo 'Build nodejs dummy success'
//                 sh 'docker build -t securebrowsing/shield-cef Containers/Docker/shield-cef'
//                 echo 'build cef image success'
//             }
//     }
}