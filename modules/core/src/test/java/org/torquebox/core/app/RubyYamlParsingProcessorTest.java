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

package org.torquebox.core.app;

import java.net.URL;

import org.jboss.as.server.deployment.DeploymentUnit;
import org.junit.Before;
import org.junit.Test;
import org.torquebox.core.TorqueBoxYamlParsingProcessor;
import org.torquebox.core.runtime.RubyRuntimeMetaData;
import org.torquebox.test.as.AbstractDeploymentProcessorTestCase;
import org.torquebox.test.as.MockDeploymentPhaseContext;
import org.torquebox.test.as.MockDeploymentUnit;

import static org.junit.Assert.*;

public class RubyYamlParsingProcessorTest extends AbstractDeploymentProcessorTestCase {
    
    @Before
    public void setUpDeployer() throws Throwable {
        appendDeployer( new TorqueBoxYamlParsingProcessor() );
        appendDeployer( new RubyYamlParsingProcessor() );
    }

    @Test
    public void testInvalidVersionMetaData() throws Exception {
        URL rubyYml = getClass().getResource( "ruby-invalid.yml" );

        MockDeploymentPhaseContext phaseContext = createPhaseContext( "torquebox.yml", rubyYml );
        MockDeploymentUnit unit = phaseContext.getMockDeploymentUnit();
        
        deploy( phaseContext );

        RubyRuntimeMetaData runtimeMetaData = unit.getAttachment( RubyRuntimeMetaData.ATTACHMENT_KEY );
        assertEquals( RubyRuntimeMetaData.Version.V1_8, runtimeMetaData.getVersion() );
        assertEquals( null, runtimeMetaData.getCompileMode() );
    }

    @Test
    public void testWithRuntimeMetaData18() throws Exception {
        URL rubyYml = getClass().getResource( "ruby-1.8.yml" );
        MockDeploymentPhaseContext phaseContext = createPhaseContext( "torquebox.yml", rubyYml );
        MockDeploymentUnit unit = phaseContext.getMockDeploymentUnit();
        
        RubyRuntimeMetaData runtimeMetaData = new RubyRuntimeMetaData();
        unit.putAttachment( RubyRuntimeMetaData.ATTACHMENT_KEY, runtimeMetaData );
        
        deploy( phaseContext );

        RubyRuntimeMetaData runtimeMetaData2 = unit.getAttachment( RubyRuntimeMetaData.ATTACHMENT_KEY );

        assertSame( runtimeMetaData, runtimeMetaData2 );
        assertEquals( RubyRuntimeMetaData.Version.V1_8, runtimeMetaData.getVersion() );
    }

    @Test
    public void testWithRuntimeMetaData19() throws Exception {
        URL rubyYml = getClass().getResource( "ruby-1.9.yml" );
        MockDeploymentPhaseContext phaseContext = createPhaseContext( "torquebox.yml", rubyYml );
        MockDeploymentUnit unit = phaseContext.getMockDeploymentUnit();
        
        RubyRuntimeMetaData runtimeMetaData = new RubyRuntimeMetaData();
        unit.putAttachment( RubyRuntimeMetaData.ATTACHMENT_KEY, runtimeMetaData );
        
        deploy( phaseContext );

        RubyRuntimeMetaData runtimeMetaData2 = unit.getAttachment( RubyRuntimeMetaData.ATTACHMENT_KEY );

        assertSame( runtimeMetaData, runtimeMetaData2 );
        assertEquals( RubyRuntimeMetaData.Version.V1_9, runtimeMetaData.getVersion() );
    }


    @Test
    public void testWithRuntimeMetaDataCompileModeForce() throws Exception {
        URL rubyYml = getClass().getResource( "ruby-compile-mode-force.yml" );
        MockDeploymentPhaseContext phaseContext = createPhaseContext( "torquebox.yml", rubyYml );
        MockDeploymentUnit unit = phaseContext.getMockDeploymentUnit();
        
        RubyRuntimeMetaData runtimeMetaData = new RubyRuntimeMetaData();
        unit.putAttachment( RubyRuntimeMetaData.ATTACHMENT_KEY, runtimeMetaData );
        
        deploy( phaseContext );

        RubyRuntimeMetaData runtimeMetaData2 = unit.getAttachment( RubyRuntimeMetaData.ATTACHMENT_KEY );

        assertSame( runtimeMetaData, runtimeMetaData2 );
        assertEquals( RubyRuntimeMetaData.CompileMode.FORCE, runtimeMetaData.getCompileMode() );
    }

    @Test
    public void testWithRuntimeMetaDataCompileModeJit() throws Exception {
        URL rubyYml = getClass().getResource( "ruby-compile-mode-jit.yml" );
        MockDeploymentPhaseContext phaseContext = createPhaseContext( "torquebox.yml", rubyYml );
        MockDeploymentUnit unit = phaseContext.getMockDeploymentUnit();
        
        RubyRuntimeMetaData runtimeMetaData = new RubyRuntimeMetaData();
        unit.putAttachment( RubyRuntimeMetaData.ATTACHMENT_KEY, runtimeMetaData );
        
        deploy( phaseContext );

        RubyRuntimeMetaData runtimeMetaData2 = unit.getAttachment( RubyRuntimeMetaData.ATTACHMENT_KEY );

        assertSame( runtimeMetaData, runtimeMetaData2 );
        assertEquals( RubyRuntimeMetaData.CompileMode.JIT, runtimeMetaData.getCompileMode() );
    }

    @Test
    public void testWithRuntimeMetaDataCompileModeOff() throws Exception {
        URL rubyYml = getClass().getResource( "ruby-compile-mode-off.yml" );
        MockDeploymentPhaseContext phaseContext = createPhaseContext( "torquebox.yml", rubyYml );
        MockDeploymentUnit unit = phaseContext.getMockDeploymentUnit();
        
        RubyRuntimeMetaData runtimeMetaData = new RubyRuntimeMetaData();
        unit.putAttachment( RubyRuntimeMetaData.ATTACHMENT_KEY, runtimeMetaData );
        
        deploy( phaseContext );

        RubyRuntimeMetaData runtimeMetaData2 = unit.getAttachment( RubyRuntimeMetaData.ATTACHMENT_KEY );

        assertSame( runtimeMetaData, runtimeMetaData2 );
        assertEquals( RubyRuntimeMetaData.CompileMode.OFF, runtimeMetaData.getCompileMode() );
    }

}
