package org.zkoss.zkspringboot.security;

import org.junit.rules.ExternalResource;
import org.zkoss.zats.junit.AutoClient;
import org.zkoss.zats.mimic.impl.EmulatorClient;

import java.net.CookieHandler;
import java.net.CookieManager;

public class AutoCookieManager extends ExternalResource {
	private AutoClient client;
	private CookieManager cookieManager;

	public AutoCookieManager(AutoClient client) {
		this.client = client;
	}

	@Override
	protected void before() throws Throwable {
		cookieManager = new CookieManager();
		CookieHandler.setDefault(cookieManager);
	}

	@Override
	protected void after() {
		CookieHandler.setDefault(null);
	}

	public void syncToZats() {
		EmulatorClient emulatorClient = (EmulatorClient) client.getClient();
		cookieManager.getCookieStore().getCookies().stream()
				.forEach(cookie -> emulatorClient.setCookie(cookie.getName(), cookie.getValue()));
	}
}
