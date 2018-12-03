# Python 修饰器的一些小细节

[Python 修饰器的一些小细节 - V2EX](https://www.v2ex.com/t/513981)

```python
"""
带参数修饰器会在初始化时就执行修饰器的代码并将方法体重新赋值给方法名。
"""


def deco1(args):
    """
    方法执行时不会再执行修饰器代码，**因为该代码返回了方法本身**
    等价于：
        def deco1(args):
            print("deco1:",args)
            return lambda fn:fn
        test1=deco1(args='deco1args')(mymethod)
    :param args:
    :return:
    """

    def decorator(fn):
        print('deco1:', args)
        return fn

    return decorator


@deco1("deco1args")
def test1():
    print("test1")


"""
不带参数修饰器在初始化的时候会将方法名赋值给修饰器方法，修饰器方法内部来手动调用被修饰的方法。
"""


def deco2(fn):
    """
    方法每次执行时都会执行修饰器因为 test2 重新赋值为修饰器的函数
    等价于：
        def deco2(fn):
            def inFn():
                print('deco2')
                fn()
            return inFn
        myMethod2 = deco2(lambda: print("call myMethod2"))
    :param fn:
    :return:
    """

    def decorator():
        print('deco2')
        fn()

    return decorator


@deco2
def test2():
    print("test2")


def deco3(fn):
    """
    不会执行 fn 因为 fn 没有被调用
    等价于:
        def deco3(fn):
            def inFn():
                print('deco3')
                return fn
            return inFn
        test3=deco3(test3)

    :return:
    """

    def decorator():
        print('deco3')
        return fn

    return decorator


@deco3
def test3():
    print("test3")


if __name__ == '__main__':
    print("-----开始执行 main 方法-----")

    print("test1 每次调用都不会执行修饰器：")
    test1()
    print("")
    test1()

    print("======")
    print("test2 每次调用都会执行修饰器：")
    test2()
    print("")
    test2()
    print("======")
    print("test3 不会被执行：")
    test3()
    print("test3 的返回值才是 test3 方法，因此要这样执行：")
    test3()()

"""
---------
"""

print("-------等价函数------")


def eqDeco1(args):
    print("deco1:", args)
    return lambda fn: fn


print("deco1 的等价：")

myMethod = eqDeco1(args='deco1args')(lambda: print("call myMethod1"))
myMethod()


def eqDeco2(fn):
    def inFn():
        print('deco2')
        fn()

    return inFn


myMethod2 = eqDeco2(lambda: print("call myMethod2"))
myMethod2()


def eqDeco3(fn):
    def inFn():
        print('deco3')
        return fn

    return inFn


myMethod3 = eqDeco3(lambda: print("call myMethod3"))
myMethod3()
myMethod3()()
```

执行结果

```bash
deco1: deco1args
-----开始执行 main 方法-----
test1 每次调用都不会执行修饰器：
test1

test1
======
test2 每次调用都会执行修饰器：
deco2
test2

deco2
test2
======
test3 不会被执行：
deco3
test3 的返回值才是 test3 方法，因此要这样执行：
deco3
test3
-------等价函数------
deco1 的等价：
deco1: deco1args
call myMethod1
deco2
call myMethod2
deco3
deco3
call myMethod3
```

## 在Django中实现flask的修饰器注册url映射

目录结构

```bash
django_school
├── __init__.py
├── controller
│   ├── __init__.py
│   ├── test.py
│   └── test2.py
├── settings.py
├── urls.py
└── wsgi.py
```

在`url.py`文件中创建修饰器，并import用到修饰器的方法以触发修饰器代码执行

```python
# url.py
from django.contrib import admin
from django.urls import path

urlpatterns = [
    path('admin/', admin.site.urls)
]

# 创建修饰器
def route(url_path):
    def add(fn):
        urlpatterns.append(path(url_path, fn))
        return fn

    return add

# import文件 执行修饰器
from django_school.controller import test,test2
```

具体使用的代码

```python
# controller.test1.py
from django.http import HttpResponse
from django_school.urls import route

@route("demo1/")
def index1(request):
    return HttpResponse("this is demo1")
```
