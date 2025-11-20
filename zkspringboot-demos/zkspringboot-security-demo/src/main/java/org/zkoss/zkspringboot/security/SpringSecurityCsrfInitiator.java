package org.zkoss.zkspringboot.security;

import java.util.Map;

import org.springframework.security.web.csrf.CsrfToken;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.sys.PageCtrl;
import org.zkoss.zk.ui.util.Initiator;
import org.zkoss.zk.ui.util.InitiatorExt;
import org.zkoss.zul.Script;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Automatically creates meta tags in the page using the CSRF token from spring-security
 * Also automatically adds the JS override file to any page loaded
 * See https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html for more information on Spring-security CSRF configuration
 * */
public class SpringSecurityCsrfInitiator implements Initiator, InitiatorExt {

	@Override
	public void doAfterCompose(Page page, Component[] comps) throws Exception {
		//retrieves the token supplier from the request attributes
		CsrfToken tokenSupplier = ((CsrfToken)((HttpServletRequest)Executions.getCurrent().getNativeRequest()).getAttribute("_csrf"));
		//creates meta with the token info which can be read by the JS override
		String metas = "<meta name='_csrf' content='"+tokenSupplier.getToken()+"' />"
					+ "<meta name='_csrf_header' content='"+tokenSupplier.getHeaderName()+"' />";
		((PageCtrl) page).addAfterHeadTags(metas);
		//loads the JS override script
		Script jsScript = new Script();
		jsScript.setSrc("~./static/js/csrf-header-override.js");
		page.getFirstRoot().appendChild(jsScript);
	}

	@Override
	public boolean doCatch(Throwable ex) throws Exception {
		throw new RuntimeException(ex);
	}

	@Override
	public void doFinally() throws Exception {
	}

	@Override
	public void doInit(Page page, Map<String, Object> args) throws Exception {
	}

}
