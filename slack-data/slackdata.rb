#
# this script helps to add data in 'slack-data' Directory
#
# it adds new 'user' or 'channel' objects to respective files
# it manipulates when seeing any change in data provided by 'slack'
#

require 'yaml'

class SlackData

  def initialize
    # user data file's path
    @user_file = '../slack-data/users.yaml'

    # channel data file's path
    @channel_file = '../slack-data/channels.yaml'

    # yaml data object in users file
    @user_data = YAML.load_file(@user_file)

    # yaml data object in channels file
    @channel_data = YAML.load_file(@channel_file)
  end

  # checks for a username availability in Slackdata
  def have_username(name)
    @user_data.each do |user|
      if user['user']['name'] == name
	return true
      end
    end

    return false
  end

  # checks for a userid availability in Slackdata
  def have_userid(uid)
    @user_data.each do |user|
      if user['user']['id'] == uid
	return true
      end
    end

    return false
  end

  # remove user object
  def remove_user(index)
    @user_data.delete_at(index)
    File.open(@user_file, 'w'){ |f| f.write @user_data.to_yaml }
  end

  # change username (of userid: uid) to name
  def change_username(uid, name)
    i, j  = 0, nil

    @user_data.each do |user|
      if user['user']['id'] == uid
	# remove this object
	j = i
      end
      i = i + 1
    end
    remove_user(j)

    # add again with updated username
    add_user([uid, name])
  end

  # argument => user : array object
  def add_user(arg)
    user = {"user" => {"id" => arg[0], "name" => arg[1]}}
    user_data = @user_data.push(user)
    File.open(@user_file, 'w'){ |f| f.write user_data.to_yaml }
  end

  # argument => channel : array object
  def add_channel(arg)
    channel = {"channel" => arg[0]}
    channel_data = @channel_data.push(channel)
    File.open(@channel_file, 'w'){ |f| f.write channel_data.to_yaml }
  end

  # returns array of all channels
  def channels
    result = []
    @channel_data.each do |channel|
      result.push(channel['channel'])
    end
    result
  end

  # returns array of all users
  def users
    result = []
    @user_data.each do |user|
      result.push(user['user']['name'])
    end
    result
  end

end
