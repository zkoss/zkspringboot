package org.zkoss.zkspringboot.security;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.zkoss.zats.junit.AutoClient;
import org.zkoss.zats.junit.AutoEnvironment;
import org.zkoss.zats.mimic.ComponentAgent;
import org.zkoss.zats.mimic.DesktopAgent;
import org.zkoss.zats.mimic.QueryAgent;
import org.zkoss.zats.mimic.impl.EmulatorClient;
import org.zkoss.zul.Label;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SecurePageTest {
	@ClassRule
	public static AutoEnvironment env = new AutoEnvironment("src/test/webapp/WEB-INF", "src/test/webapp");
	@Rule
	public AutoClient client = env.autoClient();
	@Rule //captures cookies (changing JSESSIONID) after login to sync them into the ZATS client
	public AutoCookieManager autoCookieManager = new AutoCookieManager(client);

	private ComponentAgent loginButton(QueryAgent qa) {
		return qa.query("button[label='Login']");
	}

	private ComponentAgent loginSuccessLabel(QueryAgent qa) {
		return qa.query("div > label");
	}

	private String[] loginInfo = {"/login", "user", "password"};

	@Test
	public void testSecureMainPage() throws IOException {
		DesktopAgent dt = connectWithLogin("/secure/main", loginInfo);
		assertTrue(loginSuccessLabel(dt)
				.as(Label.class).getValue()
				.contains("You should see this a secure page after a successful authentication."));
	}

	@Test
	public void testSecureAnotherPage() throws IOException {
		DesktopAgent dt = connectWithLogin("/secure/another", loginInfo);
		assertTrue(loginSuccessLabel(dt)
				.as(Label.class).getValue()
				.contains("this is another page"));
	}

	private DesktopAgent connectWithLogin(String securePath, String[] loginInfo) throws IOException {
		DesktopAgent dt = client.connect(securePath);
		assertNotNull("unauthenticated access should redirect to login page", loginButton(dt));

		final HttpURLConnection response = postSpringFormLogin(loginInfo[0], loginInfo[1], loginInfo[2]);
		assertEquals(302, response.getResponseCode());
		final String redirectPath = new URL(response.getHeaderField("location")).getPath();
		assertEquals("application should redirect to same path after login", securePath, redirectPath);

		autoCookieManager.syncToZats();
		return client.connect(redirectPath);
	}


	private HttpURLConnection postSpringFormLogin(String loginPath, final String username, final String password) throws IOException {
		final HttpURLConnection connection = ((EmulatorClient) client.getClient()).getConnection(loginPath, "POST");
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setInstanceFollowRedirects(false);
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
		connection.connect();
		final OutputStream os = connection.getOutputStream();
		os.write(("username=" + username + "&password=" + password).getBytes("utf-8"));
		os.close();
		return connection;
	}
}
