# sqoop源码解析

## 自定义hive配置

### 问题

sqoop操作hive时，会读取hive的配置文件，但是如果想要通过参数动态的覆盖默认的hive配置该如何？

### 探讨

首先看这个类`org.apache.sqoop.hive.HiveConfig`  

```java
//class: org.apache.sqoop.hive.HiveConfig
//line: 42
public static Configuration getHiveConf(Configuration conf) throws IOException {
    //...
      Class HiveConfClass = Class.forName(HIVE_CONF_CLASS);
      return ((Configuration)(HiveConfClass.getConstructor(Configuration.class, Class.class)
          .newInstance(conf, Configuration.class)));
    //...
  }
```

这段代码获取的是`org.apache.hadoop.hive.conf.HiveConf`类的实例。在HiveConf中有这段代码

```java
// class: org.apache.hadoop.hive.conf.HiveConf
// line: 2935
public static Map<String, String> getConfSystemProperties() {
    Map<String, String> systemProperties = new HashMap<String, String>();
    for (ConfVars oneVar : ConfVars.values()) {
      if (System.getProperty(oneVar.varname) != null) {
        if (System.getProperty(oneVar.varname).length() > 0) {
          systemProperties.put(oneVar.varname, System.getProperty(oneVar.varname));
        }
      }
    }
    return systemProperties;
  }
```

在sqoop`getHiveConf`方法中去实例化HiveConf类时，会读取hive的配置，其中会调用到上述的`getConfSystemProperties`方法，该方法会读取系统变量中的参数来覆盖默认的配置。这些可用的参数可以在`ConfVars`枚举类中看到。

### 解决

因此如果想要覆盖默认的hive配置只需要添加系统变量即可，例如

```bash
java \
-D'hive.metastore.uris=thrift://service:90831' \
-cp libs \
org.apache.sqoop.Sqoop args
```

也可以通过修改sqoop配置文件的方式

## 获取sqoop导入导出记录数量

### 问题

如何获取sqoop导入或者导出数据条数，以进行例如上报日志等操作？

### 探讨

mapreduce有一个计数器的东西，其源码已经实现了对map输入输出的计数。

```java
//class: org.apache.sqoop.config.ConfigurationHelper
//line: 73
/**
* @return the number of mapper output records from a job using its counters.
*/
public static long getNumMapOutputRecords(Job job)
    throws IOException, InterruptedException {
return job.getCounters().findCounter(
    ConfigurationConstants.COUNTER_GROUP_MAPRED_TASK_COUNTERS,
    ConfigurationConstants.COUNTER_MAP_OUTPUT_RECORDS).getValue();
}

/**
* @return the number of mapper input records from a job using its counters.
*/
public static long getNumMapInputRecords(Job job)
    throws IOException, InterruptedException {
return job.getCounters().findCounter(
        ConfigurationConstants.COUNTER_GROUP_MAPRED_TASK_COUNTERS,
        ConfigurationConstants.COUNTER_MAP_INPUT_RECORDS).getValue();
}
```

对于导入操作来说，数据传入map并从map按照一条条的数据写出到hdfs中，因此`getNumMapOutputRecords`便是数据导入的条数，  
对于导出操作来说，传入map的数据是hdfs中的数据，一般情况下便是一条条的数据，因此`getNumMapInputRecords`便是数据导出的条数。

而对于一些特殊格式的数据，sqoop重写了计数器的计数逻辑。

```java
//class: org.apache.sqoop.mapreduce.mainframe.MainframeDatasetImportMapper
private long numberOfRecords;

//line: 50
public void map(LongWritable key,  SqoopRecord val, Context context)
    throws IOException, InterruptedException {
String dataset = inputSplit.getCurrentDataset();
outkey.set(val.toString());
numberOfRecords++;
mos.write(outkey, NullWritable.get(), dataset);
}
//line: 68
@Override
protected void cleanup(Context context)
    throws IOException, InterruptedException {
super.cleanup(context);
mos.close();
context.getCounter(
    ConfigurationConstants.COUNTER_GROUP_MAPRED_TASK_COUNTERS,
    ConfigurationConstants.COUNTER_MAP_OUTPUT_RECORDS)
    .increment(numberOfRecords);
}
```

在map的cleanup方法中进行了计数

### 解决

因此只需要从计数器获取数据即可

```java
//导出(export)操作条数
long inputRecords = ConfigurationHelper.getNumMapInputRecords(job);

//导入(import)操作条数
long outputRecords = ConfigurationHelper.getNumMapOutputRecords(job);
```