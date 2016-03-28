package de.muenchen.selenipo.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.jsoup.nodes.Element;

import de.muenchen.selenipo.IdentifierStrategy;

public class IdentifierBaseStrat implements IdentifierStrategy {

	@Override
	public String genIdentefier(String prefix, Element element) {
		String returnIdentefier = prefix;
		String suffix = "";
		if (element.hasText()) {
			suffix = prepareIdentefierString(element.text());
		}
		if (suffix.equals("") && element.hasAttr("value")) {
			suffix = prepareIdentefierString(element.attr("value"));
		}
		if (suffix.equals("") && element.hasAttr("name")) {
			suffix = prepareIdentefierString(element.attr("name"));
		}
		if (suffix.equals("") && element.hasAttr("id")) {
			suffix = prepareIdentefierString(element.attr("id"));
		}
		if (suffix.equals("") && element.hasAttr("title")) {
			suffix = prepareIdentefierString(element.attr("title"));
		}

		if (suffix.equals("")) {
			suffix = RandomStringUtils.randomAlphanumeric(6);
		}
		if (suffix.length() > 10) {
			suffix = suffix.substring(0, 10);
		}

		return returnIdentefier + suffix;
	}

	private String prepareIdentefierString(String str) {
		return WordUtils.capitalize(str).replaceAll("\\s+", "")
				.replaceAll("[^a-zA-Z]+", "");
	}

}
