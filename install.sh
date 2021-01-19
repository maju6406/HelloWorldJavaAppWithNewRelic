#!/bin/bash
yum install wget git nano -y
wget https://github.com/AdoptOpenJDK/openjdk15-binaries/releases/download/jdk-15.0.1%2B9/OpenJDK15U-jdk_x64_linux_hotspot_15.0.1_9.tar.gz
mkdir /opt/jdk
tar xvf OpenJDK15U-jdk_x64_linux_hotspot_15.0.1_9.tar.gz  -C /opt/jdk/
wget https://www-eu.apache.org/dist/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz
tar xvf apache-maven-3.6.3-bin.tar.gz  -C /usr/lib/

cat << EOF >> /etc/profile
JAVA_HOME=/opt/jdk/
export JAVA_HOME

M2_HOME="/usr/lib/apache-maven-3.6.3"
export M2_HOME

M2="\$M2_HOME/bin"
MAVEN_OPTS="-Xms256m -Xmx512m"
export M2 MAVEN_OPTS

PATH=\$M2:\$PATH:/opt/jdk/bin
export PATH
EOF

. /etc/profile
source /etc/profile