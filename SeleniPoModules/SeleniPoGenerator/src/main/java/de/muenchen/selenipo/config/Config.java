package de.muenchen.selenipo.config;

import org.springframework.context.annotation.Bean;

import de.muenchen.selenipo.GeneratorService;
import de.muenchen.selenipo.impl.GeneratorServiceImpl;

public class Config {

	@Bean
	public GeneratorService generatorService() {
		return new GeneratorServiceImpl();
	}
}