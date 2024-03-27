package org.zkoss.zkspringboot;

import org.slf4j.*;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.*;
import org.zkoss.stateless.ui.http.DispatcherRichletFilter;

import java.util.*;

/**
 * @since 3.2.3
 */
@Configuration
@EnableConfigurationProperties({ZkProperties.class})
@ConditionalOnClass(org.zkoss.stateless.Version.class)
public class ZkStatelessAutoConfiguration {

    private final ZkProperties	zkProperties;

    public ZkStatelessAutoConfiguration(ZkProperties zkProperties) {
        this.zkProperties = zkProperties;
    }

    private static final Logger logger = LoggerFactory.getLogger(ZkStatelessAutoConfiguration.class);

    @Bean
    @ConditionalOnProperty(prefix = "zk", name = "stateless-dispatcher-richlet-filter-mapping")
    public FilterRegistrationBean<DispatcherRichletFilter> dispatcherRichletFilter(){
        String basePackages = zkProperties.getStatelessDispatcherRichletFilterBase();
        if (basePackages == null || basePackages.isEmpty()){
            logger.warn("stateless-dispatcher-richlet-filter-base should not be empty");
            return null;
        }
        FilterRegistrationBean<DispatcherRichletFilter> reg = new FilterRegistrationBean<>(new DispatcherRichletFilter());
        reg.addUrlPatterns(zkProperties.getStatelessDispatcherRichletFilterMapping());
        Map<String, String> param = new HashMap<>();
        param.put("basePackages", basePackages);
        reg.setInitParameters(param);
        logger.info("ZK-Springboot: add stateless DispatcherRichletFilter with url pattern " + zkProperties.getStatelessDispatcherRichletFilterMapping()
        + ", base packages: " + basePackages);
        return reg;
    }
}
