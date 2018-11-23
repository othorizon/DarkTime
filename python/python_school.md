# python 学习

## 环境

### 安装python

安装python

```bash
apt install python2.7
apt install python3.4
```

安装pip
>pip is already installed if you are using Python 2 >=2.7.9 or Python 3 >=3.4 downloaded from python.org or if you are working in a Virtual Environment created by virtualenv or pyvenv. Just make sure to upgrade pip.  
[Installation &#8212; pip 18.1 documentation](https://pip.pypa.io/en/stable/installing/)

[同时安装 Python 2 与Python 3 的方法及pip模块的下载安装 - 天木星辰 - 博客园](https://www.cnblogs.com/zcool/p/7147245.html)  
使用这个脚本[get-pip.py](https://bootstrap.pypa.io/get-pip.py),使用不同版本的python运行就会安装对应的pip。

### 安装jupyterlab

帮助文档 [The Jupyter Notebook &mdash; Jupyter Notebook 5.7.2 documentation](https://jupyter-notebook.readthedocs.io/en/stable/index.html)  
docker镜像 [https://hub.docker.com/r/jupyter/datascience-notebook/](https://hub.docker.com/r/jupyter/datascience-notebook/)  
官方的datascience镜像整合了很多东西，也可以选择使用base镜像。  
默认启用的是经典的jupyter，如果想使用正在测试的jupyterlab可以执行docker时增加cmd：`start.sh jupyter lab`。  
start.sh和start-notebook.sh提供了很多可选参数，参考[Common Features docker-stacks documentation](https://jupyter-docker-stacks.readthedocs.io/en/latest/using/common.html#start-sh)  

完整启动命令：`docker run -d -it --name datascience_jupyter_lab -v jupyter_notebook:/home/jovyan/work -p 8909:8888 jupyter/datascience-notebook start.sh jupyter lab`  

jupyter安装其他内核

[How do I add python3 kernel to jupyter (IPython)](https://stackoverflow.com/questions/28831854/how-do-i-add-python3-kernel-to-jupyter-ipython)  [Installing the IPython kernel &mdash; IPython 7.1.1 documentation](https://ipython.readthedocs.io/en/stable/install/kernel_install.html#kernel-install)  

```bash
python -m ipykernel install --help

# --user 表示安装到当前用户
python2 -m pip install ipykernel
python2 -m ipykernel install --user

python3 -m pip install ipykernel
python3 -m ipykernel install --user
```