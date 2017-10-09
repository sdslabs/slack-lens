# slack-lens

Slack-lens is leiningen project written in [clojure](https://clojure.org).Slack-lens is for listening, storaging and retrieving the message so that the messages could still be found after 10,000 messages limit on slack.  Slack-lens use the [slack-rtm](https://api.slack.com/rtm) API to listen to every event that is triggered on the slack. Slack-lens listens to these and filters the events with `type = message`, then these are stored in elastic-search database. 

A web interface is also implemented for viewing the messages .     

## SetUp

    sudo apt-get install vagrant
    vagrant up

## Alternate Manually setup
### Installing Leiningen
Run [this](https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein) script to self install the leiningen package.

### Install the elasticsearch 
    wget https://download.elastic.co/elasticsearch/elasticsearch/elasticsearch-1.7.2.deb
    sudo dpkg -i elasticsearch-1.7.2.deb



## Running

For listening to the slack-rtm API 

    lein run -m in.co.sdslabs.slack-lens.listener.run -t "Your Token" 

To start a web server for slack-lens on localhost, run:

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
