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

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RubyTriggerListener implements TriggerListener {

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public void triggerFired(Trigger trigger, final JobExecutionContext jobExecutionContext) {

    }

    @Override
    public boolean vetoJobExecution(Trigger trigger, final JobExecutionContext jobExecutionContext) {
        log.info("|||||||||||||||| triggerFired tobe: " + jobExecutionContext.getFireTime().toString());
        log.info("|||||||||||||||| light my fire tobe: " + jobExecutionContext.getJobDetail().getFullName());
        log.info("|||||||||||||||| job was Fired tobe: " + jobExecutionContext.getScheduledFireTime());
        final String jobName = jobExecutionContext.getJobDetail().getName();
        final String groupName = jobExecutionContext.getJobDetail().getGroup();

        int delay = 5000;   // delay for 5 sec.

        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);

        service.schedule(new Runnable() {
            public void run() {
                try {
                    log.info("|||||||||||||||| jobName |||||||||||||||| " + jobName);
                    log.info("|||||||||||||||| groupName |||||||||||||||| " + groupName);
                    ((InterruptableJob) jobExecutionContext.getJobInstance()).interrupt();
                    log.info("|||||||||||||||| interrupted |||||||||||||||| ");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, delay, TimeUnit.MILLISECONDS);

        return true;
    }

    @Override
    public void triggerMisfired(Trigger trigger) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext jobExecutionContext, int i) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    private static final Logger log = Logger.getLogger("org.torquebox.jobs");


}
