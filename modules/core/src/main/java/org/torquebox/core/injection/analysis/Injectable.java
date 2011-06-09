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

package org.torquebox.core.injection.analysis;

import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.msc.service.ServiceName;

/**
 * Injectable item which translates to an acceptable MSC {@link ServiceName}.
 * 
 * @see InjectableHandler
 * @see ServiceName
 * 
 * @author Bob McWhirter
 */
public interface Injectable {

    /**
     * Retrieve the type of the injectable, typically related-to, if not
     * identical-to, the originating {@link InjectableHandler}'s type.
     * 
     * @return
     */
    String getType();

    /**
     * Retrieve the specific name of this injectable.
     * 
     * @return The name.
     */
    String getName();

    /**
     * Retrieve the lookup key for this injectable.
     * 
     * <p>
     * The key may not match the name, given the way expressions evaluate at
     * runtime within the Ruby * interpreter (specifically, how the constant
     * 'org.foo.Someclass' translates to a string.
     * </p>
     * 
     * @return The key used to index this injectable in the Ruby interpreter.
     */
    String getKey();

    boolean isGeneric();

    /**
     * Retrieve the MSC <code>ServiceName</code> of the actual underlying
     * injectable asset.
     * 
     * @param phaseContext The deployment context.
     * @return The <code>ServiceName</code> of the injectable item.
     * @throws Exception
     */
    ServiceName getServiceName(DeploymentPhaseContext phaseContext) throws Exception;

}
