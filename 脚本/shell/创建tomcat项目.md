# 自动创建tomcat项目脚本

```bash
#!/bin/bash
checkNull(){
if [ -z "$1" ];then
echo 不能为空
exit 1
fi
}
create(){

unzip tomcat8.zip -d $pname
sed -i  "s/{SHUTDOWN_PORT}/${sport}/" $pname/conf/server.xml
sed -i  "s/{CONNECT_PORT}/${cport}/" $pname/conf/server.xml
sed -i  "s/{AJP_PORT}/${aport}/" $pname/conf/server.xml
echo $finish
exit 1
}
echo start create project

read -p "项目名称?  " pname
checkNull $pname

read -p "服务端口?  " cport
checkNull $cport

sport=`expr $cport + 1`
aport=`expr $cport + 2`

read -p "shutdown port？default:$sport  " sport2
if [ -n "$sport2" ];then
sport=$sport2
fi

read -p "ajp port?default:$aport  " aport2
if [ -n "$aport2" ];then
aport=$aport2
fi

echo "poroject name is:$pname"
echo "server port is:$cport"
echo "shutdown port is:$sport"
echo "ajp port is:$aport"

read -r -p "is OK?[y/n]" confirm
case $confirm in
        [yY][eE][sS]|[yY])
          create
          ;;
        *)
          echo "No,finish"
          exit 1
          ;;
esac

```