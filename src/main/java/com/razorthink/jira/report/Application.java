package com.razorthink.jira.report;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class Application {
	public static void main(String[] args){
		new SpringApplicationBuilder(Application.class).showBanner(false).run(args);
	}
}
