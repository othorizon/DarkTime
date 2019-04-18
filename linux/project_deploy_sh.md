# 简易灰度部署脚本 不使用jenkins的纯shell方式

tag: 项目部署 灰度部署 shell部署项目

部署环境需要安装maven和git

## 编译项目并启动服务

```bash
#!/bin/sh
srcPath=/data/git_repository/springboot_app #源码地址
deployPath=/data/apps/springboot_app #部署路径
jarName=springboot_app-0.0.1-SNAPSHOT.jar

# 编译依赖的模块并install
cd /data/git_repository/common_project
git pull
mvn clean install -Dmaven.test.skip=true
echo "common_project 代码构建完成！"

cd $srcPath
git pull
mvn clean package -Dmaven.test.skip=true
echo "app代码构建完成"

if [ ! -f "/data/git_repository/springboot_app/web/target/springboot_app-0.0.1-SNAPSHOT.jar" ];then
echo "打包失败，文件不存在！"
exit
fi

cd $deployPath

#当前时间 精确到秒  
currentTimeStamp=`date '+%Y%m%d-%H%M%S'`

mv lib/$jarName lib/$jarName.$currentTimeStamp
echo "jar备份完成"

cp $srcPath/manager-web/target/$jarName lib/
echo "jar启动完成"

./bin/springboot_app.sh restart

# tail -300f logs/start.log

```

## 引用到的脚本

**判断服务状态**
ping.sh  
curl请求的的方式判断服务是否已经启动完成

```bash ping.sh
#!bin/bash
#set -x
server_url=$1

while [[ true ]]
do
  echo waiting  server $server_url ...
  urlstatus=$(curl -s -m 5 -IL $server_url |grep HTTP)
  if [ "${urlstatus}" != "" ];then
    echo "${server_url} is ONLINE"
    break
  fi
  sleep 3
done
```

**主启动脚本**
springboot_app.sh  
主启动服务脚本

```bash springboot_app.sh
#!/usr/bin/env bash
set -e
export set LC_ALL='en_US.UTF-8'
PRG="$0"
version="0.0.1-SNAPSHOT"
moduleName="springboot_app"
BIN_HOME=`cd $(dirname "$PRG"); pwd`
APP_HOME=`cd ${BIN_HOME}/..;pwd`
echo "APP_HOME: $APP_HOME"
logs=$APP_HOME/logs
jar_path="${APP_HOME}/lib"
jar_name="${moduleName}-$version.jar"
startlog="${logs}/start.log"
server_port="9081"
slave_port="9181"
slave_sh=${BIN_HOME}/springboot_app-slave.sh
ping_sh=${BIN_HOME}/ping.sh

get_pid(){
pid=`ps -ef | grep -v grep | grep "${jar_path}/${jar_name}" |  awk '{print $2}'`
echo $pid
}

process_is_running(){
    pid=`get_pid`
    if [ -z $pid ]
    then
        echo 1
    else
        echo 0
    fi
}

start() {

    pid=`get_pid`
    if test `process_is_running` -eq 0
    then
        echo "WARN:process is running,pid is $pid"
        exit 1
    else
        echo "Starting server: "
        JVM_OPTS="-server -Xms4G -Xmx4G -XX:MaxMetaspaceSize=256M -XX:MetaspaceSize=256M -Xloggc:${logs}/gc.log -XX:-PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCApplicationStoppedTime -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=70  -XX:+UseCMSCompactAtFullCollection -XX:+CMSParallelRemarkEnabled -XX:+HeapDumpOnOutOfMemoryError"
        echo "java ${JVM_OPTS} -Dspring.config.location=classpath:application.properties,classpath:application.yml,file:${APP_HOME}/conf/application.properties -jar ${jar_path}/${jar_name}"
        setsid su hdfs -c "java ${JVM_OPTS} -Dapp.log.home=${logs} -Dspring.config.location=classpath:application.properties,classpath:application.yml,file:${APP_HOME}/conf/application.properties,file:${APP_HOME}/conf/application.yml -jar ${jar_path}/${jar_name} > ${startlog} 2>&1  &"
        sleep 2s
        pid=`get_pid`
        if test `process_is_running` -eq 0
        then
            echo "start success! pid is $pid"
        else
            echo "start fail."
        fi
    fi
        #等待主服务启动完成
        sh $ping_sh http://localhost:$server_port
        #为了避免日志打印到多处，停止slave服务，开发环境不需要做负载均衡
        echo stop slave
        sh $slave_sh stop
}

stop() {
        #停止服务的时候先启动slave_server
        echo start slave_server
        sh $slave_sh restart
        #等待slave服务启动完成
        sh $ping_sh http://localhost:$slave_port

    pid=`get_pid`
    if test `process_is_running` -eq 0
    then
        echo "stopping..."
        pid=`get_pid`
        kill -9 $pid
        if test `process_is_running` -eq 0
        then
            echo "stop fail"
        else
            echo "stop success"
        fi
    else
        echo "WARN:process is not exist."
    fi
}

restart() {
    stop
    start
}

rh_status() {
    pid=`get_pid`
    if test `process_is_running` -eq 0
    then
        echo "process is running,pid is $pid"
    else
        echo "process is not running"
    fi
    RETVAL=$?
    return $RETVAL
}

case "$1" in
    start)
        start
    ;;
    stop)
        stop
    ;;
    restart)
        restart
    ;;
    status)
        rh_status
    ;;
    *)
        echo $"Usage: $0 {start|stop|status|restart}"
        exit 1
esac
```

