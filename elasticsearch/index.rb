# this script helps to index a message

require 'json'

require_relative 'client.rb'
require_relative '../config/configer.rb'

class Index
	# set class var :message as argument
 	def initialize(msg)
		@message = msg
	end

	# index a message object
	def index()
		# ES client
		es = ES.new
		client = es.client()

		client.index 	index: 'testing',
			     				type: 'message',
			     				id: 3,
			     				body: {
				   					user: @message['user'],
				 						message: @message['message'],
				 						channel: @message['channel'],
				 						timestamp: @message['timestamp']
			     				}
	end
end

# take message as a JSON object and index it
Index.new(JSON.parse('{"user":"sm", "message":"solani", "channel":"general", "timestamp": "3454534"}')).index()
