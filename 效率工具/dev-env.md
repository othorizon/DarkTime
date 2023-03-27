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
