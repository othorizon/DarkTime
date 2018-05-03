# buildsummary

自动生成gitbook的SUMMARY.md文件，会按照目录结构来生成逐级的列表。
比如你的文件结构是这样的

```bash
$ tree .
.
├── topic1
│   ├── artic1.md
│   └── topic1-1
│       └── artic1-1.md
└── topic2
    └── artic2.md
```

那么生成`SUMMARY.md`文件内容是

```markdown
* topic2
  * [artic2](./topic2/artic2.md)
* topic1
  * [artic1](./topic1/artic1.md)
  * topic1-1
    * [article1-1](./topic1/topic1-1/artic1-1.md)
```