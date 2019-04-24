# nginx

## nginx 做域名绑定

[nginx server_name](http://tengine.taobao.org/nginx_docs/cn/docs/http/server_names.html)
[nginx学习（十）——nginx的配置系统6之server_name - CSDN博客](http://blog.csdn.net/xxcupid/article/details/52515237)
[Nginx中的默认主机及location设置（摘自淘宝） - CSDN博客](http://blog.csdn.net/u010566813/article/details/51274954)

## nginx 配置ssl https请求

[Nginx启动SSL功能，并进行功能优化，你看这个就足够了](https://www.cnblogs.com/piscesLoveCc/p/6120875.html)
[FreeSSL - 一个申请免费HTTPS证书的网站](https://freessl.org/)
[Nginx+Https配置 - 倚楼听风雨 - SegmentFault 思否](https://segmentfault.com/a/1190000004976222)
[官方-配置HTTPS服务器](http://tengine.taobao.org/nginx_docs/cn/docs/http/configuring_https_servers.html)

```nginx
server {
    listen       443 ssl http2;
    listen       [::]:443 ssl http2;
    server_name *.rizon.top;
    ssl on;
    root html;
    index index.html index.htm;
    ssl_certificate   /etc/nginx/cert/certificate.pem;
    ssl_certificate_key  /etc/nginx/cert/certificate.key;
    ssl_session_timeout 5m;
    ssl_ciphers ECDHE-RSA-AES128-GCM-SHA256:ECDHE:ECDH:AES:HIGH:!NULL:!aNULL:!MD5:!ADH:!RC4;
    ssl_protocols TLSv1 TLSv1.1 TLSv1.2;
    ssl_prefer_server_ciphers on;
    location / {
        root /home/website;
        index index.html index.htm;
    }
}
```

## nginx 配置301跳转

[Nginx环境强制http 301跳转https设置记录](http://www.laozuo.org/9953.html)

```conf /etc/nginx/nginx.conf
server_name example.com;
return 301 https://$host$request_uri;
```

## nginx 反向代理

[nginx配置url重定向-反向代理-大風-51CTO博客](http://blog.51cto.com/lansgg/1575274)  

/etc/nginx/nginx.conf

```conf /etc/nginx/nginx.conf
server {
listen    80 default_server;
server_name    www.lansgg.com lansgg.com;
access_log    logs/lansgg.access.log main;
error_log    logs/lansgg.error.log;
root        /opt/nginx/nginx/html/lansgg;
location / {
    index index.html;
    }
location /other {
proxy_pass          http://192.168.10.129/other;
proxy_set_header    X-Real-IP $remote_addr;
    }
}
```

## 使用nginx stream

tag 代理mysql stream代理ssh tcp反向代理 nginx代理ssh  

[Module ngx_stream_core_module](http://nginx.org/en/docs/stream/ngx_stream_core_module.html)  
[caojx-git/learn nginx使用stream模块做ssh转发](https://github.com/caojx-git/learn/blob/master/notes/nginx/nginx%E4%BD%BF%E7%94%A8stream%E6%A8%A1%E5%9D%97%E5%81%9Assh%E8%BD%AC%E5%8F%91.md)  

/etc/nginx/nginx.conf

```conf /etc/nginx/nginx.conf
# 要注意不要放到http块中，因为这个不是http转发而是tcp层的转发
# stream模块使用yum安装的高版本nginx是有的，编译安装的默认可能没有

stream {
    #配置upstream是用与负载均衡的，可以不这样配置
    upstream cloudsocket {
       hash $remote_addr consistent;
      # $binary_remote_addr;
       server 192.168.182.155:3306 weight=5 max_fails=3 fail_timeout=30s;
    }
    #代理mysql
    server {
       listen 3306;#数据库服务器监听端口
       proxy_connect_timeout 10s;
       proxy_timeout 5m; #这个属性的含义不太清楚，和mysql的连接时间应该似乎有关系，时间太短似乎会导致mysql的连接被中断
       #负载均衡配置方法
       #proxy_pass cloudsocket;
       # 简单配置方法
       proxy_pass 10.69.65.96:3306;
    }

    #代理ssh
    server {
            listen 80;
            proxy_pass ssh;
            proxy_connect_timeout 1h;
            proxy_timeout 1h;
    }
}

```

## nginx 重定向 rewrite

[http重定向301/302/303/307](https://blog.csdn.net/reliveit/article/details/50776984)
[Nginx URL重写（rewrite）配置及信息详解](https://www.cnblogs.com/czlun/articles/7010604.html)

```conf /etc/nginx/nginx.conf
server {
        listen 80;
        server_name abc.com;
        #显式url跳转 永久重定向
        #rewrite ^/(.*)$ http://www.abc.com/$1 permanent;
        # 显式url跳转，临时重定向
        rewrite ^/(.*)$ https://rizon.top/tool/ last;
}
```

语法说明
    rewrite    regex   replacement    [flag];
    关键字      正则     替代内容        flag标记

正则：perl兼容正则表达式语句进行规则匹配
替代内容：将正则匹配的内容替换成replacement
flag标记：rewrite支持的flag标记

flag标记说明：
last  #本条规则匹配完成后，继续向下匹配新的location URI规则
break  #本条规则匹配完成即终止，不再匹配后面的任何规则
redirect  #返回302临时重定向，浏览器地址会显示跳转后的URL地址
permanent  #返回301永久重定向，浏览器地址栏会显示跳转后的URL地址