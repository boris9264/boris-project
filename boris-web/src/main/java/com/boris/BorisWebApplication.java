package com.boris;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class BorisWebApplication{
/*
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(BorisWebApplication.class);
	}

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		servletContext.setInitParameter("logbackConfigLocation", "classpath:log4j/logback.xml");
		servletContext.addListener(new LogbackConfigListener());
		super.onStartup(servletContext);
	}*/

	public static void main(String[] args) {
		SpringApplication.run(BorisWebApplication.class, args);
	}
}
