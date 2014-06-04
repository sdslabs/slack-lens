require 'sinatra'
require 'haml'
require 'json'
require 'rest-client'

require_relative '../config/configer.rb'
require_relative 'helpers/permission.rb'
require_relative 'helpers/authenticator.rb'
require_relative 'helpers/indexer.rb'


helpers do
  def loggedin
    if session[:oauth_access_token]
      return true
    else
      return false
    end
  end
end

enable :sessions

# ask for user permission
get '/login' do
  # generates the oauth access token
  if params[:code] and params[:state] and !loggedin()
    authenticator = Authenticator.new()
    address = authenticator.address(params[:code], params[:state])
    
    if authenticator.token(address)
      session[:oauth_access_token] = authenticator.token(address)
      redirect to('/home')
    else
      'things wrong'
    end
    
  else
    if loggedin()
      redirect to('/home')

    # ask user to grant permissions
    else
      permission = Permission.new()
      redirect to(permission.address())
    end
  end
end

# view that asks for login, if not logged in
get '/' do
  if !loggedin()
    haml :login,
    :locals => {
      :value => 'Slack-lens',
      :view => 'Login'
    }
  else
    redirect to('/home')
  end
end

# home page for application
get '/home' do
  haml :home,
  :locals => {
    :value => 'Slack-lens',
    :view => 'Home'
  }
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

# index a message object coming from Slack
post '/index' do
  #indexer = Indexer.new()
  #indexer.index(params)
  [200, 'OK']
end

get '/index' do
  haml :home
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
  haml :error,
  :locals => {
    :value => 'Slack-lens',
    :view => 'Error'
  }
end
