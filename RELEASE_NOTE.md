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
