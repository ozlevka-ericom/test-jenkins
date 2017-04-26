#!groovy
node {
   stage('Pull code') {
       git([url: 'https://github.com/EricomSoftwareLtd/SB.git', credentialsId: 'ozlevka-github'])
   }

   stage('Check changes') {
       echo 'Test changes in components'
   }


   stage('Make params') {
       env.REPO_CWD = pwd()
       sh 'echo $REPO_CWD'
   }

   withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'beny-docker',
                            usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
            stage('Build Images') {
                sh 'docker logout && docker login -u $USERNAME -p $PASSWORD && docker pull securebrowsing/secure-remote-browser-ubuntu-base'
                echo 'Fetch ubuntu image success'
                sh 'docker build -t securebrowsing/secure-remote-browser-ubuntu-nodejs-xdummy Containers/Docker/secure-remote-browser-ubuntu-nodejs-xdummy'
                echo 'Build nodejs dummy success'
                sh 'cd Containers/Docker/shield-cef && ./_build.sh'
                echo 'build cef image success'
            }

            stage('Test System') {
                try {
                    echo 'Run unitests....'
                } catch (err) {
                    throw err
                }                
            }

            stage('Push Images') {
                echo 'Push images'
            }
    }
}