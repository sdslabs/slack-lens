#
# this method authenticate user for a Slack Team
#

require 'json'
require 'rest_client'
require_relative '../../config/configer.rb'

class Authenticator
  def initialize
    @config = Configer.new()
  end

  # generates the url address which returns the OAuth access token
  def address(code, state)
    client_id = @config.value('client_id')
    client_secret = @config.value('client_secret')
    redirect_uri = 'https://pravj.ngrok.com'
  
    # Slack API method endpoint that exchange code with OAuth token
    exchange_endpoint = "https://slack.com/api/oauth.access"

    # url with custom variable that will generate the OAuth token
    url = "#{exchange_endpoint}?client_id=#{client_id}&client_secret=#{client_secret}&code=#{code}&redirect_uri=#{redirect_uri}"
  end

  # returns the generated Oauth access token
  def token(url)
    begin
      response = RestClient.get "#{url}"
      token = JSON.parse(response)['access_token']
      return token
    resque
      token = nil
      return token
    end
  end
end
