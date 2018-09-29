# gobblin

## docker安装

[Docker Integration - Gobblin Documentation](https://gobblin.readthedocs.io/en/latest/user-guide/Docker-Integration/)

```sh
docker run -v /home/gobblin/conf:/etc/opt/job-conf \
           -v /home/gobblin/work-dir:/home/gobblin/work-dir \
           -v /home/gobblin/logs:/var/log/gobblin \
           gobblin/gobblin-standalone:ubuntu-gobblin-latest
```

[Gobblin编译支持CDH5.4.0 - CSDN博客](https://blog.csdn.net/cssdongl/article/details/77750444)

## 采集kafka数据

[Gobblin采集kafka数据 - Syn良子 - 博客园](https://www.cnblogs.com/cssdongl/p/6121382.html)





## 概念

[Gobblin--一个用于Hadoop的统一&quot;数据抽取框架&quot; - CSDN博客](https://blog.csdn.net/lmalds/article/details/53940549)

### 组件

    1、source
    2、extractor
    3、convertor
    4、quality checker
    5、writer
    6、publisher
- Source主要负责将源数据整合到一系列workunits中，并指出对应的extractor是什么。这有点类似于Hadoop的InputFormat。

- Extractor则通过workunit指定数据源的信息，例如kafka，指出topic中每个partition的起始offset，用于本次抽取使用。Gobblin使用了watermark的概念，记录每次抽取的数据的起始位置信息。

- Converter顾名思义是转换器的意思，即对抽取的数据进行一些过滤、转换操作，例如将byte arrays 或者JSON格式的数据转换为需要输出的格式。转换操作也可以将一条数据映射成0条或多条数据（类似于flatmap操作）。

- Quality Checker即质量检测器，有2中类型的checker：record-level和task-level的策略。通过手动策略或可选的策略，将被check的数据输出到外部文件或者给出warning。

- Writer就是把导出的数据写出，但是这里并不是直接写出到output file，而是写到一个缓冲路径（ staging directory）中。当所有的数据被写完后，才写到输出路径以便被publisher发布。Sink的路径可以包括HDFS或者kafka或者S3中，而格式可以是Avro,Parquet,或者CSV格式。同时Writer也可是根据时间戳，将输出的文件输出到按照“小时”或者“天”命名的目录中。

- Publisher就是根据writer写出的路径，将数据输出到最终的路径。同时其提供2种提交机制：完全提交和部分提交；如果是完全提交，则需要等到task成功后才pub，如果是部分提交模式，则当task失败时，有部分在staging directory的数据已经被pub到输出路径了。
