require 'elasticsearch'

class ES
	def initialize
		# Connect to localhost:9200 by default:
		@es = Elasticsearch::Client.new log: true
	end
	
	# ElasticSearch instance
	def client
		return @es
	end
end

