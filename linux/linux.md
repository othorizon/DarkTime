# linux脚本

[TOC]

----

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