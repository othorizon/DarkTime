# kubernetes

## 学习札记

### 参考资料

[Kubernetes概述：Pods、Nodes、Containers和Clusters - DockOne.io](http://dockone.io/article/3050)  
[十分钟带你理解Kubernetes核心概念 - DockOne.io](http://www.dockone.io/article/932)  
[第一次部署Kubernetes - DockOne.io](http://www.dockone.io/article/8255)  
[kubernetes Setup](https://kubernetes.io/docs/setup/)  
[Install and Set Up kubectl](https://kubernetes.io/docs/tasks/tools/install-kubectl/#optional-kubectl-configurations)  
[kubernetes-sigs/kind](https://github.com/kubernetes-sigs/kind)  
[bsycorp/kind](https://github.com/bsycorp/kind)  

### 笔记

>在Pod中的任何容器都共享了容器命名空间以及本地网络。因此在Pod的容器直接可以非常方便的进行通讯，就好像它们是运行在同一个机器上一样，同时彼此之间又保持隔离。 http://dockone.io/article/3050

>Pod中可以包含多个容器，但是你还是应该尽可能的限制一下。因为Pod是作为一个最小单元，整体进行伸缩。这可能导致资源的浪费以及更多的费用开销。为了避免这种问题。Pod应该尽可能的保持“小”，通常指应该包含一个主进程，以及与其紧密合作的辅助容器（这些辅助容器通常被称为Sidecar）。 http://dockone.io/article/3050  

[Overview of kubectl](https://kubernetes.io/docs/reference/kubectl/overview/)

### 学习

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
