# docker

## 常用命令

`docker run -d -p 8100:8100 --name custome_name -it IMAGE_NAME`
`-d` 后台
`-i` 让容器的标准输入保持打开
`-t` 让docker分配一个伪终端并绑定到容器的标准输入上
`-e` username="ritchie"  设置环境变量；
>`-i`打开了输入，`-t`模拟一个终端

>如果没有后台运行，在不使用-t参数的情况下，会因为没有终端而无法退出输出界面。因此如果不需要交互输入而且又没有-d参数置于后台，那么应该加上-t参数分配一个终端用于执行ctrl+c命令退出

`docker run --rm image_name [cmd]`  --rm 参数会在容器退出时自动删除容器以及产生volume,一般在测试的时候会用

`docker start CONTAINER_ID` 启动容器,使用`-i`参数可以打开标准输入

`docker exec -it CONTAINER_ID /bin/bash` 进入容器
`docker logs --details -f CONTAINER_ID` 查看日志

`docker run -d -p 8100:8100 -v /root/workspace/projects/scriptbot-8100/docker_scriptbot.log:/scriptbot.log -v /root/workspace/projects/scriptbot-8100/scriptbot.jar:/scriptbot.jar --name scriptbot java:8 java -jar /scriptbot.jar --spring.profiles.active=demo  --server.port=8100 --logging.file=/scriptbot.log`
通过挂载文件的方式启动

Docker Volume
`docker run --name container-test -v /data debian /bin/bash` ：将`/data`挂在到容器中，文件可以在主机直接操作，`docker inspect -f {{.Volumes}} container-test`可以看到该volume在主机上的存储位置。dockerfile文件中的`VOLUME /data`是一样的效果。
`docker run -v /home/adrian/data:/data debian ls /data`：这种方式使用`-v`可以明确指定将主机的目录`/home/adrian/data`挂载到容器内的`/data`上

