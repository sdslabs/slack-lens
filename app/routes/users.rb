module Slacklens
  module Routes
    class Users < Base
      get '/user/:user' do
	haml :user,
	:locals => {
	  :view => 'User',
	  :value => params[:user]
	}
      end

      get '/users' do
	haml :users,
	:locals => {
	  :view => 'Users',
	  :team => (Slacklens::Helpers::Commondata).team(),
	  :data => (Slacklens::Helpers::Commondata).users()
	}
      end
    end
  end
end
