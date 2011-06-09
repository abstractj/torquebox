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

package org.torquebox.cdi;

import javax.enterprise.inject.spi.BeanManager;

import org.jboss.as.naming.NamingStore;
import org.jboss.as.naming.ValueManagedReferenceFactory;
import org.jboss.as.naming.deployment.ContextNames;
import org.jboss.as.naming.service.BinderService;
import org.jboss.as.server.deployment.Attachments;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.as.weld.WeldContainer;
import org.jboss.as.weld.WeldDeploymentMarker;
import org.jboss.as.weld.arquillian.WeldContextSetup;
import org.jboss.as.weld.deployment.BeanDeploymentArchiveImpl;
import org.jboss.as.weld.deployment.WeldAttachments;
import org.jboss.as.weld.services.BeanManagerService;
import org.jboss.as.weld.services.WeldService;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceTarget;
import org.jboss.msc.value.InjectedValue;
import org.torquebox.core.app.RubyApplicationMetaData;

/**
 * {@link DeploymentUnitProcessor} that binds the bean manager to JNDI
 * 
 * @author Stuart Douglas
 * 
 */
public class HackWeldBeanManagerServiceProcessor implements DeploymentUnitProcessor {

    @Override
    public void deploy(DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {

        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();
        final DeploymentUnit topLevelDeployment = deploymentUnit.getParent() == null ? deploymentUnit : deploymentUnit.getParent();
        final ServiceTarget serviceTarget = phaseContext.getServiceTarget();
        
        if (!WeldDeploymentMarker.isPartOfWeldDeployment( topLevelDeployment )) {
            return;
        }
        
        RubyApplicationMetaData rubyAppMetaData = deploymentUnit.getAttachment( RubyApplicationMetaData.ATTACHMENT_KEY );
        if (rubyAppMetaData == null) {
            return;
        }

        BeanDeploymentArchiveImpl rootBda = deploymentUnit.getAttachment( WeldAttachments.DEPLOYMENT_ROOT_BEAN_DEPLOYMENT_ARCHIVE );
        if (rootBda == null) {
            rootBda = topLevelDeployment.getAttachment( WeldAttachments.DEPLOYMENT_ROOT_BEAN_DEPLOYMENT_ARCHIVE );
        }
        if (rootBda == null) {
            throw new RuntimeException( "Could not find BeanManager for deployment " + deploymentUnit.getName() );
        }

        final ServiceName beanManagerServiceName = BeanManagerService.serviceName( deploymentUnit );
        if (phaseContext.getServiceRegistry().getService( beanManagerServiceName ) != null) {
            return;
        }

        final ServiceName weldServiceName = topLevelDeployment.getServiceName().append( WeldService.SERVICE_NAME );

        BeanManagerService beanManagerService = new BeanManagerService( rootBda.getId() );
        serviceTarget.addService( beanManagerServiceName, beanManagerService ).addDependency( weldServiceName,
                    WeldContainer.class, beanManagerService.getWeldContainer() ).install();

        deploymentUnit.addToAttachmentList( Attachments.SETUP_ACTIONS, new WeldContextSetup() );
    }

    private void bindBeanManager(ServiceTarget serviceTarget, ServiceName beanManagerServiceName, ServiceName contextServiceName) {
        final ServiceName beanManagerBindingServiceName = contextServiceName.append( "BeanManager" );

        InjectedValue<BeanManager> injectedBeanManager = new InjectedValue<BeanManager>();
        BinderService beanManagerBindingService = new BinderService( "BeanManager" );
        serviceTarget.addService( beanManagerBindingServiceName, beanManagerBindingService )
                    .addInjection( beanManagerBindingService.getManagedObjectInjector(), new ValueManagedReferenceFactory( injectedBeanManager ) )
                    .addDependency( contextServiceName, NamingStore.class, beanManagerBindingService.getNamingStoreInjector() )
                    .addDependency( beanManagerServiceName, BeanManager.class, injectedBeanManager )
                    .install();
    }

    @Override
    public void undeploy(DeploymentUnit deploymentUnit) {

    }

}
