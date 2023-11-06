package org.tesis.xc.util;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.tesis.util.jakarta.controller.FacesMessageUtil;
import org.tesis.util.jakarta.controller.FacesUtil;
import org.tesis.xs.exception.BasicException;
import org.tesis.xs.exception.ConexionFailedException;
import org.tesis.xs.exception.ExceptionEntity;
import org.tesis.xs.exception.ForbiddenException;
import org.tesis.xs.exception.IdentityDeniedException;
import org.tesis.xs.exception.IdentityExistsException;
import org.tesis.xs.exception.IdentityNotAvailableException;
import org.tesis.xs.exception.IdentityNotfoundException;
import org.tesis.xs.exception.MasterException;
import org.tesis.xs.exception.MasterExceptionEnum;
import org.tesis.xs.exception.PermissionException;
import org.tesis.xs.exception.UnauthorizedException;

import jakarta.ws.rs.core.Response.Status;

public class AnalyzerException {
	private final static Logger log = LogManager.getLogger(AnalyzerException.class);    

    public static <T> void analyzer(T classSend, Throwable exception) {
        log.debug("AnalyzerException.analyzer({})", exception.getClass().getName());

        if (exception instanceof UnauthorizedException) {
            log.debug("No autorizado, posiblemente por token vencido o invalido");
            FacesUtil.getFacesSession(true).setAttribute("status_session", false);
            PrimeFaces.current().executeScript("PF('loginDialogWidget').show();");
        }
        else if (exception instanceof PermissionException) {
            try {
                log.debug("Excepci\u00f3n por permisos", exception);
                FacesUtil.redirectPage("access.xhtml");

            }
            catch (Throwable e) {
                log.error("Error", e);
                FacesMessageUtil.generateErrorMessage("No posee permiso para acceder a esta pantalla");
            }
        }
        else if (exception instanceof MasterException) {
        	MasterException masterE = (MasterException)exception;
            FacesMessageUtil.generateErrorMessage(
            		masterE.getEntity().getMessage());
        }
        else if (exception instanceof ConexionFailedException) {
            FacesMessageUtil.generateErrorMessage(
                    "Error en la conexi\u00F3n.");
        }
        else {
            log.error("Error", exception);
            LogManager.getLogger(classSend.getClass()).error("Error", exception);
            FacesMessageUtil.generateErrorMessage("Error inesperado. (Error: 500).");
        }

    }

    public static Object analyzer(int responseCode)
            throws UnauthorizedException, IdentityExistsException, IdentityNotfoundException, ForbiddenException,
            IdentityDeniedException, IdentityNotAvailableException, BasicException
    {
        

       return analyzer(responseCode, null);
        
        
    }
    
    public static Object analyzer(int responseCode, ExceptionEntity entity)
            throws BasicException
    {
        log.info("AnalyzerException.analyzer({})", entity);

        if (responseCode == Status.UNAUTHORIZED.getStatusCode()) {
            return new UnauthorizedException();
        }
        
        else if (responseCode == Status.NOT_FOUND.getStatusCode()) {
            return new ConexionFailedException("Error conectando con el servicio");
        }
        else if (responseCode == Status.FORBIDDEN.getStatusCode()) {
        	return MasterExceptionEnum.exception(entity.getCode());
        }
        else {
            //log.error("Error desconocido, codigo = {}", entity);
        	log.info("HTTP Code "+responseCode);
            return new BasicException("Error inesperado en la respuesta del servicio");
        }
    }
    
}
