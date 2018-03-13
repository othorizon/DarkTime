# maven使用

## maven多模块

maven的多模块，由于项目比较大，每次修改后构建整个工程耗时太久，需要单独构建某个模块，mvn有支持的选项：

```bash
-pl, --projects
        Build specified reactor projects instead of all projects
-am, --also-make
        If project list is specified, also build projects required by the list
-amd, --also-make-dependents
        If project list is specified, also build projects that depend on projects on the list
```

首先切换到工程的根目录，
单独构建模块 pingjuan-web，同时会构建 pingjuan-web 模块依赖的其他模块
`$ mvn install -pl pingjuan-web -am`
单独构建模块 pingjuan-common，同时构建依赖模块 pingjuan-common 的其他模块
`$ mvn install -pl pingjuan-common -am -amd`

## maven版本管理

参考：
[Versions maven plugin 修改版本 - 详细使用](http://blog.csdn.net/ggbomb2/article/details/78316068)
[versions-maven-plugin插件官网](http://www.mojohaus.org/versions-maven-plugin/index.html)

当使用此插件在父Maven项目下，运行如下命令将更新全部项目的版本号，包括子项目之间的依赖也都同步更新：
`mvn versions:set -DnewVersion=2.0-SNAPSHOT`

当进入到子Maven项目下，运行如下命令将更新全部项目对该子项目引用的版本号：
`mvn versions:set -DnewVersion=2.1-SNAPSHOT`

当更改版本号时有问题，可以通过以下命令进行版本号回滚：
`mvn versions:revert`

如果一切都没有问题，那就直接提交版本号：
`mvn versions:commit`
修改版本所产生的backup文件会在commit后自动删除。

## maven打tar.gz包来部署

[maven assembly打tar.gz包。 - CSDN博客](http://blog.csdn.net/sdlyjzh/article/details/53396370)