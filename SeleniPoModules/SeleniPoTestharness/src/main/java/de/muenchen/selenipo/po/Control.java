package de.muenchen.selenipo.po;

import org.openqa.selenium.WebElement;

/**
 * @author matthias
 */
public interface Control {

	WebElement resolve();

	void sendKeys(String t);

	void uncheck();

	void check();

	void check(boolean state);

	void click();

	void set(String text);

	void set(Object text);

	void select(String option);

	String value();

	boolean isReadOnly();
}
