#!/bin/bash
CONFIG_FILE=$(dirname "$(readlink -f "$0")")"/../resources/config.json"
if [ -f $CONFIG_FILE ]
then
   hostname=$(cat $CONFIG_FILE | ruby -r json -e 'puts JSON.parse(STDIN.read)["host"]')
    portnum=$(cat $CONFIG_FILE | ruby -r json -e 'puts JSON.parse(STDIN.read)["port"]')
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

    container_name="elasticsearch_slacklens"
    if [ "$(docker ps -aq -f name=$container_name)" ]
    then
      if [ "$(docker ps -aq -f status=exited -f name=$container_name)" ]
      then
        echo "Resuming stopped container"
        docker start $container_name
        echo "Elasticsearch running at address: "$hostname":"$portnum
      else
        echo "Container already exists."
        echo "Elasticsearch running at address: "$hostname":"$portnum
      fi
    else
      echo "Pulling docker image"
      docker pull docker.elastic.co/elasticsearch/elasticsearch:5.6.0
      echo "Docker image ready"
      echo "Starting new container"
      docker run -d -p "127.0.0.1:"$portnum":9200" --name "elasticsearch_slacklens" -e "http.host=0.0.0.0" -e "transport.host="$hostname -e "xpack.security.enabled=false" -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:5.6.0
      echo "Elasticsearch running at address: "$hostname":"$portnum
    fi
else
   echo "resources/config.json does not exist."
   exit
fi
