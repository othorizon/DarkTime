# 批量请求url 解析json数据

```python
# -*- coding: UTF-8 -*-
#!/usr/bin/python

import urllib2
import urllib
import json

def byteify(input):
    if isinstance(input, dict):
        return {byteify(key): byteify(value) for key, value in input.iteritems()}
    elif isinstance(input, list):
        return [byteify(element) for element in input]
    elif isinstance(input, unicode):
        return input.encode('utf-8')
    else:
        return input


result="uid price\n"

def request(uid,cid):
    uid=str(uid)
    cid=str(cid)
    print "uid:",uid,"cid:",cid
    dataRes = urllib.urlopen('http://host/request&contractId='+cid+'&iamsid='+uid)
    #通过urllib模块中的urlopen的方法打开url
    data = dataRes.read()
    #通过read方法获取返回数据
    # print "url返回的json数据：",data
    #打印返回信息
    dataJSON = json.loads(data)
    dataJSON=dataJSON["data"]

    for price in dataJSON:
        if price["code"] == "kvm" :
                print uid,price["priceDate"]
                global result
                result=result+"\n"+uid+" "+price["priceDate"]

# param=[(70304362,157536),(70304362,157536)]
# for p in param:
#     request(p[0],p[1])


with open('uids.txt') as f:
    for line in f:
        uidcid=line.split()
        request(uidcid[0],uidcid[1])

#将返回的json格式的数据转化为python对象，json数据转化成了python中的字典，按照字典方法读取数据
# print "python的字典数据：",dataJSON
# print "字典中的data数据",dataJSON["data"]
# print "lists列表的数据",dataJSON["data"]["lists"][0]
#lists里面的数据是一个列表（按照序列编号来查看数据）
# print weatherJSON["data"]["lists"][0]["SongName"]
#lists的0号数据是一个字典，按照字典方法查看数据


#dumps()默认中文伟ascii编码格式，ensure_ascii默认为Ture
#禁用ascii编码格式，返回Unicode字符串
# dataJson=json.dumps(dataJSON,ensure_ascii=False)

with open("export.txt","w") as f:
    f.write(result.encode("utf-8"))
```

uids.txt 参数文件示例

```txt uids.txt 参数文件
13368 156622
79682 156011
263190 155071
```

## 讲解

`dataJSON = json.loads(data)`读取的文件是unicode编码，如果要输出utf8格式的需要如下方式：

```python
#调用dumps方法输出字符串
dataStr=json.dumps(dataJSON,ensure_ascii=False)
#以utf8格式编码
print dataStr.encode("utf-8")
```

## 参考资料

[python爬虫 如何解析json文件 json文件的解析提取和jsonpath的应用 - CSDN博客](https://blog.csdn.net/t8116189520/article/details/78727971)
[python 数据提取之JSON与JsonPATH - AlamZ - 博客园](https://www.cnblogs.com/alamZ/p/7413968.html)