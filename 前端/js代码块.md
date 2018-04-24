# 一些有用的js代码

## 读取远程文件内容并渲染markdown格式

```javascript
<!--markdown渲染工具-->
<script src="//cdn.bootcss.com/markdown.js/0.5.0/markdown.min.js"></script>

<div id="appAwesome">
    appAwesome 加载中(https://raw.githubusercontent.com/othorizon/MacAwesome/master/app.md)
</div>

<script type="text/javascript">
    let xmlHttpRequest;
    function createXmlHttpRequest() {
        if (window.ActiveXObject) { //如果是IE浏览器      
            return new ActiveXObject("Microsoft.XMLHTTP");
        } else if (window.XMLHttpRequest) { //非IE浏览器      
            return new XMLHttpRequest();
        }
    }
    function loadData(url,callback) {
        xmlHttpRequest = createXmlHttpRequest();
        xmlHttpRequest.onreadystatechange = callback;
        xmlHttpRequest.open("GET", url, true);
        xmlHttpRequest.send(null);
    }

    function loadComplete(elementId) {
        if (xmlHttpRequest.readyState == 4) {// 4 = "loaded"
            if (xmlHttpRequest.status == 200) {// 200 = OK
                document.getElementById(elementId).innerHTML = markdown.toHTML(xmlHttpRequest.responseText);
            }
            else {
                document.getElementById(elementId).innerHTML += "<BR> #加载失败";
            }
        }
    }

    let appAwesomeUrl="https://raw.githubusercontent.com/othorizon/MacAwesome/master/app.md";
    // load app awesome
    loadData(appAwesomeUrl,function(){
        loadComplete("appAwesome")
    });
</script>

```