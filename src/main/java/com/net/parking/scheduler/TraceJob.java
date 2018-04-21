package com.net.parking.scheduler;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.net.parking.dao.util.DaoUtil;
import com.net.parking.entity.Constants;
import com.net.parking.util.Service;

public class TraceJob implements Job {
	
	private String tracePath;
	private Service service;
	private DaoUtil daoUtil;
	private Properties properties;
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println("*******************job begins********************");
		inItProcess();
		System.out.println("==================================="+(Constants.logindex));
		Constants.logindex ++;
	}
	
	
	public static void main(String[] args) {
		new TraceJob().inItProcess();
	}
	public void inItProcess() {
		
		File file = new File("resource/trace.properties");
		
		service = new Service();
	    daoUtil = new DaoUtil();
	    try
	    {
	    	String absolutePath = file.getAbsolutePath();
	    	properties = new Properties();
	    	properties.load(new FileInputStream(new File(absolutePath)));
	    	tracePath = properties.getProperty("file.path", "");
	    	System.out.println("TracePath : " + tracePath);
	    	service.traceRead(this.tracePath, daoUtil);
	    }
	    catch (Exception exception){	exception.printStackTrace();	}
	}
}