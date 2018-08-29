# docker

## 常用命令

`docker run -d -p 8100:8100 --name scriptbot_1 scriptbot -it`
`-d` 后台
`-i` 让容器的标准输入保持打开
`-t` 让docker分配一个伪终端并绑定到容器的标准输入上
>`-i`打开了输入，`-t`模拟一个终端

`docker start CONTAINER_ID` 启动容器,使用`-i`参数可以打开标准输入

`docker exec -it CONTAINER_ID /bin/bash` 进入容器
`docker logs --details -f CONTAINER_ID` 查看日志

`docker run -d -p 8100:8100 -v /root/workspace/projects/scriptbot-8100/docker_scriptbot.log:/scriptbot.log -v /root/workspace/projects/scriptbot-8100/scriptbot.jar:/scriptbot.jar --name scriptbot java:8 java -jar /scriptbot.jar --spring.profiles.active=demo  --server.port=8100 --logging.file=/scriptbot.log`
通过挂载文件的方式启动

Docker Volume
`docker run --name container-test -v /data debian /bin/bash` ：将`/data`挂在到容器中，文件可以在主机直接操作，`docker inspect -f {{.Volumes}} container-test`可以看到该volume在主机上的存储位置。dockerfile文件中的`VOLUME /data`是一样的效果。
`docker run -v /home/adrian/data:/data debian ls /data`：这种方式使用`-v`可以明确指定将主机的目录`/home/adrian/data`挂载到容器内的`/data`上

### 离线安装镜像

```bash
# 1 拉取镜像
docker pull images_names

# 2 保存本地
docker save [IMAGE ID/REPOSITORY]> /root/image.tar

# 3 上传服务器后 加载镜像
docker load < /root/image.tar
```

---

## docker理解

[(转)Docker镜像中的base镜像理解 - 笑侃码农 - 博客园](https://www.cnblogs.com/kb342/p/7649598.html)

## 深入学习

### CMD 与 ENTRYPOINT

在Dockerfile中，只能有一个ENTRYPOINT指令，如果有多个ENTRYPOINT指令则以最后一个为准。  
在Dockerfile中，只能有一个CMD指令，如果有多个CMD指令则以最后一个为准。  
在Dockerfile中，ENTRYPOINT指令或CMD指令，至少必有其一。  
任何docker run设置的命令参数或CMD指令的命令，都将作为ENTRYPOINT指令的命令参数，追加到ENTRYPOINT指令的命令之后。  
如果没有ENTRYPOINT指令而是使用CMD作为启动命令，那么docker run设置的命令参数会覆盖CMD指令。  

### 容器间的通讯

使用 --link 或者使用network
[docker network基础 - wadeson - 博客园](https://www.cnblogs.com/jsonhc/p/7823286.html)

### Docker volume

[理解Docker（8）：Docker 存储之卷（Volume） - SammyLiu - 博客园](https://www.cnblogs.com/sammyliu/p/5932996.html)

**关于文件覆盖**
参考
[Docker数据持久之volume和bind mount - CSDN博客](https://blog.csdn.net/docerce/article/details/79265858)

## docker Compose

常用命令

```bash
docker-compose up -d [service]
#start/stop/restart/kill 服务:
docker-compose start/stop/restart/kill [service]
```

[Compose 命令说明 - Docker &mdash;&mdash; 从入门到实践 - 极客学院Wiki](http://wiki.jikexueyuan.com/project/docker-technology-and-combat/commands.html)
[Docker Compose 配置文件详解](https://www.jianshu.com/p/2217cfed29d7)
[Docker-Compose入门 - CSDN博客](https://blog.csdn.net/chinrui/article/details/79155688)

在一个docker-compose中的容器，会被自动放在一个网络环境里，不用再使用 --link 去连接容器了，可以直接通过容器的名称(`container_name`)或者service的名称来访问其他容器。使用`docker network ls`命令可以看到会自动创建一个桥接网络，在第一次运行compose时也可以看到提示创建了网络。

```yml
#web容器中可以通过yapi-mongodb 这个容器名称或者 mongodb 这个service名称 访问mongodb容器:ping yapi-mongodb；ping mongodb
version: "3"
services:
  mongodb:
    image: mongo:latest
    container_name: yapi-mongodb
    volumes:
      - ./mongo/db:/data/db
  web:
    image: crper/yapi
    container_name: yapi-web
    depends_on:
      - mongodb
    ports:
      - "3000:3000"
```

参考配置：

- https://store.docker.com/community/images/fiochen227/yapi
- https://store.docker.com/community/images/branchzero/yapi

---

## 参考

[**Docker折腾记: (1)构建yapi容器,从构建发布到可用 - 掘金**](https://juejin.im/post/5b4c518b6fb9a04fd4508af1)

[在Docker容器中运行Spring Boot应用](https://blog.csdn.net/lsy0903/article/details/53048198/)

[CentOS7 Tomcat 启动过程很慢,JVM上的随机数与熵池策略](https://blog.csdn.net/lanmo555/article/details/51909021)

[docker运行jar文件](https://blog.csdn.net/bianchengninhao/article/details/80143950)

[Docker network第五讲-替代“--link”（Docker系列）](https://blog.csdn.net/zsl129/article/details/53939646)

[离线服务器下docker的部署与应用](https://blog.csdn.net/u011372108/article/details/80549731)

[**Docker镜像构建-构建yapi容器,从构建发布到可用**](https://juejin.im/post/5b4c518b6fb9a04fd4508af1)

[Docker数据持久化与容器迁移 - shiningrise - 博客园](https://www.cnblogs.com/shiningrise/p/5821604.html)