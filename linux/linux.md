# linux脚本

[TOC]

----

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

### 批量文件末尾追加内容

网上的一些方法，使用`sed`等命令，我在mac上实验总是失败，于是用了这个简单的方案

```base
find . -name "*.sql"|xargs -I "{}" echo "echo "LIMIT 10" >> {}"|sh
```
