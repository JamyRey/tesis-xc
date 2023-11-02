package org.tesis.xc.config.jersey;

import java.net.URI;
import java.util.HashMap;

import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MultivaluedHashMap;

import org.tesis.util.net.URIUtil;
import org.tesis.xc.config.PropertiesEnum;
import org.tesis.xc.jakarta.net.rest.jersey.client.JerseyClientGenery;
import org.tesis.xc.jakarta.net.rest.jersey.client.JerseyMethodEnum;
import org.tesis.xc.jakarta.net.rest.jersey.client.JerseyServiceInterface;


public class JerseyClientToken extends JerseyClientGenery implements AutoCloseable {

	public JerseyClientToken(JerseyMethodEnum method, JerseyServiceInterface service) {
		super(method, service);
	}

	
	public JerseyClientToken(JerseyMethodEnum method,
			                 JerseyServiceInterface service,
			                 HashMap<String, String> parameters)
	{
		super(method, service, parameters);
	}

	@Override
	protected URI serverURI() {
		return URIUtil.createURI(PropertiesEnum.SERVER_URL.getContent());
	}

	@Override
	protected void sendParametersHeader() {
		if (TokenUtil.getToken()!=null){
			setParametersHeader(new MultivaluedHashMap<>());
			getParametersHeader().add(HttpHeaders.AUTHORIZATION, TokenUtil.getToken());
		}
		
	}
}
