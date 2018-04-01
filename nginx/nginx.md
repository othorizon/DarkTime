# nginx

## nginx 做域名绑定

[nginx学习（十）——nginx的配置系统6之server_name - CSDN博客](http://blog.csdn.net/xxcupid/article/details/52515237)
[Nginx中的默认主机及location设置（摘自淘宝） - CSDN博客](http://blog.csdn.net/u010566813/article/details/51274954)

## nginx 配置ssl https请求

[Nginx启动SSL功能，并进行功能优化，你看这个就足够了 - 雨~桐 - 博客园](https://www.cnblogs.com/piscesLoveCc/p/6120875.html)
[FreeSSL - 一个申请免费HTTPS证书的网站](https://freessl.org/)
[Nginx+Https配置 - 倚楼听风雨 - SegmentFault 思否](https://segmentfault.com/a/1190000004976222)
[配置HTTPS服务器](http://tengine.taobao.org/nginx_docs/cn/docs/http/configuring_https_servers.html)

## nginx 配置301跳转

[http://www.laozuo.org/9953.html](http://www.laozuo.org/9953.html)
server_name example.com;
return 301 https://$host$request_uri;