#!/usr/bin/env bash

SDK_HOME=~/Library/Android/sdk
ADB=$SDK_HOME/platform-tools/adb

#gradle assembleDebug
gradle installDebug

MAIN=at.aberger.smallrye.config/.MainActivity

#$ADB kill-server
#sleep 1
#$ADB start-server
#$ADB -d shell am start -n $MAIN
$ADB shell am start -e debug true -a android.intent.action.MAIN -c android.intent.category.LAUNCHER -n $MAIN
