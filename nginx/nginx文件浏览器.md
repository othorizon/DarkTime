# ngix 文件浏览器 文件服务器


## nginx配置

```
...
server {
        listen       80;
        server_name  localhost;

        #charset koi8-r;

        #access_log  logs/host.access.log  main;

        location / {
            root   /opt/boss-html;
            index  index.html index.htm;
        }
        location /files {
            alias   /usr/local/axure/workspace/git_home/;
            charset utf-8;
            autoindex on; # 索引
            autoindex_exact_size off; # 显示文件大小
            autoindex_localtime on; # 显示文件时间
        }
... 
```

说明：

autoindex on; 
打开索引另外两个参数最好也加上去:

autoindex_exact_size off;
默认为on，显示出文件的确切大小，单位是bytes。
改为off后，显示出文件的大概大小，单位是kB或者MB或者GB
 
autoindex_localtime on;
默认为off，显示的文件时间为GMT时间。
改为on后，显示的文件时间为文件的服务器时间

charset utf-8; 中文编码

**alias 和 root的区别**
使用root时 location后面配置的地址会跟在root地址后面来请求访问，alias则不会

先看root的用法
location /request_path/image/ {
    root /local_path/image/;
}
这样配置的结果就是当客户端请求 /request_path/image/cat.png 的时候， 
Nginx把请求映射为/local_path/image/**request_path/image/**cat.png

再看alias的用法
location /request_path/image/ {
    alias /local_path/image/;
}
这时候，当客户端请求 /request_path/image/cat.png 的时候， 
Nginx把请求映射为/local_path/image/cat.png 

## Jenkins配置 实现自动部署

jenkins execute shell 执行脚本如下：

```
#!/bin/sh -ex

FILE_HOME=/usr/local/axure/workspace/git_home/

REMOTE_HOST=10.69.57.76

touch 数据更新日期_`date "+%Y-%m-%d_%H-%M-%S"`

# convert 2 pdf
mkdir doc_pdf
mkdir doc_html
mkdir xls_html
find . -name '*.doc*' -print0 |xargs -0 /usr/bin/libreoffice  --invisible --convert-to pdf --outdir 'doc_pdf'
find . -name '*.doc*' -print0 |xargs -0 /usr/bin/libreoffice  --invisible --convert-to html --outdir 'doc_html'
find . -name '*.xls*' -print0 |xargs -0 /usr/bin/libreoffice  --invisible --convert-to html --outdir 'xls_html'

ssh $REMOTE_HOST "rm -rf ${FILE_HOME}*"

scp -r * $REMOTE_HOST:$FILE_HOME

ssh $REMOTE_HOST "chmod -R 755 ${FILE_HOME}"

```

说明：
libreoffice 是把office文档转换成可以页面直接看的文档，具体说明看文档 `效率工具/linux-office.md`
jenkins 可以部署为通过钩子来自动触发，具体看文档 `jenkins/gitlab触发jenkins自动构建.md`

