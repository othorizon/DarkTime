# gitbook

## 使用

自动生成summary文件[summarybuilder](https://www.npmjs.com/package/summarybuilder)

```bash
gitbook init //初始化目录文件
gitbook help //列出gitbook所有的命令
gitbook --help //输出gitbook-cli的帮助信息
gitbook build //生成静态网页
gitbook serve //生成静态网页并运行服务器
gitbook build --gitbook=2.0.1 //生成时指定gitbook的版本, 本地没有会先下载
gitbook ls //列出本地所有的gitbook版本
gitbook ls-remote //列出远程可用的gitbook版本
gitbook fetch 标签/版本号 //安装对应的gitbook版本
gitbook update //更新到gitbook的最新版本
gitbook uninstall 2.0.1 //卸载对应的gitbook版本
gitbook build --log=debug //指定log的级别
gitbook builid --debug //输出错误信息
```

### 插件

[Plugins for GitBook](https://plugins.gitbook.com/)

启用方法：To add a plugin, create or edit your book.json to include the plugin identifier:

```json
{
    "plugins": ["thePlugin"]
}
```

**导航插件 my-navigator**
add a table of content navigator on the right top of each page, and a return to top button without smart-phone or tablet

```json
{
    "plugins": ["my-navigator"]
}
```

## 脚本

自动生成summary文件的脚本，用shell写的，逐级递归遍历
记：脚本写于2018-03-10 02:14 ，`summarybuilder`这个是其他人用npm写的工具，但是不太好用，于是自己写了一个，不会写shell深夜研究了好久才弄出来

```bash
#!/bin/bash
#set -x

getspcae(){
    tab=""
    for ((i=2;i<$1;i++))
    do
     tab=${tab}"  "
    done
    echo "${tab}"
}

writeline(){
    fpath=$1
    depth=$2
    title=`head -1 $fpath`
    title=${title:1}
    #将开头的#号去掉
    title=${title/#\#/}
    #去掉开头空格
    title=${title/# /}

    tab=`getspcae $depth`

    result="${result}${tab}* [${title}](${fpath})\n"
}
getnode(){
    title=$1
    depth=$2
    title=${title:2}
    title=${title#*/}
    tab=`getspcae $depth`

    echo "${tab}* ${title}"
}
dive(){
    local dirs=$1
    if [ -z "$dirs" ]; then return;fi


    local depth=$2
    local node=$3
    local nextdepth=`expr $2 + 1`
    #写节点
    if [ -n "$node" ];then
      result="${result}${node}\n"
    fi

    for dir in ${dirs[@]}
    do

        if [ -d "${dir}" ];then
          nextnode=`getnode "${dir}" ${nextdepth}`
          dive "`find ${dir} \( -iname "*.md" -or -type d \) -d 1`" $nextdepth "${nextnode}"
        else
          writeline "${dir}" ${nextdepth}
        fi
    done
}

result=""
#\( -iname "*.md" -or -type d \)
dive "`find . \( -iname "*.md" -or -type d \) ! -iname "README.MD" ! -iname "SUMMARY.MD" ! -iname "_SUMMARY.MD" ! -path "./.git" -d 1`" 1

#写文件
echo make SUMMARY.md
echo "${result}" > SUMMARY.md
echo finish
```