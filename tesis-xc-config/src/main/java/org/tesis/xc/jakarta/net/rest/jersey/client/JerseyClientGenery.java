package org.tesis.xc.jakarta.net.rest.jersey.client;

import java.net.HttpURLConnection;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tesis.xs.exception.ConexionFailedException;
import org.tesis.xs.exception.ConexionNotFoundException;
import org.tesis.util.net.URIUtil;

public abstract class JerseyClientGenery {
	protected static Logger log = LoggerFactory.getLogger(JerseyClientGenery.class);

	//---------------------------
	private JerseyMethodEnum method;
	private HashMap<String, String> parametersQuery;
	private MultivaluedHashMap<String, Object> parametersHeader;
	private JerseyServiceInterface service = null;
	private String urlExternal = null;
	private MediaType mediaType = MediaType.APPLICATION_JSON_TYPE;

	protected Client    client = ClientBuilder.newClient(new ClientConfig());
	protected WebTarget target;
	protected Response  response = null;
	protected int       responceCode;

	//-------------------
	//Constructores con url externas
	//-------------------
	
	public JerseyClientGenery(JerseyMethodEnum method, JerseyServiceInterface service, String urlExternal) {
		this.method=method;
		target = client.target(URIUtil.createURI(urlExternal)).path(service.getService());
		this.service = service;
		this.urlExternal = urlExternal;
	}
	
	public JerseyClientGenery(JerseyMethodEnum method, JerseyServiceInterface service, String urlExternal, MediaType mediaType) {
		this(method, service, urlExternal);
		this.mediaType = mediaType;
	}

	public JerseyClientGenery(JerseyMethodEnum method, JerseyServiceInterface service, MultivaluedHashMap<String, Object> parametersHeader, String urlExternal) {
		this(method, service, urlExternal);
		this.parametersHeader=parametersHeader;
	}
	
	public JerseyClientGenery(JerseyMethodEnum method, JerseyServiceInterface service, MultivaluedHashMap<String, Object> parametersHeader, String urlExternal, MediaType mediaType) {
		this(method, service, urlExternal);
		this.parametersHeader=parametersHeader;
		this.mediaType = mediaType;
	}

	public JerseyClientGenery(JerseyMethodEnum method, JerseyServiceInterface service, HashMap<String, String> parametersQuery, String urlExternal) {
		this(method, service, urlExternal);
		this.parametersQuery=parametersQuery;
	}
	
	public JerseyClientGenery(JerseyMethodEnum method, JerseyServiceInterface service, HashMap<String, String> parametersQuery, String urlExternal, MediaType mediaType) {
		this(method, service, urlExternal);
		this.parametersQuery=parametersQuery;
		this.mediaType = mediaType;
	}
	
	public JerseyClientGenery(JerseyMethodEnum method, JerseyServiceInterface service, HashMap<String, String> parametersQuery, MultivaluedHashMap<String, Object> parametersHeader, String urlExternal) {
		this(method, service, urlExternal);
		this.parametersQuery=parametersQuery;
		this.parametersHeader=parametersHeader;
	}
	
	public JerseyClientGenery(JerseyMethodEnum method, JerseyServiceInterface service, HashMap<String, String> parametersQuery, MultivaluedHashMap<String, Object> parametersHeader, String urlExternal, MediaType mediaType) {
		this(method, service, urlExternal);
		this.parametersQuery=parametersQuery;
		this.parametersHeader=parametersHeader;
		this.mediaType = mediaType;
	}
	
	//-------------------
	//Constructores con url externas sin path de servicio
	//-------------------
	
	public JerseyClientGenery(JerseyMethodEnum method, String urlExternal) {
		this.method=method;
		target = client.target(URIUtil.createURI(urlExternal));
		this.urlExternal = urlExternal;
	}
	
	public JerseyClientGenery(JerseyMethodEnum method, String urlExternal, MediaType mediaType) {
		this(method, urlExternal);
		this.mediaType = mediaType;
	}
	
	public JerseyClientGenery(JerseyMethodEnum method, MultivaluedHashMap<String, Object> parametersHeader, String urlExternal) {
		this(method, urlExternal);
		this.parametersHeader=parametersHeader;
	}
	
