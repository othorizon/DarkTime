# sql

## sql命令

[mysqldump详解](/linux/mysqldump脚本.md)
创建数据库：`CREATE DATABASE `activiti` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;`
mysqldump:`mysqldump -hlocalhost -uadmin -P3308 -p123 --opt --single-transaction activiti `
mysql导入:`mysql -hlocahost -P3310 -uroot -p1234 activiti < ./activiti.sql`
导出并导入:`mysqldump -h10.0.0.0 -uadmin -P9800 -p123456 --opt --single-transaction boss table1 table 2 | mysql  -uroot -P8306 -p123456 -C boss`

## sql函数

### 时间函数

#### 时间增加间隔 date_add()

[mysql 日期操作 增减天数、时间转换、时间戳 - 蚊子吃青蛙 - 博客园](https://www.cnblogs.com/wenzichiqingwa/archive/2013/03/05/2944485.html)

```sql
set @dt = now();
select date_add(@dt, interval 1 day);        -- add 1 day
select date_add(@dt, interval 1 hour);       -- add 1 hour
select date_add(@dt, interval 1 minute);     -- ...
select date_add(@dt, interval 1 second);
select date_add(@dt, interval 1 microsecond);
select date_add(@dt, interval 1 week);
select date_add(@dt, interval 1 month);
select date_add(@dt, interval 1 quarter);
select date_add(@dt, interval 1 year);
```
