# git配置多个远程仓库

**需求**
想要把项目同步备份到coding中一份，期望每次push代码时可以同步push到coding仓库中

**解决方案**

1. 在git仓库根目录执行
  `git remote add --mirror=push coding git@git.coding.net:example/example.git`,添加一个镜像仓库，用来初始化镜像仓库数据或者pull镜像仓库数据时使用。
   >对镜像仓库push数据时相当于执行`git push --mirror`，**会强制覆盖仓库中的数据** ，会以镜像的方式把包括所有分支和历史的commit提交到目标仓库

2. 执行

    ```bash
    git remote set-url origin --push --add git@git.coding.net:example/example.git
    git remote set-url origin --push --add git@github.com:example/example.git
    ```

    > **一定要把两个都设置上**:设置`origin`的pushurl为这两个远程仓库url，当设置了`pushurl`之后便不会再使用`url`作为默认的pushurl了，所以一定要把两个都设置上

3. 第一次设置后先执行`git push coding`把origin的代码镜像到coding仓库
4. 使用
 使用时修改文件之后只需要正常的执行`git push`便会向两个目标仓库都推送一遍。

```git git config
[remote "origin"]
        url = git@github.com:example/example.git
        fetch = +refs/heads/*:refs/remotes/origin/*
        pushurl = git@git.coding.net:example/example.git
        pushurl = git@github.com:example/example.git
[branch "master"]
        remote = origin
        merge = refs/heads/master
[remote "coding"]
        url = git@git.coding.net:example/example.git
        mirror = true
```

**另外**
不推荐在镜像仓库修改文件，但是如果真的在镜像仓库修改了文件而需要同步到原始仓库，那么先`git pull coding`获取镜像仓库的最新代码，然后`check out origin master` ,然后在merge代码过来。具体可以借鉴[Syncing a fork](https://help.github.com/articles/syncing-a-fork/)GitHub官网的fork代码同步的操作步骤，

**参考**
[git push如何至两个git仓库 - SegmentFault 思否](https://segmentfault.com/q/1010000000646988)
[准备更换git托管，如何迁移原git仓库 - SegmentFault 思否](https://segmentfault.com/q/1010000000124379)
[git本地仓库关联多个remote,怎么用本地一个分支向不同remote不同分支推送代码？ - 知乎](https://www.zhihu.com/question/46543115)

---
