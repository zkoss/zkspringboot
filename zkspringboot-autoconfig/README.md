# Before Release
run test case in zkspringboot-autoconfig, zkspringboot-demo-jar-zats, zkspringboot=security-demo-zats

# Release Process
## run `upVer` to change version string in all places to the target version
## Build release bundle and zip files 
* release freshly: `release`. 
* release official: `release official`
## manually upload to fileserver
## publish to maven CE repository with [PBFUM](http://jenkins2/view/All/job/PBFUM/)