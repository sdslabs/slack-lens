# Description:
#   Allows hubot to listen to chat in public channels
#   hubot silently send all chat-data in **Space** to power Slack-lens
#
# Configuration:
#   # url where your slacklens instance is serving 
#   HUBOT_SLACKLENS_URL
#
#   # token to make sure your hubot is sending data, not aliens
#   HUBOT_SLACKLENS_TOKEN
#
#   # your timezone, for example in India : "Asia/Calcutta"
#   # otherwise hubot will use UTC : default by Heroku
#   TZ
#
# Dependencies:
#   None
#
# Author:
#   pravj

module.exports = (robot) ->
  robot.hear /.*/, (msg) ->
      
    http = require 'http'

    if process.env.HUBOT_SLACKLENS_URL? and process.env.HUBOT_SLACKLENS_TOKEN?

      slacklens_url = process.env.HUBOT_SLACKLENS_URL.match(/https?:\/\/(.*)/)[1]
      slacklens_token = process.env.HUBOT_SLACKLENS_TOKEN

      # chat message from your slack channels
      Message = msg.message.text

      # slack channel from where the message is coming
      Channel = msg.message.user.room

      # user who send that chat message
      User = msg.message.user.name

      # time thing for the message
      Timestamp = new Date()
      
      # chatdata json object
      chatdata =
        username: User
        message: Message
        channel: Channel
        timestamp: Timestamp
        token: slacklens_token

      chatdata = JSON.stringify(chatdata)

      headers =
        "Content-Type": "application/json"
        "Content-Length": chatdata.length

      options =
        host: slacklens_url
        path: "/index"
        method: "POST"
        headers: headers

      req = http.request(options)
      req.write chatdata
      req.end()
