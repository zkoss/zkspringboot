package org.zkoss.zkspringboot;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Pattern;

@ConfigurationProperties(prefix = "zk")
@Validated
public class ZkProperties {
	@Pattern(regexp="(jar)|(war)")
	private String springbootPackaging = "jar";

	private String updateUri = "/zkau";
	private boolean websocketsEnabled = true; //ZK-EE only
	private boolean servlet3PushEnabled = true; //ZK-EE only

	private String homepage = null;
	private boolean zulViewResolverEnabled = true;
	private String zulViewResolverPrefix = "";
	private String zulViewResolverSuffix = ".zul";

	private String richletFilterMapping;

	public String getSpringbootPackaging() {
		return springbootPackaging;
	}

	public void setSpringbootPackaging(String springbootPackaging) {
		this.springbootPackaging = springbootPackaging;
	}

	public boolean isWar() {
		return "war".equals(getSpringbootPackaging());
	}

	public String getUpdateUri() {
		return updateUri;
	}

	public void setUpdateUri(String updateUri) {
		this.updateUri = updateUri;
	}

	public boolean isWebsocketsEnabled() {
		return websocketsEnabled;
	}

	public void setWebsocketsEnabled(boolean websocketsEnabled) {
		this.websocketsEnabled = websocketsEnabled;
	}

	public boolean isServlet3PushEnabled() {
		return servlet3PushEnabled;
	}

	public void setServlet3PushEnabled(boolean servlet3PushEnabled) {
		this.servlet3PushEnabled = servlet3PushEnabled;
	}

	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public boolean isZulViewResolverEnabled() {
		return zulViewResolverEnabled;
	}

	public void setZulViewResolverEnabled(boolean zulViewResolverEnabled) {
		this.zulViewResolverEnabled = zulViewResolverEnabled;
	}

	public String getZulViewResolverPrefix() {
		return zulViewResolverPrefix;
	}

	public void setZulViewResolverPrefix(String zulViewResolverPrefix) {
		this.zulViewResolverPrefix = zulViewResolverPrefix;
	}

	public String getZulViewResolverSuffix() {
		return zulViewResolverSuffix;
	}

	public void setZulViewResolverSuffix(String zulViewResolverSuffix) {
		this.zulViewResolverSuffix = zulViewResolverSuffix;
	}

	public String getRichletFilterMapping() {
		return richletFilterMapping;
	}

	public void setRichletFilterMapping(String richletFilterMapping) {
		this.richletFilterMapping = richletFilterMapping;
	}
}
