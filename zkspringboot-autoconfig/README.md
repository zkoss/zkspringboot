# Before Release
run test case in zkspringboot-autoconfig, zkspringboot-demo-jar-zats, zkspringboot-security-demo-zats

# Development
* append version with `-SNAPSHOT`

# Version Naming Convention
* `a.b.c.d`. The first 3 version numbers match the springboot version it depends on. If we implement any new feature or fixing bugs, we will increase the last number.

# Release Process
## run `upVer` to change version string in all places to the target version
## Build release bundle and zip files  
* release freshly: run `release`
* release official: `release official`
(it removes the `-SNAPSHOT`)
## manually upload `*-bundle.jar` under `/target` to fileserver
## publish to maven CE repository with [PBFUM](http://jenkins2/view/All/job/PBFUM/)

# Architecture Decision Record
## a starter should help devs start with a minimal setup
* This starter project should include a zk CE dependency as compile scope by default, so devs can start a new project with this starter dependency without adding any zk dependency by default. If no default zk is included, people tend to forget to include a ZK dependency.
* If devs don't want this default zk version, they can override them.
* put EE dependencies as optional, so devs can override with desired ZK version easily.
