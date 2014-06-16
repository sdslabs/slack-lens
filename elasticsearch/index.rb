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
    @json = (JSON.parse(json_str))
    @message = @json['chatdata']
    @config = Configer.new()
  end

  # index a message object
  def index()
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
  end
end
