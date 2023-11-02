package org.tesis.xc.config;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public enum PropertiesEnum {
	
    SERVER_URL("server.url");

	private final String name;
	private String msg;

	public String getName() {
		return name;
	}
	
	private PropertiesEnum(String name) {
		this.name = name;
	}

	public final String getContent() {
		if (msg == null) {
		    Path path = Paths.get(ConfigLoadServlet.REAL_PATH, "WEB-INF", "tesis.properties");
			Properties messages = PropertiesUtil.getRecursoURLPath(path.toAbsolutePath().toString());
			msg = messages.getProperty(getName());
		}
		return msg;
	}
}
