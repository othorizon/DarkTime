#!/bin/bash
#set -x

getspcae(){
    tab=""
    for ((i=2;i<$1;i++))
    do 
     tab=${tab}" "
    done
    echo "${tab}"
}

writeline(){
    fpath=$1
    depth=$2
    title=`head -1 $fpath`
    title=${title:1}
    #将开头的#号去掉
    title=${title/#\#/}
    #去掉开头空格
    title=${title/# /}

    tab=`getspcae $depth`
    
    result="${result}${tab}* [${title}](${fpath})\n"
}
getnode(){
    fpath=$1
    depth=$2
    title=${fpath%/*}
    title=${title#*/}
    tab=`getspcae $depth`
    
    echo "${tab}* ${title}\n"
}
dive(){
    local dirs=$1
    if [ -z "$dirs" ]; then return;fi
    
    #写节点
    local existnode=0
    local depth=$2
    local nextdepth=`expr $2 + 1`

    for dir in ${dirs[@]}
    do
        local node=`getnode "${dir}" ${depth}`
        if [ -d "${dir}" ];then
          dive "`find ${dir} \( -iname "*.md" -or -type d \) -d 1`" $nextdepth
        else
          if [ $existnode == 0 ];then
             echo "${node}"
             result="${result}${node}\n"
             existnode=1
          fi
          writeline "${dir}" ${nextdepth}
        fi
    done
}

result=""
#\( -iname "*.md" -or -type d \)
dive "`find . \( -iname "*.md" -or -type d \) ! -iname "README.MD" ! -iname "SUMMARY.MD" ! -iname "_SUMMARY.MD" ! -path "./.git" -d 1`" 1

#写文件
echo make SUMMARY.md
echo "${result}" > SUMMARY.md
echo finish