package org.tesis.xc.webservices.jersey;

import org.tesis.xc.jakarta.net.rest.jersey.client.JerseyServiceInterface;

public enum ServiceStudentEnum implements JerseyServiceInterface {

    initialData("initialData"),
    getStudentById("getStudentById"),
    createStudent("createStudent"),
    updateStudent("updateStudent"),
    deleteStudent("deleteStudent")
	;
	
    private String controllerPath = "student";
    private String methodPath;

    @Override
    public String getService() {
        return controllerPath+"/"+methodPath;
    }

    private ServiceStudentEnum(String methodPath) {
        this.methodPath = methodPath;
    }
	
}
