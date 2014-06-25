module Slacklens
  module Routes

    class IndexMessage < Base

      indexer = Slacklens::SlackElastic::Index

      # index an incoming message object
      post '/index' do
	indexer.index(request.body.read)
      end
    end

  end
end
