module Slacklens
  module Routes

    class Base < Sinatra::Base
      configure do
	set :views, 'slack-lens/views'
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
