package org.zkoss.zkspringboot.test;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
@Controller
public class ZatsTestApplication {
	@GetMapping("/test")
	public String testPage() {
		return "zul/test";
	}
}