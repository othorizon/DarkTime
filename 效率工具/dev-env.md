# 配置开发环境

## vagrant

vagrant可以快速启动一个虚拟机，在虚拟机环境中创建开发环境，与主机隔离。并且开发环境可以快速打包分享给他人。
而且和intellj brains系列的开发套件兼容很好。

### 安装

[Install | Vagrant | HashiCorp Developer](https://developer.hashicorp.com/vagrant/downloads)

`vagrant autocomplete install --bash --zsh`
>[Command-Line Interface | Vagrant | HashiCorp Developer](https://developer.hashicorp.com/vagrant/docs/cli)

### 基本使用

amr64 架构的ubuntu镜像：[Vagrant Cloud by HashiCorp](https://app.vagrantup.com/bento/boxes/ubuntu-20.04-arm64)

```shell
vagrant init bento/ubuntu-20.04-arm64
vagrant up
```

这个镜像的作者bento是受官方信任的，并且这个镜像支持parallels作为provider。

### 使用parallels desktop作为虚拟机

[GitHub - Parallels/vagrant-parallels: Vagrant Parallels Provider](https://github.com/Parallels/vagrant-parallels)

**原理**
parallese desktop的virtual machine包含一个uuid，vagrant同该uuid将一个vagrant的配置和指定的vm虚拟机建立联系。  
打开一个pvm的虚拟机文件（右键显示包内容），其中包含一个config.vps文件，文本打开文件，内容：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<ParallelsVirtualMachine schemaVersion="1.0" dyn_lists="VirtualAppliance 0">
   <AppVersion>18.1.1-53328</AppVersion>
   <ValidRc>2147485060</ValidRc>
   <Identification dyn_lists="">
      <VmUuid>{9c27ae79-fbe5-4c84-9cdc-e1459f642b91}</VmUuid>
      <VmType>0</VmType>
```

其中 `<VmUuid>` 就是这个虚拟机的uuid。
vagrant的配置文件

```bash
$cd .vagrant/machines/default/parallels
$ls -al
action_provision action_set_name  box_meta creator_uid id index_uuid private_key synced_folders vagrant_cwd
$cat id
9c27ae79-fbe5-4c84-9cdc-e1459f642b91
$cat action_set_name
1.5:9c27ae79-fbe5-4c84-9cdc-e1459f642b91
```

如果想使用一个现存的vm虚拟机作为vagrant的虚拟机，可以通过修改`action_provision`和`id`这两个文件的值来实现关联到存在的vm。

## podman/docker 等虚拟环境

### ubunt

**安装c编译环境**

[ubuntu-ports | 镜像站使用帮助 | 清华大学开源软件镜像站 | Tsinghua Open Source Mirror](https://mirrors.tuna.tsinghua.edu.cn/help/ubuntu-ports/)

```shell
apt-get update
apt install make
apt install gcc
apt install build-essential gdb 

```

**arm64配置国内源**

参考：https://blog.csdn.net/weixin_45902201/article/details/128821714

```bash
# 默认注释了源码仓库，如有需要可自行取消注释
deb https://mirrors.ustc.edu.cn/ubuntu-ports/ focal main restricted universe multiverse
# deb-src https://mirrors.ustc.edu.cn/ubuntu-ports/ focal main main restricted universe multiverse
deb https://mirrors.ustc.edu.cn/ubuntu-ports/ focal-updates main restricted universe multiverse
# deb-src https://mirrors.ustc.edu.cn/ubuntu-ports/ focal-updates main restricted universe multiverse
deb https://mirrors.ustc.edu.cn/ubuntu-ports/ focal-backports main restricted universe multiverse
# deb-src https://mirrors.ustc.edu.cn/ubuntu-ports/ focal-backports main restricted universe multiverse
deb https://mirrors.ustc.edu.cn/ubuntu-ports/ focal-security main restricted universe multiverse
# deb-src https://mirrors.ustc.edu.cn/ubuntu-ports/ focal-security main restricted universe multiverse

# 预发布软件源，不建议启用
# deb https://mirrors.ustc.edu.cn/ubuntu-ports/ focal-proposed main restricted universe multiverse
# deb-src https://mirrors.ustc.edu.cn/ubuntu-ports/ focal-proposed main restricted universe multiverse

```