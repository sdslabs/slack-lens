module Slacklens
  module Routes
    require 'slack-lens/routes/base'

    require 'slack-lens/routes/login'
    require 'slack-lens/routes/index'

    require 'slack-lens/routes/home'

    require 'slack-lens/routes/channels'
    require 'slack-lens/routes/users'

    require 'slack-lens/routes/messages'
    require 'slack-lens/routes/index_message'
  end
end
