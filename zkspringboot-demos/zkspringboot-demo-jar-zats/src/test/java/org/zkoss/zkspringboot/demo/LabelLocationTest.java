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
import static org.junit.Assert.assertTrue;

public class LabelLocationTest {

	@ClassRule
	public static AutoEnvironment env = new AutoEnvironment("src/test/webapp/WEB-INF", "src/test/webapp");

	@Rule
	public AutoClient client = env.autoClient();

	@Test
	public void testModuleALabels_webInfPath() {
		// /WEB-INF/moduleA.properties — resolved via ServletContext.getResource()
		DesktopAgent desktop = client.connect("/label-location");
		assertLabelNotEmpty(desktop, "moduleAGreeting");
		assertLabelNotEmpty(desktop, "moduleADescription");
	}

	@Test
	public void testModuleBLabels_classpathPath() {
		// classpath: is a Spring Boot concept — LabelLocationConfig registers a ClasspathLabelLocator
		DesktopAgent desktop = client.connect("/label-location");
		assertLabelNotEmpty(desktop, "moduleBGreeting");
		assertLabelNotEmpty(desktop, "moduleBDescription");
	}

	@Test
	public void testModuleCLabels_tildePath() {
		// ~./ is ZK's own classpath-resource prefix — LabelLocationConfig handles it too
		DesktopAgent desktop = client.connect("/label-location");
		assertLabelNotEmpty(desktop, "moduleCGreeting");
		assertLabelNotEmpty(desktop, "moduleCDescription");
	}

	@Test
	public void testModuleDLabels_springStaticPath() {
		// /moduleD.properties is in src/main/resources/static/ (Spring Boot static content).
		// ServletContext.getResource() cannot see static/ — labels are expected to be empty in JAR mode.
		// In ZATS (Jetty), the file is placed directly in src/test/webapp/ so it IS accessible.
		DesktopAgent desktop = client.connect("/label-location");
		assertLabelNotEmpty(desktop, "moduleDGreeting");
		assertLabelNotEmpty(desktop, "moduleDDescription");
	}

	private void assertLabelNotEmpty(DesktopAgent desktop, String id) {
		ComponentAgent component = desktop.query("#" + id);
		assertNotNull(id + " component not found", component);
		assertFalse(id + " label should not be empty", component.as(Label.class).getValue().isEmpty());
	}
}
