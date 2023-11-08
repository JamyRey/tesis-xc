package org.tesis.xc.manager.controller;

import java.io.Serializable;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import org.tesis.util.jakarta.controller.FacesUtil;
import org.tesis.xc.manager.imp.SessionDaoImp;
import org.tesis.xc.util.AnalyzerException;
import org.tesis.xc.util.SessionUtil;
import org.tesis.xs.entity.SessionEntity;
import org.tesis.xs.serv.SessionDao;

@SuppressWarnings("serial")
@Named("template")
@ViewScoped
public class TemplateController implements Serializable {

	private Logger log = LogManager.getLogger(this.getClass());
	
	private SessionEntity session;
	private Locale locale;
	private TimeZone timezone;
	private LocalDateTime currentDate;
	private LocalTime currentTime;
	
	private SessionDao sessionDao = new SessionDaoImp();
	
	@PostConstruct
    public void init() {

		try {
			this.session = SessionUtil.getSession();
			this.locale = SessionUtil.getLocale();
			this.timezone = SessionUtil.getTimeZone();
			
			this.currentDate = LocalDateTime.now(this.timezone.toZoneId());
			this.currentTime = LocalTime.now(this.timezone.toZoneId());
			
			log.debug("locale = "+locale.toString()+" timezone: "+timezone.getDisplayName());
			
		}
		catch (Exception e) {
			log.error("Error obteniendo datos de sesion: ", e);
		}
    }
	
	public void logout() {

		try {
			
			sessionDao.logout(session.getUserId());
			FacesUtil.getFacesSession(true).setAttribute("status_session", false);
			FacesUtil.redirectPage("login.xhtml");
		}
		catch (Exception e) {
			AnalyzerException.analyzer(this, e);
		}
	}
	
	public void extendSession() {
	    try {
	       // FacesMessageUtil.generateInfoMessage(
	         //       ResourceBundleUtil.getResourceBundleValue(msgBudget, "msg.controller.sessionExtendedSuccessfully"));
	    	
	        log.debug("Sesion extendida");
	    } catch (Exception e) {
	        AnalyzerException.analyzer(this, e);
        }
	}
	
	public SessionEntity getSession() {
		return session;
	}

	public Locale getLocale() {
		return locale;
	}

	public TimeZone getTimezone() {
		return timezone;
	}

	public LocalDateTime getCurrentDate() {
		return currentDate;
	}

	public LocalTime getCurrentTime() {
		return currentTime;
	}

}
