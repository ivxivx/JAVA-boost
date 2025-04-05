package poc.boost;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import poc.boost.app.AppConfig;

/**
 * The @SpringBootApplication annotation (or, more precisely the inferred @ComponentScan annotation) by default only scans the classpath next to and below the annotated class.
 *
 * So, all configuration class must be placed next to or in a sub package of you Application class.
 */
@EnableCaching
@Import(AppConfig.class)
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class BoostApplication {
	private static ConfigurableApplicationContext context;

	public static void main(String[] args) {
		context = SpringApplication.run(BoostApplication.class, args);
	}

	public static ApplicationContext restart(String profile) {
		context.close();
		context = SpringApplication.run(BoostApplication.class, "--spring.profiles.active=" + profile);

		return context;
//		Thread thread = new Thread(() -> {
//			context.close();
//			context = SpringApplication.run(BoostApplication.class, "--spring.profiles.active=" + profile);
//		});
//
//		thread.setDaemon(false);
//		thread.start();
	}
}
