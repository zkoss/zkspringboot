package org.zkoss.zkforge.springboot.zats;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

//import org.springframework.boot.web.support.SpringBootServletInitializer; /*Spring boot 1.5.x*/

/**
 * experimental ContextLoaderListener to load a spring boot application context using ZATS' embedded jetty
 */
public class ZatsSpringBootContextLoaderListener extends ContextLoaderListener {

	public static final String CONTEXT_CONFIG_LOCATION = "contextConfigLocation";

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		new SpringBootServletInitializer() {
			@Override
			public void onStartup(ServletContext servletContext) {
				createRootApplicationContext(servletContext);
			}

			@Override
			protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
				try {
					Class<?> configClass = Class.forName(sce.getServletContext().getInitParameter(CONTEXT_CONFIG_LOCATION));
					return builder.sources(configClass);
				} catch (ClassNotFoundException e) {
					throw new IllegalArgumentException("couldn't initialize contextConfigLocation");
				}
			}
		}.onStartup(sce.getServletContext());
	}
}
