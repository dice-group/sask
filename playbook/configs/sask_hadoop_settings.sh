#!/bin/bash

cd $HADOOP_PREFIX
alias hadoop='$HADOOP_PREFIX/bin/hadoop'
alias hdfs='$HADOOP_PREFIX/bin/hdfs'

#Enable webhdfs, disable permissions
sed -i "6i <property>\n<name>dfs.permissions.enabled</name>\n<value>false</value>\n</property><property><name>dfs.safemode.threshold.pct</name><value>0</value></property><property><name>dfs.webhdfs.enabled</name><value>true</value></property>" /usr/local/hadoop-2.7.0/etc/hadoop/hdfs-site.xml
/usr/local/hadoop/sbin/./stop-dfs.sh;
/usr/local/hadoop/sbin/./start-dfs.sh;

#Create directories in Hadoop where the sask files will be stored
$HADOOP_PREFIX/bin/hadoop fs -mkdir /user/DICE
$HADOOP_PREFIX/bin/hadoop fs -mkdir /user/DICE/repo
$HADOOP_PREFIX/bin/hadoop fs -mkdir /user/DICE/workflow

echo "SASK directories created"