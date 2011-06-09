# Copyright 2008-2011 Red Hat, Inc, and individual contributors.
# 
# This is free software; you can redistribute it and/or modify it
# under the terms of the GNU Lesser General Public License as
# published by the Free Software Foundation; either version 2.1 of
# the License, or (at your option) any later version.
# 
# This software is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
# Lesser General Public License for more details.
# 
# You should have received a copy of the GNU Lesser General Public
# License along with this software; if not, write to the Free
# Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
# 02110-1301 USA, or see the FSF site: http://www.fsf.org.

require 'torquebox/messaging/destination'
require 'torquebox/messaging/connection_factory'
require 'torquebox/service_registry'

module TorqueBox
  module Messaging
    class Topic < Destination

      def self.start( name, options={} )
        jndi = options.fetch( :jndi, [].to_java(:string) )
        TorqueBox::ServiceRegistry.lookup("jboss.messaging.jms.manager") do |server|
          server.createTopic( false, name, jndi )
        end
        new( name )
      end

      def stop
        TorqueBox::ServiceRegistry.lookup("jboss.messaging.jms.manager") do |server|
          server.destroyTopic( name )
        end
      end

      def to_s
        "[Topic: #{super}]"
      end
    end
  end
end
