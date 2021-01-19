#!/bin/bash
yum install wget git nano -y
yum install java-11-openjdk-devel -y
wget https://www-eu.apache.org/dist/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz
tar xvf apache-maven-3.6.3-bin.tar.gz  -C /usr/lib/

cat << EOF >> /etc/profile
JAVA_HOME=/usr/lib/jvm/java
export JAVA_HOME

M2_HOME="/usr/lib/apache-maven-3.6.3"
export M2_HOME

M2="\$M2_HOME/bin"
MAVEN_OPTS="-Xms256m -Xmx512m"
export M2 MAVEN_OPTS

PATH=\$M2:\$PATH:/usr/lib/jvm/java/bin
export PATH
EOF

. /etc/profile
source /etc/profile