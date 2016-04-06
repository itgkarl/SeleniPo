package de.muenchen.selenipo.impl;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.prefs.Preferences;

import javafx.print.PageOrientation;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.thoughtworks.xstream.XStream;

import de.muenchen.selenipo.ConverterService;
import de.muenchen.selenipo.Element;
import de.muenchen.selenipo.PoGeneric;
import de.muenchen.selenipo.PoModel;
import de.muenchen.selenipo.Transition;
import de.muenchen.selenipo.ValidationMessage;
import de.muenchen.selenipo.ValidationMessage.ListType;
import de.muenchen.selenipo.impl.fxModel.ElementFx;
import de.muenchen.selenipo.impl.fxModel.PoGenericFx;
import de.muenchen.selenipo.impl.fxModel.PoModelFx;
import de.muenchen.selenipo.impl.fxModel.TransitionFx;
import de.muenchen.selenipo.impl.persistanceModel.ElementImpl;
import de.muenchen.selenipo.impl.persistanceModel.PoGenericImpl;
import de.muenchen.selenipo.impl.persistanceModel.PoModelImpl;
import de.muenchen.selenipo.impl.persistanceModel.TransitionImpl;

/**
 * 
 * @author matthias
 *
 */
public class ConverterServiceImpl implements ConverterService {

	private static final Logger logger = Logger
			.getLogger(ConverterServiceImpl.class);

	@Autowired
	private XStream xStream;

