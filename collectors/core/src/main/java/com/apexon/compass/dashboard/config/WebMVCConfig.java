package com.apexon.compass.dashboard.config;

import com.apexon.compass.dashboard.mapper.CustomObjectMapper;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.apexon.compass.dashboard.*")
public class WebMVCConfig implements WebMvcConfigurer {

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		MappingJackson2HttpMessageConverter jackson = new MappingJackson2HttpMessageConverter();

		jackson.setObjectMapper(new CustomObjectMapper());
		jackson.getObjectMapper()
			.disable(SerializationFeature.WRITE_NULL_MAP_VALUES)
			.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
			.setSerializationInclusion(JsonInclude.Include.NON_NULL);

		converters.add(jackson);
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
		resolver.setMaxPageSize(Integer.MAX_VALUE);
		argumentResolvers.add(resolver);
	}

	/*
	 * Added for Swagger
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String[] staticResourceMappingPath = { "classpath:/static/" };

		registry.addResourceHandler("/**").addResourceLocations(staticResourceMappingPath);
	}

}
