package com.boris;

import ch.qos.logback.ext.spring.web.LogbackConfigListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

@SpringBootApplication
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
