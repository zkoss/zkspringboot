#!/bin/bash
VERSION=$1

set -e

UPLOAD_FOLDER=./target/to_upload
MVN_FOLDER=$VERSION/maven

ZSB_AUTOCONFIG_PATH=$UPLOAD_FOLDER/zkspringboot-autoconfig/$MVN_FOLDER
ZSB_STARTER_PATH=$UPLOAD_FOLDER/zkspringboot-starter/$MVN_FOLDER

rm -rf $UPLOAD_FOLDER

mkdir -p $ZSB_AUTOCONFIG_PATH/$EVAL_FOLDER
mkdir -p $ZSB_STARTER_PATH/$EVAL_FOLDER
cp zkspringboot-starter/target/zkspringboot-starter-$VERSION-bundle.jar $ZSB_STARTER_PATH
cp zkspringboot-autoconfig/target/zkspringboot-autoconfig-$VERSION-bundle.jar $ZSB_AUTOCONFIG_PATH
