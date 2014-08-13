#
# Slackdata module helps to manage data in 'slack-data' Directory
#
# it adds new 'user' or 'channel' objects to respective files
# it manipulates on seeing any change in data provided by 'slack'
#

require 'yaml'

module Slacklens
  module Slackdata

    extend self

    # user data file's path
    User_file = File.join(File.dirname(__FILE__), 'users.yaml')

    # channel data file's path
    Channel_file = File.join(File.dirname(__FILE__), 'channels.yaml')

    # yaml data object in users file
    User_data = YAML.load_file(User_file)

    # yaml data object in channels file
    Channel_data = YAML.load_file(Channel_file)


    # checks for a username availability in Slackdata
    def have_username(name)
      return false if User_data['users'] == nil

      User_data['users'].each do |user|
        if user['name'] == name
	  return true
        end
      end

      return false
    end

    # checks for a userid availability in slackdata
    def have_userid(uid)
      return false if User_data['users'] == nil

      User_data['users'].each do |user|
	if user['id'] == uid
	  return true
	end
      end

      return false
    end

    # remove user object
    def remove_user(index)
      User_data['users'].delete_at(index)
      File.open(User_file, 'w'){ |f| f.write User_data.to_yaml }
    end

    # change username (of userid: uid) to name
    def change_username(uid, name)
      i, j  = 0, nil

      User_data['users'].each do |user|
        if user['id'] == uid
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
      user = {"id" => arg[0], "name" => arg[1]}

      temp = (User_data['users'] == nil ? [] : User_data['users'])
      user_data = {"users" => temp.push(user)}

      File.open(User_file, 'w'){ |f| f.write user_data.to_yaml }
    end

    # argument => channel : array object
    def add_channel(arg)
      channel = arg[0]

      temp = (Channel_data['channels'] == nil ? [] : Channel_data['channels'])
      channel_data = {"channels" => temp.push(channel)}

      File.open(Channel_file, 'w'){ |f| f.write channel_data.to_yaml }
    end

    # returns array of all channels
    def channels
      result = []
      Channel_data['channels'].each do |channel|
        result.push(channel)
      end
      result
    end

    # returns array of all users
    def users
      result = []
      User_data['users'].each do |user|
        result.push(user['name'])
      end
      result
    end
  end
end
