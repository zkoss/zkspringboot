package org.zkoss.zkspringboot.demo.service;

import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TestService {
	public Date getTime() {
		return new Date();
	}
}
