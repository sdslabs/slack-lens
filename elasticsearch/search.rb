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
	result = JSON.parse(response.to_json)

	result['hits']['hits']
      end
      
      # return 50 messages near a particular message in a channel
      def self.message(msgid, channel)
	response = Client.search index: channel, body: {from: msgid.to_i - 1, size: 50, sort: {msgid: {order: 'asc'}}}
	result = JSON.parse(response.to_json)

	result['hits']['hits']
      end

      # search a particular channel
      def self.channel(keyword, channel)
	# use only fuzzy search for now
	response = Client.search index: channel, body: {query: {fuzzy: {message: keyword}}}

	# search result data
	result = (JSON.parse(response.to_json))['hits']['hits']

	# search result template file
	template = File.read(File.join(File.dirname(__FILE__), '../app/views/search_result.haml'))

	# variable to render in search result
        options = {:data => result, :channel => channel}

	# callback to generate and return HTML using Haml::Engine
	Haml::Engine.new(template).render(Object.new, :locals => options)
      end

    end

  end
end
