#
# Permission class ask user to grant permissions with a Slack Team
#


module Slacklens
  module Helpers

    class Permission
      def self.address
        config = Slacklens::Configer

        # query variables, according to Slack API
        client_id = config.value('client_id')
        scope = config.value('scope')
        state = config.value('state')
        team = config.value('slack_team_id')

        oauth_endpoint = "https://slack.com/oauth/authorize"

        # url to redirect, for asking authentication permission
        "#{oauth_endpoint}?client_id=#{client_id}&scope=#{scope}&state=#{state}&team=#{team}"
      end
    end

  end
end
