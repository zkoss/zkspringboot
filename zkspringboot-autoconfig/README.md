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