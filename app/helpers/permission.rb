#
# this method ask user to grant permissions with a Slack Team
#

require_relative '../../config/configer.rb'

class Permission
  def initialize
    @config = Configer.new()
  end
  
  # return to custom url(for an application) which ask for user permissions
  def address
    # query variables, according to Slack API
    client_id = @config.value('client_id')
    scope = @config.value('scope')
    state = @config.value('state')
    team = @config.value('slack_team_id')

    oauth_endpoint = "https://slack.com/oauth/authorize"

    # url to redirect, for asking authentication permission
    url = "#{oauth_endpoint}?client_id=#{client_id}&scope=#{scope}&state=#{state}&team=#{team}"
  end
end
