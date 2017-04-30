#!groovy
class ComponentsBuilder implements java.io.Serializable {
    def components = [:]
    def changedComponents = [:]

    ComponentsBuilder() {
        components["CEF"] = "Containers/Docker/shield-cef"
        components["ICAP"] = "Containers/Docker/shield-icap"
        components["ELK"] = "Containers/Docker/shield-elk"
        components["UBUNTU"] = "Containers/Docker/secure-remote-browser-ubuntu-base"
        components["NODEJS"] = "Containers/Docker/secure-remote-browser-ubuntu-nodejs-xdummy"
        components["CONSUL"] = "Containers/Docker/shield-configuration"
        components["TEST"] = "Containers/Docker/shield-test"
        components["SQUID"] = "Containers/Docker/shield-squid-alpine"
    }

    def findComponent(String path) {
        def lst = components.keySet() as List
        for(def key in lst) {
            if(path.startsWith(components[key].toString())) {
                return key
            }
        }

        return null
    }

    def appendComponent(String path) {
        def k = findComponent(path)
        if(k) {
            changedComponents[k] = true
        }
    }

    def executeBuild(String component) {
        changedComponents.containsKey(component)
    }

    def changesList() {
        changedComponents.keySet() as List
    }
}

node {

   def builder = new ComponentsBuilder()
   stage('Pull code') {
       git([url: 'https://github.com/EricomSoftwareLtd/SB.git', credentialsId: '451bb7d7-5c99-4d21-aa3a-1c6a1027406b', changelog: true])
       def changeLogSets = currentBuild.rawBuild.changeSets
       for (int i = 0; i < changeLogSets.size(); i++) {
            def entries = changeLogSets[i].items
            for (int j = 0; j < entries.length; j++) {
                def entry = entries[j]
                //echo "${entry.commitId} by ${entry.author} on ${new Date(entry.timestamp)}: ${entry.msg}"
                def files = new ArrayList(entry.affectedFiles)
                for (int k = 0; k < files.size(); k++) {
                    def file = files[k]
                    //echo "  ${file.editType.name} ${file.path}"
                    builder.appendComponent file.path
                }
            }
       }
   }

   if (builder.changedComponents.size() > 0) {
       stage('Make params') {
           withEnv(["REPO_CWD=${pwd()}"]) {
                sh 'echo $REPO_CWD'
           }
       }

       withCredentials([[$class          : 'UsernamePasswordMultiBinding', credentialsId: 'beny-docker',
                             usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
                stage('Login to docker') {
                    sh 'docker logout && docker login -u $USERNAME -p $PASSWORD'
                }

                def list_of_changes = builder.changesList()
                stage('Build Images') {

                    for(i = 0; i < list_of_changes.size(); i++) {
                        def k = list_of_changes[i]
                        def buildPath = builder.components[k]
                        sh "cd ${buildPath} && ./_build.sh"
                        echo "Param ${k} build success"
                    }

                }

                stage('Test System') {
                    try {
                        echo 'Run unitests....'
                    } catch (err) {
                        throw err
                    }
                }

                stage('Push Images') {
                    for(i = 0; i < list_of_changes.size(); i++) {
                        def k = list_of_changes[i]
                        def buildPath = builder.components[k]
                        sh "cd ${buildPath} && ./_upload.sh"
                        echo "Param ${k} upload success"
                    }
                }
        }
   } else {
       echo 'Nothing changed'
   }
}
