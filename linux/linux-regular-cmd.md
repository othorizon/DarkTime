# 常用命令

## zip压缩解压缩

Linux中zip压缩和unzip解压缩命令详解

1、把/home目录下面的mydata目录压缩为mydata.zip
zip -r mydata.zip mydata #压缩mydata目录
2、把/home目录下面的mydata.zip解压到mydatabak目录里面
unzip mydata.zip -d mydatabak
3、把/home目录下面的abc文件夹和123.txt压缩成为abc123.zip
zip -r abc123.zip abc 123.txt
4、把/home目录下面的wwwroot.zip直接解压到/home目录里面
unzip wwwroot.zip
5、把/home目录下面的abc12.zip、abc23.zip、abc34.zip同时解压到/home目录里面
unzip abc*.zip
6、查看把/home目录下面的wwwroot.zip里面的内容
unzip -v wwwroot.zip
7、验证/home目录下面的wwwroot.zip是否完整
unzip -t wwwroot.zip
8、把/home目录下面wwwroot.zip里面的所有文件解压到第一级目录
unzip -j wwwroot.zi

>[Linux中zip压缩和unzip解压缩命令](https://blog.csdn.net/a19860903/article/details/46711869)
主要参数:
-c：将解压缩的结果
-l：显示压缩文件内所包含的文件
-p：与-c参数类似，会将解压缩的结果显示到屏幕上，但不会执行任何的转换
-t：检查压缩文件是否正确
-u：与-f参数类似，但是除了更新现有的文件外，也会将压缩文件中的其它文件解压缩到目录中
-v：执行是时显示详细的信息
-z：仅显示压缩文件的备注文字
-a：对文本文件进行必要的字符转换
-b：不要对文本文件进行字符转换
-C：压缩文件中的文件名称区分大小写
-j：不处理压缩文件中原有的目录路径
-L：将压缩文件中的全部文件名改为小写
-M：将输出结果送到more程序处理
-n：解压缩时不要覆盖原有的文件
-o：不必先询问用户，unzip执行后覆盖原有文件
-P：使用zip的密码选项
-q：执行时不显示任何信息
-s：将文件名中的空白字符转换为底线字符
-V：保留VMS的文件版本信息
-X：解压缩时同时回存文件原来的UID/GID
