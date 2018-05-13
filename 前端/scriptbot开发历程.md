# scriptbot的前端开发经验总结

## jquery

### 片段

选取包含`sidebar-nav-link`这个class的`a`标签,添加一个click监听事件，事件的目的是动态修改class

```javascript
$(".sidebar-nav-link a").click(function () {
    $('.active').removeClass('active');
    $(this).addClass('active')
});
```

---

监听按键操作，并且当包含`am-modal`这个class的元素不可见时，使`quickFilter`这个文本框获得焦点，这样按键便会成为文本框的输入操作
按键码是27即按下esc键时，将`quickFilter`文本框内容清空，
`quickFilter.blur();quickFilter.trigger('change');`的作用是让`quickFilter`上的`ng-modal`起作用，不然的话数据是不会回写的，参考[Angular中Jquery修改input的值之后如何更新Model](https://blog.csdn.net/meloseven/article/details/70170130)

```javascript
quickFilter = $("[quickFilter]");

$(document).keypress(function (e) {
    if ($(".am-modal:visible").length === 0) {
        quickFilter.focus();
    }
});
$(document).keyup(function (e) {
    if ($(".am-modal:visible").length === 0 && e.keyCode === 27) {
        quickFilter.val("");
        quickFilter.blur();
        quickFilter.trigger('change');
    }
});
```

## angularjs