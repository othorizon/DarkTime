# linux screen 工具

[Screen User’s Manual](https://www.gnu.org/software/screen/manual/screen.html)

## 常用命令

### 修改session name

创建session 时，使用参数指定名字： `screen -S my_screen_name`

修改已有session name：`ctrl+a :sessionname my_screen_name`

## FAQ

### screen中出现命令无法使用问题 环境变量缺失文图

这是因为进入screenn之后，用户环境变量没有加载（比如 ~/.bashrc),  
在screen的配置文件: `～/.screenrc`文件或者 `/etc/screenrc` 或者其他在`man screen`中提及的文件中添加如下内容：

```bash
# make the shell in every window a login shell
shell -$SHELL
```

参考：[How do I ask screen to behave like a standard bash shell?](https://serverfault.com/questions/126009/how-do-i-ask-screen-to-behave-like-a-standard-bash-shell)  [Linux screen如何加载用户配置 - yasaken - CSDN博客](https://blog.csdn.net/yasaken/article/details/7418583)

### screen 中使用256色 ls等命令没有颜色显示

screen默认使用8-color，  
在screen的配置文件: `～/.screenrc`文件或者 `/etc/screenrc` 或者其他在`man screen`中提及的文件中添加如下内容：

```bash
term screen-256color
```

参考： [GNU Screen #use_256_colors - ArchWiki](https://wiki.archlinux.org/index.php/GNU_Screen#Use_256_colors)