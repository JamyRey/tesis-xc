package org.tesis.xc.manager.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.tesis.util.jakarta.controller.FacesMessageUtil;
import org.tesis.xc.manager.imp.StudentDaoImp;
import org.tesis.xc.util.AnalyzerException;
import org.tesis.xs.entity.ClassEntity;
import org.tesis.xs.entity.GameSession;
import org.tesis.xs.entity.StudentEntity;
import org.tesis.xs.entity.full.StudentFullEntity;
import org.tesis.xs.exception.MasterException;
import org.tesis.xs.serv.StudentDao;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class StudentController implements Serializable {

	private Logger log = LogManager.getLogger(this.getClass());
	
	private StudentDao dao = new  StudentDaoImp();
	
	private StudentEntity item;

	private List<StudentEntity> students;
	private List<ClassEntity> classes;
	private List<GameSession> gameSessionsDisplay;	
	
	private String noMatch = "No se encontraron resultados.";
	
	@PostConstruct
	public void init() {
		
		try{
			
			StudentFullEntity result = this.dao.initialData();
			
			students = result.getStudents();
			classes = result.getClasses();
			
			
			initNewStudent();
		} catch (Throwable e) {
			AnalyzerException.analyzer(this, e);
		}
		
	}
	
	public void initNewStudent() {
		
		item = new StudentEntity();
		item.setClasses(new ArrayList<>());
	}
	
	public void getStudentById(int id) {
		
		try {
			
			item = dao.getStudentById(id);
			
		} catch (Throwable e) {
			AnalyzerException.analyzer(this, e);
		}
		
	}
	
	public void save() {

		try {

			if(item.getStatus()==0) {
				item.setStatus(1);
				dao.createStudent(item);
			}			
			else {
				dao.updateStudent(item);
				StudentEntity temp = 
						students.stream().filter(c -> c.getId() == item.getId()).findAny().orElseThrow();
				students.set(students.indexOf(temp),temp);		
			}
			PrimeFaces.current().executeScript("PF('crudDialog').hide();");
    	    FacesMessageUtil.generateInfoMessage("Cambios guardados satisfactoriamente.");
		}catch (MasterException e) {    	    	
    			FacesMessageUtil.generateErrorMessage(e.getEntity().getMessage());
    			log.info(e.getMessage());
            
		} catch (Throwable e) {
			
		}
	}
	
	public void getScoresById(int id) {
		
		try {
			gameSessionsDisplay = item.getSessions().stream().filter(g -> g.getClassE().getId() == id).collect(Collectors.toList());
			Collections.sort(gameSessionsDisplay, (o1, o2) -> o1.getStartAt().compareTo(o2.getStartAt()));
			//item = dao.getStudentById(id);
			
		} catch (Throwable e) {
			AnalyzerException.analyzer(this, e);
		}
		
	}
	
	public StudentEntity getItem() {
		return item;
	}

	public void setItem(StudentEntity item) {
		this.item = item;
	}

	public List<StudentEntity> getStudents() {
		return students;
	}

	public void setStudents(List<StudentEntity> students) {
		this.students = students;
	}

	public String getNoMatch() {
		return noMatch;
	}

	public void setNoMatch(String noMatch) {
		this.noMatch = noMatch;
	}

	public List<ClassEntity> getClasses() {
		return classes;
	}

	public void setClasses(List<ClassEntity> classes) {
		this.classes = classes;
	}

	public List<GameSession> getGameSessionsDisplay() {
		return gameSessionsDisplay;
	}

	public void setGameSessionsDisplay(List<GameSession> gameSessionsDisplay) {
		this.gameSessionsDisplay = gameSessionsDisplay;
	}
	
}
