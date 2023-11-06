package org.tesis.xc.manager.imp;

import java.io.Serializable;
import java.util.HashMap;

import org.tesis.xc.config.jersey.JerseyClient;
import org.tesis.xc.jakarta.net.rest.jersey.client.JerseyMethodEnum;
import org.tesis.xc.util.AnalyzerException;
import org.tesis.xc.webservices.jersey.ServiceSessionEnum;
import org.tesis.xs.entity.LoginEntity;
import org.tesis.xs.entity.SessionEntity;
import org.tesis.xs.exception.BasicException;
import org.tesis.xs.exception.ExceptionEntity;
import org.tesis.xs.serv.SessionDao;

import jakarta.ws.rs.core.Response.Status;

@SuppressWarnings("serial")
public class SessionDaoImp implements SessionDao, Serializable {

    @Override
    public SessionEntity login(LoginEntity login) throws BasicException {

        try (JerseyClient service = new JerseyClient(JerseyMethodEnum.POST, ServiceSessionEnum.login)) {
            if (service.execute(login)) 
                return service.getResponce(SessionEntity.class);
            else if (service.getResponceCode() == Status.FORBIDDEN.getStatusCode()) 
				throw (BasicException) AnalyzerException.analyzer(service.getResponceCode(),service.getResponce(ExceptionEntity.class));
			else
				throw (BasicException) AnalyzerException.analyzer(service.getResponceCode());
            
        } catch (BasicException e) {
            throw e;
        } catch (Throwable e) {
            throw new BasicException("Error en llamado a servicio de login",e);
        }
       
    }

    @Override
    public void logout(int userId) throws BasicException {

    	HashMap<String, String> param = new HashMap<>();
    	param.put("userId", String.valueOf(userId));

    	try (JerseyClient service = new JerseyClient(JerseyMethodEnum.GET, ServiceSessionEnum.logout, param)) {
    		if (!service.execute()) 
    			if (service.getResponceCode() == Status.FORBIDDEN.getStatusCode()) 
    				throw (BasicException) AnalyzerException.analyzer(service.getResponceCode(),service.getResponce(ExceptionEntity.class));
    			else
    				throw (BasicException) AnalyzerException.analyzer(service.getResponceCode());

    	} catch (BasicException e) {
    		throw e;
    	} catch (Throwable e) {
    		throw new BasicException("Error en llamado a servicio de login",e);
    	}

    }

}
