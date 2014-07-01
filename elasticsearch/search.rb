#
# Search class help to provide response for search requests
# Results differ according to some parameters in requests
#

module Slacklens
  module SlackElastic

    class Search

      Client = (SlackElastic::ES).client()

      # return recent (<=)50 message objects for a channel
      # Slack-lens initially renders recent 50 messages in a channel
      def self.recent(channel)
	response = Client.search index: channel, body: {sort: {msgid: {order: 'desc'}}, size: 50}
	result = (JSON.parse(response.to_json))

	result['hits']['hits']
      end


    end

  end
end
