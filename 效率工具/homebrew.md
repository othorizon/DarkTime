# homebrew的使用

## 使用
1. 先安装HomeBrew(参考官网) 
搜索：`brew search` MySQL
查询：`brew info mysql` 主要看具体的信息，比如目前的版本，依赖，安装后注意事项等
更新：`brew update` 这会更新 Homebrew 自己，并且使得接下来的两个操作有意义——
检查过时（是否有新版本）：`brew outdated` 这回列出所有安装的软件里可以升级的那些
升级：`brew upgrade` 升级所有可以升级的软件们
清理：`brew cleanup` 清理不需要的版本极其安装包缓存

2. `brew update` 更新brew
3. `brew install MongoDB` 安装mongodb数据库
4. `brew services start mongodb` 启动mongodb数据库
5. `brew services stop mongodb` 停止mongodb数据库

## 技巧
homebrew有个`services`命令可以管理自启动服务

``` shell
brew services [-v|--verbose] [list | run | start | stop | restart | cleanup] [...]
    Easily start and stop formulae via launchctl.
```

