#!/bin/bash
set -ex
mes=$1
echo start deploy
summary -b -t;mv _summary.md SUMMARY.md
summary -d
git pull
git status
git acm $mes
git push
git status