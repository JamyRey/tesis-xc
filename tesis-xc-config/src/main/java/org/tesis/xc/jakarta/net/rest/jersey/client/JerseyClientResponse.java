package org.tesis.xc.jakarta.net.rest.jersey.client;

import java.util.HashMap;

import org.tesis.xs.exception.Analyzer;
import org.tesis.xs.exception.ConexionFailedException;
import org.tesis.xs.exception.ConexionNotFoundException;
import org.tesis.xs.exception.ErrorCode;
import org.tesis.xs.exception.ErrorResponse;

import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.Response.Status;

public abstract class JerseyClientResponse extends JerseyClientGenery {

    private boolean        isError = false;
    private ErrorResponse errorResponse;

    /* ---[ Constructores ]--- */
    public JerseyClientResponse(JerseyMethodEnum method, JerseyServiceInterface service,
            HashMap<String, String> parametersQuery, MultivaluedHashMap<String, Object> parametersHeader,
            String urlExternal)
    {
        super(method, service, parametersQuery, parametersHeader, urlExternal);
    }

    public JerseyClientResponse(JerseyMethodEnum method, JerseyServiceInterface service,
            HashMap<String, String> parametersQuery, MultivaluedHashMap<String, Object> parametersHeader)
    {
        super(method, service, parametersQuery, parametersHeader);
    }

    public JerseyClientResponse(JerseyMethodEnum method, JerseyServiceInterface service,
            HashMap<String, String> parametersQuery, String urlExternal)
    {
        super(method, service, parametersQuery, urlExternal);
    }

    public JerseyClientResponse(JerseyMethodEnum method, JerseyServiceInterface service,
            HashMap<String, String> parametersQuery)
    {
        super(method, service, parametersQuery);
    }

    public JerseyClientResponse(JerseyMethodEnum method, JerseyServiceInterface service,
            MultivaluedHashMap<String, Object> parametersHeader, String urlExternal)
    {
        super(method, service, parametersHeader, urlExternal);
    }

    public JerseyClientResponse(JerseyMethodEnum method, JerseyServiceInterface service,
            MultivaluedHashMap<String, Object> parametersHeader)
    {
        super(method, service, parametersHeader);
    }

    public JerseyClientResponse(JerseyMethodEnum method, JerseyServiceInterface service, String urlExternal) {
        super(method, service, urlExternal);
    }

    public JerseyClientResponse(JerseyMethodEnum method, JerseyServiceInterface service) {
        super(method, service);
    }
    /* ---[ Constructores ]--- */

    @Override
    public boolean execute() throws ConexionFailedException, ConexionNotFoundException  {
        isError = false;
        return execute(null);
    }

    @Override
    public <T> boolean execute(T objectSend) throws ConexionFailedException, ConexionNotFoundException  {
        try {
            isError = false;
            return super.execute(objectSend);
        }
        catch (Throwable e) {
            isError = true;
            errorResponse = Analyzer.analyzeForResponse(e);
            ErrorCode code = ErrorCode.fromCode(errorResponse.getCode());

            switch (code) {
            case Unknown:
                responceCode = Status.INTERNAL_SERVER_ERROR.getStatusCode();
                break;

            default:
                responceCode = Status.BAD_REQUEST.getStatusCode();
                break;
            }

            return false;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getResponce(Class<T> entityType) {
        if (!isError)
            return super.getResponce(entityType);
        else
            return (T) errorResponse;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getResponce(GenericType<T> entityType) {
        if (!isError)
            return super.getResponce(entityType);
        else
            return (T) errorResponse;
    }
}
