# shell脚本

- [shell脚本](#shell脚本)
  - [一些特性](#一些特性)
  - [shell中对参数的处理](#shell中对参数的处理)
  - [防止脚本同时运行多个 防止脚本多开](#防止脚本同时运行多个-防止脚本多开)

编写shell脚本的参考例子：[ClassicOldSong/shadow](https://github.com/ClassicOldSong/shadow)

## 一些特性

sh脚本可以直接放到`/usr/local/bin`等bin目录下去作为命令工具直接使用

```bash
$ vim /usr/local/bin/demo
#文件内容
#!/bin/bash
echo 123

$ chmod +x /usr/local/bin/demo #加权限

$ demo #执行命令
123
```

[演示](https://asciinema.org/a/lnmVbhObG6XeV27VxVZGeuTIt)

---

linux一行命令中可以指定环境变量给要执行的命令，如

```bash
$ cat demo.sh
echo $ENV1
echo $ENV2

$ ENV1=a ENV2=b sh demo.sh
a
b
```

## shell中对参数的处理

```bash
#echo -n "$1"|md5sum|awk '{print $1}'|xargs -I{} kubectl -n {} $2 $3 $4 $5 $6 $7
id=`echo -n "$1"|md5sum|awk '{print $1}'`
if [ -z $2 ];then
echo $id
exit 0
fi
kubectl -n $id ${@:2}
# ${@:2} 会返回第二个开始的所有参数，参考： https://www.jianshu.com/p/eaa3406b7cff
# 示例： ${@:1:$#-1} : 其中$@是列表形式列出所有的传入参数，然后:1是从第一个参数开始，后面不加任何东西的话是一直到结尾，而加:$#-1是$#是参数总个数-1，即显示除去最后一个参数外的所有参数。
```

## 防止脚本同时运行多个 防止脚本多开

如何避免shell脚本被同时运行多次
转自：http://www.etwiki.cn/linux/2786.html

比如说有一个周期性(cron)备份mysql的脚本，或者rsync脚本，

如果出现意外，运行时间过长，
很有可能下一个备份周期已经开始了，当前周期的脚本却还没有运行完，
显然我们都不愿意看到这样的情况发生。

其实只要对脚本自身做一些改动，就可以避免它被重复运行。

```bash
#!/bin/bash

LOCK_NAME="/tmp/my.lock"
if [[ -e $LOCK_NAME ]] ; then
echo "re-entry, exiting"
exit 1
fi

### Placing lock file
touch $LOCK_NAME
echo -n "Started..."

### 开始正常流程
### 正常流程结束

### Removing lock
rm -f $LOCK_NAME

echo "Done."
```

当脚本开始运行时， 创建 /tmp/my.lock文件，
这时如果再次运行此脚本，发现存在my.lock，就退出，
脚本运行结束时删除这个文件。

大多数情况下，这样做都没有什么问题。
意外1) 如果同时运行二次此脚本， 二个进程都会发现my.lock不存在，然后都可以继续执行。
意外2) 如果脚本在运行过程中意外退出， 没有来得及删除 my.lock文件， 那么就悲剧了。

修改如下：

```bash
#!/bin/bash

LOCK_NAME="/tmp/my.lock"
if ( set -o noclobber; echo "$$" > "$LOCK_NAME") 2> /dev/null; 
then
trap 'rm -f "$LOCK_NAME"; exit $?' INT TERM EXIT

### 开始正常流程
### 正常流程结束

### Removing lock
rm -f $LOCK_NAME
trap - INT TERM EXIT
else
echo "Failed to acquire lockfile: $LOCK_NAME." 
echo "Held by $(cat $LOCK_NAME)"
exit 1
fi



echo "Done."
```

set -o noclobber 的意思：

If set, bash does not overwrite an existing file with the >, >&, and <> redirection operators.

这样就能保证my.lock只能被一个进程创建出来。比touch靠谱多了。

trap 可以捕获各种信号，然后做出处理：
INT 用来处理 ctrl+c取消脚本执行的情况。
TERM 用来处理 kill -TERM pid 的情况。
EXIT 不清楚

另外，对于 kill -9 无效。。

还记得N年前，在php群里面，草人也问过这个问题，
我们给的答案是 ps aux|grep filename |wc -l ，哈哈，真2。