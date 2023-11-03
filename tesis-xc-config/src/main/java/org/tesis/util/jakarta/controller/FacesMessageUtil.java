package org.tesis.util.jakarta.controller;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.application.FacesMessage.Severity;
import jakarta.faces.context.FacesContext;

public class FacesMessageUtil {
	public static void generateErrorMessage(String msj) {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msj,"");
		FacesContext.getCurrentInstance().addMessage(null, msg);	
	}
	
	public static void generateInfoMessage(String msj) {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, msj,"");
		FacesContext.getCurrentInstance().addMessage(null, msg);	
	}
	public static void generateWarnMessage(String msj) {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, msj,"");
		FacesContext.getCurrentInstance().addMessage(null, msg);	
	}
	public static void generateFaltalMessage(String msj) {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, msj,"");
		FacesContext.getCurrentInstance().addMessage(null, msg);	
	}
	
	public static void generateMessage(Severity icon,String msj) {
		FacesMessage msg = new FacesMessage(icon, msj,"");
		FacesContext.getCurrentInstance().addMessage(null, msg);	
	}
}
