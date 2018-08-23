# ZK Spring-Boot Demos

* zkspringboot-minimal-jar - A Minimal jar example
* zkspringboot-minimal-war - A Minimal war example
* zkspringboot-demo-jar - An example jar showing multiple features including ZATS testing

## Maven (using maven-wrapper)

build all demos

    ./mvnw clean package

run individual demo
    
    ./mvnw spring-boot:run -pl zkspringboot-minimal-jar
    ./mvnw spring-boot:run -pl zkspringboot-minimal-war
    ./mvnw spring-boot:run -pl zkspringboot-demo-jar

run each demo from its individual folder

    cd zkspringboot-demo-jar
    ../mvnw spring-boot:run
    
## Gradle (using gradle-wrapper)

build all demos
    
    ./gradlew clean build

run individual demo
    
    ./gradlew zkspringboot-minimal-jar:bootRun
    ./gradlew zkspringboot-minimal-war:bootRun
    ./gradlew zkspringboot-demo-jar:bootRun

run each demo from its individual folder

    cd zkspringboot-demo-jar
    ../gradlew bootRun
    
## Test urls

After successfull startup the applications can be accessed under the following urls

(for all demos)

http://localhost:8080 - a simple hello page

(for zkspringboot-demo-jar)

http://localhost:8080/mvvm - featuring basic mvvm based navigation

http://localhost:8080/resources - demonstrating how to access resources

http://localhost:8080/richlet/example - basic example richlet
