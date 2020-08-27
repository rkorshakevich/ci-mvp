#!/usr/bin/bash

while [ true ] ;
do
  curl -XGET --silent http://master:8080
  if [ $? -eq 0 ]
  then
    break
  else
    echo "Waiting for Jenkins master to come up..."
    sleep 2
  fi
done

while ! [ -f /tmp/jenkins_shared/apiToken ] ;
do
      echo "Waiting for token file..."
      sleep 2
done

java -jar ~/swarm-client-3.22.jar -master http://master:8080 -username admin -passwordFile /tmp/jenkins_shared/apiToken -name agent -labels buildenv-curl -executors 3
