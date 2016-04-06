package de.muenchen.selenipo.po;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;

import de.muenchen.selenipo.ByFactory;

/**
 * Generisches PageObject. Ältester Vater aller anderen PageObjects.
 * 
 * @see http://code.google.com/p/selenium/wiki/PageObjects
 * 
 * @author matthias.karl
 */
public class PageObject {

	@Autowired
	protected ByFactory by;
	@Autowired
	protected WebDriver driver;


	public WebDriver getDriver() {
		return driver;
	}

	protected Control control(By... bys) {
		return new ControlBys(this, bys);
	}

	public ByFactory getBy() {
		return by;
	}

}
