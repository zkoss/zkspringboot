# 4.0.5
## Features
* Upgrade Spring Boot to 4.0.5
* Spring Security 7: replace removed `AntPathRequestMatcher` with `PathPatternRequestMatcher` (`org.springframework.security.web.servlet.util.matcher`)
* Spring Security 7: ZUL file protection pattern changed from `/zkau/web/**/*.zul` to `/zkau/web/**` (`PathPatternRequestMatcher` requires `**` at start or end of pattern)
* Spring Boot 4 test support: `@AutoConfigureMockMvc` moved to new `spring-boot-webmvc-test` module; add it explicitly alongside `spring-boot-starter-test`
* Spring Boot 4 test support: `@MockBean` removed; use `@MockitoBean` from `org.springframework.test.context.bean.override.mockito`
* Add `DemoApplicationMockMvcTest` to `zkspringboot-demo-jar` demonstrating `@AutoConfigureMockMvc` and `@MockitoBean`
* Add `SecurityConfigTest` to `zkspringboot-security-demo` demonstrating MockMvc security tests with `PathPatternRequestMatcher`
* Jackson 3.x compatibility: force `com.fasterxml.jackson.core:jackson-annotations:2.21` in test scope to resolve conflict between Jackson 3 (Spring Boot 4) and Jackson 2 (ZK transitive dependency)
* ZATS: upgrade `zats.version` to `10.2.1-jakarta` across all modules
* ZATS: fix `ZatsSpringBootContextLoaderListener` to use thread context ClassLoader for application class loading (required for Jetty EE10)
* ZATS: add `<classifier>exec</classifier>` to `spring-boot:repackage` in `zkspringboot-demo-jar` and `zkspringboot-security-demo` so the thin JAR remains as the main artifact (ZATS requires classes accessible via standard ClassLoader, not nested in `BOOT-INF/classes/`)
* ZATS `zkspringboot-minimal-war`: exclude `tomcat-embed-websocket` to prevent conflict with ZATS's embedded Jetty EE10 WebSocket container

# 3.5.13
## Features
* Upgrade Spring Boot to 3.5.13
* Spring Security demo: add CSRF protection for ZK AU requests using `CookieCsrfTokenRepository` and `SpringSecurityCsrfInitiator`
  * A ZK page initiator (`SpringSecurityCsrfInitiator`) injects the Spring Security CSRF token into page meta tags and loads a JS helper (`csrf-header-override.js`) that patches ZK's AU engine to include the `X-XSRF-TOKEN` header on every AU request
  * The login form now includes a hidden `_csrf` field for standard form-based login
* Spring Security demo: exempt `rmDesktop` AU requests from Spring CSRF (ZK handles desktop cleanup internally)
* `zkspringboot-minimal-war`: configure the Spring Boot Maven plugin `repackage` goal to produce a self-contained runnable WAR

# 3.2.7.1

## Bug Fixes
* [#51 a browser saves a downloaded file without extension name under jar packaging](https://github.com/zkoss/zkspringboot/issues/51)

# 3.2.7
## Bug Fixes
* [#50 the starter project should include a zk CE dependency as compile scope by default](https://github.com/zkoss/zkspringboot/issues/50)

# 3.2.6.1
## Bug Fixes
* [#48 DHtmlLayoutServlet initialization fails if the property zk.resource-uri is defined](https://github.com/zkoss/zkspringboot/issues/48)

# 3.2.6
## Features
* upgrade spring-boot to 3.2.6 

# 3.2.3
## Features
* upgrade spring-boot to 3.2.3
* start to depend on ZK 10.0.0
* support the properties for stateless dispatcher filter and client MVVM

# 3.2.1
## Features
* upgrade spring-boot to 3.2.1

# 3.0.0
## Features
* upgrade spring-boot to 3.0.0


# 2.7.7
## Features
* upgrade spring-boot to 2.7.7
