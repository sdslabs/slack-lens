#!/bin/bash
CONFIG_FILE=$(dirname "$(readlink -f "$0")")"/../resources/config.json"
if [ -f $CONFIG_FILE ]; then
   echo "File 'config.json' exists."
   hostname=$(cat $CONFIG_FILE | ruby -r json -e 'puts JSON.parse(STDIN.read)["host"]')
    echo "Read host as: "$hostname
    portnum=$(cat $CONFIG_FILE | ruby -r json -e 'puts JSON.parse(STDIN.read)["port"]')
    echo "Read port as: "$portnum
    if [ -z "$hostname" ]
    then
      echo "Host is empty in configuration file"
      exit
    fi
    if [ -z "$portnum" ]
    then
      echo "Port is empty in configuration file"
      exit
    fi
    echo "Pulling docker image"
    docker pull docker.elastic.co/elasticsearch/elasticsearch:5.6.0
    echo "Docker image ready"
    echo "Removing any previous containers (if present)"
    docker stop $(docker ps -aq)
    docker rm $(docker ps --no-trunc -aq)
    echo "Starting new container"
    docker run -d -p $portnum":9200" --name "elasticsearch" -e "http.host=0.0.0.0" -e "transport.host="$hostname -e "xpack.security.enabled=false" -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:5.6.0
    echo "Elasticsearch running at address: "$hostname":"$portnum
else
   echo "resources/config.json does not exist."
   exit
fi
