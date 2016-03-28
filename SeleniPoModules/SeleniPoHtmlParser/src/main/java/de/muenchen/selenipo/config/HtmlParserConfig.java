package de.muenchen.selenipo.config;

import org.springframework.context.annotation.Bean;

import de.muenchen.selenipo.HtmlParserService;
import de.muenchen.selenipo.IdentifierStrategy;
import de.muenchen.selenipo.impl.HtmlParserServiceImpl;
import de.muenchen.selenipo.impl.IdentifierBaseStrat;

public class HtmlParserConfig {

	@Bean
	HtmlParserService htmlParserService() {
		return new HtmlParserServiceImpl();
	}

	/**
	 * Setzt initial die basisstrategie
	 */
	@Bean
	IdentifierStrategy identifierStrategy() {
		return new IdentifierBaseStrat();
	}

}
