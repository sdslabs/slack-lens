module Slacklens
  module Routes
    class Channels < Base
      get '/channel/:channel' do
	params[:channel]
      end

      get '/channels' do
	'channels'
      end
    end
  end
end
