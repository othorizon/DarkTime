#!/bin/bash
# set -ex
root=$1
cd $root

ignore="! -iname "README.MD" ! -iname "SUMMARY.MD" ! -iname "_SUMMARY.MD" ! -path "./.git" ! -path "./node_modules""

getspcae(){
    tab=""
    local count=`expr $1 - 2`
    if [ "${count}" -le 0 ]; then return;fi

    for a in `seq $count`
    do
     tab=${tab}"  "
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
    title=$1
    depth=$2
    title=${title:2}
    title=${title#*/}
    tab=`getspcae $depth`
    
    echo "${tab}* ${title}"
}
dive(){
    local dirs=$1
    if [ -z "$dirs" ]; then return;fi


    local depth=$2
    local node=$3
    local nextdepth=`expr $2 + 1`
    #写节点
    if [ -n "$node" ];then
      result="${result}${node}\n"
    fi

    for dir in ${dirs[@]}
    do
        if [ -d "${dir}" ];then
          nextnode=`getnode "${dir}" ${nextdepth}`
        dive "`find ${dir} -maxdepth 1  -mindepth 1 \( -iname "*.md" -or -type d \)`" $nextdepth "${nextnode}"
        else
          writeline "${dir}" ${nextdepth}
        fi
    done
}

result=""

dive "`find . -maxdepth 1  -mindepth 1 \( -iname "*.md" -or -type d \) ${ignore}`" 1

#写文件
echo generate SUMMARY.md
echo -e "${result}" > ${root}/SUMMARY.md
echo finish
