# RESTful API

## 参考

[服务端指南 | 良好的 API 设计指南 - 掘金](https://juejin.im/post/59826a4e518825359c5e72d1)
[RESTful 架构详解 | 菜鸟教程](https://www.runoob.com/w3cnote/restful-architecture.html)
[RESTful API 最佳实践 - 阮一峰的网络日志](http://www.ruanyifeng.com/blog/2018/10/restful-api-best-practices.html)
[RESTful API浅谈 - 老_张 - 博客园](https://www.cnblogs.com/imyalost/p/7923230.html)

## 概述

### 资源与URI

动词 http  method+ 名词uri

```text
【GET】          /users                # 查询用户信息列表
【GET】          /users/1001           # 查看某个用户信息
【POST】         /users                # 新建用户信息
【PUT】          /users/1001           # 更新用户信息(全部字段)
【PATCH】        /users/1001           # 更新用户信息(部分字段)
【DELETE】       /users/1001           # 删除用户信息
```

避免多级url，而是采用过滤条件

```text
GET /authors/12/categories/2
应该换成
GET /authors/12?categories=2
```

>使用_或-来让URI可读性更好
使用/来表示资源的层级关系
使用?用来过滤资源
,或;可以用来表示同级资源的关系

**动词的覆盖**
有些客户端无法支持PUT、DELETE等方法，这种情况必须使用POT来模拟，
客户端可以在发出的HTTP请求中，加上`X-HTTP-Method-Override`属性，告诉服务器使用哪一个动词

```http
POST /api/Person/4 HTTP/1.1  
X-HTTP-Method-Override: PUT
```

#### 版本

api接口升级时，为了去兼容一些暂时无法升级的客户端（比如移动端的app是运行在客户设备上，不像服务器上的客户端可以直接升级），需要保留旧版本接口。
为了解决版本兼容问题，在RESTful API中使用了版本号，常见的情况下在url使用版本号，也可以在header中使用版本号，不过一般都在url中，这样更直观。

```http
【GET】  /v1/users/{user_id}  // 版本 v1 的查询用户列表的 API 接口
【GET】  /v2/users/{user_id}  // 版本 v2 的查询用户列表的 API 接口
```

#### 提供链接

为了方便api使用者，让资源之间具有连通性（超媒体），在响应体中，返回其他资源的url

例如，GitHub 的 API 都在 [https://api.github.com/](https://api.github.com/) 这个域名。访问它，就可以得到其他 URL。

```json
{
  ...
  "feeds_url": "https://api.github.com/feeds",
  "followers_url": "https://api.github.com/user/followers",
  "following_url": "https://api.github.com/user/following{/target}",
  "gists_url": "https://api.github.com/gists{/gist_id}",
  "hub_url": "https://api.github.com/hub",
  ...
}
```

在返回头中返回link

```bash
url -I https://api.github.com/orgs/github/repos
HTTP/1.1 200 OK

ETag: "c4f394acb6f2603b159ee8d694d308cb"
X-GitHub-Media-Type: github.v3; format=json
Link: <https://api.github.com/organizations/9919/repos?page=2>; rel="next", <https://api.github.com/organizations/9919/repos?page=10>; rel="last"
```

### 状态码

应该尽量在http状态码中表述错误状态，而不是统一使用200，统一200的方式实际上几近取消了状态码

```http
HTTP/1.1 400 Bad Request
Content-Type: application/json

{
  "error": "Invalid payoad.",
  "detail": {
     "surname": "This field is required."
  }
}
```

## 附加

### 常用状态码

**GET**
安全且幂等
获取表示
变更时获取表示（缓存）
200（OK） - 表示已在响应中发出
204（无内容） - 资源有空表示
301（Moved Permanently） - 资源的URI已被更新
303（See Other） - 其他（如，负载均衡）
304（not modified）- 资源未更改（缓存）
400 （bad request）- 指代坏请求（如，参数错误）
404 （not found）- 资源不存在
406 （not acceptable）- 服务端不支持所需表示
500 （internal server error）- 通用错误响应
503 （Service Unavailable）- 服务端当前无法处理请求

**POST**
不安全且不幂等
使用服务端管理的（自动产生）的实例号创建资源
创建子资源
部分更新资源
如果没有被修改，则不过更新资源（乐观锁）
200（OK）- 如果现有资源已被更改
201（created）- 如果新资源被创建
202（accepted）- 已接受处理请求但尚未完成（异步处理）
301（Moved Permanently）- 资源的URI被更新
303（See Other）- 其他（如，负载均衡）
400（bad request）- 指代坏请求
404 （not found）- 资源不存在
406 （not acceptable）- 服务端不支持所需表示
409 （conflict）- 通用冲突
412 （Precondition Failed）- 前置条件失败（如执行条件更新时的冲突）
415 （unsupported media type）- 接受到的表示不受支持
500 （internal server error）- 通用错误响应
503 （Service Unavailable）- 服务当前无法处理请求

**PUT**
不安全但幂等
用客户端管理的实例号创建一个资源
通过替换的方式更新资源
如果未被修改，则更新资源（乐观锁）
200 （OK）- 如果已存在资源被更改
201 （created）- 如果新资源被创建
301（Moved Permanently）- 资源的URI已更改
303 （See Other）- 其他（如，负载均衡）
400 （bad request）- 指代坏请求
404 （not found）- 资源不存在
406 （not acceptable）- 服务端不支持所需表示
409 （conflict）- 通用冲突
412 （Precondition Failed）- 前置条件失败（如执行条件更新时的冲突）
415 （unsupported media type）- 接受到的表示不受支持
500 （internal server error）- 通用错误响应
503 （Service Unavailable）- 服务当前无法处理请求

**DELETE**
不安全但幂等
删除资源
200 （OK）- 资源已被删除
301 （Moved Permanently）- 资源的URI已更改
303 （See Other）- 其他，如负载均衡
400 （bad request）- 指代坏请求
404 （not found）- 资源不存在
409 （conflict）- 通用冲突
500 （internal server error）- 通用错误响应
503 （Service Unavailable）- 服务端当前无法处理请求