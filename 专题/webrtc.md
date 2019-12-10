# webrtc技术分析

## 参考

[WebRTC：连接建立过程的网络穿透](https://zhuanlan.zhihu.com/p/75387873)
[WebRTC 初体验](https://blog.whezh.com/webrtc-first-experience/)
[WebRTC 是如何进行通信的，WebRCT 的三种网络结构 | 野狗 WebRTC 专栏 | Wilddog Blog](https://blog.wilddog.com/?p=2196)

相关链接：
[群晖 nas 的外网访问是直连 nas 服务器的吗；以及 NAT 技术的一些问题请教，谢谢 - V2EX](https://www.v2ex.com/t/625035)

## 知识

>因为NAT带来的网络穿透问题，不管是直接通信，还是间接通信，都不是简单的事情。WebRTC通过ICE框架来解决网络穿透的问题，并对应用开发者屏蔽了复杂的技术细节。
ICE (Interactive Connectivity Establishment) ，交互式连接建立，是一种NAT穿透的框架，它集成了多种NAT穿越技术，比如STUN、TURN。
直接通信STUN：Client A <——————————————> Client B
间接通信TURN：Client A <——————代理 ——————> Client B

## 项目

[基于 webrtc 的浏览器 p2p 技术 - V2EX](https://www.v2ex.com/t/241322#reply12)

webtorrent：  
基于p2p的流播放种子文件  
[WebTorrent - Streaming browser torrent client](https://webtorrent.io/)  
[webtorrent/webtorrent - github](https://github.com/webtorrent/webtorrent)

filepizza：  
基于p2p的文件传输，也依赖了webtorrent的技术
[kern/filepizza - github](https://github.com/kern/filepizza)  
