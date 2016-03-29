package de.muenchen.selenipo.impl;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

import de.muenchen.selenipo.HtmlParserService;
import de.muenchen.selenipo.IdentifierStrategy;
import de.muenchen.selenipo.PoGeneric;
import de.muenchen.selenipo.Selector;
import de.muenchen.selenipo.impl.persistanceModel.ElementImpl;
import de.muenchen.selenipo.impl.persistanceModel.PoGenericImpl;

public class HtmlParserServiceImpl implements HtmlParserService {
	private static final Logger logger = Logger
			.getLogger(HtmlParserServiceImpl.class);

	@Autowired
	private IdentifierStrategy identifierStrat;

	@Override
	public PoGeneric parseElementsFromHtmlForType(String html, Selector type,
			IdentifierStrategy identifierStrategy) {
		this.identifierStrat = identifierStrategy;
		PoGeneric poGeneric = null;
		switch (type) {
		case LINK:
			poGeneric = parseElementsFromHtmlForLink(html);
			break;
		case INPUT:
			poGeneric = parseElementsFromHtmlForInput(html);
			break;
		case BUTTON:
			poGeneric = parseElementsFromHtmlForButton(html);
			break;
		case ID:
			poGeneric = parseElementsFromHtmlForId(html);
			break;
		case HREF:
			poGeneric = parseElementsFromHtmlForHref(html);
			break;
		case CHECKBOX:
			poGeneric = parseElementsFromHtmlForCheckBox(html);
			break;
		case RADIOBUTTON:
			poGeneric = parseElementsFromHtmlForRadioButton(html);
			break;
		case OPTION:
			poGeneric = parseElementsFromHtmlForOption(html);
			break;
		case TEXTAREA:
			poGeneric = parseElementsFromHtmlForTextArea(html);
			break;
		case TEXTFIELD:
			poGeneric = parseElementsFromHtmlForTextField(html);
			break;
		case SELECT:
			poGeneric = parseElementsFromHtmlForSelect(html);
			break;

		default:
			logger.warn(String.format("Der Selector %s wird nicht unterstützt",
					type));
			break;
		}
		return poGeneric;
	}

	private PoGeneric parseElementsFromHtmlForSelect(String html) {
		final String PREFIX = "sel";
		PoGeneric poGeneric = new PoGenericImpl();
		Document doc = Jsoup.parse(html);
		Elements elements = doc.select("select");
		for (Element element : elements) {
			if (element.hasAttr("id")) {
				de.muenchen.selenipo.Element createdElement = createElement(
						identifierStrat.genIdentefier(PREFIX, element),
						Selector.SELECT, element.attr("id"));
				poGeneric.getElements().add(createdElement);
			}

			else if (element.hasAttr("name")) {
				de.muenchen.selenipo.Element createdElement = createElement(
						identifierStrat.genIdentefier(PREFIX, element),
						Selector.SELECT, element.attr("name"));
				poGeneric.getElements().add(createdElement);
			}

			else if (element.hasAttr("value")) {
				de.muenchen.selenipo.Element createdElement = createElement(
						identifierStrat.genIdentefier(PREFIX, element),
						Selector.SELECT, element.attr("value"));
				poGeneric.getElements().add(createdElement);
			}

			else if (element.hasAttr("placeholder")) {
				de.muenchen.selenipo.Element createdElement = createElement(
						identifierStrat.genIdentefier(PREFIX, element),
						Selector.SELECT, element.attr("placeholder"));
				poGeneric.getElements().add(createdElement);
			}

		}

		return poGeneric;
	}

	private PoGeneric parseElementsFromHtmlForTextField(String html) {
		final String PREFIX = "tf";
		PoGeneric poGeneric = new PoGenericImpl();
		Document doc = Jsoup.parse(html);
		Elements elements = doc
				.select("input[type = text], input[type = search],input[type = password],input[type = tel],input[type = url],input[type = email]");
		for (Element element : elements) {
			if (element.hasAttr("id")) {
				de.muenchen.selenipo.Element createdElement = createElement(
						identifierStrat.genIdentefier(PREFIX, element),
						Selector.TEXTFIELD, element.attr("id"));
				poGeneric.getElements().add(createdElement);
			}

			else if (element.hasAttr("name")) {
				de.muenchen.selenipo.Element createdElement = createElement(
						identifierStrat.genIdentefier(PREFIX, element),
						Selector.TEXTFIELD, element.attr("name"));
				poGeneric.getElements().add(createdElement);
			}

			else if (element.hasAttr("value")) {
				de.muenchen.selenipo.Element createdElement = createElement(
						identifierStrat.genIdentefier(PREFIX, element),
						Selector.TEXTFIELD, element.attr("value"));
				poGeneric.getElements().add(createdElement);
			}

			else if (element.hasAttr("placeholder")) {
				de.muenchen.selenipo.Element createdElement = createElement(
						identifierStrat.genIdentefier(PREFIX, element),
						Selector.TEXTFIELD, element.attr("placeholder"));
				poGeneric.getElements().add(createdElement);
			}

		}

		return poGeneric;
	}

