package de.muenchen.selenipo.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

import com.thoughtworks.xstream.XStream;

import de.muenchen.selenipo.ConverterService;
import de.muenchen.selenipo.impl.ConverterServiceImpl;

/**
 * Class with beandeclarations for springcontext.
 * 
 * @author matthias
 *
 */
@Configurable
public class ConverterConfig {

	@Bean
	ConverterService persistService() {
		return new ConverterServiceImpl();
	}

	@Bean
	XStream xStream() {
		return new XStream();
	}

}