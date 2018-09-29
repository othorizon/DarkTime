# kafka

## docker compose 部署kafka

参考：
[docker-compose安装kafka集群和kafka-manager管理界面](https://blog.csdn.net/sinat_31908303/article/details/80447383)  
[Kafka Docker-官方](http://wurstmeister.github.io/kafka-docker/)  
[使用docker安装kafka - CSDN博客](https://blog.csdn.net/lblblblblzdx/article/details/80548294)  

```yml
#示例
version: '2'
services:
  zookeeper:
    image: wurstmeister/zookeeper   ## 镜像
    # ports:
      # - "2181:2181"                 ## 对外暴露的端口号
  kafka:
    image: wurstmeister/kafka       ## 镜像
    # ports:
      # - "9092:9092"
    environment:
      KAFKA_ADVERTISED_HOST_NAME: kafka   ## 修改:宿主机IP
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181       ## 卡夫卡运行是基于zookeeper的
    depends_on:
      - zookeeper
  kafka-manager:  
    image: sheepkiller/kafka-manager                ## 镜像：开源的web管理kafka集群的界面
    environment:
        ZK_HOSTS: zookeeper:2181                  ## 修改:宿主机IP
    ports:  
      - "9000:9000"                                 ## 暴露端口
    depends_on:
     - kafka

```

测试命令  
参考：
[kafka实战 - 嘿123 - 博客园](https://www.cnblogs.com/hei12138/p/7805475.html)  
[Gobblin部署--standalone模式 - CSDN博客](https://blog.csdn.net/lmalds/article/details/53993826)
[Gobblin部署--mapreduce模式 - CSDN博客](https://blog.csdn.net/lmalds/article/details/53994091)

打开管理界面(locahost:9000)，创建一个cluster，并创建一个topic（demo）。  
然后测试发送和接受消息  
http://kafka.apache.org/documentation.html#quickstart

```bash
# consumer
## 进入容器
docker exec -it kafka_kafka_1 /bin/sh
## 进入容器后执行,等待消息
kafka-console-consumer.sh --bootstrap-server kafka:9092 --topic demo --from-beginning

# producer
## 进入容器
docker exec -it kafka_kafka_1 /bin/sh
## 进入容器后执行
kafka-console-producer.sh --broker-list kafka:9092 --topic demo
## 然后试着输入一些文本来发送消息，查看消息接收
```

## 杂项

拼接jar包依赖路径

```bash
 --jars `ls /home/hdfs/test/gobblin-dist/lib/* | tr "\n" ","`
```