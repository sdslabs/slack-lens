# slack-lens

Slack-lens is leiningen project written in [clojure](https://clojure.org).Slack-lens is for listening, storaging and retrieving the message so that the messages could still be found after 10,000 messages limit on slack.  Slack-lens use the [slack-rtm](https://api.slack.com/rtm) API to listen to every event that is triggered on the slack. Slack-lens listens to these and filters the events with `type = message`, then these are stored in elastic-search database. 

A web interface is also implemented for viewing the messages .     

## Quick Setup
**(Not preferred as VM will eat up your resources)**

    sudo apt-get install vagrant
    vagrant up

## Alternate Manually setup 
**(Preferred method)**
### Installing Leiningen
Run [this](https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein) script to self install the leiningen package.

### Install the elasticsearch
Run following commands to install elasticsearch locally

    wget https://download.elastic.co/elasticsearch/elasticsearch/elasticsearch-5.6.0.deb
    sudo dpkg -i elasticsearch-5.6.0.deb
    
Or use the elasticsearch docker image instead (to manage multiple versions of elasticsearch)

    docker pull docker.elastic.co/elasticsearch/elasticsearch:5.6.0
    docker run -d -p 9200:9200 --name elasticsearch -e "http.host=0.0.0.0" -e "transport.host=127.0.0.1" -e "xpack.security.enabled=false" -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:5.6.0
    
## Running

For listening to the slack-rtm API 

    lein run -m in.co.sdslabs.slack-lens.listener.run -t "Your Token" 

To start a web server for slack-lens on localhost, run:

    # Port is optional
    lein ring server-headless [port]

By default, the server uses port 40000.  To query the service, run:

    curl -X GET http://localhost:40000

## Testing

For starting the local web-scoket server, and transmitting the messages for testing purposes

    lein run -m in.co.sdslabs.slack-lens.test.message

For  running slack-lens against the local web-socket server for testing

    lein run -m in.co.sdslabs.slack-lens.listener.run -u 'ws://localhost:8080'

## API Documentation

Swagger spec and documentation is available at [http//localhost:40000](http//localhost:40000) that describes the endpoints.

## License
