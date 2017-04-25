#!groovy
node {
   stage('Pull code') {
       echo 'Start fetch from github'
       git([url: 'https://github.com/EricomSoftwareLtd/SB.git', credentialsId: 'ozlevka-github'])
   }
   
   stage("Buid Images") {
        withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'ozlevka-github',
                            usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
                //available as an env variable, but will be masked if you try to print it out any which way
                sh 'echo $PASSWORD'
                echo "${env.USERNAME}"
        }
   }
}