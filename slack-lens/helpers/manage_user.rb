#
# ManageUser class manage users list in Slackdata
# add a new user instance, if not already there
# update username in Slackdata, if user change it
#


module Slacklens
  module Helpers

    class ManageUser
      def self.manage(name, id)

	slackdata = Slacklens::Slackdata

        if slackdata.have_userid(id)
          # change username
          if !(slackdata.have_username(name))
	    slackdata.change_username(id, name)
          end
        else
          # add user to slackdata
          slackdata.add_user([id, name])
        end
      end
    end

  end
end
