#!/usr/bin/env bash

SDK_HOME=~/Library/Android/sdk
echo "SDK is located in $SDK_HOME"

EMULATOR=$SDK_HOME/emulator/emulator

AVD=$($EMULATOR -list-avds | head -1)
echo "AVD is $AVD"

$EMULATOR @$AVD
