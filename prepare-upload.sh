#!/bin/bash
VERSION=$1

UPLOAD_FOLDER=./target/to_upload
MVN_FOLDER=$VERSION/maven
EVAL_FOLDER=EE-eval

ZSB_STARTER_PATH=$UPLOAD_FOLDER/zkspringboot-starter/$MVN_FOLDER
ZSB_ZATS_PATH=$UPLOAD_FOLDER/zkspringboot-zats/$MVN_FOLDER

rm -rf $UPLOAD_FOLDER

mkdir -p $ZSB_STARTER_PATH/$EVAL_FOLDER
mkdir -p $ZSB_ZATS_PATH/$EVAL_FOLDER

cp zkspringboot-starter/target/zkspringboot-starter-$VERSION-bundle.jar $ZSB_STARTER_PATH
cp zkspringboot-starter/target/zkspringboot-starter-$VERSION-bundle.jar $ZSB_STARTER_PATH/$EVAL_FOLDER

cp zkspringboot-zats/target/zkspringboot-zats-$VERSION-bundle.jar $ZSB_ZATS_PATH
cp zkspringboot-zats/target/zkspringboot-zats-$VERSION-bundle.jar $ZSB_ZATS_PATH/$EVAL_FOLDER

