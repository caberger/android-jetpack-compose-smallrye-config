#!/usr/bin/env bash
#see https://asantoso.wordpress.com/2009/09/26/using-jdb-with-adb-to-debugging-of-android-app-on-a-real-device/
SDK_HOME=~/Library/Android/sdk
ADB=$SDK_HOME/platform-tools/adb
MAIN=at.aberger.smallrye.config/.MainActivity
#adb kill-server
#sleep 1
#adb start-server

$ADB devices

echo "wait for device..."
$ADB wait-for-device
echo "wait done"

cmd="$ADB -d shell am start -e debug true -a android.intent.action.MAIN -c android.intent.category.LAUNCHER -n $MAIN";
echo $cmd;
exec $cmd
echo "search for debug port..."
app_debug_port=$($ADB jdwp | tail -1);
echo "app_debug_port: $app_debug_port";
cmd0="$ADB -d forward tcp:29882 jdwp:$app_debug_port";
echo $cmd0;
exec $cmd0 &
cmd1="jdb -J-Duser.home=. -connect com.sun.jdi.SocketAttach:hostname=localhost,port=29882  -sourcepath /Users/aberger/Documents/school/android-jetpack-compose-smallrye-config/app/src/main/java";
cmd2="jdb -J-Duser.home=. -attach asantoso_xpl:29882";
echo $cmd1;
exec $cmd1;

#cmd1="jdb -connect com.sun.jdi.SocketAttach:hostname=localhost,port=29882";
