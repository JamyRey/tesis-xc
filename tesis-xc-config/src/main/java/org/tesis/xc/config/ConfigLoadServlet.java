package org.tesis.xc.config;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SuppressWarnings("serial")
@WebServlet(name = "ConfigLoadServlet", urlPatterns = "/test/config", loadOnStartup = 0)
public class ConfigLoadServlet extends HttpServlet {
    private Logger log = LoggerFactory.getLogger(ConfigLoadServlet.class);

    public static String REAL_PATH;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        REAL_PATH = config.getServletContext().getRealPath("/");
        //ProviderUtils.loaderProvider();
        log.info("REAL PATH load: {}", REAL_PATH);
    }
}

