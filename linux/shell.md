# shell脚本

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