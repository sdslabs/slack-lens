#!/bin/bash

# Install Java
sudo add-apt-repository ppa:webupd8team/java
sudo apt-get -y -q update
echo oracle-java7-installer shared/accepted-oracle-license-v1-1 select true | sudo /usr/bin/debconf-set-selections
sudo apt-get -y -q install oracle-java7-installer
sudo apt-get install -y --force-yes oracle-java8-*
sudo update-alternatives --set java /usr/lib/jvm/java-8-oracle/jre/bin/java

# Install elasticsearch
wget https://download.elasticsearch.org/elasticsearch/elasticsearch/elasticsearch-1.6.0.deb
sudo dpkg -i elasticsearch-1.6.0.deb
sudo service elasticsearch start # Run it
