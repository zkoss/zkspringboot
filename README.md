# ZK - Spring Boot Starter
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

* [zkspringboot-starter](zkspringboot-starter) : the starter project that helps you use spring-boot with zk easily.
* [zkspringboot-autoconfig](zkspringboot-autoconfig) : apply default configuration in spring-boot for ZK.
* [zkspringboot-demos](zkspringboot-demos) : contains demo projects.


[Spring Boot 3.0](https://spring.io/blog/2022/05/24/preparing-for-spring-boot-3-0) makes use of Jakarta EE 9 APIs (`jakarta.*`) instead of EE 8 (`javax.*`).

*(Adapted from Dirk's amazing [zk-spring-boot-starter](https://github.com/dirkdeyne/zk-spring-boot-starter) - Thanks a lot for the input and ideas!)*
## Features
* automatically apply default configurations in spring-boot for ZK 
* zul view resolver
* zk-specific properties for configuration

## Getting started

* Add a single dependency (as in [zkspringboot-minimal-jar/pom.xml](zkspringboot-demos/zkspringboot-minimal-jar/pom.xml##L28-L32))
* Place your zul files below [src/main/resources/web](zkspringboot-demos/zkspringboot-minimal-jar/src/main/resources/web/zul)
* Create a spring controller or define a `zk.homepage` parameter in [application.properties](zkspringboot-demos/zkspringboot-minimal-jar/src/main/resources/application.properties)
* or just have a look into the [demos](zkspringboot-demos)
* when using `jar` packaging, including a file is different, please see [Application_structure](https://www.zkoss.org/wiki/ZK_Installation_Guide/Quick_Start/Create_and_Run_Your_First_ZK_Application_with_Spring_Boot#Application_structure)

## Configuration options (for Spring Boot style application.properties)

Defaults as in [ZkProperties.java](zkspringboot-autoconfig/src/main/java/org/zkoss/zkspringboot/ZkProperties.java)
```
zk.springboot-packaging=jar

zk.homepage=
zk.zul-view-resolver-enabled=true
zk.zul-view-resolver-prefix=
zk.zul-view-resolver-suffix=.zul

zk.richlet-filter-mapping=

zk.websockets-enabled=true
zk.servlet3-push-enabled=true
zk.update-uri=/zkau
```

property                                        | default | example(s)      | description
----------------------------------------------- |---------|-----------------| -----------
`zk.springboot-packaging`                       | `jar`   | `war`/`jar`     | package as jar or war file
`zk.homepage`                                   | null    | `home`/`main`   | when set will setup a @GetMapping for "/" to return the configured view-name
`zk.zul-view-resolver-prefix`                   | empty   | `/zul`          | prefix prepended to a view name (i.e. a folder inside the web resource package on the classpath)
`zk.zul-view-resolver-enabled`                  | `true`  | `true`/`false`  | enable/disable InternalViewResolver for zul files. See details below. 
`zk.zul-view-resolver-suffix`                   | `.zul`  | `.zul`/`.zhtml` | usually `.zul` does what you need
`zk.richlet-filter-mapping`                     | null    | `/richlets/*`   | filter-mapping string as the basepath for richlets
`zk.websockets-enabled=true`                    | `true`  | `true`/`false`  | enable/disable websockets (available in ZK-EE)
`zk.servlet3-push-enabled=true`                 | `true`  | `true`/`false`  | enable/disable servlet3 CometServerPush (available in ZK-EE)
`zk.update-uri`                                 | `/zkau` | `/mypath`       | configure servlet path for ZK's Update Engine (rarely used)
`zk.resource-uri`                               | null    | `/zkres`        | configure/enable separate servlet path for ZK's Resource Engine (since ZK 9.5.0)
`zk.stateless-dispatcher-richlet-filter-mapping`| null    | `/*`            | url-pattern for `org.zkoss.stateless.ui.http.DispatcherRichletFilter`. since 3.2.3
`zk.stateless-dispatcher-richlet-filter-base`   | null    | `myrichlet`     | base package of your `StatelessRichlet`. since 3.2.3
`zk.client-mvvm-listener-enabled`               | `false` | `true`          | if `true`, add a listener `org.zkoss.clientbind.BinderPropertiesRenderer` for client mvvm. since 3.2.3
`zk.client-mvvm-globally-enabled`               | `false` | `true`          | if `true`, set the library property `org.zkoss.bind.defaultComposer.class` with `org.zkoss.clientbind.ClientBindComposer`. This enables client MVVM on the whole application. since 3.2.3

### zk.homepage
Shortcut configuration to enable a Spring MVC Controller with a @GetMapping for the root path '/' returning the configured view name. (disabled by default).
The default zul-view-resolver config will prepend the the servlet mapping for ZK's update engine (default `/zkau`) and append the `.zul`-extension to locate the zul file in your application.

### zk.zul-view-resolver-*
Defines a simple InternalViewResolver resolving view names to zul files inside the class-web package (not required but still possible for `war` packaging).

The default root path that the zul view resolver looks for is:
* war packaging
`src/main/webapp`
* jar packaging
`src/main/resources/web/` (it's [zk's classpath web resource path](https://www.zkoss.org/wiki/ZK_Developer%27s_Reference/UI_Composing/ZUML/Include_a_Page#Classpath_Web_Resource_Path))

For example, given the get mapping:
```java
	@GetMapping("/demo")
	public String demo() {
		return "views/demo";
	}
```
Resolves to `/zkau/web/views/demo.zul`

ZK's DHtmlUpdateServlet will locate the file on the classpath e.g. in `src/main/resources/web/views/demo.zul`
A configured prefix e.g. `/myprefix` will be inserted before the view name: `/zkau/web[/myprefix]/views/demo.zul`

### zk.richlet-filter-mapping
Enables [ZK's RichletFilter](https://www.zkoss.org/wiki/ZK_Developer's_Reference/UI_Composing/Richlet#Turn_on_Richlet). The value has to be a servlet filter mapping such as: `/richlet/*`
Requires additional [richlet-mappings configured in zk.xml](https://www.zkoss.org/wiki/ZK_Configuration_Reference/zk.xml/The_richlet-mapping_Element).

### zk.resource-uri 
Registers and enables [ZK's Resource Engine](https://www.zkoss.org/wiki/ZK_Configuration_Reference/web.xml/ZK_Resource_Engine).
When packging as war file this will also set the necessary [init-parameter for the ZK Loader Servlet](https://www.zkoss.org/wiki/ZK_Configuration_Reference/web.xml/ZK_Loader#The_Initial_Parameters)

## Using springboot-devtools (restart)

When using the restart feature of [spring-boot-devtools](https://docs.spring.io/spring-boot/docs/2.4.4/reference/html/using-spring-boot.html#using-boot-devtools) make sure to include the ZK jars in the restart process
by [customizing the Restart Classloader](https://docs.spring.io/spring-boot/docs/2.4.4/reference/html/using-spring-boot.html#using-boot-devtools-customizing-classload).

Create a file on the classpath:

src/main/resources/**META-INF/spring-devtools.properties**

    restart.include.zklibs=/z[\\w]+-[\\w\\d-\.]+\.jar

This regex will match all jar files matching the expression e.g. `zk-9.5.1.jar` or `zul-9.5.1.jar` etc.

### Reload zul change
add `spring.profiles.active=dev` in `application.properties`. After modifying a zul, re-build it, then your browser can load the latest zul.
See [Triggering a restart](https://docs.spring.io/spring-boot/docs/2.4.4/reference/html/using-spring-boot.html#using-boot-devtools-restart)


## Debug Spring Boot Application
### jar
Because a breakpoint doesn't stop when running with Maven `springboot:run`, you can run an application with `@SpringBootApplication` class as a Java application in debug mode.
### war
1. Enable debug mode

`mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=*:5005"`
2. Attach a Debugger in Your IDE