package org.torquebox.jobs;

import org.jboss.logging.Logger;
import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * User: Bruno
 * Date: 1/10/12
 * Time: 1:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class RubyJobWatchDog {

    public static void start(final JobExecutionContext jobExecutionContext) {
        int delay = 5000;   // delay for 5 sec.
        log.info("|||||||||||||||| watchdog delay |||||||||||||||| " + delay);

        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        //ThreadPool


        service.schedule(new Runnable() {
            public void run() {
                try {
                    log.info("|||||||||||||||| trying to interrupt |||||||||||||||| ");
                    ((InterruptableJob) jobExecutionContext.getJobInstance()).interrupt();
                    log.info("|||||||||||||||| interrupted |||||||||||||||| ");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, delay, TimeUnit.MILLISECONDS);
    }

    private static final Logger log = Logger.getLogger( "org.torquebox.jobs" );
}
