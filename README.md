# ZK - Spring Boot
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

*(Adapted from Dirk's amazing [zk-spring-boot-starter](https://github.com/dirkdeyne/zk-spring-boot-starter) - Thanks a lot for the input and ideas!)*

## Getting started

* Add a single dependency (as in [zkspringboot-minimal-jar/pom.xml](zkspringboot-demos/zkspringboot-minimal-jar/pom.xml##L28-L32))
* Place your zul files below [src/main/resources/web](zkspringboot-demos/zkspringboot-minimal-jar/src/main/resources/web/zul)
* Create a spring controller or define a `zk.homepage` parameter in [application.properties](zkspringboot-demos/zkspringboot-minimal-jar/src/main/resources/application.properties)
* or just have a look into the [demos](zkspringboot-demos)

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

property | default | example(s) | description
-------- | ------- | ------- | -----------
`zk.springboot-packaging`      | `jar`   | `war`/`jar`       | package as jar or war file
`zk.homepage`                  | null    | `home`/`main`     | when set will setup a @GetMapping for "/" to return the configured view-name
`zk.zul-view-resolver-enabled` | `true`  | `true`/`false`    | enable/disable InternalViewResolver for zul files
`zk.zul-view-resolver-prefix`  | empty   | `/zul`            | prefix prepended to a view name (i.e. a folder inside the web resource package on the classpath)
`zk.zul-view-resolver-suffix`  | `.zul`  | `.zul`/`.zhtml`   | usually `.zul` does what you need
`zk.richlet-filter-mapping`    | null    | `/richlets/*`     | filter-mapping string as the basepath for richlets
`zk.websockets-enabled=true`   | `true`  | `true`/`false`    | enable/disable websockets (available in ZK-EE)
`zk.servlet3-push-enabled=true`| `true`  | `true`/`false`    | enable/disable servlet3 CometServerPush (available in ZK-EE)
`zk.update-uri`                | `/zkau` | `/mypath`         | configure servlet path for ZK's Update Engine (rarely used)
`zk.resource-uri`              | null    | `/zkres`          | configure/enable separate servlet path for ZK's Resource Engine (since ZK 9.5.0)

### zk.homepage
Shortcut configuration to enable a Spring MVC Controller with a @GetMapping for the root path '/' returning the configured view name. (disabled by default).
The default zul-view-resolver config will prepend the the servlet mapping for ZK's update engine (default `/zkau`) and append the `.zul`-extension to locate the zul file in your application.

### zk.zul-view-resolver-*
Defines a simple InternalViewResolver resolving view names to zul files inside the class-web package (not required but still possible for `war` packaging)

The defaults above have the following effect, given the get mapping:
```java
	@GetMapping("/demo")
	public String demo() {
		return "views/demo";
	}
```
Resolves to `/zkau/web/views/demo.zul`
ZK's UpdateServlet will locate the file on the classpath e.g. in `src/main/resources/web/views/demo.zul`
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
Because a breakpoint doesn't stop when running with Maven `springboot:run`, you can run an application with `@SpringBootApplication` class as a Java application in debug mode. 
