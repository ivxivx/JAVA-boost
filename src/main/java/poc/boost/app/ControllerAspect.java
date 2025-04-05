package poc.boost.app;

import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import poc.boost.config.ConfigService;
import poc.boost.config.Namespace;

@Aspect
@Component
@Slf4j
public class ControllerAspect {
    private static final String LOG_KEY_NAMESPACE = "namespace";
    private static final String LOG_KEY_COMPONENT = "component";

    @Autowired
    private List<BaseController> controllers;

    @Autowired
    private ConfigService configService;

    @PostConstruct
    public void postConstruct() {
        if (controllers != null) {
            //controllers.get
        }
    }

    @Pointcut("execution(public * poc.boost.*.*Controller.*(..))")
    public void ControllerAspect() {

    }

    @Around("ControllerAspect()")
    public Object handle(ProceedingJoinPoint pjp) throws Throwable {
        if (!(pjp.getTarget() instanceof BaseController)) {
            log.error("unsupported aop target, target={}", pjp.getTarget());
            throw new BoostRuntimeException("unsupported aop target");
        }

        if (!(pjp.getSignature() instanceof MethodSignature)) {
            log.error("unsupported aop signature, signature={}", pjp.getSignature());
            throw new BoostRuntimeException("unsupported aop signature");
        }

        Object[] arguments = pjp.getArgs();

        if (arguments == null || arguments.length < 1) {
            log.error("unsupported aop arguments, args={}", arguments);
            throw new BoostRuntimeException("unsupported aop arguments");
        }

        BaseController controller = (BaseController) pjp.getTarget();
        ControllerMetadata controllerMetadata = controller.getClass().getAnnotation(ControllerMetadata.class);
        ControllerType controllerType = controllerMetadata.type();

        Namespace namespace = getNamespace(controllerType, arguments);

        try {
            MDC.put(LOG_KEY_NAMESPACE, namespace.toString());
            MDC.put(LOG_KEY_COMPONENT, controllerType.name().toUpperCase(Locale.ROOT));

            log.debug("request {}", (Object) pjp.getArgs());

            Object response = doHandle(pjp);

            log.debug("response {}", response);

            return response;
        } catch (Throwable t) {
            if (t instanceof RuntimeException) {
                throw t;
            } else {
                throw new BoostRuntimeException(t);
            }
        } finally {
            MDC.put(LOG_KEY_COMPONENT, "");
            MDC.put(LOG_KEY_NAMESPACE, "");
        }
    }

    private Object doHandle(ProceedingJoinPoint pjp) {
        try {
            return pjp.proceed();
        } catch (Throwable t) {
            if (t instanceof RuntimeException) {
                throw (RuntimeException)t;
            } else {
                throw new BoostRuntimeException(t);
            }
        }
    }

    private Namespace getNamespace(ControllerType controllerType, Object[] arguments) {
        Namespace namespace = Namespace.DEFAULT;

        if (arguments != null && arguments.length > 0) {
            if (arguments[0] instanceof Request) {
                Request request = (Request) arguments[0];

                namespace = request.getNamespace();
                configService.setCurrentConfig(namespace);
            }
        }

        return namespace;
    }
}
