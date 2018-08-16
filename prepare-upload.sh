#!/bin/bash
VERSION=$1

UPLOAD_FOLDER=./target/to_upload
MVN_FOLDER=$VERSION/maven

ZSB_STARTER_FOLDER=$UPLOAD_FOLDER/zkspringboot-starter/$MVN_FOLDER
ZSB_ZATS_FOLDER=$UPLOAD_FOLDER/zkspringboot-zats/$MVN_FOLDER

rm -rf $UPLOAD_FOLDER

mkdir -p $ZSB_STARTER_FOLDER
mkdir -p $ZSB_ZATS_FOLDER

cp zkspringboot-starter/target/zkspringboot-starter-$VERSION-bundle.jar $ZSB_STARTER_FOLDER
cp zkspringboot-zats/target/zkspringboot-zats-$VERSION-bundle.jar $ZSB_ZATS_FOLDER
