package de.muenchen.selenipo.impl.persistanceModel;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByCssSelector;
import org.openqa.selenium.By.ByXPath;

import de.muenchen.selenipo.Element;
import de.muenchen.selenipo.Selector;

public class ElementImpl implements Element {

	private Selector type;
	private String locator;
	private String identifier;

	public ElementImpl(String idetifier, Selector type, String locator) {
		super();
		this.identifier = idetifier;
		this.type = type;
		this.locator = locator;
	}

	public ElementImpl() {
		super();
	}

	public Selector getType() {
		return type;
	}

	public void setType(Selector type) {
		this.type = type;
	}

	public String getLocator() {
		return locator;
	}

	public void setLocator(String locator) {
		this.locator = locator;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getXPath() {
		if (locator != null && type != null) {
			By by = type.by(locator);
			if (by instanceof ByXPath) {
				return by.toString().substring(10);
			}
		}
		return null;
	}

	public String getCssSelector() {
		if (locator != null && type != null) {
			By by = type.by(locator);
			if (by instanceof ByCssSelector) {
				return by.toString().substring(16);
			}
		}
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((identifier == null) ? 0 : identifier.hashCode());
		result = prime * result + ((locator == null) ? 0 : locator.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ElementImpl other = (ElementImpl) obj;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		if (locator == null) {
			if (other.locator != null)
				return false;
		} else if (!locator.equals(other.locator))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "------ Element [type=" + type + ", locator=" + locator
				+ ", identifier=" + identifier + "]" + System.lineSeparator();
	}

}
