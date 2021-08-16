package org.zkoss.zkspringboot.demo;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.zkoss.zats.junit.AutoClient;
import org.zkoss.zats.junit.AutoEnvironment;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zul.Label;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ExampleRichletTest {

	@ClassRule
	public static AutoEnvironment env = new AutoEnvironment("src/test/webapp/WEB-INF", "src/test/webapp");

	@Rule
	public AutoClient client = env.autoClient();

	private DesktopAgent desktopAgent;

	@Test
	public void testExampleRichlet() {
		desktopAgent = client.connect("/richlet/example");
		assertNotNull(getTimeButton());
		assertEquals("initially no labels expected on page", 0, timeLabels().size());

		getTimeButton().click();
		assertEquals("1 label expected after first click", 1, timeLabels().size());

		getTimeButton().click();
		getTimeButton().click();
		assertEquals("3 labels expected after third click", 3, timeLabels().size());
	}

	private ComponentAgent getTimeButton() {
		return desktopAgent.query("button[label='Get Time from Spring Service']");
	}
	private List<ComponentAgent> timeLabels() {
		return desktopAgent.queryAll("label");
	}
	private ComponentAgent demoButton() {
		return desktopAgent.query("#demoButton");
	}
}
