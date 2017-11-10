# sonarqube 相关配置


- 使用maven向sonar推送代码时指定分支：
`-Dsonar.branch=branchname`

- 配置单元测试代码覆盖率,使用JaCoCO
    1. To launch JaCoCo as part of your Maven build, use this command: mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent install -Dmaven.test.failure.ignore=true
    2. 想要让sonar去展示得到单元测试的报告，需要配置单元测试报告目录：sonar.jacoco.reportPaths=target/jacoco.exec

        方法1:在maven项目中配置
        方法2:在mvn去执行sonar的代码扫描时增加这个参数配置
- 示例jenkins构建命令

``` bash
mvn clean org.jacoco:jacoco-maven-plugin:0.7.9:prepare-agent \
package org.sonarsource.scanner.maven:sonar-maven-plugin:3.3.0.603:sonar \
-Dmaven.test.failure.ignore=true -Dsonar.jacoco.reportPaths=target/jacoco.exec -Dsonar.branch=${specify} -Pdev -U
```



