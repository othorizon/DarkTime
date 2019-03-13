# docker 常用镜像

## 命令

`docker build --rm -t name:tag -f Dockerfile .`

## mysql

具体帮助查看：https://hub.docker.com/r/library/mysql/

```bash
docker volume create mysqldb
docker run --hostname mysql --name mysql -v mysqldb:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=123456 -d mysql:5.6.41
```

## python

### 网易云音乐下载

```Dockerfile
## https://github.com/othorizon/Dockerfiles
FROM alpine/git:1.0.7 as stage1

ENV NETEASE_DL_GIT https://github.com/ziwenxie/netease-dl
RUN git clone $NETEASE_DL_GIT /netease_dl_git

FROM python:3.6 as stage2

COPY --from=stage1 /netease_dl_git /netease_dl_git
WORKDIR /netease_dl_git
RUN python setup.py install

FROM python:3.6-alpine

LABEL description="dockerized netease-dl(https://github.com/ziwenxie/netease-dl)"
LABEL dockerfile_author="https://github.com/othorizon"

COPY --from=stage2 /usr/local/lib/python3.6/site-packages /usr/local/lib/python3.6/site-packages
COPY --from=stage2 /usr/local/bin/netease-dl /usr/local/bin/netease-dl

VOLUME [ "/output" ]

ENTRYPOINT [ "netease-dl","-o","/output" ]

CMD ["--help"]

```

## 优秀dockerfile示例

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