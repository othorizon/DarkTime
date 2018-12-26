# sqoop 简介及使用

## 1. 前言

  Sqoop是用于在Hadoop和关系型数据库之间流转数据的工具。可以使用Sqoop从关系型数据库（RDBMS）比如Mysql或者Oracle导入数据到Hadoop分布式文件系统上，在Hadoop MapReduce上对数据做转换，然后导回到RDBMS上。
  借助数据库的Schema描述信息，Sqoop自动实现了上述的很多过程，Sqoop使用MapReduce去导入和导出数据，这样可以提供并行操作以及提高容错能力。

## 2. Sqoop的Shell（命令行客户端）操作

```
usage: sqoop COMMAND [ARGS]

Available commands:
  codegen            生成Java代码
  create-hive-table  根据表结构生成hive表
  eval               执行SQL语句并显示结果
  export             导出HDFS目录到数据库表
  help               帮助
  import             从数据库导入数据到HDFS
  import-all-tables  导入数据库所有表到HDFS
  list-databases     列举所有的database
  list-tables        列举数据库中的所有表
  version            查看版本信息
```

### 2.1 sqoop import

#### 2.1.1 命令行客户端支持的命令参数

```
usage: sqoop import [GENERIC-ARGS] [TOOL-ARGS]

Common arguments:
   --connect <jdbc-uri>                                       Specify JDBC
                                                              connect
                                                              string
   --connection-manager <class-name>                          Specify
                                                              connection
                                                              manager
                                                              class name
   --connection-param-file <properties-file>                  Specify
                                                              connection
                                                              parameters
                                                              file
   --driver <class-name>                                      Manually
                                                              specify JDBC
                                                              driver class
                                                              to use
   --hadoop-home <hdir>                                       Override
                                                              $HADOOP_MAPR
                                                              ED_HOME_ARG
   --hadoop-mapred-home <dir>                                 Override
                                                              $HADOOP_MAPR
                                                              ED_HOME_ARG
   --help                                                     Print usage
                                                              instructions
   --metadata-transaction-isolation-level <isolationlevel>    Defines the
                                                              transaction
                                                              isolation
                                                              level for
                                                              metadata
                                                              queries. For
                                                              more details
                                                              check
                                                              java.sql.Con
                                                              nection
                                                              javadoc or
                                                              the JDBC
                                                              specificaito
                                                              n
   --oracle-escaping-disabled <boolean>                       Disable the
                                                              escaping
                                                              mechanism of
                                                              the
                                                              Oracle/OraOo
                                                              p connection
                                                              managers
-P                                                            Read
                                                              password
                                                              from console
   --password <password>                                      Set
                                                              authenticati
                                                              on password
   --password-alias <password-alias>                          Credential
                                                              provider
                                                              password
                                                              alias
   --password-file <password-file>                            Set
                                                              authenticati
                                                              on password
                                                              file path
   --relaxed-isolation                                        Use
                                                              read-uncommi
                                                              tted
                                                              isolation
                                                              for imports
   --skip-dist-cache                                          Skip copying
                                                              jars to
                                                              distributed
                                                              cache
   --temporary-rootdir <rootdir>                              Defines the
                                                              temporary
                                                              root
                                                              directory
                                                              for the
                                                              import
   --throw-on-error                                           Rethrow a
                                                              RuntimeExcep
                                                              tion on
                                                              error
                                                              occurred
                                                              during the
                                                              job
   --username <username>                                      Set
                                                              authenticati
                                                              on username
   --verbose                                                  Print more
                                                              information
                                                              while
                                                              working

Import control arguments:
   --append                                                   Imports data
                                                              in append
                                                              mode
   --as-avrodatafile                                          Imports data
                                                              to Avro data
                                                              files
   --as-parquetfile                                           Imports data
                                                              to Parquet
                                                              files
   --as-sequencefile                                          Imports data
                                                              to
                                                              SequenceFile
                                                              s
   --as-textfile                                              Imports data
                                                              as plain
                                                              text
                                                              (default)
   --autoreset-to-one-mapper                                  Reset the
                                                              number of
                                                              mappers to
                                                              one mapper
                                                              if no split
                                                              key
                                                              available
   --boundary-query <statement>                               Set boundary
                                                              query for
                                                              retrieving
                                                              max and min
                                                              value of the
                                                              primary key
   --columns <col,col,col...>                                 Columns to
                                                              import from
                                                              table
   --compression-codec <codec>                                Compression
                                                              codec to use
                                                              for import
   --delete-target-dir                                        Imports data
                                                              in delete
                                                              mode
   --direct                                                   Use direct
                                                              import fast
                                                              path
   --direct-split-size <n>                                    Split the
                                                              input stream
                                                              every 'n'
                                                              bytes when
                                                              importing in
                                                              direct mode
-e,--query <statement>                                        Import
                                                              results of
                                                              SQL
                                                              'statement'
   --fetch-size <n>                                           Set number
                                                              'n' of rows
                                                              to fetch
                                                              from the
                                                              database
                                                              when more
                                                              rows are
                                                              needed
   --inline-lob-limit <n>                                     Set the
                                                              maximum size
                                                              for an
                                                              inline LOB
-m,--num-mappers <n>                                          Use 'n' map
                                                              tasks to
                                                              import in
                                                              parallel
   --mapreduce-job-name <name>                                Set name for
                                                              generated
                                                              mapreduce
                                                              job
   --merge-key <column>                                       Key column
                                                              to use to
                                                              join results
   --split-by <column-name>                                   Column of
                                                              the table
                                                              used to
                                                              split work
                                                              units
   --split-limit <size>                                       Upper Limit
                                                              of rows per
                                                              split for
                                                              split
                                                              columns of
                                                              Date/Time/Ti
                                                              mestamp and
                                                              integer
                                                              types. For
                                                              date or
                                                              timestamp
                                                              fields it is
                                                              calculated
                                                              in seconds.
                                                              split-limit
                                                              should be
                                                              greater than
                                                              0
   --table <table-name>                                       Table to
                                                              read
   --target-dir <dir>                                         HDFS plain
                                                              table
                                                              destination
   --validate                                                 Validate the
                                                              copy using
                                                              the
                                                              configured
                                                              validator
   --validation-failurehandler <validation-failurehandler>    Fully
                                                              qualified
                                                              class name
                                                              for
                                                              ValidationFa
                                                              ilureHandler
   --validation-threshold <validation-threshold>              Fully
                                                              qualified
                                                              class name
                                                              for
                                                              ValidationTh
                                                              reshold
   --validator <validator>                                    Fully
                                                              qualified
                                                              class name
                                                              for the
                                                              Validator
   --warehouse-dir <dir>                                      HDFS parent
                                                              for table
                                                              destination
   --where <where clause>                                     WHERE clause
                                                              to use
                                                              during
                                                              import
-z,--compress                                                 Enable
                                                              compression

Incremental import arguments:
   --check-column <column>        Source column to check for incremental
                                  change
   --incremental <import-type>    Define an incremental import of type
                                  'append' or 'lastmodified'
   --last-value <value>           Last imported value in the incremental
                                  check column

Output line formatting arguments:
   --enclosed-by <char>               Sets a required field enclosing
                                      character
   --escaped-by <char>                Sets the escape character
   --fields-terminated-by <char>      Sets the field separator character
   --lines-terminated-by <char>       Sets the end-of-line character
   --mysql-delimiters                 Uses MySQL's default delimiter set:
                                      fields: ,  lines: \n  escaped-by: \
                                      optionally-enclosed-by: '
   --optionally-enclosed-by <char>    Sets a field enclosing character

Input parsing arguments:
   --input-enclosed-by <char>               Sets a required field encloser
   --input-escaped-by <char>                Sets the input escape
                                            character
   --input-fields-terminated-by <char>      Sets the input field separator
   --input-lines-terminated-by <char>       Sets the input end-of-line
                                            char
   --input-optionally-enclosed-by <char>    Sets a field enclosing
                                            character

Hive arguments:
   --create-hive-table                         Fail if the target hive
                                               table exists
   --external-table-dir <hdfs path>            Sets where the external
                                               table is in HDFS
   --hive-database <database-name>             Sets the database name to
                                               use when importing to hive
   --hive-delims-replacement <arg>             Replace Hive record \0x01
                                               and row delimiters (\n\r)
                                               from imported string fields
                                               with user-defined string
   --hive-drop-import-delims                   Drop Hive record \0x01 and
                                               row delimiters (\n\r) from
                                               imported string fields
   --hive-home <dir>                           Override $HIVE_HOME
   --hive-import                               Import tables into Hive
                                               (Uses Hive's default
                                               delimiters if none are
                                               set.)
   --hive-overwrite                            Overwrite existing data in
                                               the Hive table
   --hive-partition-key <partition-key>        Sets the partition key to
                                               use when importing to hive
   --hive-partition-value <partition-value>    Sets the partition value to
                                               use when importing to hive
   --hive-table <table-name>                   Sets the table name to use
                                               when importing to hive
   --map-column-hive <arg>                     Override mapping for
                                               specific column to hive
                                               types.

HBase arguments:
   --column-family <family>    Sets the target column family for the
                               import
   --hbase-bulkload            Enables HBase bulk loading
   --hbase-create-table        If specified, create missing HBase tables
   --hbase-row-key <col>       Specifies which input column to use as the
                               row key
   --hbase-table <table>       Import to <table> in HBase

HCatalog arguments:
   --hcatalog-database <arg>                        HCatalog database name
   --hcatalog-home <hdir>                           Override $HCAT_HOME
   --hcatalog-partition-keys <partition-key>        Sets the partition
                                                    keys to use when
                                                    importing to hive
   --hcatalog-partition-values <partition-value>    Sets the partition
                                                    values to use when
                                                    importing to hive
   --hcatalog-table <arg>                           HCatalog table name
   --hive-home <dir>                                Override $HIVE_HOME
   --hive-partition-key <partition-key>             Sets the partition key
                                                    to use when importing
                                                    to hive
   --hive-partition-value <partition-value>         Sets the partition
                                                    value to use when
                                                    importing to hive
   --map-column-hive <arg>                          Override mapping for
                                                    specific column to
                                                    hive types.

HCatalog import specific options:
   --create-hcatalog-table             Create HCatalog before import
   --drop-and-create-hcatalog-table    Drop and Create HCatalog before
                                       import
   --hcatalog-storage-stanza <arg>     HCatalog storage stanza for table
                                       creation

Accumulo arguments:
   --accumulo-batch-size <size>          Batch size in bytes
   --accumulo-column-family <family>     Sets the target column family for
                                         the import
   --accumulo-create-table               If specified, create missing
                                         Accumulo tables
   --accumulo-instance <instance>        Accumulo instance name.
   --accumulo-max-latency <latency>      Max write latency in milliseconds
   --accumulo-password <password>        Accumulo password.
   --accumulo-row-key <col>              Specifies which input column to
                                         use as the row key
   --accumulo-table <table>              Import to <table> in Accumulo
   --accumulo-user <user>                Accumulo user name.
   --accumulo-visibility <vis>           Visibility token to be applied to
                                         all rows imported
   --accumulo-zookeepers <zookeepers>    Comma-separated list of
                                         zookeepers (host:port)

Code generation arguments:
   --bindir <dir>                             Output directory for
                                              compiled objects
   --class-name <name>                        Sets the generated class
                                              name. This overrides
                                              --package-name. When
                                              combined with --jar-file,
                                              sets the input class.
   --escape-mapping-column-names <boolean>    Disable special characters
                                              escaping in column names
   --input-null-non-string <null-str>         Input null non-string
                                              representation
   --input-null-string <null-str>             Input null string
                                              representation
   --jar-file <file>                          Disable code generation; use
                                              specified jar
   --map-column-java <arg>                    Override mapping for
                                              specific columns to java
                                              types
   --null-non-string <null-str>               Null non-string
                                              representation
   --null-string <null-str>                   Null string representation
   --outdir <dir>                             Output directory for
                                              generated code
   --package-name <name>                      Put auto-generated classes
                                              in this package

Generic Hadoop command-line arguments:
(must preceed any tool-specific arguments)
Generic options supported are
-conf <configuration file>     specify an application configuration file
-D <property=value>            use value for given property
-fs <local|namenode:port>      specify a namenode
-jt <local|resourcemanager:port>    specify a ResourceManager
-files <comma separated list of files>    specify comma separated files to be copied to the map reduce cluster
-libjars <comma separated list of jars>    specify comma separated jar files to include in the classpath.
-archives <comma separated list of archives>    specify comma separated archives to be unarchived on the compute machines.

The general command line syntax is
bin/hadoop command [genericOptions] [commandOptions]


At minimum, you must specify --connect and --table
Arguments to mysqldump and other subprograms may be supplied
after a '--' on the command line.
```

