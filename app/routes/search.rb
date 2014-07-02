module Slacklens
  module Routes
    class Search < Base

      search = Slacklens::SlackElastic::Search

      # search a particular channel
      get '/search/:channel' do
	search.channel(params[:keyword], params[:channel])
	#"#{params[:keyword]} in #{params[:channel]}"
      end

    end
  end
end
