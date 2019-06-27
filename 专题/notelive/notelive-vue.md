# notelive vue版本开发

## 技术点

实时写入到本地存储，然后在阶段性的同步到服务器
[vue中的watch方法 实时同步存储数据 - front-gl - 博客园](https://www.cnblogs.com/mmzuo-798/p/10259774.html)  
[客户端存储 — Vue.js](https://cn.vuejs.org/v2/cookbook/client-side-storage.html)  

localstorage的更加特性化功能实现，增加了keys等操作；
[yarkovaleksei/vue2-storage](https://github.com/yarkovaleksei/vue2-storage)  

[vuejs-templates](https://github.com/vuejs-templates)
[Simple Todo App with Vue - CodeSandbox](https://codesandbox.io/s/o29j95wx9)  
[vue-axios](https://www.npmjs.com/package/vue-axios)  

## 开发中的技巧

[让webstorm 识别vue cli3项目中的@路径别名正确解析的配置方法](https://blog.csdn.net/weixin_43343144/article/details/88668787)

获取localStorage中的key：`Object.keys(localStorage)`version

## vue知识沉淀

“:” 是指令 “v-bind”的缩写，“@”是指令“v-on”的缩写；“.”是修饰符。详细如下
https://blog.csdn.net/yangfengjueqi/article/details/86536347

emit 触发一个事件，可以通过v-on监听
https://www.cnblogs.com/sweeneys/p/10201458.html

## 高亮编辑器

选择使用 vs的Monaco Editor

[vue 代码编辑器monaco-editor使用](http://luchenqun.com/vue-monaco-editor/)  
[Package - monaco-editor-vue](http://npm.taobao.org/package/monaco-editor-vue)  

[Vue cli2.0 项目中使用Monaco Editor编辑器 - 时间脱臼 - 博客园](https://www.cnblogs.com/helloluckworld/p/9663308.html)

```javaScript
ed=monaco.editor.create(document.getElementById("container"), {
	value: "function hello() {\n\talert('Hello world!');\n}",
	language: "javascript"
});

ed.getModel().onDidChangeContent((e)=>{
    console.log(ed.getValue())
});
```


```javaScript
// Register a new language
monaco.languages.register({ id: 'mySpecialLanguage' });

// Register a tokens provider for the language
monaco.languages.setMonarchTokensProvider('mySpecialLanguage',{
    //自定义配置
});


monaco.editor.create(document.getElementById("container"), {
	value: '## 123123qwq',
	language: 'mySpecialLanguage'
});

```