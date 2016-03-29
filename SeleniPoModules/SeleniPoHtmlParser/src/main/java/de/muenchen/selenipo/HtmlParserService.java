package de.muenchen.selenipo;

import javafx.scene.control.TextField;

import org.apache.log4j.Logger;

import de.muenchen.selenipo.impl.IdentifierBaseStrat;
import de.muenchen.selenipo.impl.IdentifierMatcherStrat;

public interface HtmlParserService {

	PoGeneric parseElementsFromHtmlForType(String html, Selector type,
			IdentifierStrategy identifierStrategy);

	public static class IdentifiertStratFactory {
		private static final Logger logger = Logger
				.getLogger(IdentifiertStratFactory.class);

		public enum IdentifiertStrat {
			BASE, MATCHER;
		}

		public IdentifiertStratFactory() {
			super();
		};

		public static IdentifierStrategy getStrat(IdentifiertStrat strat,
				Object... optionalParams) {
			switch (strat) {
			case BASE:
				return new IdentifierBaseStrat();
			case MATCHER:
				if (optionalParams.length == 3) {
					try {
						return new IdentifierMatcherStrat(
								(TextField) optionalParams[0],
								(TextField) optionalParams[1],
								(TextField) optionalParams[2]);
					} catch (ClassCastException e) {
						System.out.println(e);
						logger.error("Für die IdentifiertStrat"
								+ IdentifiertStrat.MATCHER
								+ "Übergebene Parameter Konnten nicht als TextField gecastet werden.");
					}
				}
			default:
				logger.info("Using default Ident Strat: "
						+ IdentifiertStrat.BASE);
				return new IdentifierBaseStrat();
			}
		}
	}
}
