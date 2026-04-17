package org.zkoss.zkspringboot.demo;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Locale;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.context.WebApplicationContext;
import org.zkoss.util.resource.LabelLocator;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.http.WebManager;

/**
 * Workaround for ZK's built-in {@code ServletLabelLocator} being unable to resolve
 * certain path prefixes in {@code <label-location>} entries in {@code zk.xml}.
 *
 * <p>ZK's {@code WebManager} wraps every {@code <label-location>} entry in a
 * {@code ServletLabelLocator} which calls {@code ServletContext.getResource(path)}.
 * The following prefixes are not valid servlet context paths and fail:
 * <ul>
 *   <li>{@code classpath:} — Spring Boot concept; Tomcat returns {@code null},
 *       Jetty EE10 throws {@code MalformedURLException}.</li>
 *   <li>{@code ~./} — ZK's own classpath-resource prefix; same failure mode.</li>
 * </ul>
 *
 * <p>For each such path this configuration removes the offending
 * {@code ServletLabelLocator} and registers a {@link ClasspathLabelLocator} that
 * resolves the resource through the thread context {@link ClassLoader} instead.
 *
 * <p>Uses {@link WebManager#addActivationListener} so it fires correctly in both normal
 * JAR mode (WebManager exists at {@code ContextRefreshedEvent} time) and ZATS mode
 * (WebManager is created lazily on the first test request).
 */
@Configuration
public class LabelLocationConfig {

    private static final Logger logger = LoggerFactory.getLogger(LabelLocationConfig.class);

    @Bean
    public ApplicationListener<ContextRefreshedEvent> classpathLabelLocatorRegistrar() {
        return event -> {
            if (!(event.getApplicationContext() instanceof WebApplicationContext wac)) return;
            WebManager.addActivationListener(wac.getServletContext(), webManager -> {
                String[] locations = webManager.getWebApp().getConfiguration().getLabelLocations();
                if (locations.length == 0) return;

                removeServletLabelLocators();

                for (String loc : locations) {
                    if (ClasspathLabelLocator.handles(loc)) {
                        Labels.register(new ClasspathLabelLocator(loc));
                        logger.info("LabelLocationConfig: registered ClasspathLabelLocator for {}", loc);
                    }
                }
            });
        };
    }

    @SuppressWarnings("unchecked")
    private void removeServletLabelLocators() {
        try {
            Field loaderField = Labels.class.getDeclaredField("_loader");
            loaderField.setAccessible(true);
            Object loader = loaderField.get(null);

            Field locatorsField = loader.getClass().getDeclaredField("_locators");
            locatorsField.setAccessible(true);
            Set<Object> locators = (Set<Object>) locatorsField.get(loader);

            // Cache _path field before iterating to avoid repeated getDeclaredField per element
            Field pathField = null;
            for (Object loc : locators.toArray()) {
                if (!"org.zkoss.web.util.resource.ServletLabelLocator".equals(loc.getClass().getName()))
                    continue;
                try {
                    if (pathField == null) {
                        pathField = loc.getClass().getDeclaredField("_path");
                        pathField.setAccessible(true);
                    }
                    String path = (String) pathField.get(loc);
                    if (ClasspathLabelLocator.handles(path)) {
                        locators.remove(loc);
                    }
                } catch (Exception e) {
                    // skip this locator
                }
            }
        } catch (Exception e) {
            logger.warn("LabelLocationConfig: could not remove ServletLabelLocators: {}", e.getMessage());
        }
    }

    /**
     * Resolves {@code classpath:} and {@code ~./} label-location paths via the thread
     * context {@link ClassLoader}, bypassing ZK's {@code ServletLabelLocator} which
     * cannot handle these prefixes.
     */
    static class ClasspathLabelLocator implements LabelLocator {

        /** Spring Boot classpath prefix. */
        static final String CLASSPATH_PREFIX = "classpath:";

        /** ZK's own classpath-resource prefix. */
        static final String TILDE_PREFIX = "~./";

        private final String originalPath;
        private final String resourcePath; // path relative to classpath root, no leading slash

        ClasspathLabelLocator(String path) {
            this.originalPath = path;
            String raw;
            if (path.toLowerCase(Locale.ENGLISH).startsWith(CLASSPATH_PREFIX)) {
                raw = path.substring(CLASSPATH_PREFIX.length());
            } else {
                raw = path.substring(TILDE_PREFIX.length());
            }
            this.resourcePath = raw.startsWith("/") ? raw.substring(1) : raw;
        }

        static boolean handles(String path) {
            if (path == null) return false;
            String lower = path.toLowerCase(Locale.ENGLISH);
            return lower.startsWith(CLASSPATH_PREFIX) || lower.startsWith(TILDE_PREFIX);
        }

        @Override
        public URL locate(Locale locale) throws IOException {
            if (locale != null) {
                int dot = resourcePath.lastIndexOf('.');
                String base = dot >= 0 ? resourcePath.substring(0, dot) : resourcePath;
                String ext  = dot >= 0 ? resourcePath.substring(dot) : "";

                String localeTag = locale.toString();
                while (!localeTag.isEmpty()) {
                    URL url = load(base + "_" + localeTag + ext);
                    if (url != null) return url;
                    int last = localeTag.lastIndexOf('_');
                    if (last < 0) break;
                    localeTag = localeTag.substring(0, last);
                }
            }
            return load(resourcePath);
        }

        private URL load(String path) {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            if (cl == null) cl = ClasspathLabelLocator.class.getClassLoader();
            return cl.getResource(path);
        }

        @Override public boolean equals(Object o) {
            return o instanceof ClasspathLabelLocator other && originalPath.equals(other.originalPath);
        }
        @Override public int hashCode() { return originalPath.hashCode(); }
        @Override public String toString() { return "ClasspathLabelLocator[" + originalPath + "]"; }
    }
}
