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

package org.torquebox.jobs;

import org.jboss.logging.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.TriggerListener;

public class RubyJobListener implements TriggerListener{

    @Override
    public String getName() {
        //TODO Implement method
        return "JobListener name: " + this.getName();
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext jobExecutionContext) {
        log.info("triggerFired: " + trigger.getGroup());
        log.info("triggerFired: " + jobExecutionContext.getJobDetail().getName());
    }

    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext jobExecutionContext) {
        //TODO Implement method
        return false;
    }

    @Override
    public void triggerMisfired(Trigger trigger) {
        //TODO Implement method
    }

    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext jobExecutionContext, int i) {
        //TODO Implement method
    }

    private static final Logger log = Logger.getLogger( "org.torquebox.jobs" );
}
