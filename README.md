# ZK Spring-Boot


## Configuration options (for Spring-Boot style application.properties)


Defaults as in ZkProperties.java LINK ME
```
zk.springboot-packaging=jar

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
`zk.zul-view-resolver-enabled` | `true`  | `true`/`false`    | enable/disable InternalViewResolver for zul files
`zk.zul-view-resolver-prefix`  | empty   | `/zul`            | prefix prepended to a view name (i.e. a folder inside the web resource package on the classpath)
`zk.zul-view-resolver-suffix`  | `.zul`  | `.zul`/`.zhtml`   | usually `.zul` does what you need
`zk.richlet-filter-mapping`    | empty   | `/richlets/*`     | filter-mapping string as the basepath for richlets
`zk.websockets-enabled=true`   | `true`  | `true`/`false`    | enable/disable websockets (available in ZK-EE)
`zk.servlet3-push-enabled=true`| `true`  | `true`/`false`    | enable/disable servlet3 CometServerPush (available in ZK-EE)
`zk.update-uri`                | `/zkau` | `/mypath`         | configure servlet path for ZK's update engine (rarely used)

##zk.zul-view-resolver-*
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

##zk.richlet-filter-mapping
Enables ZK's RichletFilter LINK ME the value has to be a servlet filter mapping such as: `/richlet/*`
Requires additional richlet mappings in zk.xml LINK ME
