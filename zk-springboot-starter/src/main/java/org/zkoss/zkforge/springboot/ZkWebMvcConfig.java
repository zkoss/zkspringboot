package org.zkoss.zkforge.springboot;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.zkoss.web.util.resource.ClassWebResource;

import static org.zkoss.zkforge.springboot.ZkAutoConfig.UPDATE_URI;

@Configuration
public class ZkWebMvcConfig implements WebMvcConfigurer {
	private static String ZUL_VIEW_RESOLVER_PREFIX = UPDATE_URI + ClassWebResource.PATH_PREFIX + "/zul/";
	private static final String ZUL_VIEW_RESOLVER_SUFFIX = ".zul";

	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver(ZUL_VIEW_RESOLVER_PREFIX, ZUL_VIEW_RESOLVER_SUFFIX);
		resolver.setOrder(InternalResourceViewResolver.LOWEST_PRECEDENCE);
		registry.viewResolver(resolver);
	}
}
