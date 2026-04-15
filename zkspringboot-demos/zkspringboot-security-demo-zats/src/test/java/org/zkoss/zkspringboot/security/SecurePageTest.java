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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		assertEquals("login should succeed with a redirect", 302, response.getResponseCode());

		// Sync the authenticated session cookies into ZATS, then navigate to the target page
		autoCookieManager.syncToZats();
		return client.connect(securePath);
	}


	private HttpURLConnection postSpringFormLogin(String loginPath, final String username, final String password) throws IOException {
		// GET the login page to obtain the masked _csrf token from the rendered HTML.
		// Spring Security 6 uses XorCsrfTokenRequestAttributeHandler which masks the raw cookie
		// value — we must submit the masked value from the form, not the raw cookie.
		EmulatorClient emulatorClient = (EmulatorClient) client.getClient();
		final HttpURLConnection getConn = emulatorClient.getConnection(loginPath, "GET");
		getConn.setInstanceFollowRedirects(false);
		getConn.connect();
		String responseBody;
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(getConn.getInputStream(), StandardCharsets.UTF_8))) {
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) sb.append(line);
			responseBody = sb.toString();
		}

		// Extract the masked CSRF token from the <meta name='_csrf' content='...'> tag
		// added by SpringSecurityCsrfInitiator. Spring Security 6 uses XorCsrfTokenRequestAttributeHandler,
		// so we must submit the already-masked token value (not the raw XSRF-TOKEN cookie value).
		String csrfToken = "";
		Matcher m = Pattern.compile("name='_csrf'\\s+content='([^']+)'").matcher(responseBody);
		if (m.find()) {
			csrfToken = m.group(1);
		}

		final HttpURLConnection connection = emulatorClient.getConnection(loginPath, "POST");
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setInstanceFollowRedirects(false);
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
		connection.setRequestProperty("X-XSRF-TOKEN", csrfToken);
		connection.connect();
		final OutputStream os = connection.getOutputStream();
		os.write(("username=" + username + "&password=" + password).getBytes("utf-8"));
		os.close();
		return connection;
	}
}
