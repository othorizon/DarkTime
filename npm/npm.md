# npm 使用

## 命令

```bash
#查看全局安装的包
npm ls -g --depth 0
#npm安装模块
## 利用 npm 安装xxx模块到当前命令行所在目录；
npm install xxx
## 利用npm安装全局模块xxx
npm install -g xxx

#本地安装时将模块写入package.json中：
##安装但不写入package.json；
npm install xxx
## 安装并写入package.json的”dependencies”中；
npm install xxx –save
## 安装并写入package.json的”devDependencies”中。
npm install xxx –save-dev

#npm 删除模块
## 删除xxx模块
npm uninstall xxx
## 删除全局模块xxx
npm uninstall -g xxx

# 执行安装到本地的模块
npx xxx
`npm bin`/xxx # npm bin 命令会返回bin的安装目录

```

## 资料
[npm 模块安装机制简介 - 阮一峰的网络日志](http://www.ruanyifeng.com/blog/2016/01/npm-install.html)

[如何发布自己模块到NPM](https://www.jianshu.com/p/f5d4c891830f)
[深入 Node 模块的安装和发布 - 前端涨姿势 - SegmentFault 思否](https://segmentfault.com/a/1190000004221514)
[npm 与 package.json 快速入门 - CSDN博客](https://blog.csdn.net/u011240877/article/details/76582670)


[vue.js - npm 如何引用github上的依赖包](https://segmentfault.com/q/1010000010884415)
[git-urls-as-dependencies](https://docs.npmjs.com/files/package.json#git-urls-as-dependencies)
Git urls can be of the form:

git://github.com/user/project.git#commit-ish
git+ssh://user@hostname:project.git#commit-ish
git+http://user@hostname/project/...
git+https://user@hostname/project...
The commit-ish can be any tag, sha, or branch which can be supplied as an argument to git checkout. The default is master.



## 全局模块
[node.js - npm如何将模块安装到全局？ - SegmentFault 思否](https://segmentfault.com/q/1010000000396247)

`npm -g install eventproxy`表示用全局模式安装包，但是不代表你可以在任何地方require到这个包，在Linux下它其实把包安装到`/usr/local/lib/node_modules`下，然后通过软连接的方式把包中bin目录下的可执行文件链接到`/usr/local/bin`下面（和node一个目录）。

但是我希望能够在任何地方都能够require这个包。
我的做法是修改/etc/profile
在文件尾加上一行export NODEPATH="NODEPATH:/usr/local/lib/node_modules"
然后source /etc/profile

## 模块升级

使用 npm-check插件：[npm更新模块同步到package.js中 - CSDN博客](https://blog.csdn.net/wkl305268748/article/details/76641323),[Node.js开源项目推荐：npm模块升级工具 npm-check - CNode技术社区](http://cnodejs.org/topic/5705cd70c5f5b4a959e9192a)
[npm模块管理进阶 — npm-check + cnpm 构建完美的包更新环境](https://segmentfault.com/a/1190000011085967)

原始方案：

```bash
#检查过期
$ npm outdated

Package                      Current  Wanted  Latest  Location
hexo-generator-index-sticky    0.2.3   0.2.3   0.3.0  hexo-site

# npm install xxx@x.x.x
$ npm install hexo-generator-index-sticky@@latest
```

## npm install

>--save是对生产环境所需依赖的声明(开发应用中使用的框架，库),--save-dev是对开发环境所需依赖的声明(构建工具，测试工具).正常使用npm install时，会下载dependencies和devDependencies中的模块，当使用npm install --production或者注明NODE_ENV变量值为production时，只会下载dependencies中的模块。
[npm介绍及常见命令](https://blog.csdn.net/altaba/article/details/77429398)