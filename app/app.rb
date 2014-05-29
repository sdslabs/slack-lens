require 'sinatra'
require 'haml'
require 'json'
require 'rest-client'
require_relative '../config/configer.rb'

# Home Page
# RIGHT NOW : USING DIRECTLY THIS (without authentication route)
get '/' do
  haml :index,
  :locals => {
    :value => '',
    :view => 'Index'
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
