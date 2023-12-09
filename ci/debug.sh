#!/usr/bin/env bash
# dont use currently
SDK_HOME=~/Library/Android/sdk
ADB=$SDK_HOME/platform-tools/adb
MAIN=at.aberger.smallrye.config.MainActivity
MAIN=at.aberger.smallrye.config/at.aberger.smallrye.config.MainActivity
#$ADB kill-server
#sleep 1
#$ADB start-server

#$ADB shell am start -e debug true -a android.intent.action.MAIN -c android.intent.category.LAUNCHER 
app_debug_port=$(timeout 1 $ADB jdwp | tail -1)

echo "app_debug_port: $app_debug_port";
$ADB forward tcp:5037 jdwp:$app_debug_port
