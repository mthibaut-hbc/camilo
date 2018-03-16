package com.net.parking;

import com.net.parking.scheduler.CronTriggerExample;

public class MainApp {
	
	public static void main(String...trace) {
		try{
			System.out.println("==========================================>>");
			new CronTriggerExample().execute();
		}catch(Exception exception){	exception.printStackTrace();	}
	}
}