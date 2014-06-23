module Slacklens
  module Routes
    class Messages < Base
      get '/message/:msg_id' do
	params[:msg_id]
      end
    end
  end
end
