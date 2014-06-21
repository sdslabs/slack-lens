#
# this method collects channels list and team name
# and help this data to render on 'channels' view
#

require_relative '../../config/configer.rb'
require_relative '../../slack-data/slackdata.rb'

class Channels
  def initialize
    @config = Configer.new()
    @slackdata = SlackData.new()
  end

  def team()
    @config.value('team')
  end

  def list()
    @slackdata.channels()
  end
end


