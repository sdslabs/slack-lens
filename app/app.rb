require 'sinatra'

# basic home page routing
get '/' do
	"slack-lens : SDSLabs"
end

# initial search routing
get '/search/:keyword/:query' do
	keyword = params[:keyword]
	query = params[:query]

	# right now basic for message only
	if keyword == 'message'
		"message"
	# if keyword does not match
	else
		"no keyword match"
	end
end
