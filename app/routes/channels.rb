module Slacklens
  module Routes
    class Channels < Base

      commondata = Slacklens::Helpers::Commondata
      search = Slacklens::SlackElastic::Search

      get '/channel/:channel' do
	haml :channel,
	:locals => {
	  :view => 'Channel',
	  :value => params[:channel],
	  :team => commondata.team(),
	  :channels => commondata.channels()
	}
      end

      get '/channels' do
	haml :channels,
	:locals => {
	  :view => 'Channels',
	  :team => commondata.team(),
	  :data => commondata.channels()
	}
      end
    end
  end
end
