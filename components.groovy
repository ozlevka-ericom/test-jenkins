

class ComponentsBuilder {
    def components = [:]
    def changedComponents = []

    ComponentsBuilder() {
        components["CEF"] = "Containers/Docker/shield-cef"
        components["ICAP"] = "Containers/Docker/shield-icap"
        components["ELK"] = "Containers/Docker/shield-elk"
        components["UBUNTU"] = "Containers/Docker/secure-remote-browser-ubuntu-base"
        components["NODEJS"] = "Containers/Docker/secure-remote-browser-ubuntu-nodejs-xdummy"
    }

    def findComponent(String path) {
        components.find {
            path.startsWith(it.value.toString())
        }?.key
    }
}

def getBuilder() {
    return new ComponentsBuilder()
}
return this