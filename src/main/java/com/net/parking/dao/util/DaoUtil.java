package com.net.parking.dao.util;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import com.net.parking.entity.TraceInfo;
import com.net.parking.entity.TraceInfoDBLog;
import com.net.parking.entity.TraceInfoLog;

@SuppressWarnings("unchecked")
public class DaoUtil {
	
	private Configuration configuration = null;
	private SessionFactory sessionFactory = null;
//	private Session session = null;
	
	public DaoUtil() {
		configuration = new Configuration().configure();
		StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder() .applySettings(configuration.getProperties());
		sessionFactory = configuration .buildSessionFactory(builder.build());
	}
	
	public Session getSessionFactory() {
		return sessionFactory.openSession();
	}

	public Object create(Object e) {
		Session session = getSessionFactory();
		session.beginTransaction();
		session.save(e);
		session.getTransaction().commit();
		session.close();
		return e;
	}
	private Boolean isNumericData(String number) {
		Boolean flag = true;
		try{
			Double.parseDouble(number);
		}catch(Exception exception){	flag = false;	}
		return flag;
	}
	
	
	public Object createOrUpdate(TraceInfo e) {
		
		if(e.getTagid() .length() > 4)
		if(isNumericData(e.getTagid())){
			
			TraceInfo exist = findByTagId(e);
			
			if( (exist.getId()) != null ){
				Session session = getSessionFactory();
				session.beginTransaction();
				session.delete(exist);
				session.getTransaction().commit();
				session.close();
				
				e.setStatus("OUT");
			}else{
				e.setStatus("IN");
			}
			Session session = getSessionFactory();
			session.beginTransaction();
			session.save(e);
			session.getTransaction().commit();
			session.close();
			System.out.println(" : Record inserted inside TraceInfo : "+e.getdB());
		}
		return e;
	}

	
	private TraceInfo findByTagId(TraceInfo e) {
		Session session = getSessionFactory();
		List<TraceInfo> traceInfo = session.createQuery("FROM TraceInfo where tagid = '"+e.getTagid()+"' and status='IN'").list();
		session.close();
		return (traceInfo.size() > 0 ? traceInfo.get(0): new TraceInfo());
	}

	public List<TraceInfoLog> read() {
		Session session = getSessionFactory();
		List<TraceInfoLog> traceInfoLogs = session.createQuery("FROM TraceInfoLog").list();
		session.close();
		return traceInfoLogs;
	}

	public TraceInfo update(TraceInfo e) {
		Session session = getSessionFactory();
		session.beginTransaction();
		TraceInfo em = (TraceInfo) session.load(TraceInfo.class, e.getId());
		em.setdB(e.getdB());
		session.getTransaction().commit();
		session.close();
		return em;
	}

	public Object delete(Integer id) {
		Session session = getSessionFactory();
		session.beginTransaction();
		TraceInfoLog e = findByID(id);
		session.delete(e);
		session.getTransaction().commit();
		session.close();
		return e;
	}

	public TraceInfoLog findByID(Integer id) {
		Session session = getSessionFactory();
		TraceInfoLog e = (TraceInfoLog) session.load(TraceInfoLog.class, id);
		session.close();
		return e;
	}
	
	public void deleteAll() {
		Session session = getSessionFactory();
		session.beginTransaction();
		Query query = session.createQuery("DELETE FROM TraceInfo ");
		query.executeUpdate();
		session.getTransaction().commit();
		session.close();
	}

	public TraceInfoDBLog fatchLastRecordOfDBLog() {
		Session session = getSessionFactory();
		TraceInfoDBLog traceInfoDBLog = (TraceInfoDBLog) session.createQuery("FROM TraceInfoDBLog order by id desc").setMaxResults(1).uniqueResult();
		session.close();
		return traceInfoDBLog;
	}
	
}
