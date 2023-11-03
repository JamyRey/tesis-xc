
package org.tesis.util.jakarta.controller;

import java.io.IOException;

import jakarta.el.ELContext;
import jakarta.el.ValueExpression;
import jakarta.faces.FactoryFinder;
import jakarta.faces.application.Application;
import jakarta.faces.application.ApplicationFactory;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.application.FacesMessage.Severity;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.FacesEvent;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class FacesUtil {

	/**
	 *
	 */
	public FacesUtil() {

	}

	/**
	 * Obtener facesContext
	 *
	 * @author
	 * @return
	 */
	public static FacesContext getContext() {
		return FacesContext.getCurrentInstance();
	}

	/**
	 * obtener httprequest desde JSF
	 *
	 * @author
	 * @return
	 */
	public static HttpServletRequest getFacesRequest() {
		return (HttpServletRequest) getContext().getExternalContext()
				.getRequest();
	}

	/**
	 * Obtener HttpResponse desde JSF
	 *
	 * @author
	 * @return
	 */
	public static HttpServletResponse getFacesResponse() {
		return (HttpServletResponse) getContext().getExternalContext()
				.getResponse();
	}

	/**
	 * Obtener la session Http desde JSF
	 *
	 * @author
	 * @return
	 */
	public static HttpSession getFacesSession() {
		return getFacesRequest().getSession();
	}

	public static HttpSession getFacesSession(boolean val) {
		return getFacesRequest().getSession(val);
	}

	/**
	 * Crear un menssage JSF añadiendolo a la fase JSF
	 *
	 * @author Casto Colina - ccolina@eisconsulting.com 12/05/2009 17:39:23
	 * @param clientId
	 * @param severity
	 * @param message
	 * @param detail
	 */
	public static void addFacesMessage(String clientId, Severity severity,
			String message, String detail) {
		getContext().addMessage(clientId,
				new FacesMessage(severity, message, detail));
		getContext().renderResponse();
	}

	public static void completeFaces() {
		getContext().responseComplete();
		getContext().renderResponse();
	}

	/**
	 * Obtener JSF Application
	 *
	 * @author
	 * @return
	 */
	public static Application getApplication() {
		ApplicationFactory appFactory = (ApplicationFactory) FactoryFinder
				.getFactory(FactoryFinder.APPLICATION_FACTORY);
		return appFactory.getApplication();
	}

	/**
	 * Obtener el contexto EL
	 *
	 * @author
	 * @return
	 */
	public static ELContext getElContext() {
		return FacesContext.getCurrentInstance().getELContext();
	}

	/**
	 * Obtener un ValueExpresion a partir de una empresion por ejemplo
	 * #{beanPerson.name}
	 *
	 * @author
	 * @param el
	 * @return
	 */
	public static ValueExpression getValueExpression(String el) {
		return getApplication().getExpressionFactory().createValueExpression(
				getElContext(), el, Object.class);
	}

	/**
	 * Pasar un valor a una expresion JSF
	 *
	 * @param expresion
	 *            nombre de la expresion JSF sin las llaves ni el numeral.
	 * @param obj
	 * @author
	 */
	public static void setValueToExpression(String expresion, Object obj) {
		getValueExpression(FacesUtil.getJsfEl(expresion)).setValue(
				FacesUtil.getContext().getELContext(), obj);
	}

	/**
	 * Devuelve el valor de un parametro enviado en alguna accion
	 *
	 * @author
	 * @param paramName
	 * @return
	 */
	public static String getRequestParam(String paramName) {
		return FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap().get(paramName);
	}

	/**
	 * Obtener un atributo contenido en el FacesEvent enviado durante una
	 * accion JSF
	 *
	 * @author
	 * @param <T>
	 * @param event
	 * @param paramName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getEventAttribute(FacesEvent event, Object paramName) {
		if (event.getComponent().getAttributes().get(paramName) != null)
			return (T) event.getComponent().getAttributes().get(paramName);
		else
			return null;
	}

	/**
	 * Obtener un bean que esta dentro del contexto de JSF, solo pasar el nombre
	 * de lo que se desea sin encerrar entre parentesis como las expresiones (no
	 * EL). Ejemplo se debe llamar beanPerson en vez de #{beanPerson}.
	 *
	 * @author
	 * @param <T>
	 * @param beanName
	 * @return
	 */
	@SuppressWarnings( { "unchecked" })
	public static <T> T getManagedBean(String beanName) {
		final T bean = (T) getValueExpression(getJsfEl(beanName)).getValue(
				getElContext());
		return bean;
	}

	/**
	 * Retorna una cadena como una expresion EL encerrando el texto dentro de
	 * parentesis
	 *
	 * @author Casto Colina - ccolina@eisconsulting.com 12/05/2009 17:45:53
	 * @param value
	 * @return
	 */
	public static String getJsfEl(String value) {
		return "#{" + value + "}";
	}

	public static String getUrlRoot(){
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String url = req.getRequestURL().toString();
		url = url.substring(0, url.length() - req.getRequestURI().length()) + req.getContextPath() + "/";
		return url;
	}

	public static void redirectPage(String url) throws IOException{
		HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		FacesContext fc = FacesContext.getCurrentInstance();

		String fixedUrl = req.getContextPath();
//		fc.getExternalContext().redirect(getUrlRoot() + url);
		fc.getExternalContext().redirect(url.startsWith("/")? fixedUrl + url : fixedUrl+"/"+url);
	}
}
