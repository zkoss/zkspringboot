package org.zkoss.zkspringboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zkoss.zkmax.au.websocket.WebSocketFilter;
import org.zkoss.zkmax.au.websocket.WebSocketWebAppInit;
import org.zkoss.zkmax.ui.comet.CometAsyncServlet;

@Configuration
@EnableConfigurationProperties({ZkProperties.class})
@ConditionalOnClass(org.zkoss.zkmax.Version.class)
public class ZkMaxAutoConfiguration {
	private static final Logger logger = LoggerFactory.getLogger(ZkMaxAutoConfiguration.class);

	@Bean
	public ServletContextInitializer manualServletConfigInit() {
		return servletContext -> {
			//required to avoid duplicate installing of the CometAsyncServlet
			//startup sequence in spring boot is different to a normal servlet webapp
			servletContext.setAttribute("org.zkoss.zkmax.ui.comet.async.installed", true);
			servletContext.setAttribute("org.zkoss.zkmax.au.websocket.filter.installed", true);
		};
	}

	@Bean
	@ConditionalOnProperty(prefix = "zk", name = "servlet3-push-enabled", matchIfMissing = true)
	public ServletRegistrationBean cometAsyncServlet() {
		final String cometUri = "/zkcomet";
		ServletRegistrationBean reg = new ServletRegistrationBean(new CometAsyncServlet(), cometUri + "/*");
		reg.setAsyncSupported(true);
		logger.info("ZK-Springboot: ServletRegistrationBean for CometAsyncServlet with path " + cometUri);
		return reg;
	}

	//optional: use when websockets are enabled in zk.xml
	@Bean
	@ConditionalOnProperty(prefix = "zk", name = "websockets-enabled", matchIfMissing = true)
	public FilterRegistrationBean wsFilter() {
		FilterRegistrationBean reg = new FilterRegistrationBean(new WebSocketFilter());
		final String webSocketUrl = WebSocketWebAppInit.getWebSocketUrl();
		reg.addUrlPatterns(webSocketUrl + "/*");
		logger.info("ZK-Springboot: FilterRegistrationBean for WebSocketFilter with path " + webSocketUrl);
		return reg;
	}
}
