package com.net.parking.scheduler;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class CronTriggerExample {
	
	private Properties properties;
	private SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
	private File file = null;
	private JobDetail job = null;
	private Trigger trigger = null;
	private Scheduler scheduler = null;
	private String prefix = null;
	
    public void execute() throws Exception {
    	
    	file = new File("resource/cron.properties");
    	properties = new Properties();
		properties.load(new FileInputStream(new File(file.getAbsolutePath())));
		prefix = formatter.format(new Date());
		
    	job = JobBuilder.newJob(TraceJob.class) .withIdentity("dummyJobName_"+(prefix), "group1_" + (prefix)).build();
    	
    	trigger = TriggerBuilder
		.newTrigger()
		.withIdentity("dummyTriggerName_"+(prefix), "group1_"+(prefix))
		.withSchedule( CronScheduleBuilder.cronSchedule(properties.getProperty("time", ""))) .build();
    	
    	scheduler = new StdSchedulerFactory().getScheduler();
    	scheduler.start();
    	scheduler.scheduleJob(job, trigger);

    }
}