#!/bin/sh
BASEDIR=$(dirname "$0")

#APP_NAME and JAR_NAME will be set during the build, do not set these manually
APP_NAME="@project.artifact@"
JAR_NAME="@build.finalName@.jar"

GC_OPTS="-verbose:gc -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintHeapAtGC -Xloggc:$BASEDIR/logs/gclo.`date +%Y.%m.%d.%H.%M`.log"
JAVA_OPTS="-Xms256m -Xmx2048m -XX:MaxMetaspaceSize=256m"
LOG_OPTS="-Dlogging.config=logbak.xml"
DIAGNOSTIC_OPTS="-XX:+HeapDumpOnOutOfMemoryError"

cd $BASEDIR