#### 2.1.2 公共参数

`--connect <jdbc-url>` 制定JDBC连接串

`--connection-manager <class-name>` 指定连接管理类名

`--driver <class-name>` 指定JDBC驱动类

`--hadoop-mapred-home <dir>` 覆盖$HADOOP_MAPRED_HOME

`--help` 帮助

`--password-file` 指定密码文件

`-P` 从命令行读取密码

`--password <password>` 指定密码

`--username <username>` 指定用户名

`--verbose` 显示更丰富的日志

`--connection-param-file <filename>` 连接参数的可选配置文件

#### 2.1.3 连接一个数据库

使用`--conenct`来连接数据库  
`$ sqoop import --connect jdbc:mysql://database.example.com/employees`

这个连接串会连接database.example.com服务器上的employees数据库。必须要注意如果你准备在分布式Hadoop集群使用Sqoop导入数据，你不能使用`localhost`作为Url来连接数据源，因为这个连接串会被应用于你的MapReduce集群的任务节点上。  
指定数据库连接后，需要提供数据库访问的用户名和密码，密码有多种设定方式，显式的指定密码是不安全的

```bash
$ sqoop import --connect jdbc:mysql://database.example.com/employees \
    --username venkatesh --password-file ${user.home}/.password
```

Sqoop内置了MySql的驱动，对于其他的，需要自己安装驱动，将驱动文件放到`$SQOOP_HOME/lib`目录下，然后使用`--driver`参数指定驱动的全类名

