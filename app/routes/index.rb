module Slacklens
  module Routes

    class Index < Base
      # view that asks for login, if not logged in
      get '/' do
	if !(loggedin())
	  haml :login
	else
	  redirect to('/home')
	end
      end
    end

  end
end
