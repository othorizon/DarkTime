# 简单Mock服务（moco）

下载地址：https://github.com/dreamhead/moco

## 简单使用 （Standalone Moco Runner）

[Qucik Start 官方说明](https://github.com/dreamhead/moco#quick-start)
[Standalone Moco Runner Download（v 0.11.1)](http://central.maven.org/maven2/com/github/dreamhead/moco-runner/0.11.1/moco-runner-0.11.1-standalone.jar)

### 启动

`java -jar moco-runner-<version>-standalone.jar http -p 12306 -s 22306 -c settings.json`
`-p`启动端口
`-s`shutdown端口，可以不指定，会默认分配。shutdown命令`java -jar moco-runner-<version>-standalone.jar shutdown -s 9527`
`-c`配置文件

### 配置

```json settings.json
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

### 匹配json请求参数

[JSON Request](https://github.com/dreamhead/moco/blob/master/moco-doc/apis.md#json-request)

#### 完全匹配json请求

```json
{
    "request": {
        "uri": "/json",
        "json": {
            "foo": "bar"
        }
    },
    "response": {
        "text": "foo"
    }
}
```

#### 使用JSONPath来匹配是否包含指定的参数

示例：
当请求参数包含一个`"$.biz.type"="lookback_bill"`时，则返回数据
```bash
curl -X POST \
  http://10.111.17.157:8100/activiti/thrid/calApi \
  -H 'Content-Type: application/json' \
  -d '{
	"biz":{
		"type":"lookback_bill",
		"as":"assa",
		"aaa":"xxx"
	}
}'
```

```json settings.json
{
    "request": {
        "uri": "/activiti/thrid/calApi",
        "json_paths":{
              "$.biz.type":"lookback_bill"
          }
    },
    "response": {
        "json": {
            "code": 200,
            "message": "模拟计费成功"
        }
    }
}
```

[官方文档](https://github.com/dreamhead/moco/blob/master/moco-doc/apis.md#jsonpath)
官方示例：

```json
{
  "request":
    {
      "uri": "/jsonpath",
      "json_paths":
        {
          "$.book[*].price": "1"
	    }
    },
  "response":
    {
      "text": "response_for_json_path_request"
    }
}
```