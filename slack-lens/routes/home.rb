module Slacklens
  module Routes

    class Home < Base
      get '/home' do
	haml :home
      end
    end

  end
end