```bash
$ sqoop import --driver com.microsoft.jdbc.sqlserver.SQLServerDriver \
    --connect <connect-string> ...
```

当使用JDBC连接一个数据库时，也可以使用`--connection-param-file`参数来通过一个配置文件指定额外的参数，这个文件会被作为标准的Java配置来解析并传递给驱动来创建连接。

#### 2.1.4 Import 配置参数

`--append` 追加数据到HDFS中已有的数据文件

`--as-avrodatafile` 导入数据为Avro格式

`--as-sequencefile` 导入数据为SqeuqnceFile

`--as-textfile` 导入数据格式为textfile(默认)

`--as-parquetfile` 导入数据为Parquet格式

`--boundary-query <statement>` 覆盖创建分割时的边界查询SQL

`--columns <col,col,col…>` 指定导入列

`--delete-target-dir`  如果存在，删除导入的target目录

`--direct` 如果数据源支持，使用direct模式导入

`-m,--num-mappers <n>` 指定并行导入数据的map数量(正整数)，默认为4个

`-e,--query <statement>` 导入查询语句得到的数据

`--split-by <column-name>` 指定分割split的字段

`--table < table-name >` 制定数据库中的表名

`--target-dir < dir >` 目标HDFS路径

`--warehouse-dir <dir>` 表目标位置父路径

