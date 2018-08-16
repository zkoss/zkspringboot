#!/bin/bash
VERSION=$1

UPLOAD_FOLDER=./target/to_upload/$VERSION
MVN_FOLDER=$UPLOAD_FOLDER/maven

rm -rf $UPLOAD_FOLDER
mkdir -p $UPLOAD_FOLDER
mkdir -p $MVN_FOLDER

cp zkspringboot-starter/target/zkspringboot-starter-$VERSION-bundle.jar $MVN_FOLDER
cp zkspringboot-zats/target/zkspringboot-zats-$VERSION-bundle.jar $MVN_FOLDER
