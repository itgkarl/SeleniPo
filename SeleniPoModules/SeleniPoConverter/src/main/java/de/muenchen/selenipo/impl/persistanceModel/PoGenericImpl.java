package de.muenchen.selenipo.impl.persistanceModel;

import java.util.ArrayList;
import java.util.List;

import de.muenchen.selenipo.Element;
import de.muenchen.selenipo.PoGeneric;
import de.muenchen.selenipo.Transition;

/**
 * Wrapper for the informations that specify a pageObject.
 * 
 * @author matthias
 *
 */
public class PoGenericImpl implements PoGeneric {

	private String identifier;
	private String packageName;
	private List<Element> elements = new ArrayList<Element>();
	private List<Transition> transitions = new ArrayList<Transition>();

	public PoGenericImpl(String identifier, String packageName) {
		super();
		this.identifier = identifier;
		this.packageName = packageName;
	}

	public PoGenericImpl() {
		super();
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public List<Element> getElements() {
		return elements;
	}

	public void setElements(List<Element> elements) {
		this.elements = elements;
	}

	public List<Transition> getTransitions() {
		return transitions;
	}

	public void setTransitions(List<Transition> transitions) {
		this.transitions = transitions;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((elements == null) ? 0 : elements.hashCode());
		result = prime * result
				+ ((identifier == null) ? 0 : identifier.hashCode());
		result = prime * result
				+ ((packageName == null) ? 0 : packageName.hashCode());
		result = prime * result
				+ ((transitions == null) ? 0 : transitions.hashCode());
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
		PoGenericImpl other = (PoGenericImpl) obj;
		if (elements == null) {
			if (other.elements != null)
				return false;
		} else if (!elements.equals(other.elements))
			return false;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		if (packageName == null) {
			if (other.packageName != null)
				return false;
		} else if (!packageName.equals(other.packageName))
			return false;
		if (transitions == null) {
			if (other.transitions != null)
				return false;
		} else if (!transitions.equals(other.transitions))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("--PoGeneric - " + identifier + ": " + System.lineSeparator());
		sb.append("---Package - " + packageName + System.lineSeparator());
		sb.append("----Elements: " + System.lineSeparator());
		for (Element element : elements) {
			sb.append(element.toString());
		}
		sb.append("----Transitions: " + System.lineSeparator());
		for (Transition transition : transitions) {
			sb.append(transition.toString());
		}
		return sb.toString();
	}

}