**备服务启动脚本**
springboot_app-slave.sh  
备服务启动服务脚本

```bash springboot_app-slave.sh
#!/usr/bin/env bash
export set LC_ALL='en_US.UTF-8'
PRG="$0"
version="0.0.1-SNAPSHOT"
moduleName="springboot_app"
BIN_HOME=`cd $(dirname "$PRG"); pwd`
APP_HOME=`cd ${BIN_HOME}/..;pwd`
echo "APP_HOME: $APP_HOME"
logs=$APP_HOME/logs_slave
jar_path="${APP_HOME}/lib"
jar_name="${moduleName}-$version-slave.jar"
s_jar_name="${moduleName}-$version.jar"
startlog="${logs}/start.log"
server_port="9181"
get_pid(){
pid=`ps -ef | grep -v grep | grep "${jar_path}/${jar_name}" |  awk '{print $2}'`
echo $pid
}

process_is_running(){
    pid=`get_pid`
    if [ -z $pid ]
    then
        echo 1
    else
        echo 0
    fi
}

start() {
    cp ${jar_path}/${s_jar_name} ${jar_path}/${jar_name}
    pid=`get_pid`
    if test `process_is_running` -eq 0
    then
        echo "WARN:process is running,pid is $pid"
        exit 1
    else
        echo "Starting server: "
        JVM_OPTS="-server -Xms4G -Xmx4G -XX:MaxMetaspaceSize=256M -XX:MetaspaceSize=256M -Xloggc:${logs}/gc.log -XX:-PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCApplicationStoppedTime -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=70  -XX:+UseCMSCompactAtFullCollection -XX:+CMSParallelRemarkEnabled -XX:+HeapDumpOnOutOfMemoryError"
        echo "java ${JVM_OPTS} -Dspring.config.location=classpath:application.properties,classpath:application.yml,file:${APP_HOME}/conf/application.properties -jar ${jar_path}/${jar_name}"
        setsid su hdfs -c "java ${JVM_OPTS} -Dapp.log.home=${logs} -Dspring.config.location=classpath:application.properties,classpath:application.yml,file:${APP_HOME}/conf/application.properties,file:${APP_HOME}/conf/application.yml -jar ${jar_path}/${jar_name} --server.port=${server_port} > ${startlog} 2>&1  &"
        sleep 2s
        pid=`get_pid`
        if test `process_is_running` -eq 0
        then
            echo "start success! pid is $pid"
        else
            echo "start fail."
        fi
    fi
}

stop() {
    pid=`get_pid`
    if test `process_is_running` -eq 0
    then
        echo "stopping..."
        pid=`get_pid`
        kill -9 $pid
        if test `process_is_running` -eq 0
        then
            echo "stop fail"
        else
            echo "stop success"
        fi
    else
        echo "WARN:process is not exist."
    fi
}

restart() {
    stop
    start
}

rh_status() {
    pid=`get_pid`
    if test `process_is_running` -eq 0
    then
        echo "process is running,pid is $pid"
    else
        echo "process is not running"
    fi
    RETVAL=$?
    return $RETVAL
}

case "$1" in
    start)
        start
    ;;
    stop)
        stop
    ;;
    restart)
        restart
    ;;
    status)
        rh_status
    ;;
    *)
        echo $"Usage: $0 {start|stop|status|restart}"
        exit 1
esac
```