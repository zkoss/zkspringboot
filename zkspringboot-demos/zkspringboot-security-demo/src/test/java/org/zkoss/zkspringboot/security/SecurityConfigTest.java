package org.zkoss.zkspringboot.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Spring Boot 4 security demo:
 * - @AutoConfigureMockMvc is now in spring-boot-webmvc-test (new module in Spring Boot 4)
 * - PathPatternRequestMatcher replaces the removed AntPathRequestMatcher (Spring Security 7)
 * - Tests verify the WebSecurityConfig rules still correctly permit/deny requests
 */
@SpringBootTest
@AutoConfigureMockMvc  // Spring Boot 4: requires spring-boot-webmvc-test; was in spring-boot-test-autoconfigure in 3.x
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testLoginPageIsPublic() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    public void testSecurePageRequiresAuthentication() throws Exception {
        // Unauthenticated access to /secure should redirect to login
        mockMvc.perform(get("/secure"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testSecurePageAccessibleWithUser() throws Exception {
        // Authenticated USER passes the security filter for /secure — no 401/403.
        // Spring MVC has no handler for /secure itself (ZK handles *.zul URLs via its own servlet),
        // so the response may be a redirect or 404, but must NOT be an auth denial.
        int status = mockMvc.perform(get("/secure"))
                .andReturn().getResponse().getStatus();
        assert status != 401 && status != 403 : "Authenticated USER should not be blocked from /secure";
    }

    @Test
    public void testZkResourcesArePublic() throws Exception {
        // ZK static resources (/zkres/**) should be publicly accessible (GET)
        // PathPatternRequestMatcher (Spring Security 7) replaces removed AntPathRequestMatcher
        mockMvc.perform(get("/zkres/some-resource.css"))
                .andExpect(status().isNotFound()); // 404 = passed security, resource just doesn't exist in test
    }
}
