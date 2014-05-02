require 'elasticsearch'

# ElaticSearch client instance
class Client
	def initialize
		# Connect to localhost:9200 by default:
		client = Elasticsearch::Client.new log: true
		return client
	end
end