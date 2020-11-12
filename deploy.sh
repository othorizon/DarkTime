#!/bin/bash
set -x
dir=$1
mes=$2
echo start deploy,dir:$dir,message:$mes
# summary -b -t;mv _summary.md SUMMARY.md
# summary -d

sh fix.sh
sh buildsummary.sh "${dir}"
cp SUMMARY.md README.md
git status
git add .
git commit -m "$mes"
git pull
git push
git status