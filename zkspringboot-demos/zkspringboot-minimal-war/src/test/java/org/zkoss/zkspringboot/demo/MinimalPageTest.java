package org.zkoss.zkspringboot.demo;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.zkoss.zats.junit.AutoClient;
import org.zkoss.zats.junit.AutoEnvironment;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zul.Label;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MinimalPageTest {

	@ClassRule
	public static AutoEnvironment env = new AutoEnvironment("src/test/webapp/WEB-INF", "src/main/webapp");

	@Rule
	public AutoClient client = env.autoClient();

	private DesktopAgent desktopAgent;

	@Test
	public void testDemoPage() {
		desktopAgent = client.connect("/");
		assertNotNull(demoButton());
		assertNotNull(demoLabel());
		assertEquals("", demoLabel().as(Label.class).getValue());
		
		final String testName = "Test Name";
		demoInput().input(testName);
		demoButton().click();
		assertEquals("Hello " + testName + "!", demoLabel().as(Label.class).getValue());
	}

	private ComponentAgent demoLabel() {
		return desktopAgent.query("#demoLabel");
	}
	private ComponentAgent demoInput() {
		return desktopAgent.query("#demoInput");
	}
	private ComponentAgent demoButton() {
		return desktopAgent.query("#demoButton");
	}
}
