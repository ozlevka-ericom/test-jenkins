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
        components["BROKER"] = "Containers/Docker/shield-broker"
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

def build_data = [
        "errors":[],
        "containers":[]
]


@NonCPS
def send_notification(data) {
    //def emails = ["Beny.Haddad@ericom.com", "lev.ozeryansky@ericom.com", "Erez.Pasternak@ericom.com", "shield-build@ericom.com"]
    //Uncomment before merge
    def emails = ["shield.build@ericom.com", "lev.ozeryansky@ericom.com"]
    def result = currentBuild.result
    def containers = data["containers"]

    if (result == null) {
        echo "No changes found"
    } else {

        if(result == "SUCCESS") {
            emailext(
                    to: emails.join(","),
                    subject: "SUCCESS: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
                    body: """<p>SUCCESS: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
                                <p>List of Containers built and pushed: ${containers}</p>
                                <p>Tag: ${tag}</p>
                                <p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${
                        env.BUILD_NUMBER
                    }]</a>&QUOT;</p> """//,
                    //recipientProviders: [[$class: 'RequesterRecipientProvider']]
            )
        } else {
            def errors = data["errors"]
            def log = currentBuild.rawBuild.log.replaceAll(/\n/, '<br/>')
            emailext(
                    to: emails.join(","),
                    subject: "FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
                    body: """<p>FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
                                <p>Errors: ${errors}</p>
                                <p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER
                    }]</a>&QUOT;</p>""",
                    attachLog: true,
                    compressLog: true
            )
        }

    }
}

try {
    node {

       def builder = new ComponentsBuilder()
       def list_of_containers = []
       def startDate = new Date()
       def tag = startDate.format('yyMMdd-HH.mm')
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
                   def res = build job:'run-shield-tests', propagate: false
                   def strRes = res.getResult()
                   echo "Test system result: ${strRes}"
                   if(!strRes.equals('SUCCESS')) {
                       echo 'Start fetch log'
                       def log = res.getRawBuild().getLog(200).join('\n')
                       echo log
                       throw new Exception('Test stage failed')
                   }
               }

               stage('Push Images') {
                   for(i = 0; i < list_of_changes.size(); i++) {
                       def k = list_of_changes[i]
                       def buildPath = builder.components[k]
                       sh "cd ${buildPath} && ./_upload.sh ${tag}-${env.BUILD_NUMBER}"
                       echo "Param ${k} upload success"
                       list_of_containers << "${buildPath} by tag ${tag}-${env.BUILD_NUMBER}"
                   }

                   echo "List of build containers: ${list_of_containers}"
               }

               currentBuild.result = "SUCCESS"
               build_data["containers"] = list_of_containers
            }
       } else {
           echo 'Nothing changed'
       }
    }
} catch (Exception ex) {
    currentBuild.result = "FAILED"
    build_data["errors"] << ex.toString()
} finally {
    send_notification(build_data)
}
