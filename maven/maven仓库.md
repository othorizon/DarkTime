# 仓库

## 安装到私有仓库

```shell
mvn deploy:deploy-file\
 -DgroupId=com.example.example -DartifactId=common-utils -Dversion=1.0-SNAPSHOT\
 -Dpackaging=jar -Dfile=common-utils.jar\
 -DrepositoryId=snapshots\
 -Durl=http://127.0.0.1/nexus/content/repositories/snapshots/
```

### 标准的mvn deploy 到仓库

[pom配置之：&lt;distributionManagement&gt;snapshot快照库和release发布库 - CSDN博客](http://blog.csdn.net/aitangyong/article/details/53332091)

```xml
<distributionManagement>
    <snapshotRepository>
        <id>snapshots</id>
        <name>snapshots</name>
        <url>http://127.0.0.1:8888/nexus/content/repositories/snapshots/</url>
    </snapshotRepository>
</distributionManagement>
```