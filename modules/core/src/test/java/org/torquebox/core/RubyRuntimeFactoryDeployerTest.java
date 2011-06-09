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

package org.torquebox.core;

import static org.junit.Assert.*;

import org.jboss.as.server.deployment.Attachments;
import org.jboss.modules.Module;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.value.Value;
import org.jruby.CompatVersion;
import org.jruby.RubyInstanceConfig.CompileMode;
import org.junit.Before;
import org.junit.Test;
import org.torquebox.core.app.RubyApplicationMetaData;
import org.torquebox.core.as.CoreServices;
import org.torquebox.core.runtime.RubyRuntimeFactory;
import org.torquebox.core.runtime.RubyRuntimeFactoryDeployer;
import org.torquebox.core.runtime.RubyRuntimeMetaData;
import org.torquebox.test.as.AbstractDeploymentProcessorTestCase;
import org.torquebox.test.as.MockDeploymentPhaseContext;
import org.torquebox.test.as.MockDeploymentUnit;
import org.torquebox.test.as.MockServiceBuilder;

public class RubyRuntimeFactoryDeployerTest extends AbstractDeploymentProcessorTestCase {
    

    @Before
    public void setUpDeployer() throws Throwable {
        appendDeployer( new RubyRuntimeFactoryDeployer() );
    }

    @Test
    public void testDeployment() throws Exception {
        
        MockDeploymentPhaseContext phaseContext = createPhaseContext();
        MockDeploymentUnit unit = phaseContext.getMockDeploymentUnit();

        RubyRuntimeMetaData metaData = new RubyRuntimeMetaData();
        unit.putAttachment( RubyRuntimeMetaData.ATTACHMENT_KEY, metaData );
        
        RubyApplicationMetaData rubyAppMetaData = new RubyApplicationMetaData( "foo" );
        unit.putAttachment( RubyApplicationMetaData.ATTACHMENT_KEY, rubyAppMetaData );
        
        unit.putAttachment( Attachments.MODULE, Module.getSystemModule() );

        deploy( phaseContext );

        ServiceName factoryServiceName = CoreServices.runtimeFactoryName( unit );
        MockServiceBuilder<?> factoryBuilder = phaseContext.getMockServiceTarget().getMockServiceBuilder( factoryServiceName );
        Value<?> factoryValue = factoryBuilder.getValue();
        RubyRuntimeFactory factory = (RubyRuntimeFactory) factoryValue.getValue();

        assertNotNull( factory );
        assertEquals( CompatVersion.RUBY1_8, factory.getRubyVersion() );
    }

    @Test
    public void testDeploymentWithNonDefaultRubyCompatibilityVersion() throws Exception {
        
        MockDeploymentPhaseContext phaseContext = createPhaseContext();
        MockDeploymentUnit unit = phaseContext.getMockDeploymentUnit();
        
        RubyRuntimeMetaData metaData = new RubyRuntimeMetaData();
        metaData.setVersion( RubyRuntimeMetaData.Version.V1_9 );
        unit.putAttachment( RubyRuntimeMetaData.ATTACHMENT_KEY, metaData );
        
        RubyApplicationMetaData rubyAppMetaData = new RubyApplicationMetaData( "foo");
        unit.putAttachment( RubyApplicationMetaData.ATTACHMENT_KEY, rubyAppMetaData );
        
        unit.putAttachment( Attachments.MODULE, Module.getSystemModule() );

        deploy( phaseContext );

        ServiceName factoryServiceName = CoreServices.runtimeFactoryName( unit );
        MockServiceBuilder<?> factoryBuilder = phaseContext.getMockServiceTarget().getMockServiceBuilder( factoryServiceName );
        Value<?> factoryValue = factoryBuilder.getValue();
        RubyRuntimeFactory factory = (RubyRuntimeFactory) factoryValue.getValue();

        assertNotNull( factory );
        assertEquals( CompatVersion.RUBY1_9, factory.getRubyVersion() );
    }

