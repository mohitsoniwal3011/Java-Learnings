package com.aspect_programming.aspect_programming_demo;

import org.apache.catalina.core.ApplicationContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.reactive.context.AnnotationConfigReactiveWebApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class AspectProgrammingDemoApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(AspectProgrammingDemoApplication.class, args);

		Greet g = context.getBean(Greet.class);
		g.show();
		System.out.println("======");
		g.show("Mohit");

    }

}
