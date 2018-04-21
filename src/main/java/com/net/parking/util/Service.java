package com.net.parking.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;
import com.net.parking.dao.util.DaoUtil;
import com.net.parking.entity.TraceInfo;
import com.net.parking.entity.TraceInfoDBLog;
import com.net.parking.entity.TraceInfoLog;

public class Service {
	
	private BufferedReader bufferedReader = null;
	private StringBuffer buffer = null;
	private TraceInfo traceInfo = null;
	private TraceInfoLog traceInfoLog = null;
	private String sCurrentLine, timestamp, dbinfo, tagId = null;
	private FileReader fileReader = null;
	
	public String traceRead(String path_, DaoUtil daoUtil) throws Exception {
		
		try {
			fileReader = new FileReader(path_);
			bufferedReader = new BufferedReader(fileReader);
			buffer = new StringBuffer();
			
			TraceInfoDBLog traceInfoDBLog = daoUtil.fatchLastRecordOfDBLog();
			
			while ((sCurrentLine = bufferedReader.readLine()) != null) {
				
				if ( sCurrentLine.contains("DB:    ") ) {
					
					buffer.append(sCurrentLine+"<br/>\n");
					timestamp = getTimestamp(sCurrentLine);
					
					if (traceInfoDBLog != null) {
						Date date1 = null, date2 = null;
						try {
							date1 = new SimpleDateFormat("dd.mm.yy HH:mm:ss", Locale.ENGLISH).parse(traceInfoDBLog.getTime().trim());
						}catch(Exception exception) {	exception.getMessage();	}
						try {
							date2 = new SimpleDateFormat("dd.mm.yy HH:mm:ss", Locale.ENGLISH).parse(timestamp.trim());
						}catch(Exception exception) {	exception.getMessage();	}
						if (date1 != null & date2 != null)
						if (date1.before(date2)) {
							System.out.println("***************************************************************");
							System.out.println("********************* NEW RECORD FOUND : *********************");
							System.out.println("***************************************************************");
							traceOperation(daoUtil, dbinfo, tagId, traceInfo, traceInfoLog);
						}
					}
					else
						traceOperation(daoUtil, dbinfo, tagId, traceInfo, traceInfoLog);
				}
			}
		} catch (IOException e) {	e.printStackTrace();	} 
		finally {	bufferedReader = null;	fileReader = null;	}
		return buffer.toString();
	}
	
	
	
	private void traceOperation(DaoUtil daoUtil, String dbinfo, String tagId, TraceInfo traceInfo, TraceInfoLog traceInfoLog) {
		
		dbinfo = getDBInfo(sCurrentLine, timestamp);
		tagId = getTagIdInfo(sCurrentLine);
		
		if ( tagId.length() > 4 ) {
			if ( isNumericData(tagId) ) {
				
				traceInfo = new TraceInfo( dbinfo, timestamp, tagId );
				traceInfo = (TraceInfo) daoUtil.createOrUpdate(traceInfo);
				
				traceInfoLog = new TraceInfoLog( dbinfo, timestamp, tagId );
				traceInfoLog.setStatus(traceInfo.getStatus());
				daoUtil.create(traceInfoLog);
			}
		}
		
		TraceInfoDBLog traceInfoDBLog1 = new TraceInfoDBLog( dbinfo, timestamp, tagId );
		daoUtil.create(traceInfoDBLog1);
	}



	private String getDBInfo(String sCurrentLine, String time) {
		String DB = sCurrentLine;
		if(time.length() > 0)
		DB = sCurrentLine.substring(sCurrentLine.indexOf(time));
		return DB;
	}
	
	
	
	private String getTagIdInfo(String sCurrentLine) {
		String line = "";
		try{
		StringTokenizer tokenizer = new StringTokenizer(sCurrentLine);
		while (tokenizer.hasMoreElements()) {
			String object = (String) tokenizer.nextElement();
			if(object.length() > 7){
				
				if(isNumericData(object))
					line = object;
			}
		}
		}catch(Exception exception){exception.getMessage();}
		return line;
	}
	
	private Boolean isNumericData(String number) {
		Boolean flag = true;
		try{
			Double.parseDouble(number);
		}catch(Exception exception){	flag = false;	}
		return flag;
	}


	private String getTimestamp(String sCurrentLine) {
		String time = "";
		if(sCurrentLine.length() > 0)
		time = sCurrentLine.substring(0, (18));
		return time;
	}

}
