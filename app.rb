require 'rubygems'

require 'bundler'
Bundler.require

# adds path '../' to $LOAD_PATH ($:)
# so things can be used with 'require' only
$: << File.expand_path('../', __FILE__)

require 'slack-lens/models'
require 'slack-lens/helpers'
require 'slack-lens/routes'

require 'config/configer'

require 'slack-data/slackdata'

module Slacklens
  class App < Sinatra::Base

    # sinatra configurations
    # :app_file will auto set to this file(__FILE__)
    enable :sessions
    set :session_secret, 'a public secret that will be ENV var in production'

    get '/sdslabs' do
      'yay'
    end

    # start server, if this ruby file executed directly
    run! if app_file == $0

    use Slacklens::Routes::Base
    use Slacklens::Routes::Login
    use Slacklens::Routes::Home
    use Slacklens::Routes::Channels
    use Slacklens::Routes::Users
    use Slacklens::Routes::Messages

  end
end