	private PoGeneric parseElementsFromHtmlForTextArea(String html) {
		final String PREFIX = "ta";
		PoGeneric poGeneric = new PoGenericImpl();
		Document doc = Jsoup.parse(html);
		Elements elements = doc.select("textarea");
		for (Element element : elements) {
			if (element.hasAttr("id")) {
				de.muenchen.selenipo.Element createdElement = createElement(
						identifierStrat.genIdentefier(PREFIX, element),
						Selector.TEXTAREA, element.attr("id"));
				poGeneric.getElements().add(createdElement);
			}

			else if (element.hasAttr("name")) {
				de.muenchen.selenipo.Element createdElement = createElement(
						identifierStrat.genIdentefier(PREFIX, element),
						Selector.TEXTAREA, element.attr("name"));
				poGeneric.getElements().add(createdElement);
			}

			else if (element.hasAttr("value")) {
				de.muenchen.selenipo.Element createdElement = createElement(
						identifierStrat.genIdentefier(PREFIX, element),
						Selector.TEXTAREA, element.attr("value"));
				poGeneric.getElements().add(createdElement);
			}

			else if (element.hasAttr("placeholder")) {
				de.muenchen.selenipo.Element createdElement = createElement(
						identifierStrat.genIdentefier(PREFIX, element),
						Selector.TEXTAREA, element.attr("placeholder"));
				poGeneric.getElements().add(createdElement);
			}

		}

		return poGeneric;
	}

	private PoGeneric parseElementsFromHtmlForOption(String html) {
		final String PREFIX = "opt";
		PoGeneric poGeneric = new PoGenericImpl();
		Document doc = Jsoup.parse(html);
		Elements elements = doc.select("option");
		for (Element element : elements) {
			if (element.hasAttr("id")) {
				de.muenchen.selenipo.Element createdElement = createElement(
						identifierStrat.genIdentefier(PREFIX, element),
						Selector.OPTION, element.attr("id"));
				poGeneric.getElements().add(createdElement);
			} else if (element.hasAttr("value")) {
				de.muenchen.selenipo.Element createdElement = createElement(
						identifierStrat.genIdentefier(PREFIX, element),
						Selector.OPTION, element.attr("value"));
				poGeneric.getElements().add(createdElement);
			} else if (element.hasText()) {
				de.muenchen.selenipo.Element createdElement = createElement(
						identifierStrat.genIdentefier(PREFIX, element),
						Selector.OPTION, element.text());
				poGeneric.getElements().add(createdElement);
			}

		}
		return poGeneric;
	}

	private PoGeneric parseElementsFromHtmlForRadioButton(String html) {
		final String PREFIX = "rb";
		PoGeneric poGeneric = new PoGenericImpl();
		Document doc = Jsoup.parse(html);
		Elements elements = doc.select("input[type=radio]");
		for (Element element : elements) {
			if (element.hasAttr("id")) {
				de.muenchen.selenipo.Element createdElement = createElement(
						identifierStrat.genIdentefier(PREFIX, element),
						Selector.RADIOBUTTON, element.attr("id"));
				poGeneric.getElements().add(createdElement);
			}

			else if (element.hasAttr("name")) {
				de.muenchen.selenipo.Element createdElement = createElement(
						identifierStrat.genIdentefier(PREFIX, element),
						Selector.RADIOBUTTON, element.attr("name"));
				poGeneric.getElements().add(createdElement);
			}

			else if (element.hasAttr("value")) {
				de.muenchen.selenipo.Element createdElement = createElement(
						identifierStrat.genIdentefier(PREFIX, element),
						Selector.RADIOBUTTON, element.attr("value"));
				poGeneric.getElements().add(createdElement);
			}

			else if (element.hasAttr("placeholder")) {
				de.muenchen.selenipo.Element createdElement = createElement(
						identifierStrat.genIdentefier(PREFIX, element),
						Selector.RADIOBUTTON, element.attr("placeholder"));
				poGeneric.getElements().add(createdElement);
			}
		}
		return poGeneric;
	}

