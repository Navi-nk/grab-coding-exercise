package com.navi.grabcodingexercise.configuration;

import com.navi.grabcodingexercise.GrabCodingExerciseApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(GrabCodingExerciseApplication.class);
	}

}
