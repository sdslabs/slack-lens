require 'rubygems'

require 'bundler'
Bundler.require

# adds path '../' to $LOAD_PATH ($:)
# so things can be used with 'require' only
$: << File.expand_path('../', __FILE__)

require 'slack-lens/models'
require 'slack-lens/helpers'
require 'slack-lens/routes'

module Slacklens
  class App < Sinatra::Base

    # sinatra configurations
    #
    # :app_file will auto set to this file(__FILE__)
    enable :sessions
    set :session_secret, 'a public secret will be an ENV var in production'

    get '/sdslabs' do
      'YAY!!'
    end

    # start server, if this ruby file executed directly
    run! if app_file == $0

  end
end
