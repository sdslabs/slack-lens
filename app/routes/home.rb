module Slacklens
  module Routes

    class Home < Base
      get '/home' do
	haml :home,
	:locals => {
	  :view => 'Home'
	}
      end
    end

  end
end
