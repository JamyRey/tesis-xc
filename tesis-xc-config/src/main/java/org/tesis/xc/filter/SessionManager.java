package org.tesis.xc.filter;


import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@WebFilter(filterName = "SessionManager", urlPatterns = "/logged/*")
public class SessionManager implements Filter {

    private Logger log = LogManager.getLogger(this.getClass());

    public SessionManager() { }

    @Override
    public void destroy() { }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException
    {
        try {
            HttpSession sesion = ((HttpServletRequest) request).getSession(false);
            if (sesion == null || sesion.getAttribute("session") == null) {
                ((HttpServletResponse) response)
                        .sendRedirect(((HttpServletRequest) request).getContextPath() + "/login.xhtml");
            }
            else {
                filterChain.doFilter(request, response);
            }

        }
        catch (Throwable e) {
            log.error("Error:", e);
            sendError(request, response);
        }
    }

    private void sendError(ServletRequest request, ServletResponse response) throws IOException {
        ((HttpServletResponse) response).sendRedirect(((HttpServletRequest) request).getContextPath() + "/error.xhtml");
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {

    }
}
