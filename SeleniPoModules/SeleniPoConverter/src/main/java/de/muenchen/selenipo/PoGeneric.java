package de.muenchen.selenipo;

import java.util.List;

public interface PoGeneric {

	String getIdentifier();

	String getPackageName();

	void setIdentifier(String identifier);

	void setPackageName(String packageName);

	List<Element> getElements();

	List<Transition> getTransitions();

}
