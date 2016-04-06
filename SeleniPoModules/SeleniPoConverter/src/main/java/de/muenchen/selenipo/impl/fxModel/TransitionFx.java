package de.muenchen.selenipo.impl.fxModel;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import de.muenchen.selenipo.PoGeneric;
import de.muenchen.selenipo.Selector;
import de.muenchen.selenipo.Transition;

public class TransitionFx extends ElementFx implements Transition {

	private ObjectProperty<PoGenericFx> destination;

	public TransitionFx(String identifier, Selector type, String locator,
			PoGenericFx destenation) {
		super(identifier, type, locator);
		this.destination = new SimpleObjectProperty<PoGenericFx>(destenation);
	}

	public TransitionFx() {
		super();
		this.destination = new SimpleObjectProperty<PoGenericFx>();
	}

	@Override
	public PoGeneric getDestination() {
		return destination.get();
	}

	public void setDestinationFx(PoGenericFx poGenericFx) {
		this.destination.set(poGenericFx);
	}

	public ObjectProperty<PoGenericFx> destinationProperty() {
		return destination;
	}

	public PoGenericFx getDestinationFx() {
		return this.destination.get();
	}

}
