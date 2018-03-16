package com.net.parking.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "traceinfo_log")
public class TraceInfoLog implements Serializable {

private static final long serialVersionUID = 1L;
	
	@Id
	@GenericGenerator(name = "TIL", strategy = "com.net.parking.entity.TraceInfoLogIDGenerator")
	@GeneratedValue(generator = "TIL")  
	@Column(name = "id", length = 255, nullable = false)
	private String id;
	
	@Column(name = "DB")
	private String dB;
	
	@Column(name = "TIME")
	private String time;
	
	@Column(name = "TAGID")
	private String tagid;
	
	@Column(name = "STATUS")
	private String status;
	
	public TraceInfoLog() {}
	
	public TraceInfoLog(String dB, String time, String tagid) {
		this.dB = dB;
		this.time = time;
		this.tagid = tagid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getdB() {
		return dB;
	}

	public void setdB(String dB) {
		this.dB = dB;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTagid() {
		return tagid;
	}

	public void setTagid(String tagid) {
		this.tagid = tagid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Override
	public String toString() {
		
		return "{ id: "+this.id+" dB: "+this.dB+" time: "+this.time+" tagid: "+this.tagid+" status: "+this.status+"}";
	}
}