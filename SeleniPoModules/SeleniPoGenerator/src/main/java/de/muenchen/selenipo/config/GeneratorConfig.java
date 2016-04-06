package de.muenchen.selenipo.config;

import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.apache.velocity.tools.generic.DisplayTool;
import org.springframework.context.annotation.Bean;

import de.muenchen.selenipo.GeneratorService;
import de.muenchen.selenipo.impl.GeneratorServiceImpl;

public class GeneratorConfig {

	private static final Logger logger = Logger
			.getLogger(GeneratorConfig.class);

	@Bean
	public GeneratorService generatorService() {
		return new GeneratorServiceImpl();
	}

	@Bean(name = "classpath")
	public VelocityEngine velocityEngineClasspath() {
		logger.info("Using VelocityEngine for classpath.");
		VelocityEngine ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		ve.setProperty("classpath.resource.loader.class",
				ClasspathResourceLoader.class.getName());
		ve.setProperty("runtime.log.logsystem.log4j.logger", "velocity");
		ve.init();
		return ve;
	}

	@Bean(name = "filesystem")
	public VelocityEngine velocityEngineFilesystem() {
		logger.info("Using VelocityEngine for classpath.");
		VelocityEngine ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "file");
		ve.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, "./");
		ve.setProperty("runtime.log.logsystem.log4j.logger", "velocity");
		ve.init();
		return ve;
	}

	@Bean
	DisplayTool displayTool() {
		return new DisplayTool();
	}
}
