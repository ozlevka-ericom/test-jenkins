#!groovy
/*class Container implements java.io.Serializable {
    String path;
    String containerName;
    List<String> dependencies

    Container() {}

    Container(
            String path,
            String containerName) {
        this.path = path;
        this.containerName = containerName
    }

    def setDependencies(List<String> deps) {
        dependencies = deps;
    }
}*/

class ComponentsBuilder implements java.io.Serializable {
    def components = [:]
    def changedComponents = [:]

    ComponentsBuilder() {
        components["CONSUL"] = "Containers/Docker/shield-configuration"
        components["ADMIN"] = "Containers/Docker/shield-admin"
        components["CONSUL-ADMIN"] = "Containers/Docker/shield-admin-orig"
        components["UBUNTU"] = "Containers/Docker/secure-remote-browser-ubuntu-base"
        components["XDUMMY"] = "Containers/Docker/secure-remote-browser-ubuntu-nodejs-xdummy"
        components["CEF"] = "Containers/Docker/shield-cef"
        components["PROXY"] = "Containers/Docker/shield-squid-alpine"
        components["ICAP"] = "Containers/Docker/shield-icap"
        components["ELK"] = "Containers/Docker/shield-elk"
        components["UBUNTU"] = "Containers/Docker/secure-remote-browser-ubuntu-base"
        components["NODEJS"] = "Containers/Docker/secure-remote-browser-ubuntu-nodejs-xdummy"
        //components["TEST"] = "Containers/Docker/shield-test"
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

@NonCPS
def send_notification(containers) {
    //def emails = ["Beny.Haddad@ericom.com", "lev.ozeryansky@ericom.com", "Erez.Pasternak@ericom.com", "shield-build@ericom.com"]
    //Uncomment before merge
    def emails = ["lev.ozeryansky@ericom.com"]
    def result = currentBuild.result

    emailext (
            to: emails.join(","),
            subject: "SUCCESS: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
            body: """<p>SUCCESS: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
                            <p>List of Containers built and pushed: : ${containers}</p>
                            <p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>"""//,
            //recipientProviders: [[$class: 'RequesterRecipientProvider']]
    )
}

node {

   def builder = new ComponentsBuilder()
   def list_of_containers = []
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
                try {
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
                            //sh "cd ${buildPath} && ./_upload.sh"
                            echo "Param ${k} upload success"
                            list_of_containers << buildPath
                        }

                        echo "List of build containers: ${list_of_containers}"
                    }
                } catch (Exception ex) {
                    echo "Exception!!! ${ex}"
                } finally {
                    stage('Send Email') {
                        send_notification(list_of_containers)
                    }
                }


        }
   } else {
       echo 'Nothing changed'
   }
}
