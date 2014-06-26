module Slacklens
  module Routes
    class Channels < Base
      get '/channel/:channel' do
	haml :channel,
	:locals => {
	  :view => 'Channel',
	  :value => params[:channel]
	}
      end

      get '/channels' do
	haml :channels,
	:locals => {
	  :view => 'Channels',
	  :team => (Slacklens::Helpers::Commondata).team(),
	  :data => (Slacklens::Helpers::Commondata).channels()
	}
      end
    end
  end
end
