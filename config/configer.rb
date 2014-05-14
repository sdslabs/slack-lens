# Configer, Helps to supply config variables across the project

require 'yaml'

# config file
$config_file = YAML.load_file('../config/config.yaml')

class Configer
	# yaml config file object
	def initialize
		@config = $config_file
	end

	# returns value for keyword
	def value(key)
		if @config[key] == nil
			error(key)
		else
			return @config[key]
		end
	end

	# when key is not there
	def error(key)
		return nil
	end
end
