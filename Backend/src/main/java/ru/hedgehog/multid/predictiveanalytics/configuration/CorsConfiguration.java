package ru.hedgehog.multid.predictiveanalytics.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Anikushin Roman
 * Конфигурация веб приложения
 */
@Configuration
@EnableWebMvc
public class CorsConfiguration implements WebMvcConfigurer {

	/*
	 * Данный метод включает CORS для всех страниц.
	 * Допускаются GET и POST запросы.
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedMethods("GET", "POST");
	}
}
