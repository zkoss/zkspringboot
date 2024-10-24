package org.zkoss.zkspringboot.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * If you start the application with this class' main(), web context root is src/main/resources/META-INF/resources
 * If you run with mvn spring-boot:run, web context root is src/main/webapp
 */
@SpringBootApplication
@Controller
public class MinimalWarApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(MinimalWarApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(MinimalWarApplication.class);
	}

	@GetMapping("/minimal")
	public String minimal(){
		return "/minimal";
	}

	@GetMapping("/index")
	public String index(){
		return "/index";
	}
}
