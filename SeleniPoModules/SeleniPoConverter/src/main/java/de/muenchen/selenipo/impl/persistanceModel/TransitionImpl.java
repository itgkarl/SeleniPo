package de.muenchen.selenipo.impl.persistanceModel;

import de.muenchen.selenipo.PoGeneric;
import de.muenchen.selenipo.Selector;
import de.muenchen.selenipo.Transition;

public class TransitionImpl extends ElementImpl implements Transition {

	private PoGeneric to;

	public TransitionImpl(String identefier, Selector type, String locator,
			PoGeneric to) {
		super(identefier, type, locator);
		this.to = to;
	}

	public TransitionImpl() {
		super();
	}

	public PoGeneric getDestination() {
		return to;
	}

	public void setDestination(PoGeneric to) {
		this.to = to;
	}

	@Override
	public String toString() {
		return "------ Transition [to=" + to.getIdentifier() + ", type="
				+ super.getType() + ", locator=" + super.getLocator() + "]"
				+ System.lineSeparator();
	}

}
