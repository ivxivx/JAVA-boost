package poc.boost.spring;

import java.util.concurrent.atomic.AtomicBoolean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.stereotype.Component;
import poc.boost.BoostApplication;

@Component
@Slf4j
public class SpringService implements SmartApplicationListener, ApplicationContextAware {
    private AtomicBoolean contextStarted = new AtomicBoolean(false);
    private ApplicationContext applicationContext;

    public void restartApplication(String profile) {
        if (contextStarted.get()) {
            log.info("application context before restart {}", applicationContext);
            this.applicationContext = BoostApplication.restart(profile);
            log.info("application context after restart {}", applicationContext);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        log.info("application context initialized {}", applicationContext);
    }

    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return WebServerInitializedEvent.class.isAssignableFrom(eventType)
            || ContextClosedEvent.class.isAssignableFrom(eventType);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof WebServerInitializedEvent) {
            onApplicationEvent((WebServerInitializedEvent) event);
        }
        else if (event instanceof ContextClosedEvent) {
            onApplicationEvent((ContextClosedEvent) event);
        }
    }

    public void onApplicationEvent(WebServerInitializedEvent event) {
        String contextName = event.getApplicationContext().getServerNamespace();
        if (contextName == null || !contextName.equals("management")) {
            contextStarted.set(true);
        }
    }

    public void onApplicationEvent(ContextClosedEvent event) {
        if (event.getApplicationContext() == applicationContext) {
            contextStarted.set(false);
        }
    }
}
