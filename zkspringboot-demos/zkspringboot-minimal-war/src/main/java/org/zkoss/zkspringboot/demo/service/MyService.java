package org.zkoss.zkspringboot.demo.service;

import org.springframework.stereotype.Component;

@Component
public class MyService {
    public String service(){
        return "doing service";
    }
}
