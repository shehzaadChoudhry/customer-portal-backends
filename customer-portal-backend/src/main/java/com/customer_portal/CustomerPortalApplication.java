package com.customer_portal;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.convert.Jsr310Converters;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableCaching
@EntityScan(basePackageClasses = { CustomerPortalApplication.class, Jsr310Converters.class })
@EnableScheduling
public class CustomerPortalApplication extends SpringBootServletInitializer {
	private static final Logger logger = LoggerFactory.getLogger(CustomerPortalApplication.class);

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(CustomerPortalApplication.class, args);
		logger.info("-----------     ****......  Spring Boot Application Run SuccessFully  .....****     ------------");
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(CustomerPortalApplication.class);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
