# centos常用环境安装

## 安装nodejs

参考：https://segmentfault.com/a/1190000007124759
nodejs 官网：https://nodejs.org/en/download/package-manager/#enterprise-linux-and-fedora
`curl --silent --location https://rpm.nodesource.com/setup_8.x | bash -`
`yum -y install nodejs`

**安装cnpm，淘宝npm工具，提高npm下载包的速度**

cnpm：https://npm.taobao.org/

`npm install -g cnpm --registry=https://registry.npm.taobao.org`

## 安装docker (docker-ce)

官方文档：https://docs.docker.com/install/linux/docker-ce/centos/
参考：http://www.runoob.com/docker/centos-docker-install.html

step1:Docker 要求 CentOS 系统的内核版本高于 3.10
`uname -r`

step2:移除旧版本

```bash
yum remove docker \
           docker-client \
           docker-client-latest \
           docker-common \
           docker-latest \
           docker-latest-logrotate \
           docker-logrotate \
           docker-selinux \
           docker-engine-selinux \
           docker-engine
```

step3: 安装一些必要的系统工具

```bash
yum install -y yum-utils \
  device-mapper-persistent-data \
  lvm2
```

step4: 添加软件源信息，这里使用aliyun的。 官方源：https://download.docker.com/linux/centos/docker-ce.repo
`yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo`
step5:更新 yum 缓存
`yum makecache fast`
step6:安装 Docker-ce
`yum -y install docker-ce`
step7:启动 Docker 后台服务
`systemctl start docker`
step8:测试运行 hello-world
`docker run hello-world`

**安装 docker-compose**

官方文档：https://docs.docker.com/compose/install/
step1:
注意这里的版本号，最新版请从这里获取[Compose repository release page on GitHub](https://github.com/docker/compose/releases)
`curl -L https://github.com/docker/compose/releases/download/1.22.0/docker-compose-$(uname -s)-$(uname -m) -o /usr/local/bin/docker-compose`
step2:添加可执行权限
`chmod +x /usr/local/bin/docker-compose`
`docker-compose --version`

## 安装nginx

参考：https://www.jianshu.com/p/1cad13e57c43

step1:添加CentOS 7 EPEL 仓库
`yum install epel-release`
step2:安装Nginx
`yum install nginx`