# 代理

## linux开启代理客户端-trojan

客户端下载地址：https://github.com/trojan-gfw/trojan
安装文档：https://github.com/trojan-gfw/trojan/wiki/Binary-&-Package-Distributions

**docker模式安装**

```bash
#必须使用host模式，否则无法将docker里的服务作为宿主机的代理服务去连接
docker run -itd --name trojan --network host  -v ./config.json:/config/config.json  trojangfw/trojan

#启动之后，使用export命令开启代理，trojan只有socks5模式
export http_proxy=socks5://127.0.0.1:1080 https_proxy=socks5://127.0.0.1:1080 all_proxy=socks5://127.0.0.1:1080
```

config.json 文件可以参考“https://trojan-gfw.github.io/trojan/config” 中的“A valid client.json” 章节。

具体的配置来源，如果是shadowsocks官网购买的，则可以直接在官网下载配置。
