# this script helps in search

require_relative 'client.rb'

class Search
	# initialize Search class and set class var as request
	def initialize()
		#@request = req
	end

	def search()
		# ElasticSearch instance
		es = ES.new
		client = es.client()

		return client.search index: 'testing', body: { query: { match: { message: 'CCTC' } } }
	end
end

s = Search.new
puts s.search()
