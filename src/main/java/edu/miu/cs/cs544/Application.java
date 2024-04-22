package edu.miu.cs.cs544;

import edu.miu.cs.cs544.advice.logging.LoggingAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("edu.miu.cs.cs544.repository")
@EntityScan("edu.miu.cs.cs544.domain")
@EnableAspectJAutoProxy
public class Application {
	private static final Logger logger = LoggerFactory.getLogger(LoggingAdvice.class);

	public static void main(String[] args) {
		try {
			SpringApplication.run(Application.class, args);
			System.out.println("Application started successfully . . . .");
		} catch (Exception e) {
			logger.error("Failed to start the application", e);
		}
	}
}
