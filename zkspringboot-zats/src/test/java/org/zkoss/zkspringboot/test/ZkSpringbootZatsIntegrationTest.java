package org.zkoss.zkspringboot.test;

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

public class ZkSpringbootZatsIntegrationTest {

	@ClassRule
	public static AutoEnvironment env = new AutoEnvironment("src/test/webapp/WEB-INF", "src/test/webapp");

	@Rule
	public AutoClient client = env.autoClient();

	private DesktopAgent desktopAgent;

	@Test
	public void testRender() {
		desktopAgent = client.connect("/test");
		assertNotNull(testButton());
		assertNotNull(testLabel());
		assertEquals("initial value", testLabel().as(Label.class).getValue());
	}

	@Test
	public void testUpdate() {
		desktopAgent = client.connect("/test");
		assertEquals("initial value", testLabel().as(Label.class).getValue());
		testButton().click();
		assertEquals("updated value", testLabel().as(Label.class).getValue());
	}

	private ComponentAgent testLabel() {
		return desktopAgent.query("#testLabel");
	}
	private ComponentAgent testButton() {
		return desktopAgent.query("#testButton");
	}
}
