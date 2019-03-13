# linux

[TOC]

----

常用

```bash
ps -efww # 加上 ww可以显示完整的cmd

```

## 用户管理

```bash
#查看所有用户可以cat一下
cat /etc/passwd
#查看所有用户组可以cat一下
cat /etc/group

useradd web #创建用户
passwd web # 设置密码

#将用户 user1 添加到组 group1
usermod -aG group1 user1

#查看在线用户
who
w
who am i
#登出用户
pkill -kill -t pts/1

```

[systemctl命令_Linux systemctl 命令用法详解：系统服务管理器指令](http://man.linuxde.net/systemctl)

### 其他设置

[centos修改主机名的正确方法](https://www.cnblogs.com/zhaojiedi1992/p/zhaojiedi_linux_043_hostname.html)
`hostnamectl set-hostname centos77.magedu.com             # 使用这个命令会立即生效且重启也生效`

## 系统相关

### 开关机

[shutdown 中文手册 [金步国]](http://www.jinbuguo.com/systemd/shutdown.html)

### 系统启动

`shutdown -r now` 立即重启

`systemctl list-unit-files` 可以查看开机启动项,左边是服务名称，右边是状态，enabled是开机启动，disabled是开机不启动

#### 图形界面

>Linux系统的7个运行级别(runlevel)
运行级别0：系统停机状态，系统默认运行级别不能设为0，否则不能正常启动
运行级别1：单用户工作状态，root权限，用于系统维护，禁止远程登陆
运行级别2：多用户状态(没有NFS)
运行级别3：完全的多用户状态(有NFS)，登陆后进入控制台命令行模式
运行级别4：系统未使用，保留
运行级别5：X11控制台，登陆后进入图形GUI模式
运行级别6：系统正常关闭并重启，默认运行级别不能设为6，否则不能正常启动

切换级别： `init N`
init 3 关闭图形界面，init 5 打开图形界面

**默认启动级别配置**

/etc/inittab 文件内容如下：

```bash
# systemd uses 'targets' instead of runlevels. 
# by default, there are two main targets:
#
# multi-user.target: analogous to runlevel 3
# graphical.target: analogous to runlevel 5
#
# To view current default target, run:
# systemctl get-default
#
# To set a default target, run:
# systemctl set-default TARGET.target
```

`systemctl get-default`查看默认的target
`systemctl set-default multi-user.target` 命令行界面启动系统
`systemctl set-default graphical.target` 图形界面启动系统

----

## 杂项

### 端口转发 端口映射

[SSH隧道：内网穿透实战](https://cherrot.com/tech/2017/01/08/ssh-tunneling-practice.html)

```bash
ssh -v -C -N -L 5901:localhost:5902 hostB
# 通过hostB的ssh通道，将本地的5901端口转发到localhost的5902
# 在这个例子里目标localhost也就是hostB机器
# -v debug信息，-C 压缩 -N 不执行远程登陆，用于端口转发场景。 -L 映射本地端口到远程服务器
# -R 映射远程端口到本地
```

### 创建环境变量

全局针对所有用户的环境变量可在`/etc/profile.d/`目录下通过创建一个sh脚本来实现，这样方便维护，例如：

```bash
$ cat /etc/profile.d/path.sh

# mvn
export M2_HOME=/usr/local/apps/apache-maven-3.5.4
export PATH=$PATH:$JAVA_HOME/bin:$M2_HOME/bin
```

### 零散

[Why is scp so slow and how to make it faster?](https://unix.stackexchange.com/questions/238152/why-is-scp-so-slow-and-how-to-make-it-faster)
在大文件慢网速的情况下，尽量使用`rsync`代替`scp`，rsync支持断点续传。

----

## 字符串操作详解

[linux shell 字符串操作详解](https://www.cnblogs.com/gaochsh/p/6901809.html)

## linux磁盘操作

[Linux 系统格式化磁盘并挂载分区 - CSDN博客](http://blog.csdn.net/nahancy/article/details/52201121) [Linux 系统格式化磁盘并挂载分区 - yinxiang](https://app.yinxiang.com/shard/s9/nl/679699/fba3176d-fde4-4374-8614-9ac7ab3ef09e?title=Linux%20%E7%B3%BB%E7%BB%9F%E6%A0%BC%E5%BC%8F%E5%8C%96%E7%A3%81%E7%9B%98%E5%B9%B6%E6%8C%82%E8%BD%BD%E5%88%86%E5%8C%BA%20-%20CSDN%E5%8D%9A%E5%AE%A2) 

[linux下查看磁盘分区的文件系统格式 - 游必有方 - 博客园](https://www.cnblogs.com/youbiyoufang/p/7607174.html)

```bash
df -h
parted -l #可以查看未挂载的文件系统类型，以及哪些分区尚未格式化。
fdisk -l
mkfs.ext4 /dev/vdb
mkdir /data
echo '/dev/vdb /data ext4 defaults 0 0' >> /etc/fstab
mount -a
```

## sed

`-i` ：直接替换文件内容
`-r`（在mac中为`-E`） ：扩展的正则表达式

### 批量替换文件内容

```bash
#grep "a.*c" -rl ./ |xargs sed -i "" "s/a.*c/abc/g"
sed -i "" "s/2017-12-21/\${statDate_current_yyyy-MM-dd}/g" **/*.sql
```

>sed命令在mac系统中后面需要跟一个参数用于备份，`sed -i ".bak" "s/a.*c/abf/g"`，如果不需要备份可以留空，但不能不屑

`**/*.sql` 可以搜索子目录

### 应用

```bash {cmd=true}
echo \'测试替换\' |sed -E "s/'(.*替换)'/'新\1'/g"
```

`-E` 扩展正则表达式 这样可以不用使用`\`来转义一些特殊字符了
`\1`是占位符，表示正则中用`()`括起来的匹配内容，`&`表示全部匹配内容v

### 删除字符

[sed:删除命令d | 老段工作室](http://www.rhce.cc/1110.html)

>employee.txt
101,John Doe,CEO
102,Jason Smith,IT Manager
103,Raj Reddy,Sysadmin
104,Anand Ram,Developer
105,Jane Miller,Sales Manager

删除第一次包含'Jason'的行到第四行(如果在前四行没有匹配'Jason'的行，这个命令只删除第四行以后匹配'Jason'的行)：

```bash
$ sed '/Jason/,4 d' employee.txt
101,John Doe,CEO
105,Jane Miller,Sales Manager
```

删除从第一次匹配'Raj'的行到最后一行：

```bash
$ sed '/Raj/,$ d' employee.txt
101,John Doe,CEO
102,Jason Smith,IT Manager
```

----

## xargs

### 复制文件到多个目录

[技术|如何在 Linux 中复制文件到多个目录中](https://linux.cn/article-8041-1.html?utm_source=rss&utm_medium=rss)

```bash {cmd=true}
#find ./ -type d -maxdepth 1 |xargs -n 1 -I '{}' cp -v -r ./dir1 {}
find ./ -type d  -maxdepth 1 |xargs -n 1 -I '{}' echo {}cp.txt

echo dir1/ dir2/ dir3/ |xargs -n 1 -I '{}' echo {}cp.txt

# -maxdepth 1 - 最大检索深度
# -n 1 - 告诉 xargs 命令每个命令行最多使用一个参数，并发送到 cp 命令中。
# -I - (mac中)指明参数的占位符
# cp – 用于复制文件。
# -v – 启用详细模式来显示更多复制细节。
```

----

## find

### 批量向文件追加内容

参考:
[sed extra characters after \ at the end of a command](https://www.cnblogs.com/meitian/p/5907562.html)
[evernote-sed extra characters after \ at the end of a command](https://app.yinxiang.com/shard/s9/nl/679699/add0426b-388c-4da0-a910-14340b3eae75/)

#### 批量文件头追加内容

```bash
find . -name "*.txt" |xargs -n 1 -I "{}" sed -i "" '1i\
insert new line\
new line' {}
find . -name "*.txt" |xargs -n 1 -I "{}" sed -i ""  "1i\\
insert new line\\
new line" {}
```

>mac中，在追加内容前要换行，直接使用`i\insert new line`是会报错的，表示换行符的时候也通过换行来表示。
sed命令向文件内追加内容与正则替换不同的是，无法直接批量操作多个文件，因此要借助`find`命令，或者`perl`命令来实现逐一替换

示例：

```bash {cmd=true}
# sed后面可以跟 i参数直接替换文本 `sed -i ""`

find . -name "test.txt" |xargs -n 1 -I "{}" sed '1i\
new line1\
new line2' {}

echo "------"

find . -name "test.txt" |xargs -n 1 -I "{}" sed  "1i\\
new line1\\
new line2" {}
```

#### 批量文件末尾追加内容

网上的一些方法，使用`sed`等命令，在mac上存在`\`换行符识别较麻烦，于是用了这个简单的方案

```bash
find . -name "*.sql"|xargs -I "{}" echo "echo "LIMIT 10" >> {}"|sh
```

### 批量修改文件名称

```bash
find .  -name "CDN-流量-流量-今日用量数据-TOP20.sql"|awk -F'/CDN-' '{print $1}' |xargs -I "{}" mv {}/CDN-流量-流量-今日用量数据-TOP20.sql {}/CDN-流量-今日用量数据-TOP20.sql

#rename 默认在mac系统上没有，可以HomeBrew安装一下
rename 's/old/new/' *.files
```

----

## 远程复制 scp rsync

### scp

使用代理服务远程复制文件，用于无法直接连接目标机器，而需要使用跳板机的时候

```bash
#本地文件 通过 A机 到 B机
scp -o ProxyCommand='ssh A -W %h:%p' /tmp/a B:/tmp/a

#示例
scp -o ProxyCommand='ssh 10.69.57.76 -W %h:%p' ./test.txt 10.111.84.141:~/
```

参考
<a href='linux-addition.md#scp远程复制' >详细说明</a>
[scp如何跨过中转主机直接传输文件？ - 知乎](https://www.zhihu.com/question/38216180)

----

## awk比较文件

```bash
awk -F,  -v OFS="," 'BEGIN{print "id,customer_id,product_type_name,product_subtype_id,measure_value,final_measure_value,bill_real_amount,measure_amount,discount,region_name,extra_params,diff"}FNR==NR{a[$1]=$7;next}{if(a[$1]!=$7)print $0,a[$1]-$7}' 96.csv 232.csv>diff96-232.csv
```

## curl

### curl测试下载速度

使用 cURL 度量 Web 站点的响应时间
参考：[for循环测试速度](/脚本/shell/实用脚本.md#实用命令)

```bash
$ curl -o /dev/null -s -w '%{time_connect}:%{time_starttransfer}:%{time_total}\n' 'http://kisspeach.com'
.081:0.272:0.779
```

下面给出对kisspeach.com站点执行 curl 命令的情况.输出通常是 HTML 代码,通过 -o 参数发送到 /dev/null.-s 参数去掉所有状态信息.-w 参数让 curl 写出列出的计时器的状态信息： curl 使用的计时器：
计时器 | 描述
----|---
time_connect | 建立到服务器的 TCP 连接所用的时间
time_starttransfer | 在发出请求之后,Web 服务器返回数据的第一个字节所用的时间
time_total | 完成请求所用的时间
time_namelookup | DNS解析时间,从请求开始到DNS解析完毕所用时间(记得关掉 Linux 的 nscd 的服务测试)
speed_download | 下载速度，单位-字节每秒

这些计时器都相对于事务的起始时间,甚至要先于 Domain Name Service（DNS）查询.因此,在发出请求之后,Web 服务器处理请求并开始发回数据所用的时间是 0.272 – 0.081 = 0.191 秒.客户机从服务器下载数据所用的时间是 0.779 – 0.272 = 0.507 秒. 通过观察curl数据及其随时间变化的趋势,可以很好地了解站点对用户的响应性.以上变量会按CURL认为合适的格式输出，输出变量需要按照%{variable_name}的格式，如果需要输出%，double一下即可，即%%，同时，\n是换行，\r是回车，\t是TAB。 当然,Web 站点不仅仅由页面组成.它还有图像、JavaScript 代码、CSS 和 cookie 要处理，curl很适合了解单一元素的响应时间,但是有时候需要了解整个页面的装载速度.

## linux wifi使用手册

[How do I connect to a WiFi network using nmcli?](https://askubuntu.com/questions/377687/how-do-i-connect-to-a-wifi-network-using-nmcli)
[nmcli 命令行方式连接wifi及删除连接操作](http://www.huangea.com/?p=845)
[linux 无线网卡的连接（命令行） Part2 —— network-manager(nmcli)](https://blog.csdn.net/gw569453350game/article/details/53261772)

使用network-manager 管理wifi，更为方便

列出wifi
`nmcli device wifi`  `nmcli d wifi list`  
重新扫描  
`nmcli device wifi rescan`  

连接一个新的wifi  
`nmcli device wifi connect <AP name> password <password>`  

切换已经保存过的wifi连接

```bash
nmcli connection { COMMAND | help }
   COMMAND := { list | status | up | down | delete }

   list [id <id> | uuid <id>]
   status [id <id> | uuid <id> | path <path>]
   up id <id> | uuid <id> [iface <iface>] [ap <BSSID>] [--nowait] [--timeout <timeout>]
   down id <id> | uuid <id>
   delete id <id> | uuid <id>
```

列出当前连接

```bash
$ nmcli connection show

NAME                      UUID                                   TYPE              TIMESTAMP-REAL
Rishbh-Q1000              cd79a7a1-1cf4-49c3-ad58-21ab17d1ba05   802-11-wireless   Thursday 18 September 2014 05:32:34 PM IST
```

连接/切换wifi
`nmcli c up uuid cd79a7a1-1cf4-49c3-ad58-21ab17d1ba05`  `nmcli c up id Rishbh-Q1000`

## linux aria2 下载

[Linux使用Aria2命令下载BT种子/磁力/直链文件 - Rat's Blog](https://www.moerats.com/archives/347/)

安装
执行以下命令：

yum install aria2  #CentOS系统
apt-get install aria2  #Debian/Ubuntu系统
使用
1、直链下载
下载直链文件，只需在命令后附加地址，如：

aria2c http://xx.com/xx
如果需要重命名为yy的话加上--out或者-o参数，如：

aria2c --out=yy http://xx.com/xx
aria2c -o yy http://xx.com/xx
使用aria2的分段和多线程下载功能可以加快文件的下载速度，对于下载大文件时特别有用。-x 分段下载，-s 多线程下载，如：

aria2c -s 2 -x 2 http://xx.com/xx
这将使用2个连接和2个线程来下载该文件。

2、BT下载
种子和磁力下载：

aria2c ‘xxx.torrnet‘
aria2c '磁力链接'
列出种子内容：

aria2c -S xxx.torrent
下载种子内编号为1、4、5、6、7的文件，如：

aria2c --select-file=1,4-7 xxx.torrent
设置bt端口：

aria2c --listen-port=3653 ‘xxx.torrent’
3、限速下载
单个文件最大下载速度：

aria2c --max-download-limit=300K -s10 -x10 'http://xx.com/xx'
整体下载最大速度：

aria2c --max-overall-download-limit=300k -s10 -x10 'http://xx.com/xx'
这些基本都是常用的几个命令，更多的可以使用man aria2c和aria2c -h查看。