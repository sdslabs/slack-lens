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

      # search a particular channel
      def self.channel(keyword, channel)
	response = Client.search index: channel, body: {query: {fuzzy: {message: keyword}}}

	# callback to generate HTML for this response
	layout((JSON.parse(response.to_json))['hits']['hits'], channel)
      end

      # returns HTML layout for search response
      def self.layout(data, channel)
	html = ""
	start = "<div class='channel-search-result-element'>"
	user = "<span class='message-user'>"
	time = "<span class='message-time'>"

	data.each do |message|
	  html += (start + user)
	  html += "<a href='/user/#{message['_source']['username']}'>"
	  html += (message['_source']['username'] + "</a></span>")
	  html += (time + "<a href='/message/#{channel + (message['_source']['msgid']).to_s}'>")
	  html += (message['_source']['timestamp'] + "</a></span>")
	  html += "<div>" + message['_source']['message'] + "</div></div>"
	end

	html
      end

    end

  end
end
