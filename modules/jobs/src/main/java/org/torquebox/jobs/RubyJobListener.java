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
import org.quartz.*;

public class RubyJobListener implements JobListener {

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext jobExecutionContext) {
        log.info("|||||||||||||||| triggerFired tobe: " + jobExecutionContext.getFireTime().toString());
        log.info("|||||||||||||||| light my fire tobe: " + jobExecutionContext.getJobDetail().getFullName());
        log.info("|||||||||||||||| job was Fired tobe: " + jobExecutionContext.getScheduledFireTime());
        try {
            Thread.sleep(5000L);
            log.info("|||||||||||||||| trying to interrupt |||||||||||||||| ");
            String jobName = jobExecutionContext.getJobDetail().getName();
            String groupName = jobExecutionContext.getJobDetail().getGroup();
            log.info("|||||||||||||||| jobName |||||||||||||||| " + jobName);
            log.info("|||||||||||||||| groupName |||||||||||||||| " + groupName);
            jobExecutionContext.getScheduler().interrupt(jobName,
                    groupName);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (UnableToInterruptJobException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext jobExecutionContext) {
        log.info("|||||||||||||||| triggerFired: vetoed " + jobExecutionContext.getFireTime().toString());
        log.info("|||||||||||||||| light my fire: vetoed " + jobExecutionContext.getJobDetail().getFullName());
        log.info("|||||||||||||||| job was Fired: vetoed " + jobExecutionContext.getScheduledFireTime());

    }

    @Override
    public void jobWasExecuted(JobExecutionContext jobExecutionContext, JobExecutionException e) {
        log.info("|||||||||||||||| triggerFired: executed " + jobExecutionContext.getFireTime().toString());
        log.info("|||||||||||||||| light my fire: executed " + jobExecutionContext.getJobDetail().getFullName());
        log.info("|||||||||||||||| job was Fired: executed " + jobExecutionContext.getScheduledFireTime());

    }

    private static final Logger log = Logger.getLogger("org.torquebox.jobs");


}
