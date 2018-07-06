package org.zkoss.zkforge.springboot.integrationtest;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
@Controller
public class IntegrationTestApplication {
	@GetMapping("/test")
	public String testPage() {
		return "test";
	}
}