# java

## 使用Java 8中的Stream

[使用Java 8中的Stream - 随风逝去,只是凋零 - 博客园](https://www.cnblogs.com/zxf330301/p/6586750.html)

### reduce

`reduce(accumulator)` ：参数是一个执行双目运算的 Functional Interface ，假如这个参数表示的操作为op，stream中的元素为x, y, z, …，则 reduce() 执行的就是 x op y op z ...，所以要求op这个操作具有结合性(associative)，即满足： (x op y) op z = x op (y op z)，满足这个要求的操作主要有：求和、求积、求最大值、求最小值、字符串连接、集合并集和交集等。另外，该函数的返回值是Optional的：

```java
Optional <integer>sum1 = numStream.reduce((x, y) -> x + y);
```

`reduce(identity, accumulator)` ：可以认为第一个参数为默认值，但需要满足 identity op x = x ，所以对于求和操作， identity 的值为0，对于求积操作， identity 的值为1。返回值类型是stream元素的类型：

```java
Integer sum2 = numStream.reduce(0, Integer::sum);
```

reduce 如果不加参数`identity`则返回的是optional类型的，reduce在进行双目运算时，其中一个场景是与`identity`做比较操作，因此我们应该满足`identity op x = x`

示例代码：分组后每组返回最大的一条数据

```java
List<StandardTaskController.TaskResponse> taskResponses = queryResult.stream().map(task -> buildTaskResponse(task, queryParam.getBusinessType()))
                .collect(Collectors.toList());
StandardTaskController.TaskResponse zeroTime = StandardTaskController.TaskResponse
            .builder().createTime(new Date(0)).build();
taskResponses=taskResponses.stream()
.collect(
        Collectors.collectingAndThen(
                Collectors.groupingBy(StandardTaskController.TaskResponse::getProInsId,
                        Collectors.reducing(zeroTime,
                                BinaryOperator.maxBy(Comparator.comparing(StandardTaskController.TaskResponse::getCreateTime)))),
                                                        r -> new ArrayList<>(r.values())));
```