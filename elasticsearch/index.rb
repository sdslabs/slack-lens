# this script helps to index a message

require 'json'

require_relative 'client.rb'
require_relative 'msg_id.rb'
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

		# different index for each channel
		client.index 	index: @message['channel'],
		type: 'Message',
		body: {
			user: @message['user'],
			message: @message['message'],
			channel: @message['channel'],
			timestamp: @message['timestamp'],
			msgid: MsgID.new(@message['channel']).assign()
		}
	end
end

Index.new(JSON.parse('{"user":"ravi", "message":"go kill yourself", "channel":"general", "timestamp": 564621}')).index()