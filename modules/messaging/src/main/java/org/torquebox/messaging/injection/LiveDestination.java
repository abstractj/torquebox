/*
 * Copyright 2008-2011 Red Hat, Inc, and individual contributors.
 * 
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 * 
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.torquebox.messaging.injection;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;

import org.jruby.Ruby;
import org.jruby.RubyModule;
import org.jruby.javasupport.JavaEmbedUtils;
import org.torquebox.core.injection.ConvertableRubyInjection;

public class LiveDestination implements ConvertableRubyInjection {

    public LiveDestination(ConnectionFactory connectionFactory, Destination destination) {
        this.connectionFactory = connectionFactory;
        this.destination = destination;
    }
    
    public ConnectionFactory getConnectionFactory() {
        return this.connectionFactory;
    }
    
    public Destination getDestination() {
        return this.destination;
    }
    
    @Override
    public Object convert(Ruby ruby) throws Exception {
        ruby.evalScriptlet( "require %q(torquebox-messaging)" );
        RubyModule destinationClass = ruby.getClassFromPath( "TorqueBox::Messaging::" + getType() );
        Object destination = JavaEmbedUtils.invokeMethod( ruby, destinationClass, "new", new Object[] { this.destination, this.connectionFactory }, Object.class );
        return destination;
    }
    
    protected String getType() {
        if ( this.destination instanceof javax.jms.Queue ) {
            return "Queue";
        } else {
            return "Topic";
        }
    }
    
    private ConnectionFactory connectionFactory;
    private Destination destination;

}
