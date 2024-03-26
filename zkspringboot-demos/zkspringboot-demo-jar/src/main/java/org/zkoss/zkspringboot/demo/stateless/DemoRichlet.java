package org.zkoss.zkspringboot.demo.stateless;

import org.zkoss.stateless.annotation.RichletMapping;
import org.zkoss.stateless.sul.*;
import org.zkoss.stateless.ui.StatelessRichlet;

import java.util.List;

import static java.util.Arrays.asList;

/**
 * a simple stateless richlet
 */
@RichletMapping("/stateless/demo")
public class DemoRichlet implements StatelessRichlet {

    @RichletMapping("")
    public List<IComponent> index() {
        return asList(ILabel.of("stateless label"));
    }

}
