#
# ES class return ElasticSearch Client's instance
#

module Slacklens
  module SlackElastic

    class ES
      def self.client
	es = Elasticsearch::Client.new log: true
	return es
      end
    end

  end
end
