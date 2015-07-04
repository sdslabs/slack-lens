#!/bin/bash

# Install Java
sudo apt-get install -y --force-yes oracle-java8-* mysql-client haproxy
sudo update-alternatives --set java /usr/lib/jvm/java-8-oracle/jre/bin/java

# Install elasticsearch
wget https://download.elasticsearch.org/elasticsearch/elasticsearch/elasticsearch-1.6.0.deb
sudo dpkg -i elasticsearch-1.6.0.deb
sudo service elasticsearch start # Run it
