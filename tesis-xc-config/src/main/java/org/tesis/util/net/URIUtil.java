package org.tesis.util.net;

import java.net.URI;

import jakarta.ws.rs.core.UriBuilder;

public class URIUtil {

	public static URI createURI (String uri){
		return UriBuilder.fromUri(uri).build();
	}
	
}
