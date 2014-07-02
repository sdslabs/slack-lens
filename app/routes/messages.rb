module Slacklens
  module Routes
    class Messages < Base

      commondata = Slacklens::Helpers::Commondata
      search = Slacklens::SlackElastic::Search

      get '/message/:channel/:msgid' do
	haml :messages,
	:locals => {
	  :view => 'Messages',
	  :value => "#{params[:channel]}",
	  :team => commondata.team(),
          :channels => commondata.channels(),
	  :data => search.message(params[:msgid], params[:channel])
	}
      end

    end
  end
end
