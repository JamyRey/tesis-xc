package org.tesis.xc.webservices.jersey;

import org.tesis.xc.jakarta.net.rest.jersey.client.JerseyServiceInterface;

public enum ServiceSessionEnum implements JerseyServiceInterface {
	
    login("login"),
	logout("logout")
	;

    private String controllerPath = "session";
    private String methodPath;

    @Override
    public String getService() {
        return controllerPath+"/"+methodPath;
    }

    private ServiceSessionEnum(String methodPath) {
        this.methodPath = methodPath;
    }
    
}
