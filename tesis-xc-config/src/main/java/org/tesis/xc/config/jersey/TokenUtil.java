package org.tesis.xc.config.jersey;

import org.tesis.util.jakarta.controller.FacesUtil;
import org.tesis.xs.entity.SessionEntity;

public class TokenUtil {
	
	public static String getToken() { 
		SessionEntity session = (SessionEntity)FacesUtil.getFacesSession(false).getAttribute("login");
		if (session!=null)
			return session.getToken();
		else 
			return null;
		
	}
}
