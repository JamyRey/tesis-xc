package org.tesis.xc.manager.imp;

import java.io.Serializable;
import java.util.HashMap;

import org.tesis.xc.config.jersey.JerseyClient;
import org.tesis.xc.jakarta.net.rest.jersey.client.JerseyMethodEnum;
import org.tesis.xc.util.AnalyzerException;
import org.tesis.xc.webservices.jersey.ServiceStudentEnum;
import org.tesis.xs.entity.StudentEntity;
import org.tesis.xs.entity.full.StudentFullEntity;
import org.tesis.xs.exception.BasicException;
import org.tesis.xs.exception.ExceptionEntity;
import org.tesis.xs.exception.MasterExceptionEnum;
import org.tesis.xs.serv.StudentDao;

import jakarta.ws.rs.core.Response.Status;

@SuppressWarnings("serial")
public class StudentDaoImp implements StudentDao, Serializable{

	@Override
	public StudentFullEntity initialData() throws BasicException {
		//HashMap<String, String> param = new HashMap<>();

        try (JerseyClient service = new JerseyClient(JerseyMethodEnum.GET, ServiceStudentEnum.initialData)) {
            if (service.execute()) 
                return service.getResponce(StudentFullEntity.class);
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
	public StudentEntity createStudent(StudentEntity entity) throws BasicException {

        try (JerseyClient service = new JerseyClient(JerseyMethodEnum.POST, ServiceStudentEnum.createStudent)) {
            if (service.execute(entity)) 
                return service.getResponce(StudentEntity.class);
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
	public StudentEntity updateStudent(StudentEntity entity) throws BasicException {
		
	        try (JerseyClient service = new JerseyClient(JerseyMethodEnum.PUT, ServiceStudentEnum.updateStudent)) {
	            if (service.execute(entity)) 
	                return service.getResponce(StudentEntity.class);
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
	public StudentEntity getStudentById(int id) throws BasicException {
		HashMap<String, String> param = new HashMap<>();
		param.put("id", String.valueOf(id));
        try (JerseyClient service = new JerseyClient(JerseyMethodEnum.GET, ServiceStudentEnum.getStudentById,param)) {
            if (service.execute()) 
                return service.getResponce(StudentEntity.class);
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
    public void deleteStudent(int id) throws BasicException {

        HashMap<String, String> param = new HashMap<>();
        param.put("id", String.valueOf(id));

        try (JerseyClient service = new JerseyClient(JerseyMethodEnum.DELETE, ServiceStudentEnum.deleteStudent, param)) {
            
            if (!service.execute()) 
                if (service.getResponceCode() == Status.FORBIDDEN.getStatusCode()) 
    				throw MasterExceptionEnum.exception(service.getResponce(ExceptionEntity.class).getCode());
    			else
    				throw (BasicException) AnalyzerException.analyzer(service.getResponceCode());       
            
        } catch (BasicException e) {
            throw e;
        } catch (Throwable e) {
            throw new BasicException("Error en llamado a servicio de eliminación de estudiantes",e);
        }
       
    }
	
}
