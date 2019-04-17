#!/bin/bash
set -ex

# check error
# errorChar=`find . -name "*.md" |xargs grep  ""`
# if [ -n "$errorChar" ]; then
# echo "存在异常字符："
# echo "$errorChar"
# exit 1
# fi

find . -name "*.md" |xargs grep -l ""|xargs sed -i "" "s///g"
