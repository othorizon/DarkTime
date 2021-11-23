# kubernetes

## 在线学习环境

[Kubernetes主页 狐狸教程](https://www.freeaihub.com/kubernetes/)

## 学习札记

### 参考资料

[Kubernetes概述：Pods、Nodes、Containers和Clusters - DockOne.io](http://dockone.io/article/3050)  
[十分钟带你理解Kubernetes核心概念 - DockOne.io](http://www.dockone.io/article/932)  
[第一次部署Kubernetes - DockOne.io](http://www.dockone.io/article/8255)  
[kubernetes Setup](https://kubernetes.io/docs/setup/)  
[Install and Set Up kubectl](https://kubernetes.io/docs/tasks/tools/install-kubectl/#optional-kubectl-configurations)  
[kubernetes-sigs/kind](https://github.com/kubernetes-sigs/kind)  
[bsycorp/kind](https://github.com/bsycorp/kind)  
[Kubernetes（k8s）中文文档 目录_Kubernetes中文社区](https://www.kubernetes.org.cn/docs)  
[Kubernetes(K8S)中文文档_Kubernetes中文社区](http://docs.kubernetes.org.cn/)  

[Kubernetes Handbook - Kubernetes中文指南/云原生应用架构实践手册 by Jimmy Song(宋净超)](https://jimmysong.io/kubernetes-handbook/)  

### 部署kubernetes

MAC 上启用k8s
[docker desktop上启用kubernetes](https://blog.csdn.net/chinaloong1949/article/details/103440150)

> docker 启用k8s 国内网路问题解决：https://github.com/AliyunContainerService/k8s-for-docker-desktop

### 笔记

[Overview of kubectl](https://kubernetes.io/docs/reference/kubectl/overview/)  

**pod**
在Pod中的任何容器都共享了容器命名空间以及本地网络。因此在Pod的容器直接可以非常方便的进行通讯，就好像它们是运行在同一个机器上一样，同时彼此之间又保持隔离。  
Pod中可以包含多个容器，但是你还是应该尽可能的限制一下。因为Pod是作为一个最小单元，整体进行伸缩。这可能导致资源的浪费以及更多的费用开销。为了避免这种问题。Pod应该尽可能的保持"小"，通常指应该包含一个主进程，以及与其紧密合作的辅助容器（这些辅助容器通常被称为Sidecar）。 http://dockone.io/article/3050  

**services**
如果Pods是短暂的，那么重启时IP地址可能会改变，怎么才能从前端容器正确可靠地指向后台容器呢？  
Service是定义一系列Pod以及访问这些Pod的策略的一层抽象。Service通过Label找到Pod组。因为Service是抽象的，所以在图表里通常看不到它们的存在，这也就让这一概念更难以理解。

### 学习

[Using kubectl to Create a Deployment](https://kubernetes.io/docs/tutorials/kubernetes-basics/deploy-app/deploy-intro/)
[学习 Kubernetes 基础知识](https://kubernetes.io/zh/docs/tutorials/kubernetes-basics/)  
[Play with Kubernetes](https://labs.play-with-k8s.com/)
[Kubernetes Playground | Katacoda](https://www.katacoda.com/courses/kubernetes/playground)

学习环境部署

```bash
# 运行kind https://github.com/bsycorp/kind
docker run --name kind -itd --privileged -p 8443:8443 -p 10080:10080 bsycorp/kind:latest-1.13
#进入
docker exec -it kind /bin/bash
# 这个kind是alpine 的Linux，因此不太一样
# 容器内安装bash complete
# https://www.oschina.net/translate/alpine-linux-install-bash-using-apk-command
apk add bash-completion
echo 'source /etc/profile.d/bash_completion.sh' >> ~/.bashrc
#安装提示 https://kubernetes.io/docs/tasks/tools/install-kubectl/#optional-kubectl-configurations
echo 'source <(kubectl completion bash)' >> ~/.bashrc
```

```bash
kubectl run kubernetes-bootcamp --image=jocatalin/kubernetes-bootcamp --port=8080

```

pod 的yml配置

```yml
apiVersion: v1
kind: Pod
metadata:
  name: gitea-pod
spec:
  containers:
  - name: gitea-container
    image: gitea/gitea:1.4
```

service 的json

```json
{
    "apiVersion": "v1",
    "kind": "Service",
    "metadata": {
        "name": "my-service"
    },
    "spec": {
        "selector": {
            "app": "MyApp"
        },
        "ports": [
            {
                "protocol": "TCP",
                "port": 80,
                "targetPort": 9376
            }
        ]
    }
}
```

## playground 搭建

### 单节点的环境安装

https://github.com/bsycorp/kind  
使用这个kind可以做到一键安装kubenetes环境 非常简单。  
`docker run -it --privileged -p 8443:8443 -p 10080:10080 bsycorp/kind:latest-1.12`

### 多节点的环境安装

S1:  
安装一个 基于Centos的docker环境，即 docker in docker

```bash
#借助这个基于centos的dind镜像来开发
docker run --privileged --name kube-playground -itd cubedhost/centos7-dind-node
docker exec -it kube-playground bash
```

**以下均为容器内的操作**

准备：
如果系统默认没有命令补全提示，则需要安装

```bash
#yum install epel-release  
yum install bash-completion
# 安装docker completion
curl -XGET https://raw.githubusercontent.com/docker/cli/master/contrib/completion/bash/docker > /etc/bash_completion.d/docker
````

S2:  安装go  

```bash
#这是国内安装源 https://golang.google.cn/dl/
curl -O https://dl.google.com/go/go1.11.9.linux-amd64.tar.gz  
tar -C /usr/local -xzf go1.11.1.linux-amd64.tar.gz  
#gopath是go的工作目录，下载的程序都会在这里
mkdir -p ~/go; echo "export GOPATH=$HOME/go" >> ~/.bashrc  
# gopath目录中的bin目录是安装的程序的可执行文件
echo "export PATH=$PATH:$HOME/go/bin:/usr/local/go/bin" >> ~/.bashrc  
source ~/.bashrc  
```

S3: 安装kind  
https://github.com/kubernetes-sigs/kind  
kind是一个多节点的docker in docker的kubernetes服务集群部署工具  

kind使用go安装，但是默认的安装方式`go get -u sigs.k8s.io/kind` 所使用的域名 sigs.k8s.io在国内是无法访问的，  
但是其实这个域名是会转到github的，因此可以通过手动从github下载后将目录地址换成sigs.k8s.io。

ps. go下载的文件会存放在$GOPATH/src中，本地已经存在时，go get会直接使用本地的文件。 https://www.cnblogs.com/52php/p/6434771.html  

```bash
cd $GOPATH/src
git clone https://github.com/kubernetes-sigs/kind.git
mkdir sigs.k8s.io
mv kind sigs.k8s.io/kind
# 这里go get命令不要加-u参数了，这个参数会联网去检查。
go get sigs.k8s.io/kind
```

S4: 安装kubectl  
https://kubernetes.io/docs/tasks/tools/install-kubectl/  
官方文档中的安装方式国内无法访问，因此参考这个:  
[阿里云 kubernetes yum 镜像仓库 CentOS](https://www.jianshu.com/p/9bac174bd2c5)  
~~[kubectl安装(1.9版本)(解决国内安装kubectl失败)](https://blog.csdn.net/faryang/article/details/79427573)  ~~

```bash
cat <<EOF > /etc/yum.repos.d/kubernetes.repo
[kubernetes]
name=Kubernetes
baseurl=http://mirrors.aliyun.com/kubernetes/yum/repos/kubernetes-el7-x86_64
enabled=1
gpgcheck=0
repo_gpgcheck=0
gpgkey=http://mirrors.aliyun.com/kubernetes/yum/doc/yum-key.gpg http://mirrors.aliyun.com/kubernetes/yum/doc/rpm-package-key.gpg
EOF

yum info kubectl
yum install kubectl

#安装补全提示 https://kubernetes.io/docs/tasks/tools/install-kubectl/#optional-kubectl-configurations
echo 'source <(kubectl completion bash)' >>~/.bashrc
source ~/.bashrc

# 因为尚未配置集群所以执行version会看到链接失败提示
kubectl version
```

S4: 使用kind

```bash
# 安装补全提示
echo 'source <(kind completion bash)' >> ~/.bashrc
source ~/.bashrc
#创建集群
kind create cluster
#导出集群配置
export KUBECONFIG="$(kind get kubeconfig-path)"
kubectl cluster-info
# check集群
kubectl version
```

以上就安装好了

### 开始游戏

**bootcamp**
[Interactive Tutorial - Deploying an App](https://kubernetes.io/docs/tutorials/kubernetes-basics/deploy-app/deploy-interactive/)

```bash
kubectl run kubernetes-bootcamp --image=jocatalin/kubernetes-bootcamp:v2 --port=8080
kubectl describe pods kubernetes-bootcamp
kubectl proxy &

export POD_NAME=$(kubectl get pods -o go-template --template '{{range .items}}{{.metadata.name}}{{"\n"}}{{end}}')
echo Name of the Pod: $POD_NAME
curl http://localhost:8001/api/v1/namespaces/default/pods/$POD_NAME/proxy/
kubectl logs $POD_NAME
```

### 多节点配置

[kind multi-node clusters](https://kind.sigs.k8s.io/docs/user/quick-start/#configuring-your-kind-cluster)  

示例配置

```yml
# three node (two workers) cluster config
kind: Cluster
apiVersion: kind.sigs.k8s.io/v1alpha3
nodes:
- role: control-plane
- role: worker
- role: worker
```

```bash
cat <<EOF > multi-kind.yml
kind: Cluster
apiVersion: kind.sigs.k8s.io/v1alpha3
nodes:
- role: control-plane
- role: worker
- role: worker
EOF
kind create cluster --config=multi-kind.yml --name=multi-kind

export KUBECONFIG="$(kind get kubeconfig-path --name=multi-kind)"
kubectl cluster-info
``