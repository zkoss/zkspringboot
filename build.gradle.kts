/* update version string in all required places
* https://ant.apache.org/manual/Tasks/replaceregexp.html
*/
tasks.register("updateVersion") {
    doLast {
        ant.withGroovyBuilder {
            "replaceregexp"("match" to "2.5.12", "replace" to "2.7.7", "flags" to "g", "byline" to true) {
                "fileset"("dir" to "zkspringboot-autoconfig") {
                    "include"("name" to "pom.xml")
                }
                "fileset"("dir" to "zkspringboot-starter") {
                    "include"("name" to "pom.xml")
                }
                "fileset"("dir" to ".") {
                    "include"("name" to "version")
                }
                "fileset"("dir" to "zkspringboot-demos") {
                    "include"("name" to "**/pom.xml")
                }
            }
        }
    }
}