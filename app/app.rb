require 'sinatra'
require 'haml'
require 'json'
require 'rest-client'
require 'rack'

require_relative '../config/configer.rb'
require_relative '../elasticsearch/index.rb'

require_relative 'helpers/permission.rb'
require_relative 'helpers/authenticator.rb'
require_relative 'helpers/userdata.rb'
require_relative 'helpers/manage_user.rb'
require_relative 'helpers/channels.rb'
require_relative 'helpers/users.rb'


helpers do
  def loggedin
    if session[:user]
      return true
    else
      return false
    end
  end
end

enable :sessions
set :session_secret, 'secret'

# ask for user permission
get '/login' do
  # generates the oauth access token
  if params[:code] and params[:state] and !loggedin()
    authenticator = Authenticator.new()
    address = authenticator.address(params[:code], params[:state])
    
    if authenticator.token(address)
      token = authenticator.token(address)
      ud = Userdata.new(token)

      # manage users list in Slackdata
      mu = ManageUser.new(ud.data()[0], ud.data()[1])
      mu.manage()

      session[:user] = ud.data()[0]
      session[:uid] = ud.data()[1]

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
# leaving login for now : so that I can work without a tunnel
=begin
  if !loggedin()
    haml :login,
    :locals => {
      :value => 'Slack-lens',
      :view => 'Login'
    }
  else
    redirect to('/home')
  end
=end
  redirect to('/home')
end

# home page for application
get '/home' do
  haml :home,
  :locals => {
    :value => 'Slack-lens',
    :view => 'Home'
  }
end

# Channel list
get '/channels' do
  channels = Channels.new()
  haml :channels,
  :locals => {
    :value => 'Slack-lens',
    :view => 'Channels',
    :team => channels.team(),
    :channels => channels.list()
  }
end

# Users list
get '/users' do
  users = Users.new()
  haml :users,
  :locals => {
    :value => 'Slack-lens',
    :view => 'Users',
    :team => users.team(),
    :users => users.list()
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
=begin
post '/hubot/slack-webhook' do
  #indexer = Indexer.new()
  #indexer.index(params)
  [200,'OK']
end
=end

get '/index' do
  haml :home
end

post '/index' do
  indexer = Index.new(request.body.read)
  indexer.index()
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
