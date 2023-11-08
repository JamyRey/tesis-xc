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
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearTicks;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.hbar.HorizontalBarChartDataSet;
import org.primefaces.model.charts.hbar.HorizontalBarChartModel;
import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.line.LineChartModel;
import org.primefaces.model.charts.line.LineChartOptions;
import org.primefaces.model.charts.optionconfig.title.Title;
import org.tesis.util.jakarta.controller.FacesMessageUtil;
import org.tesis.xc.manager.imp.ClassDaoImp;
import org.tesis.xc.manager.imp.StudentDaoImp;
import org.tesis.xc.util.AnalyzerException;
import org.tesis.xc.util.SessionUtil;
import org.tesis.xs.entity.BasicEntity;
import org.tesis.xs.entity.ClassEntity;
import org.tesis.xs.entity.GameSession;
import org.tesis.xs.entity.ScoreValues;
import org.tesis.xs.entity.StudentEntity;
import org.tesis.xs.entity.full.ClassFullEntity;
import org.tesis.xs.enums.ScoresTypes;
import org.tesis.xs.exception.BasicException;
import org.tesis.xs.exception.MasterException;
import org.tesis.xs.serv.ClassDao;
import org.tesis.xs.serv.StudentDao;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class ClassController implements Serializable {

	private Logger log = LogManager.getLogger(this.getClass());
	
	private ClassDao dao = new  ClassDaoImp();
	private StudentDao studentDao = new  StudentDaoImp();
	private ClassEntity item;
	private StudentEntity student;

	private List<ClassEntity> classes;
	private List<GameSession> gameSessionsDisplay;
	private List<ScoreValues> allScores = new ArrayList<>();
	private List<BasicEntity> games;
	private LineChartModel lineModel;
	private HorizontalBarChartModel hbarModel;
	private int r = 50;
	private int g = 100;
	private int b = 175;
	
	
	private String noMatch = "No se encontraron resultados.";
	
	@PostConstruct
	public void init() {
		
		try{
			
			ClassFullEntity result = this.dao.initialData();
			
			classes = result.getClasses();
			games = result.getGames();
			lineModel = new LineChartModel();
			hbarModel = new HorizontalBarChartModel();
			initNewClass();
			createLineModel();

		} catch (Throwable e) {
			AnalyzerException.analyzer(this, e);
		}
		
	}
	
	public void initNewClass() {
		
		item = new ClassEntity();
		item.setStudents(new ArrayList<>());
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
			
			ClassFullEntity result = this.dao.initialData();
			
			classes = result.getClasses();
			PrimeFaces.current().executeScript("PF('crudDialog').hide();");
    	    FacesMessageUtil.generateInfoMessage("Cambios guardados satisfactoriamente.");
		}catch (MasterException e) {
			if(item.getId()==0)
				item.setStatus(0);
    			FacesMessageUtil.generateErrorMessage(e.getEntity().getMessage());
    			log.info(e.getMessage());            
		} catch (Throwable e) {
			
		}
	}
	
	public void delete() throws BasicException {
		
		try {
			
			dao.deleteClass(item.getId());
			
			ClassEntity classAux =
					classes.stream().filter(c -> c.getId() == item.getId())
                    .findAny().orElseThrow();
            int classIndex = classes.indexOf(classAux);
            classes.remove(classIndex);
            PrimeFaces.current().executeScript("PF('crudDialog').hide();");
            FacesMessageUtil.generateInfoMessage("Cambios guardados satisfactoriamente.");
			
		}catch (BasicException e) {
            AnalyzerException.analyzer(this, e);
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
	
	
	public void getScoresById(int id) {
		
		try {
			student = studentDao.getStudentById(id);
			gameSessionsDisplay = student.getSessions().stream().filter(g -> g.getClassE().getId() == item.getId()).collect(Collectors.toList());
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
        if(gameSessionsDisplay==null) {
        	gameSessionsDisplay = new ArrayList<>();
        }
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
        
        createHbarModel();
        
        
        
    }
	
	public void createHbarModel() {
		r = 50;
        g = 100;
        b = 175;
        updateRGB(r, g, b);
		hbarModel = new HorizontalBarChartModel();
		ChartData data = new ChartData();
		HorizontalBarChartDataSet hbarDataSet = new HorizontalBarChartDataSet();
		hbarDataSet.setLabel("Nota promedio");
		List<Number> values = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        List<String> bgColor = new ArrayList<>();
        List<String> borderColor = new ArrayList<>();
        Collections.sort(allScores,(o1, o2) -> Double.compare(o2.getFinalScore(),o1.getFinalScore()));
		for (ScoreValues item : allScores) {
			 updateRGB(r, g, b);
			 values.add(item.getFinalScore());
        	 labels.add(item.getType().getName());
        	 bgColor.add("rgb("+(r)+", "+(g)+", "+(b)+")");
        	 borderColor.add("rgb(0, 0, 0)");
		}
		hbarDataSet.setData(values);
		hbarDataSet.setBackgroundColor(bgColor);
		hbarDataSet.setBorderColor(bgColor);
		hbarDataSet.setHoverBackgroundColor(bgColor);
		hbarDataSet.setHoverBorderColor(borderColor);
		hbarDataSet.setBorderWidth(1);
		hbarDataSet.setHoverBorderWidth(2);
		data.addChartDataSet(hbarDataSet);
		data.setLabels(labels);
		hbarModel.setData(data);
		//Options
        BarChartOptions options = new BarChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        linearAxes.setOffset(true);
        CartesianLinearTicks ticks = new CartesianLinearTicks();
        linearAxes.setTicks(ticks);
        cScales.addXAxesData(linearAxes);
        options.setScales(cScales);

        Title title = new Title();
        title.setDisplay(true);
        title.setText("Notas promedio sobre 20 por tipo de puntaje ");
        options.setTitle(title);

        hbarModel.setOptions(options);
		
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

	public List<GameSession> getGameSessionsDisplay() {
		return gameSessionsDisplay;
	}

	public void setGameSessionsDisplay(List<GameSession> gameSessionsDisplay) {
		this.gameSessionsDisplay = gameSessionsDisplay;
	}

	public List<ScoreValues> getAllScores() {
		return allScores;
	}

	public void setAllScores(List<ScoreValues> allScores) {
		this.allScores = allScores;
	}

	public LineChartModel getLineModel() {
		return lineModel;
	}

	public void setLineModel(LineChartModel lineModel) {
		this.lineModel = lineModel;
	}

	public HorizontalBarChartModel getHbarModel() {
		return hbarModel;
	}

	public void setHbarModel(HorizontalBarChartModel hbarModel) {
		this.hbarModel = hbarModel;
	}

	public int getR() {
		return r;
	}

	public void setR(int r) {
		this.r = r;
	}

	public int getG() {
		return g;
	}

	public void setG(int g) {
		this.g = g;
	}

	public int getB() {
		return b;
	}

	public void setB(int b) {
		this.b = b;
	}

	public StudentEntity getStudent() {
		return student;
	}

	public void setStudent(StudentEntity student) {
		this.student = student;
	}

	public List<BasicEntity> getGames() {
		return games;
	}

	public void setGames(List<BasicEntity> games) {
		this.games = games;
	}
	
}
