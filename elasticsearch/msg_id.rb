#
# this script assigns/returns 'id' for a message according to 'channel'
# uses total message count for that channel
# then each message is tracked by #{channel + msgid}
#

require 'rest-client'
require 'json'
require_relative '../config/configer.rb'
require_relative '../slack-data/slackdata.rb'


class MsgID
  # set channel argument as instance var
  def initialize(channel)
    @channel = channel
    @config = Configer.new()
    @slackdata = SlackData.new() 
  end

  # assigns id, using total message count
  def assign
    # if index already exists for channel
    begin
      response = RestClient.get "#{@config.value('url')}/#{@channel}/Message/_count"
      total = JSON.parse(response)['count']
      id = total+1
      return id

    # index doesn't exists for channel
    rescue
      @slackdata.add_channel({:name => @channel})
      RestClient.put "#{@config.value('url')}/#{@channel}", :UserAgent => "slack-lens"
      return 1
    end
  end
end
