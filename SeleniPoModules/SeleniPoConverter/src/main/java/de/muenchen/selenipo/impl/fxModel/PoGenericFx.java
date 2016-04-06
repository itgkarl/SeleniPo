package de.muenchen.selenipo.impl.fxModel;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import de.muenchen.selenipo.Element;
import de.muenchen.selenipo.PoGeneric;
import de.muenchen.selenipo.Transition;

public class PoGenericFx implements PoGeneric {

	private StringProperty identifier;
	private StringProperty packageName;
	private ObservableList<ElementFx> elementsFx = FXCollections
			.observableArrayList();
	private ObservableList<TransitionFx> transitionsFx = FXCollections
			.observableArrayList();

	public PoGenericFx(String identifier, String packageName) {
		super();
		this.identifier = new SimpleStringProperty(identifier);
		this.packageName = new SimpleStringProperty(packageName);
	}

	public PoGenericFx() {
		super();
		this.identifier = new SimpleStringProperty();
		this.packageName = new SimpleStringProperty();
	}

	@Override
	public String getIdentifier() {
		return identifier.get();
	}

	@Override
	public void setIdentifier(String identifier) {
		this.identifier.set(identifier);
	}

	public StringProperty identifierProperty() {
		return identifier;
	}

	public String getPackageName() {
		return packageName.get();
	}

	public void setPackageName(String packageName) {
		this.packageName.set(packageName);
	}

	public StringProperty packageNameProperty() {
		return packageName;
	}

	@Override
	public List<Element> getElements() {
		List<Element> elem = new ArrayList<Element>();
		for (ElementFx elementFx : elementsFx) {
			elem.add(elementFx);
		}
		return elem;
	}

	@Override
	public List<Transition> getTransitions() {
		List<Transition> trans = new ArrayList<Transition>();
		for (TransitionFx transitionFx : transitionsFx) {
			trans.add(transitionFx);
		}
		return trans;
	}

	public ObservableList<ElementFx> getElementsFx() {
		return elementsFx;
	}

	public void setElementsFx(ObservableList<ElementFx> elementsFx) {
		this.elementsFx = elementsFx;
	}

	public ObservableList<TransitionFx> getTransitionsFx() {
		return transitionsFx;
	}

	public void setTransitionsFx(ObservableList<TransitionFx> transitionsFx) {
		this.transitionsFx = transitionsFx;
	}

	@Override
	public String toString() {
		return identifier.get();
	}

}
