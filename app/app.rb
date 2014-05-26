require 'sinatra'
require 'haml'
require 'json'
require 'rest-client'
require_relative '../config/configer.rb'


def data(channel)
  begin
    response = RestClient.get "#{Configer.new().value('url')}/#{channel}/Message/_search"
    messages = JSON.parse(response)['hits']['hits']
    return messages[1]['_source']['user']
  rescue
    return 'None'
  end
end

# Home Page
# RIGHT NOW : USING DIRECTLY THIS (without authentication route)
get '/' do
	haml :index
end

# Channel Archieve
get '/channel/:channel' do
  haml :channel,
  :locals => {
    :title => params[:channel],
    :data => data(params[:channel])
  }
end

# User Archieve
get '/user/:user' do
	"user data for #{params[:user]}"
end

# Route for single message
get '/message/:msg_id' do
	"message with link"
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