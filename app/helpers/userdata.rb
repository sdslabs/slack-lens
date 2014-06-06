#
# this method collects userdata from oauth token
#

class Userdata
  def initialize(token)
    @token = token
  end

  def data
    begin
      response = RestClient.get "https://slack.com/api/auth.test?token=#{@token}"
      body = JSON.parse(response.body)
      return [body['user'], body['user_id']]
    resque
      return [nil, nil]
    end
  end
end
