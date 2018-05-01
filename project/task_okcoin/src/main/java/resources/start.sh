#!/bin/sh

export PATH=/usr/local/services/jdk1.7.0_45-1/jdk1.7.0_45/bin/:$PATH

java -classpath jexchange.jar com.strategy.Strategy1 >> Strategy1.log &

