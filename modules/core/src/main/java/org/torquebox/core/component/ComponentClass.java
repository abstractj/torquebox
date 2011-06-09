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

package org.torquebox.core.component;

import org.jruby.Ruby;
import org.jruby.RubyModule;
import org.jruby.javasupport.JavaEmbedUtils;
import org.jruby.runtime.builtin.IRubyObject;

public class ComponentClass implements ComponentInstantiator {

    private String className;
    private String requirePath;

    public ComponentClass() {

    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassName() {
        return this.className;
    }

    public void setRequirePath(String requirePath) {
        this.requirePath = requirePath;
    }

    public String getRequirePath() {
        return this.requirePath;
    }

    public RubyModule getComponentClass(Ruby runtime) {
        if (this.requirePath != null) {
            runtime.getLoadService().load( this.requirePath + ".rb", false );
        }

        RubyModule componentClass = (RubyModule) runtime.getClassFromPath( this.className );

        if (componentClass == null || componentClass.isNil()) {
            return null;
        }

        return componentClass;
    }

    public IRubyObject newInstance(Ruby runtime, Object[] initParams) {
        ClassLoader originalCl = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader( runtime.getJRubyClassLoader().getParent() );
            RubyModule rubyClass = getComponentClass( runtime );
            IRubyObject component = (IRubyObject) JavaEmbedUtils.invokeMethod( runtime, rubyClass, "new", initParams, IRubyObject.class );
            return component;
        } finally {
            Thread.currentThread().setContextClassLoader( originalCl );

        }
    }
    
    public String toString() {
        return this.className;
    }

}