	@Override
	public final void persistToXml(final File file, final Object o) {
		try {
			file.createNewFile();
			// creates a FileWriter Object
			FileWriter writer = new FileWriter(file);
			BufferedWriter bWriter = new BufferedWriter(writer);
			// Writes the content to the file
			xStream.toXML(o, bWriter);
			writer.close();
			bWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public final Object loadFromXml(final File file) {
		Object o = null;
		// Creates a FileReader Object
		try {
			InputStream in = new FileInputStream(file);
			BufferedInputStream bIn = new BufferedInputStream(in);
			o = xStream.fromXML(bIn);
			bIn.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return o;
	}

	public final PoModelImpl convertToImpl(PoModel poModel) {
		PoModelImpl poModelImpl = new PoModelImpl();
		// Lege alle PoGeneric an
		for (PoGeneric poGeneric : poModel.getPoGenerics()) {
			PoGenericImpl tempPoGeneric = new PoGenericImpl();
			poModelImpl.getPoGenerics().add(tempPoGeneric);
			tempPoGeneric.setIdentifier(poGeneric.getIdentifier());
			tempPoGeneric.setPackageName(poGeneric.getPackageName());

			// Erzeuge jedes Element
			for (Element element : poGeneric.getElements()) {
				ElementImpl tempElement = new ElementImpl();
				tempElement.setIdentifier(element.getIdentifier());
				tempElement.setLocator(element.getLocator());
				tempElement.setType(element.getType());
				tempPoGeneric.getElements().add(tempElement);
			}

			// Erzeuge Transitions
			for (Transition transition : poGeneric.getTransitions()) {
				TransitionImpl tempTransitionImpl = new TransitionImpl();
				tempTransitionImpl.setIdentifier(transition.getIdentifier());
				tempTransitionImpl.setLocator(transition.getLocator());
				tempTransitionImpl.setType(transition.getType());
				tempPoGeneric.getTransitions().add(tempTransitionImpl);
			}
		}

		// Durchlaufe nochmal und setze rückreferenzen
		// Lege alle PoGeneric an
		for (PoGeneric poGeneric : poModel.getPoGenerics()) {
			PoGeneric tempPoGen = findPoInList(poModelImpl.getPoGenerics(),
					poGeneric.getIdentifier());
			// Erzeuge Transitions
			for (Transition transition : poGeneric.getTransitions()) {
				TransitionImpl tempTransInList = (TransitionImpl) findTransInList(
						tempPoGen.getTransitions(), transition.getIdentifier());

				PoGeneric innerTempPoGen = findPoInList(
						poModelImpl.getPoGenerics(), transition
								.getDestination().getIdentifier());
				tempTransInList.setDestination(innerTempPoGen);
			}
		}

		return poModelImpl;
	}

	public final PoModelFx convertToFxModel(PoModel poModel) {
		PoModelFx poModelFx = new PoModelFx();
		// Lege alle PoGeneric an
		for (PoGeneric poGeneric : poModel.getPoGenerics()) {
			PoGenericFx tempPoGeneric = convertToPoGenericFx(poGeneric);
			poModelFx.getPoGenericsFx().add(tempPoGeneric);
		}

		// Durchlaufe nochmal und setze rückreferenzen
		// Lege alle PoGeneric an
		for (PoGeneric poGeneric : poModel.getPoGenerics()) {
			PoGeneric tempPoGen = findPoInList(poModelFx.getPoGenerics(),
					poGeneric.getIdentifier());
			// Erzeuge Transitions
			for (Transition transition : poGeneric.getTransitions()) {
				TransitionFx tempTransInList = (TransitionFx) findTransInList(
						tempPoGen.getTransitions(), transition.getIdentifier());

				PoGeneric innerTempPoGen = findPoInList(
						poModelFx.getPoGenerics(), transition.getDestination()
								.getIdentifier());
				tempTransInList.setDestinationFx((PoGenericFx) innerTempPoGen);
			}
		}
		logger.debug(String.format(
				"Altes Model hat [%s] Pos. Neues FxModel hat [%s] Pos", poModel
						.getPoGenerics().size(), poModelFx.getPoGenericsFx()
						.size()));
		return poModelFx;
	}

	private PoGeneric findPoInList(List<PoGeneric> list, String identifier) {
		logger.debug("Überprüfe [" + list.size() + "] Pos");
		for (PoGeneric poGeneric : list) {
			logger.debug("Vergleiche: [" + poGeneric.getIdentifier() + "/"
					+ identifier + "]");
			if (identifier.equals(poGeneric.getIdentifier())) {
				return poGeneric;
			}
		}
		throw new RuntimeException("Konnte PoGeneric nicht in Liste finden");
	}

	private Transition findTransInList(List<Transition> list, String identifier) {
		logger.debug("Überprüfe [" + list.size() + "] Trans");
		for (Transition transitionImpl : list) {
			logger.debug("Vergleiche: [" + transitionImpl.getIdentifier() + "/"
					+ identifier + "]");
			if (identifier.equals(transitionImpl.getIdentifier())) {
				return transitionImpl;
			}
		}
		throw new RuntimeException("Konnte Transition nicht in Liste finden");
	}

	@Override
	public PoGenericFx convertToPoGenericFx(PoGeneric poGeneric) {
		PoGenericFx tempPoGeneric = new PoGenericFx();
		tempPoGeneric.setIdentifier(poGeneric.getIdentifier());
		tempPoGeneric.setPackageName(poGeneric.getPackageName());

		// Erzeuge jedes Element
		for (Element element : poGeneric.getElements()) {
			ElementFx tempElement = new ElementFx();
			tempElement.setIdentifier(element.getIdentifier());
			tempElement.setLocator(element.getLocator());
			tempElement.setType(element.getType());
			tempPoGeneric.getElementsFx().add(tempElement);
		}

		// Erzeuge Transitions
		for (Transition transition : poGeneric.getTransitions()) {
			TransitionFx tempTransition = new TransitionFx();
			tempTransition.setIdentifier(transition.getIdentifier());
			tempTransition.setLocator(transition.getLocator());
			tempTransition.setType(transition.getType());
			tempPoGeneric.getTransitionsFx().add(tempTransition);
		}
		return tempPoGeneric;
	}

	@Override
	public TransitionFx convertElementToTransitionFx(final Element element) {
		TransitionFx transFx = new TransitionFx();
		transFx.setIdentifier(element.getIdentifier());
		transFx.setType(element.getType());
		transFx.setLocator(element.getLocator());
		// Destinatin is null (element does not contain this info. Developer has
		// to handle this.
		transFx.setDestinationFx(null);
		return transFx;
	}

	/**
	 * überprüft die elemente und transitions eines Pos auf doppelte identefier.
	 */
	private List<ValidationMessage> testForDuplicateKeys(PoGeneric po) {
		List<String> uniqueKeys = new LinkedList<String>();
		List<ValidationMessage> duplecateKeysToReturn = new LinkedList<ValidationMessage>();

		List<Element> elements = po.getElements();
		List<Transition> transitions = po.getTransitions();

		if (elements != null) {
			for (Element elem : elements) {
				if (!uniqueKeys.contains(elem.getIdentifier())) {
					uniqueKeys.add(elem.getIdentifier());
				} else {
					ValidationMessage vMessage = new ValidationMessage(
							po.getIdentifier() + "." + elem.getIdentifier(),
							ListType.ELEMENT, "Identefier ist nicht eindeutig");
					if (!duplecateKeysToReturn.contains(vMessage)) {
						duplecateKeysToReturn.add(vMessage);
					}
				}
			}
		}
		if (transitions != null) {
			for (Transition trans : transitions) {
				if (!uniqueKeys.contains(trans.getIdentifier())) {
					uniqueKeys.add(trans.getIdentifier());
				} else {
					ValidationMessage vMessage = new ValidationMessage(
							po.getIdentifier() + "." + trans.getIdentifier(),
							ListType.TRANISTION,
							"Identefier ist nicht eindeutig");
					if (!duplecateKeysToReturn.contains(vMessage)) {
						duplecateKeysToReturn.add(vMessage);
					}
				}
			}
		}
		return duplecateKeysToReturn;
	}

	/**
	 * Überprüft ob in einer Po liste doppelte identefier sind. Überprüft nur
	 * die übergebene Liste, nicht die Pos selbst.
	 * 
	 * @param pos
	 * @return
	 */
	private List<ValidationMessage> testPoListForDuplecateKeys(
			List<PoGeneric> pos) {
		List<String> uniqueKeys = new LinkedList<String>();
		List<ValidationMessage> duplecateKeysToReturn = new LinkedList<ValidationMessage>();

		for (PoGeneric po : pos) {
			if (!uniqueKeys.contains(po.getIdentifier())) {
				uniqueKeys.add(po.getIdentifier());
			} else {
				ValidationMessage vMessage = new ValidationMessage(
						po.getIdentifier(), ListType.PAGEOBJECT,
						"Identefier ist nicht eindeutig");
				if (!duplecateKeysToReturn.contains(vMessage)) {
					duplecateKeysToReturn.add(vMessage);
				}
			}
		}
		return duplecateKeysToReturn;
	}

	@Override
	public List<ValidationMessage> validateModel(PoModel model) {
		List<ValidationMessage> errorsToReturn = new LinkedList<ValidationMessage>();

		List<PoGeneric> poGenerics = model.getPoGenerics();
		List<ValidationMessage> testPoListForDuplecateKeys = testPoListForDuplecateKeys(poGenerics);
		errorsToReturn.addAll(testPoListForDuplecateKeys);
		for (PoGeneric po : poGenerics) {
			List<ValidationMessage> testForDuplicateKeys = testForDuplicateKeys(po);
			errorsToReturn.addAll(testForDuplicateKeys);
		}

		return errorsToReturn;
	}

	/**
	 * Returns the file preference, i.e. the file that was last opened. The
	 * preference is read from the OS specific registry. If no such preference
	 * can be found, null is returned.
	 * 
	 * @return
	 */
	public File loadFileFromPreferences(String fileName) {
		Preferences prefs = Preferences
				.userNodeForPackage(ConverterService.class);
		String filePath = prefs.get(fileName, null);
		if (filePath != null) {
			return new File(filePath);
		} else {
			return null;
		}
	}

	/**
	 * Sets the file path of the currently loaded file. The path is persisted in
	 * the OS specific registry.
	 * 
	 * @param file
	 *            the file or null to remove the path
	 */
	public void putFileToPreferences(String fileName, File file) {
		Preferences prefs = Preferences
				.userNodeForPackage(ConverterService.class);
		if (file != null) {
			prefs.put(fileName, file.getPath());
		} else {
			prefs.remove(fileName);
		}
	}

}
