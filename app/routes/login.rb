module Slacklens
  module Routes

    # ask for user permission
    class Login < Base

      permission = Slacklens::Helpers::Permission
      authenticator = Slacklens::Helpers::Authenticator
      userdata = Slacklens::Helpers::Userdata
      manage_user = Slacklens::Helpers::ManageUser

      get '/login' do
	# generates the oauth access token
	if params[:code] and params[:state] and !loggedin()
	  address = authenticator.address(params[:code], params[:state])

	  begin
	    token = authenticator.token(address)
	    udata = userdata.data(token)

	    # manage users list in Slackdata
	    manage_user.manage(udata)

	    # update session variables
	    session[:user] = udata[0]
	    session[:uid] = udata[1]

	    redirect to('/home')

	  rescue
	    'something went wrong in login process'
	  end

	else
	  if loggedin()
	    redirect to('/home')

	  # ask user to grant permissions
	  else
	    redirect to(permission.address())
	  end
	end
      end
    end

  end
end
