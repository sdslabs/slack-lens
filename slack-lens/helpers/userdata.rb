#
# Userdata class collects userdata from oauth token
#


module Slacklens
  module Helpers

    class Userdata

      # collects username and userid using Slack's identity test
      def self.data(token)
        begin
          response = RestClient.get "https://slack.com/api/auth.test?token=#{token}"
          body = JSON.parse(response.body)
          return [body['user'], body['user_id']]
        resque
          return [nil, nil]
        end
      end
    end

  end
end
