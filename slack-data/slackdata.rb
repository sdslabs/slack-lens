#
# this script helps to add data in 'slack-data' Directory
#
# it adds new 'user' or 'channel' objects to respective files
# it acts when seeing any change in data provided by 'slack'
#

require 'yaml'

class SlackData

  def initialize
    # user data file's path
    @user_file = './users.yaml'

    # channel data file's path
    @channel_file = './channel.yaml'

    # yaml data object in users file
    @user_data = YAML.load_file(@user_file)

    # yaml data object in channels file
    @channel_data = YAML.load_file(@channel_file)
  end

  # argument => user : hash object
  def add_user(arg)
    user = {"user"=>{"id" => arg[:id], "name" => arg[:name]}}
    user_data = @user_data.push(user)
    File.open(@user_file, 'w'){ |f| f.write user_data.to_yaml }
  end

  # argument => channel : hash object
  def add_channel(arg)
    channel = {"channel" => arg[:name]}
    channel_data = @channel_data.push(channel)
    File.open(@channel_file, 'w'){ |f| f.write channel_data.to_yaml }
  end

end
