#
# Authenticator class authenticate user for a Slack Team
#

module Slacklens
  module Helpers

    class Authenticator
      # returns address that will provide oauth access token
      def self.address(code, state)

        config = Slacklens::Configer

        client_id = config.value('client_id')
        client_secret = config.value('client_secret')
        redirect_uri = 'https://pravj.ngrok.com/login'
  
        # Slack API method endpoint that exchange code with OAuth token
        exchange_endpoint = "https://slack.com/api/oauth.access"

        # url with custom variable that will generate the OAuth token
        "#{exchange_endpoint}?client_id=#{client_id}&client_secret=#{client_secret}&code=#{code}&redirect_uri=#{redirect_uri}"
      end

      # return oauth access token
      def self.token(url)
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

  end
end
