#!/bin/bash
set -x
mes=$1
echo start deploy
# summary -b -t;mv _summary.md SUMMARY.md
# summary -d

sh buildsummary.sh
git status
git acm $mes
git pull
git push
git status