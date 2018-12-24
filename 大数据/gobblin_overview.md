# gobblin

## 概念

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

>Source：每个partition中起始offset都通过Source生成到workunit中；同时，从state中获取上一次抽取结尾的offset信息，以便判断本次Job执行的起始offset。
Extractor：Extractor会逐个抽取partition的数据，抽取完成一个后，会将末尾offset信息存到状态存储中。
Converter：LinkedIn内部的Kafka集群主要存储Avro格式的数据，并对此进行一些过滤和转换。
Quality Checker：LinkedIn中数据都会包含一个时间戳，以便决定放到哪个“小时”目录和“天”目录。对于没有时间戳的数据，则会根据record-level的策略将这些数据写到外部文件中。
Writer and Publisher：内部使用基于时间的writer和基于时间的publisher去写并pub数据。
[数据采集框架Gobblin简介](https://cloud.tencent.com/developer/article/1351988)

## 架构

![架构](https://ask.qcloudimg.com/http-save/yehe-2725853/v74nkx004r.jpeg?imageView2/2/w/1620)
![流程](https://upload-images.jianshu.io/upload_images/6504531-275a78b3b2639564.jpg)
![流程2](https://ask.qcloudimg.com/http-save/yehe-2725853/mlw4m84zp1.jpeg?imageView2/2/w/1620)
![自制](/大数据/gobblin-kafka.jpg)

## Task

A physical unit of execution for a Gobblin org.apache.gobblin.source.workunit.WorkUnit.
Each task is executed by a single thread in a thread pool managed by the TaskExecutor and each Fork of the task is executed in a separate thread pool also managed by the TaskExecutor. Each Task consists of the following steps:
Extracting, converting, and forking the source schema.
Extracting, converting, doing row-level quality checking, and forking each data record.
Putting each forked record into the record queue managed by each Fork.
Committing output data of each Fork once all Forks finish.
Cleaning up and exiting.
Each Fork consists of the following steps:
Getting the next record off the record queue.
Converting the record and doing row-level quality checking if applicable.
Writing the record out if it passes the quality checking.
Cleaning up and exiting once all the records have been processed.