package org.zkoss.zkspringboot.demo;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.zkoss.zats.junit.AutoClient;
import org.zkoss.zats.junit.AutoEnvironment;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zul.Label;

public class MvvmPageTest {
	@ClassRule
	public static AutoEnvironment env = new AutoEnvironment("src/test/webapp/WEB-INF", "src/test/webapp");

	@Rule
	public AutoClient client = env.autoClient();

	private DesktopAgent desktopAgent;

	@Test
	public void testRender() {
		desktopAgent = client.connect("/mvvm");
		Assert.assertNotNull(firstNaviButton());
		Assert.assertNotNull(secondNaviButton());
		Assert.assertNull(testLabel());
	}


	@Test
	public void testButtons() {
		desktopAgent = client.connect("/mvvm");
		firstNaviButton().click();
		Assert.assertEquals("some data for page 1 (could be a more complex object)", testLabel().as(Label.class).getValue());
		secondNaviButton().click();
		Assert.assertEquals("different data for page 2", testLabel().as(Label.class).getValue());
	}

	private ComponentAgent testLabel() {
		return desktopAgent.query("window window label");
	}

	private ComponentAgent firstNaviButton() {
		return desktopAgent.query("button[label='sub page 1']");
	}

	private ComponentAgent secondNaviButton() {
		return desktopAgent.query("button[label='sub page 2']");
	}
}
