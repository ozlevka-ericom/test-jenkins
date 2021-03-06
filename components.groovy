

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


def testArray = [
        "Containers/Docker/shield-icap/web/AccessNow/resources/lang/dictionary.ko-kr.txt.gz",
        "Containers/Docker/shield-icap/web/AccessNow/src/accessnow.min.js.gz",
        "Containers/Docker/shield-icap/web/AccessNow/resources/lang/dictionary.ja-jp.txt.gz",
        "Containers/Docker/shield-icap/web/AccessNow/fonts/AvenirNextLTPro-Bold.ttf.gz",
        "Containers/Docker/shield-icap/web/AccessNow/resources/lang/dictionary.de.txt.gz",
        "Containers/Docker/shield-icap/web/AccessNow/resources/lang/dictionary.zh-cn.txt.gz",
        "Containers/Docker/shield-icap/web/AccessNow/resources/lang/dictionary.fr.txt.gz",
        "Containers/Docker/shield-icap/web/AccessNow/fonts/FontAwesome.otf.gz",
        "Containers/Docker/shield-icap/web/AccessNow/resources/lang/dictionary.ru.txt.gz",
        "Containers/Docker/shield-icap/web/AccessNow/resources/lang/dictionary.zh-tw.txt.gz",
        "Containers/Docker/shield-icap/web/AccessNow/fonts/AvenirNextLTPro-Bold.woff.gz",
        "Containers/Docker/shield-icap/web/AccessNow/resources/lang/dictionary.en-us.txt.gz",
        "Containers/Docker/shield-icap/web/AccessNow/fonts/fontawesome-webfont.eot.gz",
        "Containers/Docker/shield-icap/web/AccessNow/resources/lang/dictionary.it.txt.gz",
        "Containers/Docker/shield-icap/web/AccessNow/resources/lang/dictionary.list.txt.gz",
        "AccessNow/css/shield.css",
        "Containers/Docker/shield-icap/web/AccessNow/css/accessnow.min.css",
        "Containers/Docker/shield-icap/web/AccessNow/fonts/AvenirNextLTPro-Bold.svg.gz",
        "Containers/Docker/shield-icap/web/AccessNow/fonts/fontawesome-webfont.svg.gz",
        "Containers/Docker/shield-icap/web/AccessNow/fonts/AvenirNextLTPro-Bold.eot.gz",
        "Containers/Docker/shield-icap/web/AccessNow/resources/lang/dictionary.pt-br.txt.gz",
        "Containers/Docker/shield-icap/web/AccessNow/css/accessnow.min.css.gz",
        "Containers/Docker/shield-icap/web/AccessNow/fonts/fontawesome-webfont.woff2.gz",
        "Containers/Docker/shield-icap/web/AccessNow/fonts/fontawesome-webfont.ttf.gz",
        "Containers/Docker/shield-icap/web/AccessNow/resources/lang/dictionary.es-ar.txt.gz",
        "Containers/Docker/shield-icap/web/AccessNow/src/vendor.min.js.gz",
        "Containers/Docker/shield-icap/web/AccessNow/fonts/fontawesome-webfont.woff.gz",
        "Containers/Docker/shield-icap/web/AccessNow/resources/lang/12Dec2012ANChineseSimplified.lang.gz",
        "Containers/Docker/shield-icap/web/AccessNow/fonts/AvenirNextLTPro-Bold.min.svg.gz",
        "Containers/Docker/shield-configuration/Dockerfile"
]

def builder = new ComponentsBuilder()
testArray.each {
    builder.appendComponent(it)
}

println builder.executeBuild('CONSUL')
println builder.changesList()


