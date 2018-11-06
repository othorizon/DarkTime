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

---

## 配置

[Configuration Glossary - Gobblin Documentation](https://gobblin.readthedocs.io/en/latest/user-guide/Configuration-Properties-Glossary/)

```bash
# TaskContext.getDataWriterBuilder
## DEFAULT_WRITER_BUILDER_CLASS = "org.apache.gobblin.writer.AvroDataWriterBuilder";
writer.builder.class=

# 要和writerBuilder保持统一
# writer输出文件的后缀 以及outputformat配置（但是writer里面好像并没用上？）
writer.output.format=ORC
```

```bash
#这两个配合决定了 可以让publisher分成job和task两个来配置
publish.data.at.job.level=false
job.commit.policy=successful

data.publisher.job.type=datacenter.plugins.gobblin.publisher.MyHiveRegistrationPublisher
data.publisher.task.type=org.apache.gobblin.publisher.BaseDataPublisher
```

### HiveWritableHdfsDataWriterBuilder和HiveSerDeConverter配置

```bash
# 这个converter里面要配置 一个 HiveSerDeWrapper 用于反序列化输入数据（serde.deserializer.type） 和序列化输出（serde.serializer.type）数据，有两种配置方式
converter.classes=org.apache.gobblin.converter.serde.HiveSerDeConverter
# 这个配置是HiveSerDeConverter解析数据的schema
avro.schema.literal={"namespace":"demo.hive","type":"record","name":"hiveorc","fields":[{"name":"jobRoles","type":["int"]},{"name":"peopleWeightAvg","type":["float"]},{"name":"peopleOrg","type":["string","null"]}]}

#配置序列化和反序列化
## this.serializer = HiveSerDeWrapper.getSerializer(state).getSerDe();
## this.deserializer = HiveSerDeWrapper.getDeserializer(state).getSerDe();
# style1
## 配置一个已经定义好的wrapper(一个wrapper包括inputformat和outputformat)
## org.apache.gobblin.hive.HiveSerDeWrapper.BuiltInHiveSerDe
serde.deserializer.type=TEXTFILE
serde.serializer.type=ORC

# style2
serde.deserializer.type=TEXTFILE
## 自定义一个wrapper
### 这个type必须是存在的org.apache.hadoop.hive.serde2.SerDe的实现类
### 如果这个class是在org.apache.gobblin.hive.HiveSerDeWrapper.BuiltInHiveSerDe中已经定义的，那么不会在使用'serde.serializer.input.format.type'和'serde.serializer.output.format.type',见[HiveSerDeWrapper的get](####HiveSerDeWrapper的get)
serde.serializer.type=org.apache.hadoop.hive.serde2.SerDe
### 定义inputformat
serde.serializer.input.format.type=org.apache.hadoop.hive.ql.io.orc.OrcInputFormat
### 定义outputformat
serde.serializer.output.format.type=org.apache.hadoop.hive.ql.io.orc.OrcOutputFormat

# HiveSerDeConverter通常可以配合HiveWritableHdfsDataWriterBuilder使用，
# HiveWritableHdfsDataWriterBuilder有两种配置方式
## 1. 定义 WRITER_WRITABLE_CLASS，和WRITER_OUTPUT_FORMAT_CLASS
writer.writable.class=org.apache.hadoop.hive.serde2.lazy.LazySimpleSerDe
writer.output.format.class=org.apache.hadoop.mapred.TextOutputFormat

## 2.定义 SERDE_SERIALIZER_TYPE，用于获取WRITER_WRITABLE_CLASS和WRITER_OUTPUT_FORMAT_CLASS
## 这个serde.serializer.type和convert中定义是一个东西，所以也是有两种风格定义，见HiveSerDeConverter配置
## 这个配置和HiveSerDeConverter是共用一个配置，HiveSerDeConverter序列化输出的数据，将由HiveWritableHdfsDataWriter使用相同的序列化配置去写出到文件 见[HiveWritableHdfsDataWriter的getWriter](####HiveWritableHdfsDataWriter的getWriter)
serde.serializer.type=ORC
```

### 压缩配置

`writer..codec.type` 配置压缩格式

以在 `org.apache.gobblin.writer.SimpleDataWriterBuilder`中为例：

```java
  @Override
  protected List<StreamCodec> buildEncoders() {
    Preconditions.checkNotNull(this.destination, "Destination must be set before building encoders");

    List<StreamCodec> encoders = new ArrayList<>();

    //从 writer..codec.type 中读取压缩类型
    Map<String, Object> compressionConfig =
        CompressionConfigParser.getConfigForBranch(this.destination.getProperties(), this.branches, this.branch);
    if (compressionConfig != null) {
      // 生成StreamCodec
      encoders.add(CompressionFactory.buildStreamCompressor(compressionConfig));
    }

    Map<String, Object> encryptionConfig = EncryptionConfigParser
        .getConfigForBranch(EncryptionConfigParser.EntityType.WRITER, this.destination.getProperties(), this.branches,
            this.branch);
    if (encryptionConfig != null) {
      encoders.add(EncryptionFactory.buildStreamCryptoProvider(encryptionConfig));
    }

    return encoders;
  }
```

---

## 源码分析

### HiveWritableHdfsDataWriterBuilder和HiveSerDeConverter

配置方法见 [HiveWritableHdfsDataWriterBuilder和HiveSerDeConverter配置](###HiveWritableHdfsDataWriterBuilder和HiveSerDeConverter配置)

```java
package org.apache.gobblin.writer;
public class HiveWritableHdfsDataWriterBuilder<S> extends FsDataWriterBuilder<S, Writable> {
    public DataWriter<Writable> build() throws IOException {
        Preconditions.checkNotNull(this.destination);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(this.writerId));

        State properties = this.destination.getProperties();
        // 如果定义了WRITER_WRITABLE_CLASS和WRITER_OUTPUT_FORMAT_CLASS则直接用这两个创建writer
        // 如果没定义而是配置了serde.serializer.type则用其去生成writer
        if (!properties.contains(WRITER_WRITABLE_CLASS) || !properties.contains(WRITER_OUTPUT_FORMAT_CLASS)) {
        HiveSerDeWrapper serializer = HiveSerDeWrapper.getSerializer(properties);
        // 从org.apache.hadoop.hive.serde2.SerDe的实现类中获取serializedClass
        properties.setProp(WRITER_WRITABLE_CLASS, serializer.getSerDe().getSerializedClass().getName());
        properties.setProp(WRITER_OUTPUT_FORMAT_CLASS, serializer.getOutputFormatClassName());
        }

        return new HiveWritableHdfsDataWriter(this, properties);
    }
}
```

#### HiveWritableHdfsDataWriter的getWriter

```java
package org.apache.gobblin.writer;

public class HiveWritableHdfsDataWriter extends FsDataWriter<Writable> {
  private RecordWriter getWriter() throws IOException {
      try {
        HiveOutputFormat<?, ?> outputFormat = HiveOutputFormat.class
            .cast(Class.forName(this.properties.getProp(HiveWritableHdfsDataWriterBuilder.WRITER_OUTPUT_FORMAT_CLASS))
                .newInstance());

        @SuppressWarnings("unchecked")
        Class<? extends Writable> writableClass = (Class<? extends Writable>) Class
            .forName(this.properties.getProp(HiveWritableHdfsDataWriterBuilder.WRITER_WRITABLE_CLASS));

        return outputFormat.getHiveRecordWriter(new JobConf(), this.stagingFile, writableClass, true,
            this.properties.getProperties(), null);
      } catch (Throwable t) {
        throw new IOException(String.format("Failed to create writer"), t);
      }
    }
}
```

#### HiveSerDeWrapper的get

```java
package org.apache.gobblin.hive;

public class HiveSerDeWrapper {
  /**
   * Get an instance of {@link HiveSerDeWrapper}.
   *
   * @param serDeType The SerDe type. If serDeType is one of the available {@link HiveSerDeWrapper.BuiltInHiveSerDe},
   * the other three parameters are not used. Otherwise, serDeType should be the class name of a {@link SerDe},
   * and the other three parameters must be present.
   */
  public static HiveSerDeWrapper get(String serDeType, Optional<String> inputFormatClassName,
      Optional<String> outputFormatClassName) {
    Optional<BuiltInHiveSerDe> hiveSerDe = Enums.getIfPresent(BuiltInHiveSerDe.class, serDeType.toUpperCase());
    if (hiveSerDe.isPresent()) {
      return new HiveSerDeWrapper(hiveSerDe.get());
    }
    Preconditions.checkArgument(inputFormatClassName.isPresent(),
        "Missing input format class name for SerDe " + serDeType);
    Preconditions.checkArgument(outputFormatClassName.isPresent(),
        "Missing output format class name for SerDe " + serDeType);
    return new HiveSerDeWrapper(serDeType, inputFormatClassName.get(), outputFormatClassName.get());
  }
}
```

### 数据写出

>Writer就是把导出的数据写出，但是这里并不是直接写出到output file，而是写到一个缓冲路径（ staging directory）中。当所有的数据被写完后，才写到输出路径以便被publisher发布。Sink的路径可以包括HDFS或者kafka或者S3中，而格式可以是Avro,Parquet,或者CSV格式。同时Writer也可是根据时间戳，将输出的文件输出到按照“小时”或者“天”命名的目录中
原文：https://blog.csdn.net/lmalds/article/details/53940549

STEP1.1 写到stagingFileOutputStream

```java SimpleDataWriter.java
package org.apache.gobblin.writer;

public class SimpleDataWriter extends FsDataWriter<byte[]> {

    /**
    * Write a source record to the staging file
    *
    * @param record data record to write
    * @throws java.io.IOException if there is anything wrong writing the record
    */
    @Override
    public void write(byte[] record) throws IOException {
        Preconditions.checkNotNull(record);

        byte[] toWrite = record;
        if (this.recordDelimiter.isPresent()) {
        toWrite = Arrays.copyOf(record, record.length + 1);
        toWrite[toWrite.length - 1] = this.recordDelimiter.get();
        }
        if (this.prependSize) {
        long recordSize = toWrite.length;
        ByteBuffer buf = ByteBuffer.allocate(Longs.BYTES);
        buf.putLong(recordSize);
        toWrite = ArrayUtils.addAll(buf.array(), toWrite);
        }
        this.stagingFileOutputStream.write(toWrite);
        this.bytesWritten += toWrite.length;
        this.recordsWritten++;
    }
}
```

STEP1.2 从stagingFile 移动到outputFile，即writer组件写出文件

>`outputFile`文件就是`writer.output.dir`配置的路径.
`public static final String WRITER_OUTPUT_DIR = WRITER_PREFIX + ".output.dir";`

```java
package org.apache.gobblin.writer;

public abstract class FsDataWriter<D> implements DataWriter<D>, FinalState, MetadataAwareWriter, SpeculativeAttemptAwareConstruct {
  /**
   * {@inheritDoc}.
   *
   * <p>
   *   This default implementation simply renames the staging file to the output file. If the output file
   *   already exists, it will delete it first before doing the renaming.
   * </p>
   *
   * @throws IOException if any file operation fails
   */
  @Override
  public void commit() throws IOException {
        this.closer.close();

        setStagingFileGroup();

        if (!this.fs.exists(this.stagingFile)) {
            throw new IOException(String.format("File %s does not exist", this.stagingFile));
        }

        FileStatus stagingFileStatus = this.fs.getFileStatus(this.stagingFile);

        // Double check permission of staging file
        if (!stagingFileStatus.getPermission().equals(this.filePermission)) {
            this.fs.setPermission(this.stagingFile, this.filePermission);
        }

        this.bytesWritten = Optional.of(Long.valueOf(stagingFileStatus.getLen()));

        LOG.info(String.format("Moving data from %s to %s", this.stagingFile, this.outputFile));
        // For the same reason as deleting the staging file if it already exists, deleting
        // the output file if it already exists prevents task retry from being blocked.
        if (this.fs.exists(this.outputFile)) {
            LOG.warn(String.format("Task output file %s already exists", this.outputFile));
            HadoopUtils.deletePath(this.fs, this.outputFile, false);
        }
        // ⚠️移动stagingFile到outputFile也就是writer的写出文件
        HadoopUtils.renamePath(this.fs, this.stagingFile, this.outputFile);
 }
}
```

STEP2 写出到最终目录，获取writer写出的文件发布到最终目录

>`public static final String DATA_PUBLISHER_FINAL_DIR = DATA_PUBLISHER_PREFIX + ".final.dir";`

```java
package org.apache.gobblin.publisher;

public class BaseDataPublisher extends SingleTaskDataPublisher {
  protected void publishData(WorkUnitState state, int branchId, boolean publishSingleTaskData,
      Set<Path> writerOutputPathsMoved)
      throws IOException {
    // Get a ParallelRunner instance for moving files in parallel
    ParallelRunner parallelRunner = this.getParallelRunner(this.writerFileSystemByBranches.get(branchId));

    // The directory where the workUnitState wrote its output data.
    // ⚠️这里就是获取上一步写出的writer文件
    Path writerOutputDir = WriterUtils.getWriterOutputDir(state, this.numBranches, branchId);

    if (!this.writerFileSystemByBranches.get(branchId).exists(writerOutputDir)) {
      LOG.warn(String.format("Branch %d of WorkUnit %s produced no data", branchId, state.getId()));
      return;
    }

    // The directory where the final output directory for this job will be placed.
    // It is a combination of DATA_PUBLISHER_FINAL_DIR and WRITER_FILE_PATH.
    Path publisherOutputDir = getPublisherOutputDir(state, branchId);

    if (publishSingleTaskData) {
      // Create final output directory
      WriterUtils.mkdirsWithRecursivePermissionWithRetry(this.publisherFileSystemByBranches.get(branchId), publisherOutputDir,
          this.permissions.get(branchId), retrierConfig);
      addSingleTaskWriterOutputToExistingDir(writerOutputDir, publisherOutputDir, state, branchId, parallelRunner);
    } else {
      if (writerOutputPathsMoved.contains(writerOutputDir)) {
        // This writer output path has already been moved for another task of the same extract
        // If publishSingleTaskData=true, writerOutputPathMoved is ignored.
        return;
      }

      if (this.publisherFileSystemByBranches.get(branchId).exists(publisherOutputDir)) {
        // The final output directory already exists, check if the job is configured to replace it.
        // If publishSingleTaskData=true, final output directory is never replaced.
        boolean replaceFinalOutputDir = this.getState().getPropAsBoolean(ForkOperatorUtils
            .getPropertyNameForBranch(ConfigurationKeys.DATA_PUBLISHER_REPLACE_FINAL_DIR, this.numBranches, branchId));

        // If the final output directory is not configured to be replaced, put new data to the existing directory.
        if (!replaceFinalOutputDir) {
          addWriterOutputToExistingDir(writerOutputDir, publisherOutputDir, state, branchId, parallelRunner);
          writerOutputPathsMoved.add(writerOutputDir);
          return;
        }

        // Delete the final output directory if it is configured to be replaced
        LOG.info("Deleting publisher output dir " + publisherOutputDir);
        this.publisherFileSystemByBranches.get(branchId).delete(publisherOutputDir, true);
      } else {
        // Create the parent directory of the final output directory if it does not exist
        WriterUtils.mkdirsWithRecursivePermissionWithRetry(this.publisherFileSystemByBranches.get(branchId),
            publisherOutputDir.getParent(), this.permissions.get(branchId), retrierConfig);
      }
      // ⚠️移动writerOutputDir到最终输出的publisherOutputDir
      movePath(parallelRunner, state, writerOutputDir, publisherOutputDir, branchId);
      writerOutputPathsMoved.add(writerOutputDir);
    }
  }

  /**
   * close操作将发布的文件路径publisherOutputDirs 附加到ConfigurationKeys.PUBLISHER_DIRS（`data.publisher.output.dirs`）属性的值中，供后续使用，比如 HiveRegistrationPublisher
   */
  @Override
  public void close()
      throws IOException {
    try {
      for (Path path : this.publisherOutputDirs) {
        this.state.appendToSetProp(ConfigurationKeys.PUBLISHER_DIRS, path.toString());
      }
    } finally {
      // 调用google的`com.google.common.io.Closer`这个closer可以注册多个closable(`this.stack.push(closeable)`)在执行close方法时就会调用（`(Closeable)this.stack.pop().close()`）因此可以实现链式的关闭
      this.closer.close();
    }
  }
}
```

STEP4 publish之后的后续publish操作

如果在配置文件中配置了这个publish：

```bash
##配置方案1
data.publisher.type=org.apache.gobblin.publisher.BaseDataPublisherWithHiveRegistration

## 配置方案2
### org.apache.gobblin.runtime.Task.shouldPublishDataInTask()
publish.data.at.job.level=false
job.commit.policy=successful

data.publisher.job.type=org.apache.gobblin.publisher.HiveRegistrationPublisher
data.publisher.task.type=org.apache.gobblin.publisher.BaseDataPublisher
```

```java
package org.apache.gobblin.publisher;

public class BaseDataPublisherWithHiveRegistration extends BaseDataPublisher {

  protected final HiveRegistrationPublisher hivePublisher;

  public BaseDataPublisherWithHiveRegistration(State state) throws IOException {
    super(state);
    //向BaseDataPublisher的closer中注册该HiveRegistrationPublisher
    this.hivePublisher = this.closer.register(new HiveRegistrationPublisher(state));
  }

  @Override
  public void publish(Collection<? extends WorkUnitState> states) throws IOException {
    super.publish(states);
    this.hivePublisher.publish(states);
  }

}
```

#### Source Extractor Converter 关系

**Source获取Extractor**
Source配置`source.class=org.apache.gobblin.source.extractor.extract.kafka.KafkaDeserializerSource`

```java
public class KafkaSimpleSource extends KafkaSource<String, byte[]> {
  @Override
  public Extractor<String, byte[]> getExtractor(WorkUnitState state) throws IOException {
    return new KafkaSimpleExtractor(state);
  }
}
```

Extractor中配置了`readRecord`方法用于读取数据
Extractor中配置了`getSchema`方法用于获取schema

```java
public class KafkaSimpleExtractor extends KafkaExtractor<String, byte[]> {
  public KafkaSimpleExtractor(WorkUnitState state) {
    super(state);
    this.kafkaSchemaRegistry = new SimpleKafkaSchemaRegistry(state.getProperties());
  }
  @Override
  public String getSchema() throws IOException {
    try {
      return this.kafkaSchemaRegistry.getLatestSchemaByTopic(this.topicName);
    } catch (SchemaRegistryException e) {
      throw new RuntimeException(e);
    }
  }
}
```

**Converter 转换Schema，Schema从Extractor中获取**
Converter配置`converter.classes=org.apache.gobblin.converter.json.JsonStringToJsonIntermediateConverter,org.apache.gobblin.converter.avro.JsonIntermediateToAvroConverter`
这个配置是链式的。按顺序依次转换，[Source Schema and Converters](https://gist.github.com/tilakpatidar/2591c8f4503bcbd0bc0ab212b31ec9b5)

```java
package org.apache.gobblin.runtime;

public class Task implements TaskIFace {
  private final Converter converter;
  private final InstrumentedExtractorBase extractor;

  public Task(TaskContext context, ...) {
      //this.taskContext.getExtractor() 的内容: getSource().getExtractor(this.taskState)
    this.extractor =
            closer.register(new InstrumentedExtractorDecorator<>(this.taskState, this.taskContext.getExtractor()));
  }
  private void runSynchronousModel() throws Exception {
    // 1。 转换schema
    Object schema = converter.convertSchema(extractor.getSchema(), this.taskState);

    // 2.转换数据
    //   extractor.readRecordEnvelope():从extractor获取数据
    //   converter.convertRecord(）:将从extractor获取的数据做转换
    RecordEnvelope recordEnvelope;
      // Extract, convert, and fork one source record at a time.
      while (!shutdownRequested() && (recordEnvelope = extractor.readRecordEnvelope()) != null) {
        onRecordExtract();
        AcknowledgableWatermark ackableWatermark = new AcknowledgableWatermark(recordEnvelope.getWatermark());
        if (watermarkTracker.isPresent()) {
          watermarkTracker.get().track(ackableWatermark);
        }
        for (Object convertedRecord : converter.convertRecord(schema, recordEnvelope, this.taskState)) {
          processRecord(convertedRecord, forkOperator, rowChecker, rowResults, branches,
              ackableWatermark.incrementAck());
        }
        ackableWatermark.ack();
      }
  }

}
```