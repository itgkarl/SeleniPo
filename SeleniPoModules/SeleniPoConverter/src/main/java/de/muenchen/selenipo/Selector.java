package de.muenchen.selenipo;

import java.util.LinkedList;
import java.util.List;

import org.openqa.selenium.By;

import de.muenchen.selenipo.util.ByFactory;

public enum Selector {

	LINK("link"), XPATH("xpath"), HREF("href"), CHECKBOX("checkbox"), RADIOBUTTON(
			"radioButton"), INPUT("input"), BUTTON("button"), TAGNAME("tagName"), OPTION(
			"option"), ID("id"), TEXTAREA("textArea"), TEXTFIELD("textField"), SELECT(
			"select");

	private static final ByFactory by = new ByFactory();
	private String byMethodName;

	private Selector(String byMethodName) {
		this.byMethodName = byMethodName;
	}

	public String getByMethodName() {
		return byMethodName;
	}

	public void setByMethodName(String byMethodName) {
		this.byMethodName = byMethodName;
	}

	public By by(final String locator) {
		By returnBy = null;
		switch (this) {
		case LINK:
			returnBy = by.link(locator);
			break;
		case XPATH:
			returnBy = by.xpath(locator);
			break;
		case HREF:
			returnBy = by.href(locator);
			break;
		case CHECKBOX:
			returnBy = by.checkbox(locator);
			break;
		case RADIOBUTTON:
			returnBy = by.radioButton(locator);
			break;
		case INPUT:
			returnBy = by.input(locator);
			break;
		case BUTTON:
			returnBy = by.button(locator);
			break;
		case TAGNAME:
			returnBy = by.tagName(locator);
			break;
		case OPTION:
			returnBy = by.option(locator);
			break;
		case ID:
			returnBy = by.id(locator);
			break;
		case TEXTAREA:
			returnBy = by.textArea(locator);
			break;
		case TEXTFIELD:
			returnBy = by.textField(locator);
			break;
		case SELECT:
			returnBy = by.select(locator);
			break;
		default:
			break;
		}
		return returnBy;
	}

	public static final List<Selector> getParsableSelectors() {
		List<Selector> selectors = new LinkedList<Selector>();
		selectors.add(LINK);
		selectors.add(INPUT);
		selectors.add(BUTTON);
		selectors.add(ID);
		selectors.add(HREF);
		selectors.add(CHECKBOX);
		selectors.add(RADIOBUTTON);
		selectors.add(OPTION);
		selectors.add(TEXTAREA);
		selectors.add(TEXTFIELD);
		selectors.add(SELECT);
		return selectors;
	}
}
