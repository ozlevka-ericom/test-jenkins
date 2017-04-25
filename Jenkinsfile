#!groovy
node {
   stage('Pull code') {
       echo 'Start fetch from github'
       git([url: 'https://github.com/EricomSoftwareLtd/SB.git', credentialsId: 'ozlevka-github'])
   }
   
   stage("Buid Images") {
        withCredentials([usernameColonPassword(credentialsId: 'ozlevka-github', variable: 'USERPASS')]) {
            echo '${env.USERPASS}'
        }
   }
}