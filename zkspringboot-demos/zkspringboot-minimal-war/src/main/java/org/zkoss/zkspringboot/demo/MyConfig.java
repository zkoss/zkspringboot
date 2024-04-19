package org.zkoss.zkspringboot.demo;

import org.springframework.boot.web.servlet.*;
import org.springframework.context.annotation.*;
import org.zkoss.zk.ui.http.DHtmlLayoutServlet;

import java.util.Map;

@Configuration
public class MyConfig {


    /* 2nd solution: register dhtmlLayoutservlet with compress=false
    @Bean
    public ServletRegistrationBean<DHtmlLayoutServlet> dHtmlLayoutServletUncompressed() {
        final String[] mappings = {"*.zul", "*.zhtml"};
        ServletRegistrationBean<DHtmlLayoutServlet> reg = new ServletRegistrationBean<>(new DHtmlLayoutServlet(), mappings);
        reg.setInitParameters(Map.of("update-uri", "/zkau"
            , "compress", "false"));
        reg.setLoadOnStartup(0);
        System.out.println("register DHtmlLayoutServlet without compression");
        return reg;
    }
    */

    @Bean
    public FilterRegistrationBean cspFilter() {
        FilterRegistrationBean reg = new FilterRegistrationBean(new ZkCspFilterStrictDynamic());
        reg.addUrlPatterns("*.zul", "/");
        return reg;
    }
}
