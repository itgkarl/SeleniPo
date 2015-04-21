package de.muenchen.selenipo;

import de.muenchen.selenipo.impl.persistanceModel.PoModelImpl;

/**
 * Service zum persistieren einens objektes im XML Format.
 * 
 * @author matthias
 *
 */
public interface ConverterService {

	/**
	 * Persistiert ein Objekt im XML Format.
	 * 
	 * @param o
	 *            Objekt das Serialisiert werden soll.
	 */
	void persistToXml(String path, Object o);

	/**
	 * 
	 * @param path
	 * @param type
	 *            Type der geladen werden soll
	 * @return
	 */
	Object loadFromXml(String path);

	PoModelImpl convertToImpl(PoModel poModel);

}
