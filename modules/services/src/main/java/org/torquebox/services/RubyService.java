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

package org.torquebox.services;

import org.jruby.Ruby;
import org.torquebox.core.component.ComponentResolver;
import org.torquebox.core.runtime.RubyRuntimePool;
import org.torquebox.services.component.ServiceComponent;

public class RubyService implements RubyServiceMBean {

    public void create() throws Exception {
        this.runtime = this.runtimePool.borrowRuntime();
        this.servicesComponent = (ServiceComponent) this.resolver.resolve( runtime );
    }

    public void start() {
        this.servicesComponent.start();
        this.started = true;
    }

    public void stop() {
        if (this.servicesComponent != null) {
            this.servicesComponent.stop();
        }
        this.started = false;
    }

    public void destroy() {
        if (this.runtime != null) {
            this.runtimePool.returnRuntime( runtime );
        }
    }

    @Override
    public boolean isStarted() {
        return this.started;
    }

    @Override
    public boolean isStopped() {
        return !isStarted();
    }

    @Override
    public String getRubyClassName() {
        return resolver.getComponentInstantiator().toString();
    }

    @Override
    public String getStatus() throws Exception {
        if (isStarted()) {
            return "STARTED";
        }
        return "STOPPED";
    }

    public void setComponentResolver(ComponentResolver resolver) {
        this.resolver = resolver;
    }

    public void setRubyRuntimePool(RubyRuntimePool runtimePool) {
        this.runtimePool = runtimePool;
    }

    public ServiceComponent getComponent() {
        return this.servicesComponent;
    }

    private boolean started;

    private ComponentResolver resolver;
    private RubyRuntimePool runtimePool;
    private Ruby runtime;
    private ServiceComponent servicesComponent;

}
