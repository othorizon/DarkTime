#!/bin/bash
#set -x
echo start build summary

result="# CodeSnippets\n\n"


getspcae(){
    tab=""
    for ((i=2;i<$1;i++))
    do 
     tab=${tab}" "
    done
    echo "${tab}"
}
lastName
build(){
    path=$1
    #深度 统计字符出现次数
    depth=`echo $path | awk -F"/" '{print NF-1}'`
    #从第二个位置开始截取
    #path=${path:2}
    title=`head -1 $path`
    title=${title:1}
    #将开头的#号去掉
    title=${title/#\#/}
    #去掉开头空格
    title=${title/# /}

    
    
    tab=`getspcae $depth`
    
    result="${result}${tab}* [$title]($path)\n"
}

list=`find . -iname "*.md" ! -iname "README.MD" ! -iname "SUMMARY.MD" ! -iname "_SUMMARY.MD"`
for file in ${list[@]}
do
    build $file
done


#写文件
echo make SUMMARY.md
echo "${result}" > SUMMARY.md
echo finish