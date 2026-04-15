package org.zkoss.zkspringboot.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.zkoss.zk.au.http.DHtmlUpdateServlet;
import org.zkoss.zkspringboot.demo.service.TestService;

import java.util.Date;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Spring Boot 4 API demo:
 * - @AutoConfigureMockMvc is now in spring-boot-webmvc-test (new module in Spring Boot 4)
 *   and must be declared explicitly — @SpringBootTest alone no longer auto-configures MockMvc.
 * - @MockitoBean replaces the removed @MockBean annotation.
 */
@SpringBootTest
@AutoConfigureMockMvc  // Spring Boot 4: requires spring-boot-webmvc-test; was in spring-boot-test-autoconfigure in 3.x
public class DemoApplicationMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ServletRegistrationBean<DHtmlUpdateServlet> dHtmlUpdateServlet;

    // Spring Boot 4: @MockitoBean replaces the removed @MockBean
    @MockitoBean
    private TestService testService;

    @Test
    public void testZkAuEndpointIsRegistered() {
        assertNotNull(dHtmlUpdateServlet, "DHtmlUpdateServlet registration bean should be present");
        assertNotNull(dHtmlUpdateServlet.getUrlMappings(), "DHtmlUpdateServlet should have URL mappings");
    }

    @Test
    public void testZkAuServletUrlMapping() {
        // ZK AU endpoint should be mapped to /zkau
        assertNotNull(dHtmlUpdateServlet.getUrlMappings());
        assertNotNull(dHtmlUpdateServlet.getUrlMappings().stream()
                .filter(url -> url.contains("zkau"))
                .findFirst()
                .orElse(null), "DHtmlUpdateServlet should be mapped under /zkau");
    }

    @Test
    public void testMvvmEndpointResponds() throws Exception {
        // /mvvm returns a ZUL view resolved by ZK's InternalViewResolver (resolves to /zkau/web/mvvm.zul)
        mockMvc.perform(get("/mvvm"))
                .andExpect(status().isOk());
    }

    @Test
    public void testMockitoBean() {
        // Spring Boot 4: @MockitoBean (replaces removed @MockBean) wires a Mockito mock into the Spring context
        Date fixedDate = new Date(0);
        when(testService.getTime()).thenReturn(fixedDate);
        assertEquals(fixedDate, testService.getTime());
    }
}
