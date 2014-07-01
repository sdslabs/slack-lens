#
# ES class return ElasticSearch Client's instance
#

module Slacklens
  module SlackElastic

    class ES
      def self.client
	Elasticsearch::Client.new
      end
    end

  end
end