	private PoGeneric parseElementsFromHtmlForCheckBox(String html) {
		final String PREFIX = "cb";
		PoGeneric poGeneric = new PoGenericImpl();
		Document doc = Jsoup.parse(html);
		Elements elements = doc.select("input[type=checkbox]");
		for (Element element : elements) {
			if (element.hasAttr("id")) {
				de.muenchen.selenipo.Element createdElement = createElement(
						identifierStrat.genIdentefier(PREFIX, element),
						Selector.CHECKBOX, element.attr("id"));
				poGeneric.getElements().add(createdElement);
			}

			else if (element.hasAttr("name")) {
				de.muenchen.selenipo.Element createdElement = createElement(
						identifierStrat.genIdentefier(PREFIX, element),
						Selector.CHECKBOX, element.attr("name"));
				poGeneric.getElements().add(createdElement);
			}

			else if (element.hasAttr("value")) {
				de.muenchen.selenipo.Element createdElement = createElement(
						identifierStrat.genIdentefier(PREFIX, element),
						Selector.CHECKBOX, element.attr("value"));
				poGeneric.getElements().add(createdElement);
			}

			else if (element.hasAttr("placeholder")) {
				de.muenchen.selenipo.Element createdElement = createElement(
						identifierStrat.genIdentefier(PREFIX, element),
						Selector.CHECKBOX, element.attr("placeholder"));
				poGeneric.getElements().add(createdElement);
			}
		}
		return poGeneric;
	}

	private PoGeneric parseElementsFromHtmlForHref(String html) {
		final String PREFIX = "href";
		PoGeneric poGeneric = new PoGenericImpl();
		Document doc = Jsoup.parse(html);
		Elements elements = doc.select("a[href]");
		for (Element element : elements) {
			if (element.hasAttr("href")) {
				de.muenchen.selenipo.Element createdElement = createElement(
						identifierStrat.genIdentefier(PREFIX, element),
						Selector.HREF, element.attr("href"));
				poGeneric.getElements().add(createdElement);
			}
		}
		return poGeneric;
	}

	private PoGeneric parseElementsFromHtmlForId(String html) {
		final String PREFIX = "id";
		PoGeneric poGeneric = new PoGenericImpl();
		Document doc = Jsoup.parse(html);
		Elements elements = doc.select("[id]");
		for (Element element : elements) {
			if (element.hasAttr("id")) {
				de.muenchen.selenipo.Element createdElement = createElement(
						identifierStrat.genIdentefier(PREFIX, element),
						Selector.ID, element.attr("id"));
				poGeneric.getElements().add(createdElement);
			}
		}
		return poGeneric;
	}

	/**
	 * Sucht nach allen Links für den vorhanden ist: id or text or title
	 * 
	 * @param html
	 *            Quelltext der zu untersuchenden Seite
	 * @return PoGeneric- neues Page Object mit den gefundenen Elementen
	 */
	private PoGeneric parseElementsFromHtmlForLink(String html) {
		final String PREFIX = "a";
		PoGeneric poGeneric = new PoGenericImpl();
		Document doc = Jsoup.parse(html);
		Elements elements = doc.select("a");
		for (Element element : elements) {
			if (element.hasAttr("id")) {
				de.muenchen.selenipo.Element createdElement = createElement(
						identifierStrat.genIdentefier(PREFIX, element),
						Selector.LINK, element.attr("id"));
				poGeneric.getElements().add(createdElement);
			}

			else if (element.hasText()) {
				de.muenchen.selenipo.Element createdElement = createElement(
						identifierStrat.genIdentefier(PREFIX, element),
						Selector.LINK, element.text());
				poGeneric.getElements().add(createdElement);
			}

			else if (element.hasAttr("title")) {
				de.muenchen.selenipo.Element createdElement = createElement(
						identifierStrat.genIdentefier(PREFIX, element),
						Selector.LINK, element.attr("title"));
				poGeneric.getElements().add(createdElement);
			}
		}

		return poGeneric;
	}