	public JerseyClientGenery(JerseyMethodEnum method, MultivaluedHashMap<String, Object> parametersHeader, String urlExternal, MediaType mediaType) {
		this(method, urlExternal);
		this.parametersHeader=parametersHeader;
		this.mediaType = mediaType;
	}
	
	public JerseyClientGenery(JerseyMethodEnum method, HashMap<String, String> parametersQuery, String urlExternal) {
		this(method, urlExternal);
		this.parametersQuery=parametersQuery;
	}
	
	public JerseyClientGenery(JerseyMethodEnum method, HashMap<String, String> parametersQuery, String urlExternal, MediaType mediaType) {
		this(method, urlExternal);
		this.parametersQuery=parametersQuery;
		this.mediaType = mediaType;
	}
	
	public JerseyClientGenery(JerseyMethodEnum method, HashMap<String, String> parametersQuery, MultivaluedHashMap<String, Object> parametersHeader, String urlExternal) {
		this(method, urlExternal);
		this.parametersQuery=parametersQuery;
		this.parametersHeader=parametersHeader;
	}
	
	public JerseyClientGenery(JerseyMethodEnum method, HashMap<String, String> parametersQuery, MultivaluedHashMap<String, Object> parametersHeader, String urlExternal, MediaType mediaType) {
		this(method, urlExternal);
		this.parametersQuery=parametersQuery;
		this.parametersHeader=parametersHeader;
		this.mediaType = mediaType;
	}
	
	//-------------------
	//Constructores con url interna
	//-------------------

	public JerseyClientGenery(JerseyMethodEnum method, JerseyServiceInterface service) {
		this.method=method;
		target = client.target(serverURI()).path(service.getService());
		this.service = service;
	}

	public JerseyClientGenery(JerseyMethodEnum method, JerseyServiceInterface service, MultivaluedHashMap<String, Object> parametersHeader) {
		this(method, service);
		this.parametersHeader=parametersHeader;
	}

	public JerseyClientGenery(JerseyMethodEnum method, JerseyServiceInterface service, HashMap<String, String> parametersQuery) {
		this(method, service);
		this.parametersQuery=parametersQuery;

	}
	public JerseyClientGenery(JerseyMethodEnum method, JerseyServiceInterface service, HashMap<String, String> parametersQuery, MultivaluedHashMap<String, Object> parametersHeader) {
		this(method, service);
		this.parametersQuery=parametersQuery;
		this.parametersHeader=parametersHeader;
	}

	//-------------------
	
	public boolean execute() throws ConexionFailedException, ConexionNotFoundException, ConexionFailedException{
		return execute(null);
	}

