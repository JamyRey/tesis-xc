package org.tesis.xc.config.jersey;

import java.net.URI;
import java.util.HashMap;

import org.tesis.util.net.URIUtil;
import org.tesis.xc.config.PropertiesEnum;
import org.tesis.xc.jakarta.net.rest.jersey.client.JerseyClientGenery;
import org.tesis.xc.jakarta.net.rest.jersey.client.JerseyMethodEnum;
import org.tesis.xc.jakarta.net.rest.jersey.client.JerseyServiceInterface;


public class JerseyClient extends JerseyClientGenery implements AutoCloseable {

	public JerseyClient(JerseyMethodEnum method,
			JerseyServiceInterface service) {
		super(method, service);
	}


	public JerseyClient(JerseyMethodEnum method,
			JerseyServiceInterface service, HashMap<String, String> parameters) {
		super(method, service, parameters);
	}

	@Override
	protected URI serverURI() {
		return URIUtil.createURI(PropertiesEnum.SERVER_URL.getContent());
	}
}
