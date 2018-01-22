# sql函数

## 时间函数

### 时间增加间隔 date_add()

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