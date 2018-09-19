# gradle 学习

## 使用

跳过测试 添加`-x`参数,-x参数用来排除不需要执行的任务：`gradle build -x test` 
[Skip tests in IntelliJ using Gradle](https://stackoverflow.com/questions/26983771/skip-tests-in-intellij-using-gradle)

### 打包可运行的jar包

```groovy
buildscript {
    repositories {
        maven {
            url="https://plugins.gradle.org/m2/"
        }
    }
    dependencies {
        classpath "com.github.jengelman.gradle.plugins:shadow:2.0.4"
    }
}

group = 'datacenter'
version = '1.0-SNAPSHOT'

apply plugin: "com.github.johnrengelman.shadow"

apply plugin: 'java'
//apply plugin:'application'
//mainClassName='datacenter.etl.Main'

sourceCompatibility = 1.8

repositories {
    maven {
        url='http://10.69.34.219:8888/nexus/content/groups/public'
    }
    mavenCentral()
    maven {
        url='https://repository.cloudera.com/artifactory/cloudera-repos/'
    }
}

dependencies {
    compile group: 'com.google.code.gson', name: 'gson', version: '2.8.2'
    compile group: 'org.apache.sqoop', name: 'sqoop', version: '1.4.6-cdh5.5.2'
    compile group: 'mysql', name: 'mysql-connector-java', version: '5.1.44'

    testCompile group: 'junit', name: 'junit', version: '4.12'
}

jar {
    manifest {  //incubating版本，以后版本可能会改API
        attributes("Main-Class": "datacenter.etl.Main",
                "Implementation-Title": "Gradle")
    }
}


shadowJar {
    zip64 = true
}
assemble.dependsOn(shadowJar)
```

## 基础

一个标准的gradle项目

```bash
.
├── build.gradle
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
└── settings.gradle
```

1.使用grovvy语法

## 安装

在不安装gradle的情况下可以使用gradle wrapper，idea创建项目时会自动下载wrapper。
安装参考：[Gradle | Installation](https://gradle.org/install/)
可以选择使用brew等工具安装，也可以直接下载压缩包后配置PATH路径。

### gradle wrapper

和maven的warpper一样，使用 wrapper 保证了团队中每一个开发者都使用同样版本的 Gradle，并能够使用 Gradle 进行项目的构建（即使他们的电脑上并没有安装 Gradle）
一个wrapper所需要的文件如下

```bash
.
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
```

`gradle`文件夹下是其gradle按需精简的gradle程序包，`gradlew`和`gradlew.bat`是linux和windows环境下的启动脚本。

>当你运行 Gradle Wrapper 时它做了以下几件事：
解析给予 gradlew 的参数
安装正确版本的 Gradle (会在目录下生成`.gradle`文件夹)
调用 Gradle 去执行指定的任务
注意 wrapper 仅接受两个可选参数：
-q 或 --quiet 去阻止输出
-g 或 --gradle-user-home 去指定一个和替换的 Gradle 的 home 目录。
参考：https://blog.csdn.net/zsensei/article/details/78443501
