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

import java.util.HashMap;
import java.util.Map;

import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.vfs.VFS;
import org.junit.Before;
import org.junit.Test;
import org.torquebox.core.app.RubyApplicationMetaData;
import org.torquebox.core.runtime.BaseRubyRuntimeDeployer;
import org.torquebox.core.runtime.RubyRuntimeMetaData;
import org.torquebox.test.as.AbstractDeploymentProcessorTestCase;
import org.torquebox.test.as.MockDeploymentPhaseContext;
import org.torquebox.test.as.MockDeploymentUnit;

public class BaseRubyRuntimeDeployerTest extends AbstractDeploymentProcessorTestCase {
    
    private Map<String, String> environment = new HashMap<String, String>();

    @Before
    public void setUpDeployer() throws Throwable {
        appendDeployer( new BaseRubyRuntimeDeployer() );
    }

    @Test
    public void testHappy() throws Exception {
        environment.put( "SOME_VAR", "gassy" );
        RubyApplicationMetaData rubyAppMetaData = new RubyApplicationMetaData( "app_name");

        rubyAppMetaData.setRoot( VFS.getChild( "/foo" ) );
        rubyAppMetaData.setEnvironmentVariables( environment );

        MockDeploymentPhaseContext phaseContext = createPhaseContext();
        DeploymentUnit unit = phaseContext.getMockDeploymentUnit();

        unit.putAttachment( RubyApplicationMetaData.ATTACHMENT_KEY, rubyAppMetaData );

        deploy( phaseContext );

        RubyRuntimeMetaData runtimeMetaData = unit.getAttachment( RubyRuntimeMetaData.ATTACHMENT_KEY );
        assertNotNull( runtimeMetaData );
        assertEquals( vfsAbsolutePrefix() + "/foo", runtimeMetaData.getBaseDir().getPathName() );
        assertTrue( runtimeMetaData.getEnvironment().containsKey( "SOME_VAR" ) );
        assertEquals( RubyRuntimeMetaData.RuntimeType.BARE, runtimeMetaData.getRuntimeType() );
    }


    @Test
    public void testWithExistingRubyRuntimeMD() throws Exception {
        RubyApplicationMetaData rubyAppMetaData = new RubyApplicationMetaData( "app_name");

        rubyAppMetaData.setRoot( VFS.getChild( "/foo" ) );

        RubyRuntimeMetaData existingRuntimeMD = new RubyRuntimeMetaData();
        
        MockDeploymentPhaseContext phaseContext = createPhaseContext();
        MockDeploymentUnit unit = phaseContext.getMockDeploymentUnit();

        unit.putAttachment( RubyApplicationMetaData.ATTACHMENT_KEY, rubyAppMetaData );
        
        unit.putAttachment( RubyRuntimeMetaData.ATTACHMENT_KEY, existingRuntimeMD );
        unit.putAttachment( RubyApplicationMetaData.ATTACHMENT_KEY, rubyAppMetaData );

        deploy( phaseContext );

        RubyRuntimeMetaData runtimeMetaData = unit.getAttachment( RubyRuntimeMetaData.ATTACHMENT_KEY );
        assertNotNull( runtimeMetaData );
        assertEquals( existingRuntimeMD, runtimeMetaData );
        assertEquals( vfsAbsolutePrefix() + "/foo", runtimeMetaData.getBaseDir().getPathName() );
        assertEquals( RubyRuntimeMetaData.RuntimeType.BARE, runtimeMetaData.getRuntimeType() );
    }


    @Test
    public void testWithExistingTypedRubyRuntimeMD() throws Exception {
        RubyApplicationMetaData rubyAppMetaData = new RubyApplicationMetaData( "app_name");

        rubyAppMetaData.setRoot( VFS.getChild( "/foo" ) );

        MockDeploymentPhaseContext phaseContext = createPhaseContext();
        MockDeploymentUnit unit = phaseContext.getMockDeploymentUnit();
        
        RubyRuntimeMetaData existingRuntimeMD = new RubyRuntimeMetaData();
        existingRuntimeMD.setRuntimeType( RubyRuntimeMetaData.RuntimeType.RACK );

        unit.putAttachment( RubyRuntimeMetaData.ATTACHMENT_KEY, existingRuntimeMD );
        unit.putAttachment( RubyApplicationMetaData.ATTACHMENT_KEY, rubyAppMetaData );

        deploy( phaseContext );

        RubyRuntimeMetaData runtimeMetaData = unit.getAttachment( RubyRuntimeMetaData.ATTACHMENT_KEY );
        assertNotNull( runtimeMetaData );
        assertEquals( existingRuntimeMD, runtimeMetaData );
        assertNull( runtimeMetaData.getBaseDir() );
        assertEquals( RubyRuntimeMetaData.RuntimeType.RACK, runtimeMetaData.getRuntimeType() );
    }

}
