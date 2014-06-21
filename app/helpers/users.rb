#
# this method collects users list and team name
# and help this data to render on 'views' view
#

require_relative '../../config/configer.rb'
require_relative '../../slack-data/slackdata.rb'

class Users
  def initialize
    @config = Configer.new()
    @slackdata = SlackData.new()
  end

  def team()
    @config.value('team')
  end

  def list()
    @slackdata.users()
  end
end