	private PoGeneric parseElementsFromHtmlForInput(String html) {
		final String PREFIX = "i";
		PoGeneric poGeneric = new PoGenericImpl();
		Document doc = Jsoup.parse(html);
		Elements elements = doc.select("textarea,input,select");
		for (Element element : elements) {
			if (element.hasAttr("id")) {
				de.muenchen.selenipo.Element createdElement = createElement(
						identifierStrat.genIdentefier(PREFIX, element),
						Selector.INPUT, element.attr("id"));
				poGeneric.getElements().add(createdElement);
			}

			else if (element.hasAttr("name")) {
				de.muenchen.selenipo.Element createdElement = createElement(
						identifierStrat.genIdentefier(PREFIX, element),
						Selector.INPUT, element.attr("name"));
				poGeneric.getElements().add(createdElement);
			}

			else if (element.hasAttr("value")) {
				de.muenchen.selenipo.Element createdElement = createElement(
						identifierStrat.genIdentefier(PREFIX, element),
						Selector.INPUT, element.attr("value"));
				poGeneric.getElements().add(createdElement);
			}

			else if (element.hasAttr("placeholder")) {
				de.muenchen.selenipo.Element createdElement = createElement(
						identifierStrat.genIdentefier(PREFIX, element),
						Selector.INPUT, element.attr("placeholder"));
				poGeneric.getElements().add(createdElement);
			}

		}

		return poGeneric;
	}

	private PoGeneric parseElementsFromHtmlForButton(String html) {
		final String PREFIX = "b";
		PoGeneric poGeneric = new PoGenericImpl();
		Document doc = Jsoup.parse(html);
		Elements elementsInput = doc
				.select("input[type=\"submit\"],input[type=\"reset\"],input[type=\"image\"],input[type=\"button\"]");
		for (Element element : elementsInput) {
			if (element.hasAttr("id")) {
				de.muenchen.selenipo.Element createdElement = createElement(
						identifierStrat.genIdentefier(PREFIX, element),
						Selector.BUTTON, element.attr("id"));
				poGeneric.getElements().add(createdElement);
			}

			else if (element.hasAttr("name")) {
				de.muenchen.selenipo.Element createdElement = createElement(
						identifierStrat.genIdentefier(PREFIX, element),
						Selector.BUTTON, element.attr("name"));
				poGeneric.getElements().add(createdElement);
			}

			else if (element.hasAttr("value")) {
				de.muenchen.selenipo.Element createdElement = createElement(
						identifierStrat.genIdentefier(PREFIX, element),
						Selector.BUTTON, element.attr("value"));
				poGeneric.getElements().add(createdElement);
			}

			else if (element.hasAttr("title")) {
				de.muenchen.selenipo.Element createdElement = createElement(
						identifierStrat.genIdentefier(PREFIX, element),
						Selector.BUTTON, element.attr("title"));
				poGeneric.getElements().add(createdElement);
			} else if (element.hasAttr("type")
					&& element.attr("type").equals("image")) {
				if (element.hasAttr("alt")) {
					de.muenchen.selenipo.Element createdElement = createElement(
							identifierStrat.genIdentefier(PREFIX, element),
							Selector.BUTTON, element.attr("alt"));
					poGeneric.getElements().add(createdElement);
				}
			}

		}

		Elements elementsButton = doc.select("button");
		for (Element element : elementsButton) {
			if (element.hasAttr("id")) {
				de.muenchen.selenipo.Element createdElement = createElement(
						identifierStrat.genIdentefier(PREFIX, element),
						Selector.BUTTON, element.attr("id"));
				poGeneric.getElements().add(createdElement);
			}

			else if (element.hasAttr("value")) {
				de.muenchen.selenipo.Element createdElement = createElement(
						identifierStrat.genIdentefier(PREFIX, element),
						Selector.BUTTON, element.attr("value"));
				poGeneric.getElements().add(createdElement);
			} else if (element.hasText()) {
				de.muenchen.selenipo.Element createdElement = createElement(
						identifierStrat.genIdentefier(PREFIX, element),
						Selector.BUTTON, element.text());
				poGeneric.getElements().add(createdElement);
			}

			else if (element.hasAttr("title")) {
				de.muenchen.selenipo.Element createdElement = createElement(
						identifierStrat.genIdentefier(PREFIX, element),
						Selector.BUTTON, element.attr("title"));
				poGeneric.getElements().add(createdElement);
			}

		}

		return poGeneric;
	}

	private void chooseIdentifierStrat() {
		setIdentifierStrategy(new IdentifierBaseStrat());
	}

	private de.muenchen.selenipo.Element createElement(String identifier,
			Selector type, String locator) {
		de.muenchen.selenipo.Element elem = new ElementImpl(identifier, type,
				locator);
		return elem;
	}

	public IdentifierStrategy getIdentifierStrategy() {
		return identifierStrat;
	}

	public void setIdentifierStrategy(IdentifierStrategy identifierStrategy) {
		this.identifierStrat = identifierStrategy;
	}

}
