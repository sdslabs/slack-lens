=begin

--parameters
user
timestamp
message
channel

=end

require_relative 'client.rb'

# Helps to index a message
class Index
	# initialize Index class and set class var as message
	def initialize(msg)
		@message = msg
	end

	# index a message object
	def index()
		# ES client
		client = Client.new

		client.index index: 'testing',
					 type: 'message',
					 id: 1,
					 body: {
					 	user: 'nemo',
					 	message: 'we won CCTC \m/',
					 	channel: 'general',
					 	timestamp: 123212324
					 }
	end
end