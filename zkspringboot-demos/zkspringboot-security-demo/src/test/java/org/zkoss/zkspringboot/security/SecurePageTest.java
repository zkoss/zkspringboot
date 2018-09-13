package org.zkoss.zkspringboot.security;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.zkoss.zats.junit.AutoClient;
import org.zkoss.zats.junit.AutoEnvironment;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zats.mimic.impl.EmulatorClient;
import org.zkoss.zul.Label;

import java.io.IOException;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SecurePageTest {
	@ClassRule
	public static AutoEnvironment env = new AutoEnvironment("src/test/webapp/WEB-INF", "src/test/webapp");

	@Rule
	public AutoClient client = env.autoClient();

	private DesktopAgent desktopAgent;

	@Test
	public void testSecureMainLogin() throws IOException {
		desktopAgent = client.connect("/secure/main");
		assertNotNull("should redirect to login page", loginButton());
		performLogin("user", "password", "/secure/main");
		desktopAgent = client.connect("/secure/main");
		assertTrue(loginSuccessLabel().as(Label.class).getValue().contains("You should see this a secure page after a successful authentication."));
	}

	@Test
	public void testSecureAnotherLogin() throws IOException {
		desktopAgent = client.connect("/secure/another");
		assertNotNull("should redirect to login page", loginButton());
		performLogin("user", "password", "/secure/main");
		desktopAgent = client.connect("/secure/another");
		assertTrue(loginSuccessLabel().as(Label.class).getValue().contains("this is another page"));
	}

	private void performLogin(final String username, final String password, String path) throws IOException {
		CookieManager cm = new CookieManager();
		CookieHandler.setDefault(cm);

		EmulatorClient innerClient = (EmulatorClient) client.getClient();
		final HttpURLConnection connection = innerClient.getConnection("/login", "POST");
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setInstanceFollowRedirects(false);
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
		connection.connect();
		final OutputStream os = connection.getOutputStream();
		os.write(("username=" + username + "&password=" + password).getBytes("utf-8"));
		os.close();
		CookieHandler.setDefault(null);

		assertEquals(302, connection.getResponseCode());
		assertTrue(connection.getHeaderField("location"), connection.getHeaderField("location").contains(path));

		cm.getCookieStore().getCookies().stream().forEach(cookie -> innerClient.setCookie(cookie.getName(), cookie.getValue()));
	}

	private ComponentAgent loginButton() {
		return desktopAgent.query("button[label='Login']");
	}
	private ComponentAgent loginSuccessLabel() {
		return desktopAgent.query("div > label");
	}
}
