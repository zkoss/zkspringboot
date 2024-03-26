package org.zkoss.zkspringboot;

import jakarta.annotation.PostConstruct;
import org.slf4j.*;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.zkoss.clientbind.BinderPropertiesRenderer;
import org.zkoss.lang.Library;
import org.zkoss.zk.ui.WebApps;

@Configuration
@EnableConfigurationProperties({ZkProperties.class})
@ConditionalOnClass(org.zkoss.clientbind.Version.class)
public class ZkClientBindStatelessAutoConfiguration {

    private final ZkProperties zkProperties;

    public ZkClientBindStatelessAutoConfiguration(ZkProperties zkProperties) {
        this.zkProperties = zkProperties;
    }

    private static final Logger logger = LoggerFactory.getLogger(ZkClientBindStatelessAutoConfiguration.class);

    protected void binderPropertiesRenderer() {
        if (!zkProperties.isClientMvvmListenerEnabled())
            return;
        try {
            WebApps.getCurrent().getConfiguration().addListener(BinderPropertiesRenderer.class);
            logger.info("ZK-Springboot: add org.zkoss.clientbind.BinderPropertiesRenderer");
        } catch (Exception e) {
            logger.error("ZK-Springboot: Failed to add org.zkoss.clientbind.BinderPropertiesRenderer", e);
        }
    }


    protected void globallyEnabled(){
        if (zkProperties.isClientMvvmGloballyEnabled()) {
            Library.setProperty("org.zkoss.bind.defaultComposer.class", "org.zkoss.clientbind.ClientBindComposer");
            logger.info("ZK-Springboot: enable client MVVM globally");
        }
    }

    @PostConstruct
    public void configure() {
        binderPropertiesRenderer();
        globallyEnabled();
    }
}