`--where <where clause>` 导入的过滤条件，即sql语句的where条件

`--null-string < null-string >` 源表中为null的记录导入为string类型时显示为null-string，默认显示为”null”

`--null-non-string <null-string>` 源表中为null的记录导入为非string类型时显示为null-string，默认显示为”null”

### 2.2 sqoop export

#### 2.2.1 命令行客户端支持的命令参数

```
usage: sqoop export [GENERIC-ARGS] [TOOL-ARGS]

Common arguments:
   --connect <jdbc-uri>                                       Specify JDBC
                                                              connect
                                                              string
   --connection-manager <class-name>                          Specify
                                                              connection
                                                              manager
                                                              class name
   --connection-param-file <properties-file>                  Specify
                                                              connection
                                                              parameters
                                                              file
   --driver <class-name>                                      Manually
                                                              specify JDBC
                                                              driver class
                                                              to use
   --hadoop-home <hdir>                                       Override
                                                              $HADOOP_MAPR
                                                              ED_HOME_ARG
   --hadoop-mapred-home <dir>                                 Override
                                                              $HADOOP_MAPR
                                                              ED_HOME_ARG
   --help                                                     Print usage
                                                              instructions
   --metadata-transaction-isolation-level <isolationlevel>    Defines the
                                                              transaction
                                                              isolation
                                                              level for
                                                              metadata
                                                              queries. For
                                                              more details
                                                              check
                                                              java.sql.Con
                                                              nection
                                                              javadoc or
                                                              the JDBC
                                                              specificaito
                                                              n
   --oracle-escaping-disabled <boolean>                       Disable the
                                                              escaping
                                                              mechanism of
                                                              the
                                                              Oracle/OraOo
                                                              p connection
                                                              managers
-P                                                            Read
                                                              password
                                                              from console
   --password <password>                                      Set
                                                              authenticati
                                                              on password
   --password-alias <password-alias>                          Credential
                                                              provider
                                                              password
                                                              alias
   --password-file <password-file>                            Set
                                                              authenticati
                                                              on password
                                                              file path
   --relaxed-isolation                                        Use
                                                              read-uncommi
                                                              tted
                                                              isolation
                                                              for imports
   --skip-dist-cache                                          Skip copying
                                                              jars to
                                                              distributed
                                                              cache
   --temporary-rootdir <rootdir>                              Defines the
                                                              temporary
                                                              root
                                                              directory
                                                              for the
                                                              import
   --throw-on-error                                           Rethrow a
                                                              RuntimeExcep
                                                              tion on
                                                              error
                                                              occurred
                                                              during the
                                                              job
   --username <username>                                      Set
                                                              authenticati
                                                              on username
   --verbose                                                  Print more
                                                              information
                                                              while
                                                              working

Export control arguments:
   --batch                                                    Indicates
                                                              underlying
                                                              statements
                                                              to be
                                                              executed in
                                                              batch mode
   --call <arg>                                               Populate the
                                                              table using
                                                              this stored
                                                              procedure
                                                              (one call
                                                              per row)
   --clear-staging-table                                      Indicates
                                                              that any
                                                              data in
                                                              staging
                                                              table can be
                                                              deleted
   --columns <col,col,col...>                                 Columns to
                                                              export to
                                                              table
   --direct                                                   Use direct
                                                              export fast
                                                              path
   --export-dir <dir>                                         HDFS source
                                                              path for the
                                                              export
-m,--num-mappers <n>                                          Use 'n' map
                                                              tasks to
                                                              export in
                                                              parallel
   --mapreduce-job-name <name>                                Set name for
                                                              generated
                                                              mapreduce
                                                              job
   --staging-table <table-name>                               Intermediate
                                                              staging
                                                              table
   --table <table-name>                                       Table to
                                                              populate
   --update-key <key>                                         Update
                                                              records by
                                                              specified
                                                              key column
   --update-mode <mode>                                       Specifies
                                                              how updates
                                                              are
                                                              performed
                                                              when new
                                                              rows are
                                                              found with
                                                              non-matching
                                                              keys in
                                                              database
   --validate                                                 Validate the
                                                              copy using
                                                              the
                                                              configured
                                                              validator
   --validation-failurehandler <validation-failurehandler>    Fully
                                                              qualified
                                                              class name
                                                              for
                                                              ValidationFa
                                                              ilureHandler
   --validation-threshold <validation-threshold>              Fully
                                                              qualified
                                                              class name
                                                              for
                                                              ValidationTh
                                                              reshold
   --validator <validator>                                    Fully
                                                              qualified
                                                              class name
                                                              for the
                                                              Validator

Input parsing arguments:
   --input-enclosed-by <char>               Sets a required field encloser
   --input-escaped-by <char>                Sets the input escape
                                            character
   --input-fields-terminated-by <char>      Sets the input field separator
   --input-lines-terminated-by <char>       Sets the input end-of-line
                                            char
   --input-optionally-enclosed-by <char>    Sets a field enclosing
                                            character

Output line formatting arguments:
   --enclosed-by <char>               Sets a required field enclosing
                                      character
   --escaped-by <char>                Sets the escape character
   --fields-terminated-by <char>      Sets the field separator character
   --lines-terminated-by <char>       Sets the end-of-line character
   --mysql-delimiters                 Uses MySQL's default delimiter set:
                                      fields: ,  lines: \n  escaped-by: \
                                      optionally-enclosed-by: '
   --optionally-enclosed-by <char>    Sets a field enclosing character

Code generation arguments:
   --bindir <dir>                             Output directory for
                                              compiled objects
   --class-name <name>                        Sets the generated class
                                              name. This overrides
                                              --package-name. When
                                              combined with --jar-file,
                                              sets the input class.
   --escape-mapping-column-names <boolean>    Disable special characters
                                              escaping in column names
   --input-null-non-string <null-str>         Input null non-string
                                              representation
   --input-null-string <null-str>             Input null string
                                              representation
   --jar-file <file>                          Disable code generation; use
                                              specified jar
   --map-column-java <arg>                    Override mapping for
                                              specific columns to java
                                              types
   --null-non-string <null-str>               Null non-string
                                              representation
   --null-string <null-str>                   Null string representation
   --outdir <dir>                             Output directory for
                                              generated code
   --package-name <name>                      Put auto-generated classes
                                              in this package

HCatalog arguments:
   --hcatalog-database <arg>                        HCatalog database name
   --hcatalog-home <hdir>                           Override $HCAT_HOME
   --hcatalog-partition-keys <partition-key>        Sets the partition
                                                    keys to use when
                                                    importing to hive
   --hcatalog-partition-values <partition-value>    Sets the partition
                                                    values to use when
                                                    importing to hive
   --hcatalog-table <arg>                           HCatalog table name
   --hive-home <dir>                                Override $HIVE_HOME
   --hive-partition-key <partition-key>             Sets the partition key
                                                    to use when importing
                                                    to hive
   --hive-partition-value <partition-value>         Sets the partition
                                                    value to use when
                                                    importing to hive
   --map-column-hive <arg>                          Override mapping for
                                                    specific column to
                                                    hive types.

Generic Hadoop command-line arguments:
(must preceed any tool-specific arguments)
Generic options supported are
-conf <configuration file>     specify an application configuration file
-D <property=value>            use value for given property
-fs <local|namenode:port>      specify a namenode
-jt <local|resourcemanager:port>    specify a ResourceManager
-files <comma separated list of files>    specify comma separated files to be copied to the map reduce cluster
-libjars <comma separated list of jars>    specify comma separated jar files to include in the classpath.
-archives <comma separated list of archives>    specify comma separated archives to be unarchived on the compute machines.

The general command line syntax is
bin/hadoop command [genericOptions] [commandOptions]


At minimum, you must specify --connect, --export-dir, and --table

```

