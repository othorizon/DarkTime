# hadoop map reduce

## hadoop 运行jar

[hadoop如何分发本地的jar文件](https://blog.csdn.net/xiaolang85/article/details/8618705)  
执行`hadoop jar abc.jar arg0 arg1 ...`实际上时执行了`java org.apache.hadoop.util.RunJar abc.jar arg0 arg1 ...`  
在RunJar中，会读取abc.jar文件，然后尝试从manifest中提取"Main-Class"作为mainClass，如果manifest中没有指定，则把abc.jar之后的下一个参数当成mainClass。比如`hadoop jar packagename.ClassName arg0 arg1`  

当然你也可以像使用`java -cp dependency1.jar:dependency2.jar:main.jar packagename.ClassName`这样的方式来运行

```bash
export HADOOP_CALSSPATH=dependency1.jar:dependency2.jar:main.jar
hadoop packagename.ClassName arg0 arg1
```

## 依赖管理

1. 使用hadoop 命令运行jar包时，会自动携带hadoop的依赖，因此不需要将这些依赖打入jar包。
2. 对于hadoop之外的其他依赖包有多种方式来配置。（[【转】Mapreduce部署与第三方依赖包管理 - Mr.Ming2 - 博客园](https://www.cnblogs.com/Dhouse/p/6595344.html)）
    1. 将你的job打包成一个独立的jar包，依赖全部打包到jar包之中，这是最简单的方式，但是如果依赖太多则会导致jar包太大。
    2. 配置`export HADOOP_CLASSPATH=*.jar`来指定依赖的jar，但是这样的话需要在每个节点机器上均要同步部署这些依赖包以及配置环境变量。
    3. 使用`libjars`参数。可以在使用“hadoo jar”命令时，向启动的job传递“libjars”选项参数，同时配合ToolRunner工具来解析参数并运行Job。 libjars中需要指定job依赖的所有的jar全路径，并且这些jars必须在当前本地文件系统中(并非集群中都需要有此jars)

### libjars的配置

[MapReduce程序调用第三方Jar包的方式 - CSDN博客](https://blog.csdn.net/lizhang310/article/details/24399403)  
[解决Hadoop jar *.jar 主类名 -libjars *.jar *.jar arg1 arg2 ... 中-libjars的方法 - CSDN博客](https://blog.csdn.net/fffpppccc/article/details/46721991)

首先你的程序要使用ToolRunner的方式来运行。
>adoop自带了一些辅助类。GenericOptionsParser是一个类，用来解释常用的Hadoop命令行选项，并根据需要，为Configuration对象设置相应的取值。通常不直接使用GenericOptionsParser，更方便的方式是：实现Tool接口，通过ToolRunner来运行应用程序，ToolRunner内部调用GenericOptionsParser。

```java
public class WordCount extends Configured implements Tool {
    @Override
    public int run(String[] arg0) throws Exception {
        Job job = new Job(getConf(), "word count");
        // 略...
        System.exit(job.waitForCompletion(true) ? 0 : 1);
        return 0;
    }
    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new Configuration(), new WordCount(), args);
        System.exit(res);
    }
}
```

使用了这种方式后就可以在你的启动命令中通过指定`-libjars`来指定第三方依赖了，hadoop会将依赖上传到hdfs上共享使用。
启动命令:`hadoop jar main.jar packagename.ClassName -libjars a.jar,b.jar arg0 arg1`  
**注意的是`-libjars a.jar,b.jar`是放在指定的类名后面和要传入的参数前面。**

## 完整的示例代码

```java
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.zeroturnaround.exec.ProcessExecutor;

import java.io.IOException;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.concurrent.TimeoutException;

/**
 * @author Rizon
 * @date 2018/9/13
 */
public class WordCount extends Configured implements Tool {

    public static class TokenizerMapper
            extends Mapper<Object, Text, Text, IntWritable> {

        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();

        @Override
        public void map(Object key, Text value, Context context
        ) throws IOException, InterruptedException {
            try {
                System.out.println(new ProcessExecutor()
                        .redirectOutput(System.out)
                        .readOutput(true)
                        .command("ls", "./")
                        .execute().outputUTF8());
            } catch (TimeoutException e) {
                e.printStackTrace();
            }

            StringTokenizer itr = new StringTokenizer(value.toString());
            while (itr.hasMoreTokens()) {
                word.set(itr.nextToken());
                context.write(word, one);
            }
        }
    }

    public static class IntSumReducer
            extends Reducer<Text, IntWritable, Text, IntWritable> {
        private IntWritable result = new IntWritable();

        @Override
        public void reduce(Text key, Iterable<IntWritable> values,
                           Context context
        ) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            result.set(sum);
            context.write(key, result);
        }
    }

    public int run(String[] args) throws Exception {
        System.out.println("====="+getConf().get("tmpjars"));
        Job job = Job.getInstance(getConf(), "word count");
        job.setJarByClass(WordCount.class);
        job.setMapperClass(TokenizerMapper.class);
        job.setCombinerClass(IntSumReducer.class);
        job.setReducerClass(IntSumReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
        return 0;
    }

    public static void main(String[] args) throws Exception {
        try {
            System.out.println(new ProcessExecutor()
                    .redirectOutput(System.out)
                    .readOutput(true)
                    .command("ls", "./")
                    .execute().outputUTF8());
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        System.out.println(Arrays.asList(args));
        int res = ToolRunner.run(new Configuration(), new WordCount(), args);
        System.exit(res);
    }
}

```