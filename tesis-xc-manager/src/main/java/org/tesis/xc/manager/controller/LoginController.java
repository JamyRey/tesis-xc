package org.tesis.xc.manager.controller;


import java.io.IOException;
import java.io.Serializable;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.tesis.util.jakarta.controller.FacesMessageUtil;
import org.tesis.util.jakarta.controller.FacesUtil;
import org.tesis.xs.entity.LoginEntity;
import org.tesis.xs.entity.SessionEntity;
import org.tesis.xs.exception.IdentityNotAvailableException;
import org.tesis.xs.exception.IdentityNotfoundException;
import org.tesis.xs.utils.XSHA1Util;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class LoginController implements Serializable {

	private Logger log = LogManager.getLogger(this.getClass());

    private boolean status_session = true;

    private boolean             status_connection = true;
    private LoginEntity         login             = new LoginEntity();
    private SessionEntity       session           = new SessionEntity();
    private String              myPath;
       

    //@Inject private SessionDao sessionDao;
    private SessionDao sessionDao = new SessionDaoImp();


    @PostConstruct
    public void init() {
        // Primer Servicio llamado en todo el sistema

        try {
            
            log.debug("PostContruct init");
            System.out.println("PostContruct init");
            
            if (FacesUtil.getFacesSession(true).getAttribute("status_session") != null) {
                this.status_session = (boolean) FacesUtil.getFacesSession(true).getAttribute("status_session");
            }   
            
            this.login = new LoginEntity();
            
        }
        catch (Throwable e) {
            this.status_connection = false;
            log.error("Error: ", e);
            FacesMessageUtil.generateErrorMessage("msg.err.controller.unexpected");
            //FacesMessageUtil.generateErrorMessage(JsfResourceBundleUtil.getResourceBundleGlobalValue(
                    //LanguageEnums.BUNDLE_GLOBAL.getValue(), "msg.err.controller.unexpected"));
        }

        /*if (dataConfig == null) {
            this.status_connection = false;
        }*/
    }

    /**
     *
     * @return
     * @throws IOException
     */
    public String signIn() throws IOException {


        try {
            this.login.setUsername(this.getLogin().getUsername().trim().toUpperCase());
            this.login.setPassword(XSHA1Util.toSHA1(this.getLogin().getPassword()));

            this.session = this.sessionDao.login(this.login);

            FacesUtil.getFacesSession(true).setAttribute("session", this.session);

            FacesUtil.getFacesSession(true).setMaxInactiveInterval(session.getTimeOutSession());
            // Depuramos los status
            FacesUtil.getFacesSession(true).setAttribute("status_session", true);

            if (myPath == null) {
                //FacesUtil.redirectPage("logged/dashboard.xhtml");
                FacesUtil.redirectPage("/logged/dashboard.xhtml");
            }
            /*else {
                PrimeFaces.current().executeScript("PF('sesionWarningTimer').stop();");
                PrimeFaces.current().executeScript("PF('loginDialogWidget').hide();");
                PrimeFaces.current().executeScript("startIdleInterval();");
                
            }*/
        }
        catch (IdentityNotfoundException e) {
            FacesMessageUtil.generateErrorMessage("Usuario no encontrado."); // TODO: Cambiar por bundle
            //FacesMessageUtil.generateInfoMessage(
              //      ResourceBundleUtil.getResourceBundleValue(xLangBundle, "msg.controller.invalidUser"));
        }
        catch (IdentityNotAvailableException e) {
            FacesMessageUtil.generateErrorMessage("Su usuario se encuentra deshabilitado."); // TODO: Cambiar por bundle
            //FacesMessageUtil.generateInfoMessage(
              //      ResourceBundleUtil.getResourceBundleValue(xLangBundle, "msg.controller.userNotEnabled"));
        }
        catch (Throwable e) {
            log.error("Error: ", e);
            //AnalyzerException.analyzer(this, e);
            throw e;
        }

        return null;
    }
    
    public String validate() throws IOException {


        try {
            this.login.setUsername(this.getLogin().getUsername().trim().toUpperCase());
            this.login.setPassword(XSHA1Util.toSHA1(this.getLogin().getPassword()));

            this.session = this.sessionDao.login(this.login);

            FacesUtil.getFacesSession(true).setAttribute("login", this.login);

            FacesUtil.getFacesSession(true).setMaxInactiveInterval(session.getTimeOutSession());
            // Depuramos los status
            FacesUtil.getFacesSession(true).setAttribute("status_session", true);
            

            if (myPath == null) {
            	FacesUtil.redirectPage("/logged/marketingCampaign/index.xhtml");
            }
            
            else {
                PrimeFaces.current().executeScript("PF('sesionWarningTimerDialog').stop();");
                PrimeFaces.current().executeScript("PF('loginDialogWidget').hide();");
                PrimeFaces.current().executeScript("startIdleInterval();");
                
            }
        }
        catch (IdentityNotfoundException e) {
            FacesMessageUtil.generateErrorMessage("Usuario no encontrado."); // TODO: Cambiar por bundle
            //FacesMessageUtil.generateInfoMessage(
              //      ResourceBundleUtil.getResourceBundleValue(xLangBundle, "msg.controller.invalidUser"));
        }
        catch (IdentityNotAvailableException e) {
            FacesMessageUtil.generateErrorMessage("Su usuario se encuentra deshabilitado."); // TODO: Cambiar por bundle
            //FacesMessageUtil.generateInfoMessage(
              //      ResourceBundleUtil.getResourceBundleValue(xLangBundle, "msg.controller.userNotEnabled"));
        }
        catch (Throwable e) {
            log.error("Error: ", e);
           // XAnalyzerException.analyzer(this, e);
            throw e;
        }

        return null;
    }
    
  
    public LoginEntity getLogin() {
        return login;
    }

    public boolean isStatus_connection() {
        return status_connection;
    }

    public void setStatus_connection(boolean status_connection) {
        this.status_connection = status_connection;
    }

    public boolean isStatus_session() {
        return status_session;
    }

    public void setStatus_session(boolean status_session) {
        this.status_session = status_session;
    }

	public String getMyPath() {
		return myPath;
	}

	public void setMyPath(String myPath) {
		this.myPath = myPath;
	}

}
