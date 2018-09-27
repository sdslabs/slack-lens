# Slack-lens

Slack-lens is leiningen project written in [clojure](https://clojure.org).Slack-lens is for listening, storaging and retrieving the message so that the messages could still be found after 10,000 messages limit on slack.  Slack-lens use the [slack-rtm](https://api.slack.com/rtm) API to listen to every event that is triggered on the slack. Slack-lens listens to these and filters the events with `type = message`, then these are stored in elastic-search database. 

A web interface is also implemented for viewing the messages .     

## Prerequisites

#### Slack API setup

__Note:__ Only admin of slack workspace is authorised to get API token.

* Goto [Legacy Token.](https://api.slack.com/custom-integrations/legacy-tokens") Copy the 'token' that starts with `xoxp`.
* Goto [API setup](https://api.slack.com/apps "Slack API: Applications")
* Create app for required workspace.
* Get 'client id' and 'client secret'
* Click 'OAuth & Permissions' on sidebar
* Add new redirect url `http://localhost:40000/v1/slack/oauth`


## Quick Setup
**(Not preferred as VM will eat up your resources)**

    sudo apt-get install vagrant
    vagrant up

## Alternate Manual setup 
**(Preferred method)**
### Installing Leiningen
Run [this](https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein) script to self install the leiningen package.

### Configuration
 
    cp resources/config.sample.json resources/config.json
    
Enter slack api credentials in config file 

### Install the elasticsearch
Run following commands to install elasticsearch locally

**Not preferred if different versions required**

    wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-5.6.0.deb
    sudo dpkg -i elasticsearch-5.6.0.deb
    
Or use the elasticsearch docker image instead (to manage multiple versions of elasticsearch)

**Preferred:**

    ./scripts/docker-elasticsearch-setup.sh

## Running

For listening to the slack-rtm API 

    # To simply start listener based on saved configuration
    lein run
    # To specify entry point and token explicitly
    lein run -m in.co.sdslabs.slack-lens.listener.run -t [Your Token] 

To start a web server for slack-lens on localhost, run:

    # Port is optional
    lein ring server-headless [port]

By default, the server uses port 40000. To run web interface, run this address in browser

    http://localhost:40000

## Testing

For starting the local web-socket server, and transmitting the messages for testing purposes

    lein run -m in.co.sdslabs.slack-lens.test.message

For  running slack-lens against the local web-socket server for testing

    lein run -m in.co.sdslabs.slack-lens.listener.run -u 'ws://localhost:8080'

## API Documentation

Swagger spec and documentation is available at [http//localhost:40000](http//localhost:40000) that describes the endpoints.

## License
