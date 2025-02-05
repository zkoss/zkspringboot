package org.zkoss.zkspringboot.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkspringboot.demo.service.MyService;
import org.zkoss.zul.Label;

@Component
public class MyComposer extends SelectorComposer {
    @Autowired
    MyService myService;
    @Wire
    Label msg;

    @Override
    public void doAfterCompose(org.zkoss.zk.ui.Component comp) throws Exception {
        super.doAfterCompose(comp);
        msg.setValue(myService.service());
    }
}