[docker磁盘占用空间查看及清理](https://blog.csdn.net/weixin_32820767/article/details/81196250)

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

### docker 容器权限

[[docker]privileged参数 - CSDN博客](https://blog.csdn.net/hxpjava1/article/details/78392725)

```bash
$ docker help run
...
--privileged=false         Give extended privileges to this container
...
```

在没有开启`privileged`配置的默认情况下，docker容器内的root用户只是外部的一个普通用户权限，很多操作是不允许的，比如启动服务，比如在容器中再次启动一个docker服务。
因此`docker run --privileged=true`启动特权设置将允许你在容器内进行root操作。

### CMD 与 ENTRYPOINT

在Dockerfile中，只能有一个ENTRYPOINT指令，如果有多个ENTRYPOINT指令则以最后一个为准。  
在Dockerfile中，只能有一个CMD指令，如果有多个CMD指令则以最后一个为准。  
在Dockerfile中，ENTRYPOINT指令或CMD指令，至少必有其一。  
任何docker run设置的命令参数或CMD指令的命令，都将作为ENTRYPOINT指令的命令参数，追加到ENTRYPOINT指令的命令之后。  
如果没有ENTRYPOINT指令而是使用CMD作为启动命令，那么docker run设置的命令参数会覆盖CMD指令。  

### 容器间的通讯

使用 --link 或者使用network
[docker network基础 - wadeson - 博客园](https://www.cnblogs.com/jsonhc/p/7823286.html)

---

## Docker volume

[理解Docker（8）：Docker 存储之卷（Volume） - SammyLiu - 博客园](https://www.cnblogs.com/sammyliu/p/5932996.html)
[Docker容器学习梳理--Volume数据卷使用 - 散尽浮华 - 博客园](https://www.cnblogs.com/kevingrace/p/6238195.html)

**关于文件覆盖**
[Docker数据持久之volume和bind mount - CSDN博客](https://blog.csdn.net/docerce/article/details/79265858)

[官方文档-docker volume create](https://docs.docker.com/engine/reference/commandline/volume_create/#extended-description)

### 是什么

在不使用数据卷轴的情况下，docker容器内的数据只存在于其生命周期内，且容器外部以及其他容器都无法访问，容器一旦删除数据也丢失了（除非commit一个新的镜像），因此会有散需求需要满足：
一是 容器之间共享数据，二是容器内数据的持久化，三是容器共享宿主机数据。为了解决这三个问题所以有了数据卷轴的概念，在下文的使用中的[使用卷轴](#使用卷轴)中会说明如何解决这两个问题。

### 使用

有两种使用方式，一个是docker run命令的`-v`参数，一个是dockerfile文件中的`VOLUME`E命令

>

```bash
$ docker run --help
-v, --volume list                    Bind mount a volume  
    --volume-driver string           Optional volume driver for the container  
    --volumes-from list              Mount volumes from the specified container(s)
```

#### 挂载卷轴

**-v 方式挂载**

`-v [host-dir]:container-dir:[rw|wo]`

如果指定`host-dir`,那么就会挂载指定的目录到容器中的目录上，并且会覆盖容器中指定目录的内容。
如果不指定`host-dir`,那么会在系统的`/var/lib/docker/volumes`目录下生成一个目录挂载到容器内。
可以通过`docker inspect container-id`来查看挂载情况

**VOLUME方式挂载**

在编写dockerfile文件时可以通过`VOLUME dir`的方式去挂载一个卷轴，这种方式与使用-v但是不指定`host-dir`是相同的

#### 删除卷轴

Volume只有在下列情况下才能被自动删除：

- 该容器是用`docker rm －v`命令来删除的（-v是必不可少的）。
- docker run中使用了`--rm`参数

即使用以上两种命令，也只能删除没有容器连接的Volume。**连接到用户指定主机目录的Volume永远不会被docker删除**。即通过`-v host-dir:container-dir`明确指定主机目录的情况下，是不会删除主机上的文件的
如果你没有使用上面两种方式去删除卷轴，那么通过`docker volume COMMAND`将可以删除僵尸卷轴

`docker volume rm VOLUME [VOLUME...]`删除指定的卷轴
`docker volume prune` 清理所有不使用的卷轴

```bash
$ docker volume --help
Usage: docker volume COMMAND

Manage volumes

Commands:
  create      Create a volume
  inspect     Display detailed information on one or more volumes
  ls          List volumes
  prune       Remove all unused local volumes
  rm          Remove one or more volumes
```

**正如前面所说使用`-v host-dir:container-dir`指定的卷轴不会被删除，而且也不会出现在volume卷轴管理中，即通过`docker volume ls`命令是看不到这种方式创造的卷轴的**

docker volume命令是在后来的版本中引入的新功能，它除了可以管理所有的卷轴外（除了明确指定主机目录的卷轴），还可以独立的去创建一个卷轴，这样可以方便的在多个容器之间共享卷轴

#### 使用卷轴

卷轴的目的是为了解决前文提出的三个问题，那么我们这里一一说明

**数据持久化以及共享宿主机数据场景**

我们通过`-v host-dir:container-dir`的方式将主机上的一个目录映射到容器内，这样对container-dir目录的所有操作就是对主机host-dir目录的操作，容器删除后该目录的数据仍然存在。
这样便解决了数据持久化问题，以及宿主机和容器共享数据问题。

**容器之间共享数据场景**

docker run命令中可以通过`--volumes-from`参数来共享其他容器或卷轴的数据

一种方式是
`docker run --volumes-from container-id`指定一个容器可以共享该容器中创建的卷轴，这里只是共享指定容器中创建的数据卷轴而不是共享容器的数据（目标容器运行与否无关）.

还有一种更优雅的方式则是通过`docker volume`命令

```bash
#https://docs.docker.com/engine/reference/commandline/volume_create/#extended-description
$ docker volume create hello

hello

$ docker run -d -v hello:/world busybox ls /world
```

**⚠️通过`docker volime`命令创建的卷轴在删除容器时即使加了`-v`参数(`docker rm -v container-id`)也不会删除卷轴**

---

## docker Compose

常用命令

```bash
docker-compose up -d [service]
#start/stop/restart/kill 服务:
docker-compose start/stop/restart/kill [service]
# Stops containers and removes containers, networks, volumes(-v参数), and images(--rmi参数)
docker-compose down -v
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

## dockerfile 的多阶段构建 （多个from）

示例

```dockerfile
## https://github.com/zerotier/ZeroTierOne/blob/2d21c18ebdacc1c011831c19c162e795c0fe6dde/ext/installfiles/linux/zerotier-containerized/Dockerfile
FROM debian:stretch as builder

## Supports x86_64, x86, arm, and arm64

RUN apt-get update && apt-get install -y curl gnupg
RUN apt-key adv --keyserver ha.pool.sks-keyservers.net --recv-keys 0x1657198823e52a61  && \
    echo "deb http://download.zerotier.com/debian/stretch stretch main" > /etc/apt/sources.list.d/zerotier.list
RUN apt-get update && apt-get install -y zerotier-one=1.2.12

FROM alpine:latest
MAINTAINER Adam Ierymenko <adam.ierymenko@zerotier.com>

LABEL version="1.2.12"
LABEL description="Containerized ZeroTier One for use on CoreOS or other Docker-only Linux hosts."

# Uncomment to build in container
#RUN apk add --update alpine-sdk linux-headers

RUN apk add --update libgcc libstdc++

RUN mkdir -p /var/lib/zerotier-one

COPY --from=builder /var/lib/zerotier-one/zerotier-cli /usr/sbin/zerotier-cli
COPY --from=builder /var/lib/zerotier-one/zerotier-idtool /usr/sbin/zerotier-idtool
COPY --from=builder /usr/sbin/zerotier-one /usr/sbin/zerotier-one

ADD main.sh /
RUN chmod 0755 /main.sh
ENTRYPOINT ["/main.sh"]
CMD ["zerotier-one"]
```

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

[如何选择Docker基础镜像 - CSDN博客](https://blog.csdn.net/nklinsirui/article/details/80967677)

[docker容器配置ssh登入实录 - CSDN博客](https://blog.csdn.net/u011552182/article/details/78650907)