#### 2.2.2 公共参数

`--connect <jdbc-url>` 制定JDBC连接串

`--connection-manager <class-name>` 指定连接管理类名

`--driver <class-name>` 指定JDBC驱动类

`--hadoop-mapred-home <dir>` 覆盖$HADOOP_MAPRED_HOME

`--help` 帮助

`--password-file` 指定密码文件

`-P` 从命令行读取密码

`--password <password>` 指定密码

`--username <username>` 指定用户名

`--verbose` 显示更丰富的日志

`--connection-param-file <filename>` 连接参数的可选配置文件

#### 2.2.3 Export 配置参数

`--columns < col,col,col… >` 指定导出字段

`--direct` 使用direct模式

`--export-dir <dir>` 导出HDFS文件路径

`-m,--num-mappers <n>` 指定并行导出数据的map个数，默认为4个

`--table <table-name>` 指定数据库中的表名

`--update-key <col-name>` update模式下更新的字段，多个字段使用逗号分割

`--update-mode <mode>` 当一条数据找不到对应的记录时的操作，默认为updateonly即忽略插入操作，可以设置为allowinsert即遇到新记录会插入

`--input-null-string <null-string>` string类型字段表示null值的字符

`--input-null-non-string <null-string>` 非string类型字段表示null值的字符
`--staging-table < staging-table-name >` 在最终导出到目标表之前临时存储这些记录的表
`--clear-staging-table` 启用清除staging-table表中的记录

`--export-dir`参数，以及`--table`或者`--call`是必须指定的参数。因为这三个参数分别用来指定导出数据集，以及数据导出后的去处，既可以是导出到某张表，也可以是对每一条导出记录调用存储过程。有些数据库例如Mysql可以支持direct模式导出（在mysql中就是调用mysqldump导出数据）。

由于Sqoop将导出过程切分成了多个，那么就会有可能某个导出任务失败而导致只有部分数据提交到了导出数据库中。当失败job重试时，就有可能会出现数据重复，或者导出数据冲突等情况发生。这时可以指定一个--staging-table参数来避免这种情况的发生。导出的数据首先会缓存在该表中，最后等job执行成功后会将该表中的数据移动到最终目标表中。

### 2.3 其他配置

#### 2.3.1 使用HCatalog

`--hcatalog-database` 指定hcatalog表的数据库名

`--hcatalog-table` 指定表名

`--hcatalog-home` HCatalog安装目录

#### 2.3.2 Hive配置

`--hive-home` 指定hive home目录

`--hive-partition-key` 指定写入hive时的分区key

`--hive-partition-value` 指定写入hive时的分区value
