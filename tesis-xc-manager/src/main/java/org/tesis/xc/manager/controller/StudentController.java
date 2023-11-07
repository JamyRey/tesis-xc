package org.tesis.xc.manager.controller;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.PrimeFaces;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.line.LineChartModel;
import org.primefaces.model.charts.line.LineChartOptions;
import org.primefaces.model.charts.optionconfig.title.Title;
import org.tesis.util.jakarta.controller.FacesMessageUtil;
import org.tesis.xc.manager.imp.StudentDaoImp;
import org.tesis.xc.util.AnalyzerException;
import org.tesis.xc.util.SessionUtil;
import org.tesis.xs.entity.ClassEntity;
import org.tesis.xs.entity.GameSession;
import org.tesis.xs.entity.ScoreValues;
import org.tesis.xs.entity.StudentEntity;
import org.tesis.xs.entity.full.StudentFullEntity;
import org.tesis.xs.enums.ScoresTypes;
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
	private List<ScoreValues> allScores = new ArrayList<>();
	private LineChartModel lineModel;
	private String noMatch = "No se encontraron resultados.";
	private int r = 50;
	private int g = 100;
	private int b = 175;
	
	@PostConstruct
	public void init() {
		
		try{
			
			StudentFullEntity result = this.dao.initialData();
			
			students = result.getStudents();
			classes = result.getClasses();
			
			
			initNewStudent();
			lineModel = new LineChartModel();
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
			createLineModel();
		} catch (Throwable e) {
			AnalyzerException.analyzer(this, e);
		}
		
	}
	
	public void createLineModel() {
		r = 50;
        g = 100;
        b = 175;
        lineModel = new LineChartModel();
        ChartData data = new ChartData();
        
        DateTimeFormatter dtFormatter = 
                DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                .withLocale(SessionUtil.getLocale());

        LineChartDataSet dataSet = new LineChartDataSet();
        List<Object> values = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        allScores = new ArrayList<>();
        Map<ScoresTypes,List<Object>> valueByTypes = new HashMap<ScoresTypes,List<Object>>();
        for (GameSession item : gameSessionsDisplay) {
        	 values.add(item.getAllScoresValue());
        	 labels.add(item.getStartAt().format(dtFormatter).toString());
        	 
        	 List<ScoreValues>  temp = item.getAllScores();
        	 
        	 for (ScoreValues tempS : temp) {
        		 
        		 if(allScores.stream().anyMatch(s->s.getType()==tempS.getType())) {
      				
      				ScoreValues aux = allScores.stream().filter(s->s.getType()==tempS.getType()).findFirst().get();
      				
      				List<Object> i = valueByTypes.get(tempS.getType());
      				i.add(tempS.getFinalScore());
      				valueByTypes.put(tempS.getType(),i);
      				aux.sumScores(tempS); 
      				
      			}else {
      				List<Object> tempD = new ArrayList<Object>();
      				tempD.add(tempS.getFinalScore());
      				valueByTypes.put(tempS.getType(), tempD);
      				allScores.add(tempS);
      			}
			}
		}
        
        for (var entry : valueByTypes.entrySet()) {
        	
        	 LineChartDataSet chart = new LineChartDataSet();
        	 List<Object> temps = entry.getValue();
        	 temps.add(0);
        	 chart.setData(temps);
        	 chart.setFill(false);
        	 ScoreValues temp = allScores.stream().filter(s->s.getType()==entry.getKey()).findFirst().get();
        	 chart.setLabel(entry.getKey().getName()+"("+temp.getValid()+"/"+(temp.getValid()+temp.getInvalid ())+")");
        	 updateRGB(r, g, b);
        	 chart.setBorderColor("rgb("+(r)+", "+(g)+", "+(b)+")");
        	 chart.setTension(0.1);   
             data.addChartDataSet(chart);
		}
        values.add(0);
        dataSet.setData(values);
        dataSet.setFill(false);
        dataSet.setLabel("Media General");
        updateRGB(r, g, b);
        dataSet.setBorderColor("rgb("+(r)+", "+(g)+", "+(b)+")");
        dataSet.setTension(0.1);
        data.addChartDataSet(dataSet);    
        data.setLabels(labels);
        //Options
        LineChartOptions options = new LineChartOptions();
       // options.setMaintainAspectRatio(false);
        Title title = new Title();
        title.setDisplay(true);
        title.setText("Notas promedio sobre 20 por sesión de juego");
        options.setTitle(title);
        lineModel.setOptions(options);
        lineModel.setData(data);
    }
	
	private void updateRGB(int r, int g, int b) {
		
	 this.r = r+35;
   	 if(r>255)
   		this.r=0;
   	this.g = g+45;
   	 if(g>255)
   		this.g=0;
   	this.b = b+50;
   	 if(b>255)
   		this.b=0;
		
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
	
    public LineChartModel getLineModel() {
        return lineModel;
    }

    public void setLineModel(LineChartModel lineModel) {
        this.lineModel = lineModel;
    }

	public List<ScoreValues> getAllScores() {
		return allScores;
	}

	public void setAllScores(List<ScoreValues> allScores) {
		this.allScores = allScores;
	}
	
}
