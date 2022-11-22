package org.zkoss.zkspringboot.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TestService {

	@Value("${app.code}")
	private String appCode;
	public Date getTime() {
		return new Date();
	}

	public String getAppCode(){
		return  appCode;
	}
}
