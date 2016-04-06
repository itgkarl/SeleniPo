package de.muenchen.selenipo.impl.fxModel;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByCssSelector;
import org.openqa.selenium.By.ByXPath;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import de.muenchen.selenipo.Element;
import de.muenchen.selenipo.Selector;

public class ElementFx implements Element {

	private ObjectProperty<Selector> type;
	private StringProperty locator;
	private StringProperty identifier;

	public ElementFx(String identifier, Selector type, String locator) {
		super();
		this.type = new SimpleObjectProperty<Selector>(type);
		this.locator = new SimpleStringProperty(locator);
		this.identifier = new SimpleStringProperty(identifier);
	}

	public ElementFx() {
		super();
		this.type = new SimpleObjectProperty<Selector>();
		this.locator = new SimpleStringProperty();
		this.identifier = new SimpleStringProperty();
	}

	@Override
	public Selector getType() {
		return type.get();
	}

	@Override
	public void setType(Selector type) {
		this.type.set(type);
	}

	public ObjectProperty<Selector> typeProperty() {
		return type;
	}

	@Override
	public String getLocator() {
		return this.locator.get();
	}

	@Override
	public void setLocator(String locator) {
		this.locator.set(locator);

	}

	public StringProperty locatorProperty() {
		return locator;
	}

	@Override
	public String getIdentifier() {
		return this.identifier.get();
	}

	@Override
	public void setIdentifier(String identifier) {
		this.identifier.set(identifier);
	}

	public StringProperty identifierProperty() {
		return identifier;
	}

	@Override
	public String getXPath() {
		if (locator != null && type != null) {
			By by = type.get().by(locator.get());
			if (by instanceof ByXPath) {
				return by.toString().substring(10);
			}
		}
		return null;
	}

	@Override
	public String getCssSelector() {
		if (locator != null && type != null) {
			By by = type.get().by(locator.get());
			if (by instanceof ByCssSelector) {
				return by.toString().substring(16);
			}
		}
		return null;
	}

}
