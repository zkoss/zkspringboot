package org.zkoss.zkspringboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zkoss.zk.au.http.DHtmlUpdateServlet;
import org.zkoss.zk.ui.http.HttpSessionListener;
import org.zkoss.zk.ui.http.RichletFilter;
import org.zkoss.zk.ui.http.WebManager;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

@Configuration
@EnableConfigurationProperties({ZkProperties.class})
public class ZkAutoConfiguration {
	private static final Logger logger = LoggerFactory.getLogger(ZkAutoConfiguration.class);

	private final ZkProperties zkProperties;

	public ZkAutoConfiguration(ZkProperties zkProperties) {
		this.zkProperties = zkProperties;
	}

/*
	// original zk layout servlet (only for war files)
    @Bean
    public ServletRegistrationBean dHtmlLayoutServlet() {
        ServletRegistrationBean reg = new ServletRegistrationBean(new DHtmlLayoutServlet(), "*.zul");
        reg.setInitParameters(Collections.singletonMap("update-uri", UPDATE_URI));
        return reg;
    }
*/

	@Bean
	@ConditionalOnProperty(prefix = "zk", name = "richlet-filter-mapping")
	public FilterRegistrationBean richletFilter() {
		final String richletFilterMapping = zkProperties.getRichletFilterMapping();
		FilterRegistrationBean reg = new FilterRegistrationBean(new RichletFilter());
		reg.addUrlPatterns(richletFilterMapping);
		logger.info("ZK-Springboot: FilterRegistrationBean for RichletFilter with url pattern " + richletFilterMapping);
		return reg;
	}

	@Bean
	@ConditionalOnClass(name = "org.zkoss.zats.mimic.Zats") //Zats doesn't support custom update URI.
	public ServletRegistrationBean defaultDHtmlUpdateServlet() {
		return new ServletRegistrationBean(new DHtmlUpdateServlet(), "/zkau/*");
	}

	@Bean
	@ConditionalOnMissingClass("org.zkoss.zats.mimic.Zats") //only allow custom update URI outside Zats testcases.
	public ServletRegistrationBean customizableDHtmlUpdateServlet() {
		final String updateUri = zkProperties.getUpdateUri();
		logger.info("ZK-Springboot: ServletRegistrationBean for DHtmlUpdateServlet with path " + updateUri);
		return new ServletRegistrationBean(new DHtmlUpdateServlet(), updateUri + "/*");
	}

	/**
	 * With Zats the listener needs to be configured in web.xml.(custom update URI isn't supported by Zats anyway).
	 * Zats runs with its own embedded Jetty.
	 */
	@Bean
	@ConditionalOnMissingClass("org.zkoss.zats.mimic.Zats") //Obsolete when using Zats
	public HttpSessionListener httpSessionListener() {
		return new HttpSessionListener() {
			private WebManager webManager;
			@Override
			public void contextInitialized(ServletContextEvent sce) {
				final ServletContext ctx = sce.getServletContext();
				if (WebManager.getWebManagerIfAny(ctx) == null) {
					webManager = new WebManager(ctx, zkProperties.getUpdateUri());
				} else {
					throw new IllegalStateException("ZK WebManager already exists. Could not initialize via Spring Boot configuration.");
				}
			}

			@Override
			public void contextDestroyed(ServletContextEvent sce) {
				if (webManager != null) {
					webManager.destroy();
				}
			}
		};
	}
}


