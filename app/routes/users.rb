module Slacklens
  module Routes
    class Users < Base
      get '/user/:user' do
	params[:user]
      end

      get '/users' do
	'users'
      end
    end
  end
end
