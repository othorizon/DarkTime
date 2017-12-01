# gitlab触发Jenkins 自动构建

## 参考
[持续集成之④:GitLab触发jenkins构建项目](https://www.cnblogs.com/reblue520/p/7146638.html)
[印象-link](https://app.yinxiang.com/shard/s9/nl/679699/02412e79-a809-4ed4-8f77-f13b43013e14/)
[gitlab利用webhook实现jenkins自动构建](http://www.jianshu.com/p/4eac43872b40)
[印象-link](https://app.yinxiang.com/shard/s9/nl/679699/1cea3479-0c5e-499f-a851-5165dd8cd351/)

## 配置
1. 插件  
[Gitlab Hook Plugin](https://wiki.jenkins.io/display/JENKINS/Gitlab+Hook+Plugin)  
[Build Token Root Plugin](https://wiki.jenkins.io/display/JENKINS/Build+Token+Root+Plugin)  
>如果没有安装Build Token Root Plugin，则在Test Hook时会报403错误

2. 简单配置  

 1. jenkins的部署配置选择“远程构建”选项，验证令牌可使用随机token  
生成随机token的命令`openssl rand -hex 12`  
![](media/15121319384984.jpg)      
 2. gitlab中配置“webhooks“  
url路径格式：`http://jenkins服务器地址:8080/buildByToken/build?job=项目名&token=token值`  
插件使用详情可查看[官网文档](https://wiki.jenkins.io/display/JENKINS/Build+Token+Root+Plugin)  
![](media/15121322507924.jpg)



