package org.tesis.xc.manager.imp;

import java.io.Serializable;
import java.util.HashMap;

import org.tesis.xc.config.jersey.JerseyClient;
import org.tesis.xc.jakarta.net.rest.jersey.client.JerseyMethodEnum;
import org.tesis.xc.util.AnalyzerException;
import org.tesis.xc.webservices.jersey.ServiceClassEnum;
import org.tesis.xs.entity.ClassEntity;
import org.tesis.xs.entity.full.ClassFullEntity;
import org.tesis.xs.exception.BasicException;
import org.tesis.xs.exception.ExceptionEntity;
import org.tesis.xs.exception.MasterExceptionEnum;
import org.tesis.xs.serv.ClassDao;

import jakarta.ws.rs.core.Response.Status;

@SuppressWarnings("serial")
public class ClassDaoImp implements ClassDao, Serializable{

	@Override
	public ClassFullEntity initialData() throws BasicException {
		//HashMap<String, String> param = new HashMap<>();

        try (JerseyClient service = new JerseyClient(JerseyMethodEnum.GET, ServiceClassEnum.initialData)) {
            if (service.execute()) 
                return service.getResponce(ClassFullEntity.class);
            else if (service.getResponceCode() == Status.FORBIDDEN.getStatusCode()) 
				throw (BasicException) AnalyzerException.analyzer(service.getResponceCode(),service.getResponce(ExceptionEntity.class));
			else
				throw (BasicException) AnalyzerException.analyzer(service.getResponceCode());
            
        } catch (BasicException e) {
            throw e;
        } catch (Throwable e) {
            throw new BasicException("Error en llamado a servicio inicial de clases",e);
        }
	}

	@Override
	public ClassEntity createClass(ClassEntity entity) throws BasicException {

        try (JerseyClient service = new JerseyClient(JerseyMethodEnum.POST, ServiceClassEnum.createClass)) {
            if (service.execute(entity)) 
                return service.getResponce(ClassEntity.class);
            else if (service.getResponceCode() == Status.FORBIDDEN.getStatusCode()) 
				throw MasterExceptionEnum.exception(service.getResponce(ExceptionEntity.class).getCode());
			else
				throw (BasicException) AnalyzerException.analyzer(service.getResponceCode());
                
        } catch (BasicException e) {
            throw e;
        } catch (Throwable e) {
            throw new BasicException("Error en llamado a servicio de creación de clase",e);
        }
       
    }

	@Override
	public ClassEntity updateClass(ClassEntity entity) throws BasicException {
		
	        try (JerseyClient service = new JerseyClient(JerseyMethodEnum.PUT, ServiceClassEnum.updateClass)) {
	            if (service.execute(entity)) 
	                return service.getResponce(ClassEntity.class);
	            else if (service.getResponceCode() == Status.FORBIDDEN.getStatusCode()) 
					throw MasterExceptionEnum.exception(service.getResponce(ExceptionEntity.class).getCode());
				else
					throw (BasicException) AnalyzerException.analyzer(service.getResponceCode());	                
	            
	        } catch (BasicException e) {
	            throw e;
	        } catch (Throwable e) {
	            throw new BasicException("Error en llamado a servicio de edición de clase",e);
	        }
	       
	    }
	
	@Override
	public ClassEntity getClassById(int id) throws BasicException {
		HashMap<String, String> param = new HashMap<>();
		param.put("id", String.valueOf(id));
        try (JerseyClient service = new JerseyClient(JerseyMethodEnum.GET, ServiceClassEnum.getClassById)) {
            if (service.execute()) 
                return service.getResponce(ClassEntity.class);
            else if (service.getResponceCode() == Status.FORBIDDEN.getStatusCode()) 
				throw MasterExceptionEnum.exception(service.getResponce(ExceptionEntity.class).getCode());
			else
				throw (BasicException) AnalyzerException.analyzer(service.getResponceCode());
            
        } catch (BasicException e) {
            throw e;
        } catch (Throwable e) {
            throw new BasicException("Error en llamado a servicio para obtener clases por id",e);
        }
	}
	
	@Override
    public void updateActiveClass(ClassEntity entity) throws BasicException {

        try (JerseyClient service = new JerseyClient(JerseyMethodEnum.PUT, ServiceClassEnum.updateActiveClass)) {
        	if (!service.execute(entity)) 
                if (service.getResponceCode() == Status.FORBIDDEN.getStatusCode()) 
    				throw MasterExceptionEnum.exception(service.getResponce(ExceptionEntity.class).getCode());
    			else
    				throw (BasicException) AnalyzerException.analyzer(service.getResponceCode());

        } catch (BasicException e) {
            throw e;
        } catch (Throwable e) {
            throw new BasicException("Error en llamado a servicio para actualizar clase activa",e);
        }
       
    }
	
}
