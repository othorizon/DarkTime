# 群晖nas札记

## 增强使用

### docker相关

安装网易云音乐下载器

```bash
docker pull python:3-slim
#下载netease-dl源码
git clone https://github.com/ziwenxie/netease-dl
docker run --rm -it -v /netease-dl:/apps python:3-slim bash
#容器内安装netease-dl
(docker) python /apps/setup.py install
```