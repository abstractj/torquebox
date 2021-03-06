require 'torquebox'
require 'somethingstrange'

module TheModule
  class TheClass
  
    include Enumerable
    include TorqueBox::Injectors
    include ::SomethingElse
    
    def initialize()
      @random = inject('jboss.web:service=WebServer')
    end
    
    def do_something()
      @something = inject('java:/comp/whatever' )
    end
    
    def another_method() 
      @another = inject( com.mycorp.mypackage.MyThing )
    end
    
  end
end