	public void doTrustToCertificates() throws Exception {
		//-------------------------------------------------------------------
		log.debug("Verificando Certificados SSL");
		//-------------------------------------------------------------------
		try {
			Security.addProvider(SSLContext.getDefault().getProvider());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
        //-------------------------------------------------------------------
        TrustManager[] trustAllCerts = new TrustManager[]{
             new X509TrustManager() {
                 @Override
                public X509Certificate[] getAcceptedIssuers() {
                 return null;
             }

             @Override
            public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                 return;
             }

             @Override
            public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                 return;
             }
           }
        };
        //--------------------------------------------------------
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HostnameVerifier hv = new HostnameVerifier() {
            @Override
            public boolean verify(String urlHostName, SSLSession session) {
                if (!urlHostName.equalsIgnoreCase(session.getPeerHost())) {
                    System.out.println("Warning: URL host '" + urlHostName + "' is different to SSLSession host '" + session.getPeerHost() + "'.");
                }
                return true;
            }
        };
        //-----------------------------------------------------------
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
    }

	public <T> boolean execute(T objectSend) throws ConexionFailedException, ConexionNotFoundException {

		try {
			log.debug("execute!!!");
			//-------------
			this.doTrustToCertificates();//Nuevo Banesco
			//-------------
			prepareParametersQuery();
			sendParametersHeader();
			switch (method){
				case GET:

					if (parametersHeader!=null && parametersHeader.size()>0)
						response=target.request(mediaType).headers(parametersHeader).get();
					else
						response=target.request(mediaType).get();

					break;

				case POST:
					if (parametersHeader!=null && parametersHeader.size()>0)
						response=target.request(mediaType).headers(parametersHeader).post(Entity.entity(objectSend, MediaType.APPLICATION_JSON));
					else
						response=target.request(mediaType).post(Entity.entity(objectSend, MediaType.APPLICATION_JSON));

					break;

				case PUT:
					if (parametersHeader!=null && parametersHeader.size()>0)
						response=target.request(mediaType).headers(parametersHeader).put(Entity.entity(objectSend, MediaType.APPLICATION_JSON));
					else
						response=target.request(mediaType).put(Entity.entity(objectSend, MediaType.APPLICATION_JSON));

					break;

				case DELETE:
					if (parametersHeader!=null && parametersHeader.size()>0)
						response=target.request(mediaType).headers(parametersHeader).delete();
					else
						response=target.request(mediaType).delete();

					break;
				case DELETE_BODY:
					if(this.urlExternal == null){
						//log.debug("OBJETO --> " + objectSend);
						//Nuevo banesco
						//---------------------------
						ClientConfig clientConfig = new ClientConfig();
						clientConfig.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);
						Client client = ClientBuilder.newClient(clientConfig);
						//---------------------------
						response = client.target(serverURI()).path(service.getService())
			                .request(mediaType)
			                .build("DELETE", Entity.entity(objectSend, MediaType.APPLICATION_JSON))
			                .invoke();
						//---------------------------
						clientConfig = null;
						client = null;
						service = null;
						//------------------------------------------------------
					}else{
						//log.debug("OBJETO --> " + objectSend);
						//Nuevo banesco
						//---------------------------
						ClientConfig clientConfig = new ClientConfig();
						clientConfig.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);
						Client client = ClientBuilder.newClient(clientConfig);
						//---------------------------
						response = client.target(this.urlExternal).path(service.getService())
			                .request(mediaType)
			                .build("DELETE", Entity.entity(objectSend, MediaType.APPLICATION_JSON))
			                .invoke();
						//---------------------------
						clientConfig = null;
						client = null;
						service = null;
						//------------------------------------------------------
					}
					break;
			}
			setResponceCode(response.getStatus());


		} catch (Throwable e) {
			log.debug("ERROR:" + e.getMessage());
			throw new ConexionFailedException("Error: Imposible conectarse al servidor, verifique su Url/IP",e);
		} finally{
			target=null;
			parametersQuery=null;
			parametersHeader=null;
		}
		return successTransactionConnection();
	}

	public boolean successTransactionConnection() throws ConexionNotFoundException {
		/*
		if (getResponceCode()==HttpURLConnection.HTTP_NOT_FOUND){
			log.debug(response.getEntity().toString());
			throw new XConexionNotFoundException("Error: WebService No Encontrado");
		}*/
		return ((getResponceCode()==HttpURLConnection.HTTP_OK) ||
				(getResponceCode()==HttpURLConnection.HTTP_ACCEPTED) ||
				(getResponceCode()==HttpURLConnection.HTTP_CREATED));
	}

	public void close(){
		try {
			if (response!=null) response.close();
			if (client!=null)  client.close();
		} catch (Exception e) {
			log.error("Error: ",e);
		}finally{
			response=null;
			client=null;
		}
	}

	protected abstract URI serverURI();
	//Example: UriBuilder.fromUri(XetuxPropertiesEmun.SERVER_REST1.getContent()).build()

	private void prepareParametersQuery(){
		if (parametersQuery!=null)
			for(String key : parametersQuery.keySet())
				target = target.queryParam(key, parametersQuery.get(key));
	}

	public <T> T getResponce(Class<T> entityType) {
		return response.readEntity(entityType);
	}

	public <T> T getResponce(GenericType<T> entityType) {
		// Example param new GenericType<List<Xetux>>(){})
		return response.readEntity(entityType);
	}

	private void setResponceCode(int responceCode) {
		this.responceCode = responceCode;
	}

	public int getResponceCode() {
		return responceCode;
	}

	protected void sendParametersHeader(){

	}

	public MultivaluedHashMap<String, Object> getParametersHeader() {
		return parametersHeader;
	}

	public void setParametersHeader(
			MultivaluedHashMap<String, Object> parametersHeader) {
		this.parametersHeader = parametersHeader;
	}



}
