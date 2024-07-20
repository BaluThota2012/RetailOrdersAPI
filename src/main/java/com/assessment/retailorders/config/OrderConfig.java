package com.assessment.retailorders.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.assessment.retailorders.interceptor.RequestInterceptor;

@Configuration
public class OrderConfig implements WebMvcConfigurer {
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		WebMvcConfigurer.super.addInterceptors(registry);
		registry.addWebRequestInterceptor(new RequestInterceptor());
	}
	
}
