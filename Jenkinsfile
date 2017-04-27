#!groovy
node {

   //def builder = comp.getBuilder()
   stage('Pull code') {
       git([url: 'https://github.com/EricomSoftwareLtd/SB.git', credentialsId: 'ozlevka-github', changelog: true])
       def changeLogSets = currentBuild.rawBuild.changeSets
       for (int i = 0; i < changeLogSets.size(); i++) {
            def entries = changeLogSets[i].items
            for (int j = 0; j < entries.length; j++) {
                def entry = entries[j]
                echo "${entry.commitId} by ${entry.author} on ${new Date(entry.timestamp)}: ${entry.msg}"
                def files = new ArrayList(entry.affectedFiles)
                for (int k = 0; k < files.size(); k++) {
                    def file = files[k]
                    echo "  ${file.editType.name} ${file.path}"
                }
            }
        }

       echo builder
   }

   stage('Check changes') {
       echo 'Test changes in components'
   }


   stage('Make params') {
       withEnv(["REPO_CWD=${pwd()}"]) {
            sh 'echo $REPO_CWD'
       }
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