#
# this method index message object with help of 'elasticsearch/index.rb'
#

require_relative '../../elasticsearch/index.rb'
require_relative '../../config/configer.rb'

class Indexer
  def initialize(data)
    @index = Index.new(data)
    @config = Configer.new()
  end

  def index
    # index data, only if it's coming from a valid source
    if @config.value('state') == data[:token]
      @index.index()
    end
  end
end
