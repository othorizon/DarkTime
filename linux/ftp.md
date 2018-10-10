# ftp

```bash
#!/bin/sh
set -e

#从本地向FTP批量上传文档
#upload dir to remote ftp server 
#read -p "Input local dir: " updir     #local dir 
#read -p "Input remote dir: " todir    #remote dir 
#read -p "Input remote IP: " ip        #remote IP 
#read -p "Input ftp username: " user    #ftp username 
#read -p "Input password: " password    #password 
touch ./dist/`date +%Y-%m-%d_%H:%M:%S`
updir=./dist
todir=./wwwroot
ip=ftpserver.com

sss=`find $updir -type d -printf $todir/'%P/n'| awk '{if ($0 == "")next;print "mkdir " $0}'`  
aaa=`find $updir -type f -printf 'put %p %P /n'`  
ftp -nv $ip <<EOF  
user webuser
type binary  
prompt  
$sss  
cd $todir  
$aaa  
quit  
EOF 
echo "commit to ftp successfully"
```