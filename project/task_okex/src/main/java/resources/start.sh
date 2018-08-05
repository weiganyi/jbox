#!/bin/sh

nohup java -cp task_okex-1.0-SNAPSHOT-jar-with-dependencies.jar com.jbox.project.task_okex.TaskOkexKLine start >>task_okex.log 2>&1 &
