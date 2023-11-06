package org.tesis.xc.manager.controller;

import java.io.Serializable;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.tesis.util.jakarta.controller.FacesMessageUtil;
import org.tesis.xc.manager.imp.ClassDaoImp;
import org.tesis.xc.util.AnalyzerException;
import org.tesis.xs.entity.ClassEntity;
import org.tesis.xs.entity.full.ClassFullEntity;
import org.tesis.xs.exception.MasterException;
import org.tesis.xs.serv.ClassDao;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class ClassController implements Serializable {

	private Logger log = LogManager.getLogger(this.getClass());
	
	private ClassDao dao = new  ClassDaoImp();
	
	private ClassEntity item;

	private List<ClassEntity> classes;
	
	
	private String noMatch = "No se encontraron resultados.";
	
	@PostConstruct
	public void init() {
		
		try{
			
			ClassFullEntity result = this.dao.initialData();
			
			classes = result.getClasses();

		} catch (Throwable e) {
			AnalyzerException.analyzer(this, e);
		}
		
	}
	
	public void initNewClass() {
		
		item = new ClassEntity();
	}
	
	public void getClassById(int id) {
		
		try {
			
			item = dao.getClassById(id);
			
		} catch (Throwable e) {
			AnalyzerException.analyzer(this, e);
		}
		
	}
	
	public void save() {

		try {
			if(item.getStatus()==0) {
				item.setStatus(1);
				dao.createClass(item);
				classes.add(item);
			}			
			else {
				dao.updateClass(item);
				ClassEntity temp = 
						classes.stream().filter(c -> c.getId() == item.getId()).findAny().orElseThrow();
				classes.set(classes.indexOf(temp),temp);				
			}
			PrimeFaces.current().executeScript("PF('crudDialog').hide();");
    	    FacesMessageUtil.generateInfoMessage("Cambios guardados satisfactoriamente.");
		}catch (MasterException e) {    	    	
    			FacesMessageUtil.generateErrorMessage(e.getEntity().getMessage());
    			log.info(e.getMessage());            
		} catch (Throwable e) {
			
		}
	}
	
	public void activeClass(int id) {
		
		try {
			
			ClassEntity tempItem = classes.stream().filter(c->c.getId()==id).findFirst().orElse(null);
			if(tempItem==null) {
				return;
			}
			dao.updateActiveClass(tempItem);
			
			ClassFullEntity result = this.dao.initialData();
			
			classes = result.getClasses();
			
		} catch (Throwable e) {
			AnalyzerException.analyzer(this, e);
		}
	}
	
	public ClassEntity getItem() {
		return item;
	}

	public void setItem(ClassEntity item) {
		this.item = item;
	}

	public List<ClassEntity> getClasses() {
		return classes;
	}

	public void setClasses(List<ClassEntity> classes) {
		this.classes = classes;
	}

	public String getNoMatch() {
		return noMatch;
	}

	public void setNoMatch(String noMatch) {
		this.noMatch = noMatch;
	}
	
}
