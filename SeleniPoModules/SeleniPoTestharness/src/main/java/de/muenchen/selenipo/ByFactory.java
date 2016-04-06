package de.muenchen.selenipo;

import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.openqa.selenium.By;

/**
 * More factories for {@link By} objects.
 *
 * <p>
 * Ursprünglicher Autor: Kohsuke Kawaguchi
 * <p>
 * 
 * @author Matthias Karl
 * @see gitHub/jenkinssci/acceptance-test-harness
 * 
 */
public class ByFactory {
	public By xpath(String xpath) {
		try {
			XPathFactory.newInstance().newXPath().compile(xpath);
		} catch (XPathExpressionException ex) {
			throw new AssertionError("Invalid xpath syntax: " + xpath, ex);
		}
		return By.xpath(xpath);
	}

	public By xpath(String format, Object... args) {
		return xpath(String.format(format, args));
	}

	/**
	 * Returns the "path" selector that finds an element by following the
	 * form-element-path plugin.
	 *
	 * https://wiki.jenkins-ci.org/display/JENKINS/Form+Element+Path+Plugin
	 */
	public By path(String path, Object... args) {
		return css("[path='%s']", String.format(path, args));
	}

	public By url(String path, Object... args) {
		return css("[url='%s']", String.format(path, args));
	}

	public By action(String path, Object... args) {
		return css("[action='%s']", String.format(path, args));
	}

	public By name(String name, Object... args) {
		return css("[name='%s']", String.format(name, args));
	}

	/**
	 * Capybara's :link selector.
	 *
	 * @param locator
	 *            Text, id, title, or image alt attribute of the link
	 */
	public By link(String locator) {
		return xpath(
				".//A[@href][@id='%1$s' or normalize-space(.)='%1$s' or @title='%1$s' or .//img[@alt='%1$s']]",
				locator);
	}

	/**
	 * Link href selector.
	 *
	 * @param locator
	 *            href of the link
	 */
	public By href(String locator) {
		return css("a[href='%s']", locator);
	}

	/**
	 * Finds checkbox.
	 *
	 * @param locator
	 *            Text, id, title.
	 */
	public By checkbox(String locator) {
		return xpath(fieldXPath("input[@type='checkbox' or @type='CHECKBOX']", locator));
	}

	/**
	 * Select radio button by its name, id, or label text.
	 */
	public By radioButton(String locator) {
		return xpath(fieldXPath("input[@type='radio' or @type='RADIO']", locator));
	}

	/**
	 * Finds input fields.
	 *
	 * @param locator
	 *            Text, id, title.
	 */
	public By input(String locator) {
		return xpath(fieldXPath(
				"*[name()='INPUT' or name()='input' or name()='textarea' or name()='TEXTAREA' or name()='select' or name()='SELECT']",
				locator));
	}

	/**
	 * Finds select fields.
	 *
	 * @param locator
	 *            Text, id, title.
	 */
	public By select(String locator) {
		return xpath(fieldXPath("*[name()='select' or name()='SELECT']",
				locator));
	}

	/**
	 * Finds textarea fields.
	 *
	 * @param locator
	 *            Text, id, title.
	 */
	public By textArea(String locator) {
		return xpath(fieldXPath("*[name()='textarea' or name()='TEXTAREA']",
				locator));
	}

	/**
	 * Finds textarea fields.
	 *
	 * @param locator
	 *            Text, id, title.
	 */
	public By textField(String locator) {
		//TODO
		return xpath(fieldXPath("input[./@type = 'text' or ./@type = 'TEXT' or ./@type = 'password' or ./@type = 'PASSWORD' or ./@type = 'search' or ./@type = 'email' or ./@type = 'url' or ./@type = 'tel']", locator));
	}

	private static String fieldXPath(String base, String locator) {
		// TODO: there's actually a lot more
		return String
				.format("  .//%2$s[./@id = '%1$s' or ./@name = '%1$s' or ./@value = '%1$s' or ./@placeholder = '%1$s' or ./@id = //label[contains(normalize-space(.), '%1$s')]/@for]"
						+ "| .//label[contains(normalize-space(.), '%1$s')]//%2$s"
						+ "| .//label[contains(normalize-space(.), '%1$s')][@class='attach-previous']/preceding-sibling::%2$s",
						locator, base);
	}

	/**
	 * Finds a button
	 */
	public By button(String locator) {
		return xpath(
				".//input[./@type = 'submit' or ./@type = 'reset' or ./@type = 'image' or ./@type = 'button'][((./@id = '%1$s' or ./@name = '%1$s' or contains(./@value, '%1$s')) or contains(./@title, '%1$s'))] | .//input[./@type = 'image'][contains(./@alt, '%1$s')] | .//button[(((./@id = '%1$s' or contains(./@value, '%1$s')) or contains(normalize-space(.), '%1$s')) or contains(./@title, '%1$s'))] | .//input[./@type = 'image'][contains(./@alt, '%1$s')]",
				locator);
	}

	public By css(String css, Object... args) {
		return By.cssSelector(String.format(css, args));
	}

	public By tagName(String name) {
		return By.tagName(name);
	}

	public By option(String name) {
		return xpath(
				".//option[contains(normalize-space(.), '%1$s') or @value='%1$s']",
				name);
	}

	public By id(String s) {
		return css("#" + s);
	}

	public By parent() {
		return xpath("..");
	}

	public By ancestor(String tagName) {
		return xpath("ancestor::%s[1]", tagName);
	}
}
