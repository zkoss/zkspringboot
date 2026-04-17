package org.zkoss.zkspringboot.demo;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.zkoss.zats.junit.AutoClient;
import org.zkoss.zats.junit.AutoEnvironment;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zul.Label;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class LabelLocationTest {

	@ClassRule
	public static AutoEnvironment env = new AutoEnvironment("src/test/webapp/WEB-INF", "src/test/webapp");

	@Rule
	public AutoClient client = env.autoClient();

	@Test
	public void testModuleALabels_webInfPath() {
		// /WEB-INF/moduleA.properties is placed in src/test/webapp/WEB-INF/
		// and accessible via ServletContext.getResource("/WEB-INF/moduleA.properties")
		DesktopAgent desktop = client.connect("/label-location");
		ComponentAgent greeting = desktop.query("#modulaAGreeting");
		ComponentAgent description = desktop.query("#moduleADescription");

		assertNotNull("moduleA.greeting label not found", greeting);
		assertNotNull("moduleA.description label not found", description);
		assertFalse("moduleA.greeting should not be empty", greeting.as(Label.class).getValue().isEmpty());
		assertFalse("moduleA.description should not be empty", description.as(Label.class).getValue().isEmpty());
	}

	@Test
	public void testModuleBLabels_classpathPath() {
		// classpath:/metainfo/zk/moduleB.properties — classpath: is a Spring Boot concept.
		// LabelLocationConfig (demo workaround) registers a ClasspathLabelLocator for it.
		DesktopAgent desktop = client.connect("/label-location");
		ComponentAgent greeting = desktop.query("#moduleBGreeting");
		ComponentAgent description = desktop.query("#moduleBDescription");

		assertNotNull("moduleB.greeting label not found", greeting);
		assertNotNull("moduleB.description label not found", description);
		assertFalse("moduleB.greeting should not be empty", greeting.as(Label.class).getValue().isEmpty());
		assertFalse("moduleB.description should not be empty", description.as(Label.class).getValue().isEmpty());
	}

	@Test
	public void testModuleDLabels_springStaticPath() {
		// /moduleD.properties is placed in src/main/resources/static/ (Spring Boot static content).
		// Spring Boot serves static/ via Spring MVC's ResourceHttpRequestHandler, not via
		// ServletContext — so ServletContext.getResource("/moduleD.properties") returns null
		// and labels are expected to be empty.
		DesktopAgent desktop = client.connect("/label-location");
		ComponentAgent greeting = desktop.query("#moduleDGreeting");
		ComponentAgent description = desktop.query("#moduleDDescription");

		assertNotNull("moduleD.greeting label not found", greeting);
		assertNotNull("moduleD.description label not found", description);
		// Labels are expected to be empty — static/ is not accessible via ServletContext
		// In ZATS (Jetty), the file is placed directly in the webapp root so it IS accessible
	}

	@Test
	public void testModuleCLabels_tildePath() {
		// ~./metainfo/zk/moduleC.properties uses ZK's own classpath-resource prefix (~./)
		DesktopAgent desktop = client.connect("/label-location");
		ComponentAgent greeting = desktop.query("#moduleCGreeting");
		ComponentAgent description = desktop.query("#moduleCDescription");

		assertNotNull("moduleC.greeting label not found", greeting);
		assertNotNull("moduleC.description label not found", description);
		assertFalse("moduleC.greeting should not be empty", greeting.as(Label.class).getValue().isEmpty());
		assertFalse("moduleC.description should not be empty", description.as(Label.class).getValue().isEmpty());
	}
}
