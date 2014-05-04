# this script helps to index a message

require 'json'

require_relative 'client.rb'
require_relative '../config/configer.rb'

class Index
	# set class var :message as argument
 	def initialize(msg)
		@message = msg
		@config = Configer.new()
	end

	# index a message object
	def index()
		# ES client
		es = ES.new
		client = es.client()

		client.index 	index: @config.value('index'),
			     				type: @config.value('type'),
			     				body: {
				   					user: @message['user'],
				 						message: @message['message'],
				 						channel: @message['channel'],
				 						timestamp: @message['timestamp']
			     				}
	end
end

Index.new(JSON.parse('{"user":"aps", "message":"whatever", "channel":"muzi", "timestamp": "564621"}')).index()