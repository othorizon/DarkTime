# docker 常用镜像

## mysql

具体帮助查看：https://hub.docker.com/r/library/mysql/

```bash
docker volume create mysqldb
docker run --hostname mysql --name mysql -v mysqldb:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=123456 -d mysql:5.6.41
```
