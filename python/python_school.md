# python 学习

## 环境

### 安装python

安装python

```bash
apt install python2.7
apt install python3.4
```

[CentOS 7 安装 Python 3.6 | CentOS教程 - 与知](https://www.yuzhi100.com/tutorial/centos/centos-anzhuang-python36)

安装pip
>pip is already installed if you are using Python 2 >=2.7.9 or Python 3 >=3.4 downloaded from python.org or if you are working in a Virtual Environment created by virtualenv or pyvenv. Just make sure to upgrade pip.  
[Installation &#8212; pip 18.1 documentation](https://pip.pypa.io/en/stable/installing/)

[同时安装 Python 2 与Python 3 的方法及pip模块的下载安装 - 天木星辰 - 博客园](https://www.cnblogs.com/zcool/p/7147245.html)  
使用这个脚本[get-pip.py](https://bootstrap.pypa.io/get-pip.py),使用不同版本的python运行就会安装对应的pip。

安装pip的其他方式：
`apt-get install python3-pip` [I have python3.4 but no pip or ensurepip.. is something wrong with my python3.4 version?](https://stackoverflow.com/questions/29871372/i-have-python3-4-but-no-pip-or-ensurepip-is-something-wrong-with-my-python3-4)
`python -m ensurepip` [python3.6 安装后没有pip? - 知乎](https://www.zhihu.com/question/54906859)

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

### 安装python版本管理，包管理，虚拟环境 工具

virtualenv 、pyenv、Anaconda 均可以进行python的版本管理和包管理  
Anaconda是一个用于科学计算的Python发行版，自带了Numpy、Sklearn等机器学习相关的库  
Virtualenv是一个Python虚拟环境库，用来创建一个新的Python环境  
pyenv也是一个python虚拟环境工具，但是已经很久没有更新支持了  

**安装virtualenv**
[Installation - virtualenv 16.1.0 documentation](https://virtualenv.pypa.io/en/latest/installation/)
[virtualenv-廖雪峰的官方网站](https://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/001432712108300322c61f256c74803b43bfd65c6f8d0d0000)

Python3.3以上的版本通过venv模块原生支持虚拟环境，可以代替Python之前的virtualenv。

```bash
python3 -m venv ./venv
source ./venv/bin/activate
````

### 环境常见问题

#### mac python2.7 报错 ImportError: No module named zlib

[[已解决] mac python2.7 报错 ImportError: No module named zlib - V2EX](https://www.v2ex.com/t/511697)

解决了，用 brew 安装 python@2 时，其中有个警告时需要 apple commandline tools，如果没有他会从源码编译一个 python 出来，所以仍然可以安装上，但是这个是不 适配的，所以安装上命令行工具后(`xcode-select --install`)，再重新装一遍 python 就好了(`brew reinstall python@2`)

以下为原帖：

mac 系统，装了 python2 和 python3，但是执行 python2.7.10 的 pip 命令会报错。

```bash
Traceback (most recent call last):
  File "/usr/local/bin/pip", line 11, in <module>
    load_entry_point('pip==18.0', 'console_scripts', 'pip')()
  File "/usr/local/lib/python2.7/site-packages/pkg_resources/__init__.py", line 484, in load_entry_point
    return get_distribution(dist).load_entry_point(group, name)
  File "/usr/local/lib/python2.7/site-packages/pkg_resources/__init__.py", line 2714, in load_entry_point
    return ep.load()
  File "/usr/local/lib/python2.7/site-packages/pkg_resources/__init__.py", line 2332, in load
    return self.resolve()
  File "/usr/local/lib/python2.7/site-packages/pkg_resources/__init__.py", line 2338, in resolve
    module = __import__(self.module_name, fromlist=['__name__'], level=0)
  File "/usr/local/lib/python2.7/site-packages/pip/_internal/__init__.py", line 20, in <module>
    from pip._vendor.urllib3.exceptions import DependencyWarning
  File "/usr/local/lib/python2.7/site-packages/pip/_vendor/urllib3/__init__.py", line 8, in <module>
    from .connectionpool import (
  File "/usr/local/lib/python2.7/site-packages/pip/_vendor/urllib3/connectionpool.py", line 36, in <module>
    from .response import HTTPResponse
  File "/usr/local/lib/python2.7/site-packages/pip/_vendor/urllib3/response.py", line 3, in <module>
    import zlib
ImportError: No module named zlib
```

python 是用 homebrew 安装的。

网上说要安装 zlib 库，可是 mac 系统是自带一个版本的 zlib 的，我没法再装一个其他版本的吧？会冲突吧？
用 brew 安装 zlib 时的提示：

```bash
zlib: stable 1.2.11 (bottled) [keg-only]
General-purpose lossless data-compression library
https://zlib.net/
/usr/local/Cellar/zlib/1.2.11 (12 files, 373KB)
  Poured from bottle on 2018-11-26 at 18:31:13
From: https://github.com/Homebrew/homebrew-core/blob/master/Formula/zlib.rb
==> Caveats
zlib is keg-only, which means it was not symlinked into /usr/local,
because macOS already provides this software and installing another version in
parallel can cause all kinds of trouble.
```

---

## 知识

**__init__.py的作用**
[Python杂谈: __init__.py的作用](https://www.cnblogs.com/tp1226/p/8453854.html)

>python2中一个目录只有带有`__init__.py`才能识别会一个模块，使用import导入，但是在python3中，就算没有这个文件也可以作为模块导入。

**空值与空对象判断**
[Python中的空值判断](https://www.jianshu.com/p/a0d273550f70)
[Python中的NULL和None](https://blog.csdn.net/songyunli1111/article/details/75145533)  [python中的null值](https://www.cnblogs.com/landhu/p/6497975.html)

>python是把0，空字符串‘’，空列表[]和None都看作False，把其他数值和非空字符串都看作True

**命令行调用python的几种方式**

已如下项目结构为例:

```bash
# 目录结构如下
package/
    __init__.py
    mod1.py
package2/
    __init__.py
    run.py
```

```python
# run.py 内容如下
import sys
from package import mod1
print(sys.path)
```

启动方式：

```bash
# 直接启动（失败）
$ python package2/run.py
Traceback (most recent call last):
  File "package2/run.py", line 2, in <module>
    from package import mod1
ImportError: No module named package

# 以模块方式启动（成功） -m 方式启动类似于import模块的操作，会把当前执行目录('')加入sys.path中
$ python -m package2.run
['',
'/usr/local/Cellar/python/2.7.11/Frameworks/Python.framework/Versions/2.7/lib/python27.zip',...]

# 设置PYTHONPATH变量 (成功)
$ PYTHONPATH=. python package2/run.py
```

---

## 项目开发

python项目开发中的知识

### 项目交付

**依赖管理**
python中项目使用了那些第三方库，可以使用如下命令导出和导入。参考：[Python项目交付中环境迁移问题](https://www.jianshu.com/p/28b64c050f42)

```bash
# 导出所有库及库的版本到文件
pip freeze > requirements.txt
# 从文件安装依赖
pip install -r requirements.txt

# conda中的使用
conda env export > environment.yml
conda env create -f environment.yml
```

**项目迁移**
如果想要迁移项目，众多依赖包重新下载很费劲，但如果你的项目使用了虚拟环境，比如`virtualenv`，那么可以带着虚拟环境一起打包迁移。