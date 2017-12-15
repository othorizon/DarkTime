# maven使用

## maven多模块
maven的多模块，由于项目比较大，每次修改后构建整个工程耗时太久，需要单独构建某个模块，mvn有支持的选项：

```
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

