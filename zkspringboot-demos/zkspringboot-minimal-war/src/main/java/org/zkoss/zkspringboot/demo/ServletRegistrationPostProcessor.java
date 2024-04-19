package org.zkoss.zkspringboot.demo;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.stereotype.Component;

@Component
public class ServletRegistrationPostProcessor implements BeanPostProcessor {

    /**
     * set compress=false for DHtmlLayoutServlet for CSP filter
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof ServletRegistrationBean && "dHtmlLayoutServlet".equals(beanName)) {
            ServletRegistrationBean reg = (ServletRegistrationBean) bean;
            reg.addInitParameter("compress", "false");
        }
        return bean;
    }
}