    @Test
    public void testDeploymentWithJITCompileMode() throws Exception {
        MockDeploymentPhaseContext phaseContext = createPhaseContext();
        MockDeploymentUnit unit = phaseContext.getMockDeploymentUnit();
        
        RubyRuntimeMetaData metaData = new RubyRuntimeMetaData();
        metaData.setCompileMode( RubyRuntimeMetaData.CompileMode.JIT );
        unit.putAttachment( RubyRuntimeMetaData.ATTACHMENT_KEY, metaData );


        RubyApplicationMetaData rubyAppMetaData = new RubyApplicationMetaData( "foo");
        unit.putAttachment( RubyApplicationMetaData.ATTACHMENT_KEY, rubyAppMetaData );
        
        unit.putAttachment( Attachments.MODULE, Module.getSystemModule() );

        deploy( phaseContext );

        ServiceName factoryServiceName = CoreServices.runtimeFactoryName( unit );
        MockServiceBuilder<?> factoryBuilder = phaseContext.getMockServiceTarget().getMockServiceBuilder( factoryServiceName );
        Value<?> factoryValue = factoryBuilder.getValue();
        RubyRuntimeFactory factory = (RubyRuntimeFactory) factoryValue.getValue();

        assertNotNull( factory );
        assertEquals( CompileMode.JIT, factory.getCompileMode() );

    }

    @Test
    public void testDeploymentWithOFFCompileMode() throws Exception {
        MockDeploymentPhaseContext phaseContext = createPhaseContext();
        MockDeploymentUnit unit = phaseContext.getMockDeploymentUnit();
        
        RubyRuntimeMetaData metaData = new RubyRuntimeMetaData();
        metaData.setCompileMode( RubyRuntimeMetaData.CompileMode.OFF );
        unit.putAttachment( RubyRuntimeMetaData.ATTACHMENT_KEY, metaData );


        RubyApplicationMetaData rubyAppMetaData = new RubyApplicationMetaData( "foo");
        unit.putAttachment( RubyApplicationMetaData.ATTACHMENT_KEY, rubyAppMetaData );
        
        unit.putAttachment( Attachments.MODULE, Module.getSystemModule() );

        deploy( phaseContext );

        ServiceName factoryServiceName = CoreServices.runtimeFactoryName( unit );
        MockServiceBuilder<?> factoryBuilder = phaseContext.getMockServiceTarget().getMockServiceBuilder( factoryServiceName );
        Value<?> factoryValue = factoryBuilder.getValue();
        RubyRuntimeFactory factory = (RubyRuntimeFactory) factoryValue.getValue();

        assertNotNull( factory );
        assertEquals( CompileMode.OFF, factory.getCompileMode() );
    }

    @Test
    public void testDeploymentWithFORCECompileMode() throws Exception {
        MockDeploymentPhaseContext phaseContext = createPhaseContext();
        MockDeploymentUnit unit = phaseContext.getMockDeploymentUnit();
        
        RubyRuntimeMetaData metaData = new RubyRuntimeMetaData();
        metaData.setCompileMode( RubyRuntimeMetaData.CompileMode.FORCE );
        unit.putAttachment( RubyRuntimeMetaData.ATTACHMENT_KEY, metaData );


        RubyApplicationMetaData rubyAppMetaData = new RubyApplicationMetaData( "foo");
        unit.putAttachment( RubyApplicationMetaData.ATTACHMENT_KEY, rubyAppMetaData );
        
        unit.putAttachment( Attachments.MODULE, Module.getSystemModule() );

        deploy( phaseContext );

        ServiceName factoryServiceName = CoreServices.runtimeFactoryName( unit );
        MockServiceBuilder<?> factoryBuilder = phaseContext.getMockServiceTarget().getMockServiceBuilder( factoryServiceName );
        Value<?> factoryValue = factoryBuilder.getValue();
        RubyRuntimeFactory factory = (RubyRuntimeFactory) factoryValue.getValue();

        assertNotNull( factory );
        assertEquals( CompileMode.FORCE, factory.getCompileMode() );
    }



}
