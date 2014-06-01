require 'sinatra'
require 'haml'
require 'json'
require 'rest-client'

require_relative '../config/configer.rb'
require_relative 'helpers/permission.rb'
require_relative 'helpers/authenticator.rb'

enable :sessions
# ask for user permission
get '/' do
  # generates the oauth access token
  if params[:code] and params[:state] and !session[:oauth_access_token]
    authenticator = Authenticator.new()
    address = authenticator.address(params[:code], params[:state])
    
    if authenticator.token(address)
      session[:oauth_access_token] = authenticator.token(address)
      redirect to('/')
    else
      'things wrong'
    end
    
  else
    if session[:oauth_access_token]
      # things to do when user is logged in
      # I think instead of redirecting to home, render things that you have for '/home'
      redirect to('/channel/muzi')

    # ask user to grant permissions
    else
      permission = Permission.new()
      redirect to(permission.address())
    end
  end
end

# Channel Archieve
get '/channel/:channel' do
  haml :channel,
  :locals => {
    :value => params[:channel],
    :view => 'Channel'
  }
end

# User Archieve
get '/user/:user' do
  haml :user,
  :locals => {
    :value => params[:user],
    :view => 'User'
  }
end

# Route for single message
get '/message/:msg_id' do
  haml :message,
  :locals => {
    :value => params[:msg_id],
    :view => 'Message'
  }
end

=begin
will consider these routes later


# Total Stats for slack
get '/stats' do
	"total stats"
end

# Stats for a channel
get '/stats/channel/:channel' do
	"stats for channel #{params[:channel]}"
end

# Stats for a user
get '/stats/user/:user' do
	"stats for user #{params[:user]}"
end

=end

# 404
not_found do
  'nothing here, baby!!'
end
