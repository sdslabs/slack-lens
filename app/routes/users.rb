module Slacklens
  module Routes
    class Users < Base

      commondata = Slacklens::Helpers::Commondata

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
	  :team => commondata.team(),
	  :data => commondata.users()
	}
      end
    end
  end
end
