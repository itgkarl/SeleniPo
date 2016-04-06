/*
 * Projekt: KiTa Gebühren
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2014
 */
package de.muenchen.selenipo.po;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

/**
 * @author IT@M/A23 Matthias Karl
 */
public class ControlBys implements Control {

	private final PageObject parent;
	private final By[] bys;

	/**
	 * @param driver
	 */
	public ControlBys(PageObject parent, By... bys) {
		this.parent = parent;
		this.bys = bys;
	}

	public WebElement resolve() {
		NoSuchElementException problem = new NoSuchElementException(
				"No 'By' specified!");
		for (By by : bys) {
			try {
				return find(by);
			} catch (NoSuchElementException e) {
				problem = e;
			}
		}
		throw problem;
	}

	public void sendKeys(String t) {
		resolve().sendKeys(t);
	}

	public void uncheck() {
		check(resolve(), false);
	}

	public void check() {
		check(resolve(), true);
	}

	public void check(boolean state) {
		check(resolve(), state);
	}

	/**
	 * Sets the state of the checkbox to the specified value.
	 */
	protected void check(WebElement e, boolean state) {
		if (e.isSelected() != state)
			e.click();
	}

	public void click() {
		resolve().click();
	}

	public void set(String text) {
		WebElement e = resolve();
		e.clear();
		e.sendKeys(text);
	}

	public void set(Object text) {
		set(text.toString());
	}

	/**
	 * Select an option.
	 */
	public void select(String option) {
		WebElement e = resolve();
		e.findElement(parent.by.option(option)).click();

		// move the focus away from the select control to fire onchange event
		e.sendKeys(Keys.TAB);
	}

	/**
	 * @return value des Elements
	 */
	public String value() {
		WebElement e = resolve();
		return e.getAttribute("value");
	}

	/**
	 * @return true, wenn das Attribut "readonly" auf Wert "readonly" oder
	 *         "true" ist
	 */
	public boolean isReadOnly() {
		WebElement e = resolve();
		String attribute = e.getAttribute("readonly");
		if (attribute == null) {
			return false;
		} else if (attribute.equalsIgnoreCase("true")
				|| attribute.equalsIgnoreCase("readonly")) {
			return true;
		} else {
			throw new RuntimeException(
					"Control.isReadOnly() -- Unbekannter readonly-Attribut-Wert: "
							+ attribute);
		}
	}
	
	/**
	 * Returns the first visible element that matches the selector.
	 * 
	 * @throws NoSuchElementException
	 *             if the element is not found.
	 * @see #getElement(By) if you don't want to see an exception
	 */
	protected WebElement find(By selector) {
		try {
			long endTime = System.currentTimeMillis() + 3000;
			while (System.currentTimeMillis() <= endTime) {
				try{
				WebElement e = parent.getDriver().findElement(selector);
				if (isDisplayed(e))
					return e;

				for (WebElement f : parent.getDriver().findElements(selector)) {
					if (isDisplayed(f))
						return f;
				}

				// give a bit more chance for the element to become visible
				sleep(100);
				} catch (NoSuchElementException x) {
					//Durchlaufe Schleife bis die Zeit abgelaufen
				}
			}
			throw new NoSuchElementException("Unable to locate visible "
					+ selector + " in " + parent.getDriver().getCurrentUrl());
		} catch (NoSuchElementException x) {
			// this is often the best place to set a breakpoint
			String msg = String.format("Unable to locate %s in %s\n\n%s",
					selector, parent.getDriver().getCurrentUrl(), parent.getDriver().getPageSource());
			throw new NoSuchElementException(msg, x);
		}
	}

	/**
	 * Consider stale elements not displayed.
	 */
	private boolean isDisplayed(WebElement e) {
		try {
			return e.isDisplayed();
		} catch (StaleElementReferenceException _) {
			return false;
		}
	}

	/**
	 * Thread.sleep that masks exception.
	 */
	private static void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			throw new Error(e);
		}
	}
}
