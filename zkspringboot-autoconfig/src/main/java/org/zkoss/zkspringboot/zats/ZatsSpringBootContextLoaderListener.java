package org.zkoss.zkspringboot.zats;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Conditional;
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
		final String contextConfigLocation = sce.getServletContext().getInitParameter(CONTEXT_CONFIG_LOCATION);
		new ZatsSpringBootServletInitializer(contextConfigLocation).onStartup(sce.getServletContext());
	}

	public static class ZatsSpringBootServletInitializer extends SpringBootServletInitializer {
		private String contextConfigLocation = null;
		public ZatsSpringBootServletInitializer() {
			// NOOP: auto created by spring/tomcats classpath scanning don't initialize!!
			// contextConfigLocation remains null -> no init
		}

		public ZatsSpringBootServletInitializer(String contextConfigLocation) {
			// explicitly created with contextConfigLocation -> for intentional ZATS testing
			this.contextConfigLocation = contextConfigLocation;
		}

		@Override
		public void onStartup(ServletContext servletContext) {
			//only initialize when created from ZatsSpringBootContextLoaderListener.contextInitialized
			if(contextConfigLocation != null) {
				createRootApplicationContext(servletContext);
			}
		}

		@Override
		protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
			try {
				Class<?> configClass = Class.forName(contextConfigLocation);
				return builder.sources(configClass);
			} catch (ClassNotFoundException e) {
				throw new IllegalArgumentException("couldn't initialize contextConfigLocation");
			}
		}
	}
}
