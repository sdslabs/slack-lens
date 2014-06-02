#
# this method index message object with help of 'elasticsearch/index.rb'
#

require_relative '../../elasticsearch/index.rb'

class Indexer
  def initialize(data)
    @index = Index.new(data)
  end

  def index
    @index.index()
  end
end
