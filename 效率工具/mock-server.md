# 简单Mock服务（moco）

下载地址：https://github.com/dreamhead/moco

## 简单使用 （Standalone Moco Runner）

[Qucik Start 官方说明](https://github.com/dreamhead/moco#quick-start)
[Standalone Moco Runner Download（v 0.11.1)](http://central.maven.org/maven2/com/github/dreamhead/moco-runner/0.11.1/moco-runner-0.11.1-standalone.jar)

### 启动

`java -jar moco-runner-<version>-standalone.jar http -p 12306 -s 22306 -c foo.json`
`-p`启动端口
`-s`shutdown端口，可以不指定，会默认分配。shutdown命令`java -jar moco-runner-<version>-standalone.jar shutdown -s 9527`
`-c`配置文件

### 配置


``` json
[
    {
        "request": 
          {
            "uri": "/json",
            "method" : "post" #如果不指定method则get、post均可以
          },
        "response": 
          {
            "json": #json格式返回值
              {
                "foo" : "bar"
              }
          }
    },
    {
        "request": 
          {
            "uri": "/text",
            "method" : "get"
          },
        "response": 
          {
             "text" : "bar" #text格式返回值
          }
    }
]
```



