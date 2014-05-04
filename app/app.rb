require 'sinatra'
require 'haml'

# Home Page
get '/' do
	haml :index
end

# Channel Archieve
get '/channel/:channel' do
	"channel data of #{params[:channel]}"
end

# User Archieve
get '/user/:user' do
	"user data for #{params[:user]}"
end

# Route for single message
get '/message/:msg_id' do
	"message with link"
end

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
