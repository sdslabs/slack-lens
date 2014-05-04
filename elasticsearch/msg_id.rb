# this script assigns/returns 'id' for a message according to 'channel'
# it uses a char as a prefix to total message count for that channel

require 'rest-client'
require 'json'
require_relative '../config/configer.rb'

# hope nobody uses that much channels(>110) to break this
$chars = ["a", "b", "c", "d", "e", "f", "g", "h", "i", "j"]
$charlen = $chars.length


class MsgID
	# class var : list of all channels
	@@channels = []

	# set channel argument as instance var
	def initialize(channel)
		@channel = channel
		@config = Configer.new()
	end

	# returns prefix for a channel
	def Prefix
		# channel not there, in channel's list
		if @@channels.index(@channel) == nil
			# add channel now
			@@channels.push(@channel)
		end

		if @@channels.length <= $charlen
			prefix = $chars[@@channels.index(@channel)]
			return prefix
		else
			prefix = $chars[(@@channels.length / $charlen)-1]
			prefix = prefix + $chars[(@@channels.length % $charlen)-1]
			return prefix
		end
	end

	# assigns id, using prefix and total message count
	def assign
		response = RestClient.get "http://localhost:9200/#{@config.value('index')}/#{@config.value('type')}/_count?q=channel:" + @channel
		puts response
		total = JSON.parse(response)['count']
		puts Prefix()
		id = Prefix() + "#{total+1}"
		puts id
	end
end

MsgID.new('general').assign()