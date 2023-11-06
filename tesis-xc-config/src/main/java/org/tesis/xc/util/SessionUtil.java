package org.tesis.xc.util;

import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;

import org.tesis.util.jakarta.controller.FacesUtil;
import org.tesis.xs.entity.BasicEntity;
import org.tesis.xs.entity.SessionEntity;


public class SessionUtil {

	public static int getUserId() {
	    SessionEntity session = getSession();
	    if (session!=null)
            return session.getUserId();
        else
            return 0;
	}
	
	public static BasicEntity getUser() {
        SessionEntity session = getSession();
        if (session!=null)
            return new BasicEntity(session.getUserId(), session.getUserName());
        else
            return null;
    }
    
	public static String getToken() {
		SessionEntity session = getSession();
		if (session!=null)
			return session.getToken();
		else
			return null;
	}
	
	public static Locale getLocale() {
		
		SessionEntity session = getSession();
		if (session != null && session.getLocale() != null)
			return session.getLocale();
		else
			return  Locale.getDefault();
	
	}
	
	public static TimeZone getTimeZone() {
		
		SessionEntity session = getSession();
		if (session != null && session.getTimezone() != null)
			return session.getTimezone();
		else
			return TimeZone.getDefault();
	
	}
	
	public static ZoneId getZoneId() {
	    return getTimeZone().toZoneId();
    }
	
	public static SessionEntity getSession() {
		SessionEntity session =
	            (SessionEntity) FacesUtil.getFacesSession()
	                            .getAttribute("session");
        if (session!=null)
            return session;
        else
            return null;
    }
	
}
