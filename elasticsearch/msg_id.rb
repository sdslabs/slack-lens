#
# MsgID class assigns/returns 'id' for a message according to 'channel'
# uses total message count for that channel
# then each message is tracked by #{channel + msgid}
#

module Slacklens
  module SlackElastic

    class MsgID
      def initialize(channel)
	@channel = channel
	@config = Slacklens::Configer
	@slackdata = Slacklens::Slackdata
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
          @slackdata.add_channel([@channel])
          RestClient.put "#{@config.value('url')}/#{@channel}", "", {:'User-Agent' => 'Slack-lens'}
          return 1
        end
      end
    end

  end
end
