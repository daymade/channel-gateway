#!/bin/bash

java_version() {
    # maybe 1.8.0_162 , 11-ea
    local local_java_version

    local IFS=$'\n'
    # remove \r for Cygwin
    local lines=$("${JAVA_HOME}"/bin/java -version 2>&1 | tr '\r' '\n')
    for line in $lines; do
      if [[ (-z $local_java_version) && ($line = *"version \""*) ]]
      then
        local ver=$(echo $line | sed -e 's/.*version "\(.*\)"\(.*\)/\1/; 1q')
        # on macOS, sed doesn't support '?'
        if [[ $ver = "1."* ]]
        then
          local_java_version=$(echo $ver | sed -e 's/1\.\([0-9]*\)\(.*\)/\1/; 1q')
        else
          local_java_version=$(echo $ver | sed -e 's/\([0-9]*\)\(.*\)/\1/; 1q')
        fi
      fi
    done
    echo "$local_java_version"
}

# SETENV_SETTED promise run this only once.
if [ -z $SETENV_SETTED ]; then
    SETENV_SETTED="true"

    # app
    # set ${APP_NAME}, if empty $(basename "${APP_HOME}") will be used.
    APP_HOME=$(cd $(dirname ${BASH_SOURCE[0]})/..; pwd)
    if [[ "${APP_NAME}" = "" ]]; then
        APP_NAME=$(basename "${APP_HOME}")
    fi

    ulimit -c unlimited

    echo "INFO: OS max open files: "`ulimit -n`

    let memTotal=`cat /proc/meminfo | grep MemTotal | awk '{printf "%d", $2/1024 }'`
    echo "INFO: OS total memory: "$memTotal"M"

    export CPU_COUNT="$(grep -c 'cpu[0-9][0-9]*' /proc/stat)"
    echo "INFO: OS CPU count: "$CPU_COUNT"M"

    JAVA_VERSION=$(java_version)
    echo "INFO: java version: $JAVA_VERSION"

    # env for service(pandora boot)
    export LANG=zh_CN.UTF-8
    export JAVA_FILE_ENCODING=UTF-8
    export NLS_LANG=AMERICAN_AMERICA.ZHS16GBK

    export MIDDLEWARE_LOGS="${HOME}/logs"

    SERVICE_OPTS="${SERVICE_OPTS} -server"
    SERVICE_OPTS="${SERVICE_OPTS} -Xms4g -Xmx4g"
    SERVICE_OPTS="${SERVICE_OPTS} -Xmn2g"
    SERVICE_OPTS="${SERVICE_OPTS} -XX:MetaspaceSize=512m -XX:MaxMetaspaceSize=512m"
    SERVICE_OPTS="${SERVICE_OPTS} -XX:MaxDirectMemorySize=1g"
    SERVICE_OPTS="${SERVICE_OPTS} -XX:ParallelGCThreads=${CPU_COUNT}"
    SERVICE_OPTS="${SERVICE_OPTS} -Xloggc:${MIDDLEWARE_LOGS}/gc.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps"
    SERVICE_OPTS="${SERVICE_OPTS} -XX:SurvivorRatio=10"
    SERVICE_OPTS="${SERVICE_OPTS} -XX:+UseConcMarkSweepGC -XX:CMSMaxAbortablePrecleanTime=5000"
    SERVICE_OPTS="${SERVICE_OPTS} -XX:+CMSClassUnloadingEnabled -XX:CMSInitiatingOccupancyFraction=80 -XX:+UseCMSInitiatingOccupancyOnly"
    SERVICE_OPTS="${SERVICE_OPTS} -XX:+ExplicitGCInvokesConcurrent -Dsun.rmi.dgc.server.gcInterval=2592000000 -Dsun.rmi.dgc.client.gcInterval=2592000000"
    SERVICE_OPTS="${SERVICE_OPTS} -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=${MIDDLEWARE_LOGS}/java.hprof"
    SERVICE_OPTS="${SERVICE_OPTS} -Djava.awt.headless=true"
    SERVICE_OPTS="${SERVICE_OPTS} -Dsun.net.client.defaultConnectTimeout=10000"
    SERVICE_OPTS="${SERVICE_OPTS} -Dsun.net.client.defaultReadTimeout=30000"
    SERVICE_OPTS="${SERVICE_OPTS} -DJM.LOG.PATH=${MIDDLEWARE_LOGS}"
    SERVICE_OPTS="${SERVICE_OPTS} -DJM.SNAPSHOT.PATH=${MIDDLEWARE_SNAPSHOTS}"
    SERVICE_OPTS="${SERVICE_OPTS} -Dfile.encoding=${JAVA_FILE_ENCODING}"
    SERVICE_OPTS="${SERVICE_OPTS} -Dlog4j.defaultInitOverride=true"
    SERVICE_OPTS="${SERVICE_OPTS} -Dmanagement.port=7002 -Dmanagement.server.port=7002"
    export SERVICE_OPTS
fi
