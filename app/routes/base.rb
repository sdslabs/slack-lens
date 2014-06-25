module Slacklens
  module Routes

    class Base < Sinatra::Base
      configure do
	set :views, 'app/views'
	set :root, File.expand_path('../../', __FILE__)
      end

      helpers do
	def loggedin
	  if session[:user] and session[:uid]
	    return true
	  else
	    return false
	  end
	end
      end
    end

  end
end
