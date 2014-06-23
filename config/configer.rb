#
# Configer module Helps to supply config variables across the project
#

require 'yaml'

module Slacklens
  module Configer

    extend self

    # array object of yaml config file data
    Config_file = YAML.load_file(File.join(File.dirname(__FILE__), 'config.yaml'))

    # return value of a key in yaml file data
    def value(key)
      Config_file[key] != nil ? Config_file[key] : nil
    end
  end
end
