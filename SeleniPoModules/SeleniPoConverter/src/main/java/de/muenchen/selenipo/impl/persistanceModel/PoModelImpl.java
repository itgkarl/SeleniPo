package de.muenchen.selenipo.impl.persistanceModel;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import de.muenchen.selenipo.PoGeneric;
import de.muenchen.selenipo.PoModel;

/**
 * Wrapper that contains a list of pageobjects
 * 
 * @author matthias
 *
 */
@XmlRootElement(name = "model")
public class PoModelImpl implements PoModel {

	private List<PoGeneric> poGenerics = new ArrayList<PoGeneric>();

	public PoModelImpl() {
		super();
	}

	public List<PoGeneric> getPoGenerics() {
		return poGenerics;
	}

	public void setPoGenerics(List<PoGeneric> poGenerics) {
		this.poGenerics = poGenerics;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((poGenerics == null) ? 0 : poGenerics.hashCode());
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
		PoModelImpl other = (PoModelImpl) obj;
		if (poGenerics == null) {
			if (other.poGenerics != null)
				return false;
		} else if (!poGenerics.equals(other.poGenerics))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("PoModel: \n");
		for (PoGeneric poGeneric : poGenerics) {
			sb.append(poGeneric.toString() + System.lineSeparator());
		}
		return sb.toString();
	}

}
