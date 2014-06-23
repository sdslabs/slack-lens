#
# CommonData class collects channels/users list and team name
# and help this data to render on 'channels'/'users' view
#


module Slacklens
  module Helpers

    class CommonData
      def self.team
	Slacklens::Configer.value('team')
      end

      def self.channels
	Slacklens::Slackdata.channels()
      end

      def self.users
	Slacklens::Slackdata.users()
      end
    end

  end
end
