# docker

## 常用命令

`docker run -d -p 8100:8100 --name scriptbot_1 scriptbot -it`
`-d` 后台
`-i` 让容器的标准输入保持打开
`-t` 让docker分配一个伪终端并绑定到容器的标准输入上
>`-i`打开了输入，`-t`模拟一个终端

`docker start CONTAINER_ID` 启动容器,使用`-i`参数可以打开标准输入

`docker exec -it CONTAINER_ID /bin/bash` 进入容器

`docker run -d -p 8100:8100 -v /root/workspace/projects/scriptbot-8100/docker_scriptbot.log:/scriptbot.log -v /root/workspace/projects/scriptbot-8100/scriptbot.jar:/scriptbot.jar --name scriptbot java:8 java -jar /scriptbot.jar --spring.profiles.active=demo  --server.port=8100 --logging.file=/scriptbot.log`
通过挂载文件的方式启动

## 参考

[在Docker容器中运行Spring Boot应用](https://blog.csdn.net/lsy0903/article/details/53048198/)

[CentOS7 Tomcat 启动过程很慢,JVM上的随机数与熵池策略](https://blog.csdn.net/lanmo555/article/details/51909021)

[docker运行jar文件](https://blog.csdn.net/bianchengninhao/article/details/80143950)

[Docker network第五讲-替代“--link”（Docker系列）](https://blog.csdn.net/zsl129/article/details/53939646)