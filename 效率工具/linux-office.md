# linux中的office
[TOC]

## libreoffice

[参考](http://blog.csdn.net/ljihe/article/details/77250206)  

- 安装
`yum install libreoffice`  
- 转换文档格式  
无论是word还是excel都可以转换为pdf、html等
word转pdf
`/usr/bin/libreoffice  --invisible --convert-to pdf  *.docx`
word转html
`/usr/bin/libreoffice  --invisible --convert-to html  *.docx`
批量转换

 ``` bash
mkdir doc_pdf
mkdir doc_html
mkdir xls_html
find . -name '*.doc*' -print0 |xargs -0 /usr/bin/libreoffice  --invisible --convert-to pdf --outdir 'doc_pdf'
find . -name '*.doc*' -print0 |xargs -0 /usr/bin/libreoffice  --invisible --convert-to html --outdir 'doc_html'
find . -name '*.xls*' -print0 |xargs -0 /usr/bin/libreoffice  --invisible --convert-to html --outdir 'xls_html'
```

 > 如果转换出来的中文乱码，则需要安装中文字体，常见的如宋体'simsun.ttf'
sudo cp simsun.ttc /usr/share/fonts 
cd /usr/share/fonts 
修改权限 
sudo chmod 644 simsun.ttc 
更新字体缓存： 
sudo fc-cache -fv


