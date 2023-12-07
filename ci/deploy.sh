#!/usr/bin/env bash

SDK_HOME=~/Library/Android/sdk
ADB=$SDK_HOME/platform-tools/adb

gradle assembleDebug
gradle installDebug

MAIN=at.aberger.smallrye.config/.MainActivity

$ADB -d shell am start -n $MAIN
$ADB shell am start -n $MAIN
