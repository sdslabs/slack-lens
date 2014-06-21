#
# this method manage users list in Slackdata
# add a new user instance, if not already there
# update username in Slackdata, if user change it
#

require_relative '../../slack-data/slackdata.rb'


class ManageUser
  def initialize(username, userid)
    @user = username
    @uid = userid
    @slackdata = SlackData.new()
  end

  def manage
    if @slackdata.have_userid(@uid)
      # change username
      if !(@slackdata.have_username(@user))
	@slackdata.change_username(@uid, @user)
      end
    else
      # add user to slackdata
      @slackdata.add_user([@uid, @user])
    end
  end
end
