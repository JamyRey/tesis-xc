package org.tesis.xc.webservices.jersey;

import org.tesis.xc.jakarta.net.rest.jersey.client.JerseyServiceInterface;

public enum ServiceClassEnum implements JerseyServiceInterface {

    initialData("initialData"),
    getClassById("getClassById"),
    createClass("createClass"),
    updateClass("updateClass"),
    updateActiveClass("updateActiveClass"),
	;
	
    private String controllerPath = "class";
    private String methodPath;

    @Override
    public String getService() {
        return controllerPath+"/"+methodPath;
    }

    private ServiceClassEnum(String methodPath) {
        this.methodPath = methodPath;
    }
	
}
