# nginx

## nginx 做域名绑定

[nginx学习（十）——nginx的配置系统6之server_name - CSDN博客](http://blog.csdn.net/xxcupid/article/details/52515237)
[Nginx中的默认主机及location设置（摘自淘宝） - CSDN博客](http://blog.csdn.net/u010566813/article/details/51274954)

## nginx 配置ssl https请求

[Nginx启动SSL功能，并进行功能优化，你看这个就足够了](https://www.cnblogs.com/piscesLoveCc/p/6120875.html)
[FreeSSL - 一个申请免费HTTPS证书的网站](https://freessl.org/)
[Nginx+Https配置 - 倚楼听风雨 - SegmentFault 思否](https://segmentfault.com/a/1190000004976222)
[官方-配置HTTPS服务器](http://tengine.taobao.org/nginx_docs/cn/docs/http/configuring_https_servers.html)

## nginx 配置301跳转

[Nginx环境强制http 301跳转https设置记录](http://www.laozuo.org/9953.html)

```conf /etc/nginx/nginx.conf
server_name example.com;
return 301 https://$host$request_uri;
```

## nginx 反向代理

[nginx配置url重定向-反向代理-大風-51CTO博客](http://blog.51cto.com/lansgg/1575274)

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

## nginx 重定向 rewrite

[http重定向301/302/303/307](https://blog.csdn.net/reliveit/article/details/50776984)
[Nginx URL重写（rewrite）配置及信息详解](https://www.cnblogs.com/czlun/articles/7010604.html)

```conf /etc/nginx/nginx.conf
server {
        listen 80;
        server_name abc.com;
        rewrite ^/(.*)$ http://www.abc.com/$1 permanent;
}
```