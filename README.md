# ZK - Spring Boot
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

*(Adapted from Dirk's amazing [zk-spring-boot-starter](https://github.com/dirkdeyne/zk-spring-boot-starter) - Thanks a lot for the input and ideas!)*

## Getting started

* Add a single dependency (as in [zkspringboot-minimal-jar/pom.xml](zkspringboot-demos/zkspringboot-minimal-jar/pom.xml#L32))
* Place your zul files below [src/main/resources/web](zkspringboot-demos/zkspringboot-minimal-jar/src/main/resources/web/zul)
* Create a spring controller or define a `zk.homepage` parameter in [application.properties](zkspringboot-demos/zkspringboot-minimal-jar/src/main/resources/application.properties)

## Configuration options (for Spring Boot style application.properties)

Defaults as in [ZkProperties.java](zkspringboot-starter/src/main/java/org/zkoss/zkspringboot/ZkProperties.java)
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
`zk.homepage`                  | empty   | `home`/`main`     | when set will setup a @GetMapping for "/" to return the configured view-name
`zk.zul-view-resolver-enabled` | `true`  | `true`/`false`    | enable/disable InternalViewResolver for zul files
`zk.zul-view-resolver-prefix`  | empty   | `/zul`            | prefix prepended to a view name (i.e. a folder inside the web resource package on the classpath)
`zk.zul-view-resolver-suffix`  | `.zul`  | `.zul`/`.zhtml`   | usually `.zul` does what you need
`zk.richlet-filter-mapping`    | empty   | `/richlets/*`     | filter-mapping string as the basepath for richlets
`zk.websockets-enabled=true`   | `true`  | `true`/`false`    | enable/disable websockets (available in ZK-EE)
`zk.servlet3-push-enabled=true`| `true`  | `true`/`false`    | enable/disable servlet3 CometServerPush (available in ZK-EE)
`zk.update-uri`                | `/zkau` | `/mypath`         | configure servlet path for ZK's update engine (rarely used)

### zk.zul-view-resolver-*
Shortcut configuration to enables a Spring MVC Controller with a @GetMapping for the path '/' returning the configured view name. (disabled by default).
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
Enables ZK's RichletFilter LINK ME the value has to be a servlet filter mapping such as: `/richlet/*`
Requires additional [richlet-mappings configured in zk.xml](https://www.zkoss.org/wiki/ZK_Configuration_Reference/zk.xml/The_richlet-mapping_Element).
