# 附加文档

## scp远程复制

[scp如何跨过中转主机直接传输文件？ - 知乎](https://www.zhihu.com/question/38216180)
本地文件 通过 A机 到 B机
`scp -o ProxyCommand='ssh A -W %h:%p' /tmp/a B:/tmp/a  # B:/tmp/b 不存在则 /tmp/a -> B:/tmp/b，不然 /tmp/a -> B:/tmp/b/a`
上面命令假设 A 可以免密码登录 B，如果不是需要传递 authentication agenteval 

```bash
$(ssh-agent)
ssh-add IDENTITY_FILE_TO_ACCESS_B
scp -o ProxyCommand='ssh -A A -W %h:%p' /tmp/a B:/tmp/a
```

可以经过多个 jump server，本地文件 通过 A机、B机 到 C机
`scp -o ProxyCommand='ssh -A A ssh -A B -W %h:%p' /tmp/a C:/tmp/a`
另外，A机文件 到 B机可以用 scp -3，较新的 openssh 带
`scp -pr3 A:/tmp/a B:/tmp/b`
scp 有很多弊病scp 选项通常带 -p -r，保留 modification times, access times, and modes，递归复制，但依然无法保留 owner, group, acl, extended attributes 等，且时间戳精度为 秒根据 How the SCP protocol works (Jan Pechanec's weblog) 时间戳精度最多到 毫秒路径末尾斜杠会被去除，不像 rsync 可以指定几种语义

```bash
eval $(ssh-agent)
ssh-add IDENTITY_FILE_TO_ACCESS_B
rsync -a -e 'ssh -A A ssh' /tmp/a/ B:/tmp/b/
```

本地文件 通过 A机、B机 到 C机

```bash
eval $(ssh-agent)
ssh-add IDENTITY_FILE_TO_ACCESS_B_AND_C
rsync -a -e 'ssh -A A ssh -A B' /tmp/a/ C:/tmp/b/
```
