#!/bin/bash
java -javaagent:./target/newrelic/newrelic.jar   -Dnewrelic.config.file=$PWD/newrelic.yml  -Xshare:off --illegal-access=warn -jar ./target/helloworld-1.0-SNAPSHOT.jar