#
# SlackElastic::Index class index message objects
#

module Slacklens
  module SlackElastic

    class Index

      def self.index(json_str)
	
	message = JSON.parse(json_str)
	config = Slacklens::Configer

	es = Slacklens::SlackElastic::ES
	client = es.client()
	msgid = Slacklens::SlackElastic::MsgID

	# index message, if they are coming from valid source
	if message['token'] == config.value('state')
	  
	  # different index for each channel in ElasticSearch
	  client.index index: message['channel'],
          type: 'Message',
          body: {
            username: message['username'],
            message: message['message'],
            channel: message['channel'],
            timestamp: message['timestamp'],
            msgid: msgid.new(message['channel']).assign()
          }

	  [201, 'Message successfully indexed']

	else
	  [400, 'Authentication Error : Invalid token']
	end
      end
    end

  end
end
