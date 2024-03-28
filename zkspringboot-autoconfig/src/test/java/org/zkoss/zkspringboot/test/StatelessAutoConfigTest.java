package org.zkoss.zkspringboot.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.servlet.*;
import org.springframework.test.context.TestPropertySource;
import org.zkoss.stateless.ui.http.DispatcherRichletFilter;
import org.zkoss.zk.au.http.DHtmlUpdateServlet;
import org.zkoss.zkspringboot.ZkProperties;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:stateless-application.properties")
public class StatelessAutoConfigTest {

    @Autowired
    ServletRegistrationBean<DHtmlUpdateServlet> dHtmlUpdateServlet;
    @Autowired
    ZkProperties zkProperties;
    @Autowired
    FilterRegistrationBean<DispatcherRichletFilter> dispatcherRichletFilterFilterRegistrationBean;

    @Test
    public void testDHtmlUpdateServlet() {
        assertNotNull(dHtmlUpdateServlet);
    }

    @Test
    public void testDispatcherRichletFilterBeanExists() {
        assertNotNull(dispatcherRichletFilterFilterRegistrationBean, "FilterRegistrationBean<DispatcherRichletFilter> should not be null");
        assertEquals("/stateless/*", dispatcherRichletFilterFilterRegistrationBean.getUrlPatterns().iterator().next());
        assertEquals("org.zkoss.zkspringboot.demo.stateless", dispatcherRichletFilterFilterRegistrationBean.getInitParameters().get("basePackages"));
    }
}

