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

## 源码分析

### 数据写出

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
}
```