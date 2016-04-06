package de.muenchen.selenipo;

import java.io.IOException;
import java.util.Map;

/**
 * Service der die Funktionalität zum Generieren von PageObjecrts bereitstellt.
 * 
 * @author matthias
 *
 */
public interface GeneratorService {

	/**
	 * Generates all pageObjects of a PoModel.
	 * 
	 * @param poModel
	 *            PoModel
	 * @param rootFolder
	 *            Path to Folder which contains the 'generated'-Folder
	 * @return PoModel
	 */
	Map<String, String> generatePageObjects(PoModel poModel, String rootFolder)
			throws IOException;

	/**
	 * Generates a singel pageObject frim a PoGeneric.
	 * 
	 * @param poGeneric
	 *            poGeneric
	 * @param rootFolder
	 *            Path to Folder which contains the 'generated'-Folder
	 * @return String - Generated String
	 */
	Map<String, String> generatePageObject(PoGeneric poGeneric,
			String rootFolder) throws IOException;

}
