package org.zkoss.zkspringboot.demo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public class AppConfig {


    @Value("${app.code}")
    public String appCode;
}
