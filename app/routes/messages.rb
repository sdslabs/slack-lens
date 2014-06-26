module Slacklens
  module Routes
    class Messages < Base
      get '/message/:msg_id' do
	haml :messages,
	:locals => {
	  :view => 'Messages'
	}
      end
    end
  end
end
