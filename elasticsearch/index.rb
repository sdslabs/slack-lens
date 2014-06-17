#
# this script index messages
#

require 'json'

require_relative 'client.rb'
require_relative 'msg_id.rb'
require_relative '../config/configer.rb'

class Index
  # set class var :message as argument
  def initialize(json_str)
    @message = JSON.parse(json_str)
    @config = Configer.new()
  end

  # index a message object
  def index()
    # index message, if they are coming from authenticated source
    if @message['token'] == @config.value('state')
      # ES client
      es = ES.new
      client = es.client()

      # different index for each channel
      client.index index: @message['channel'],
      type: 'Message',
      body: {
        username: @message['username'],
        message: @message['message'],
        channel: @message['channel'],
        timestamp: @message['timestamp'],
        msgid: MsgID.new(@message['channel']).assign()
      }

      [201, 'Message successfully indexed']
    else
      [400, 'Authentication Error : invalid token']
    end
  end
end
