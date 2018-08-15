#!/bin/sh

export PATH=/usr/java/jdk1.7.0_80/bin/:$PATH

nohup java -jar test-1.0-SNAPSHOT-jar-with-dependencies.jar >/dev/null 2>&1 &
