#!/bin/bash
set -x
dir=$1
mes=$2
echo start deploy,dir:$dir,message:$mes
# summary -b -t;mv _summary.md SUMMARY.md
# summary -d

sh buildsummary.sh "${dir}"
git status
git acm "$mes"
git pull
git push
git status