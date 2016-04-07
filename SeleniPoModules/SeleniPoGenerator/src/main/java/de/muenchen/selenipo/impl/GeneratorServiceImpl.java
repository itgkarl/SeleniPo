package de.muenchen.selenipo.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.tools.generic.DisplayTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import de.muenchen.selenipo.GeneratorService;
import de.muenchen.selenipo.PoGeneric;
import de.muenchen.selenipo.PoModel;
import de.muenchen.selenipo.Transition;

/**
 * Impl des GeneratorService Interface.
 * 
 * @author matthias
 *
 */
public class GeneratorServiceImpl implements GeneratorService {

	private static final Logger logger = Logger.getLogger(GeneratorServiceImpl.class);

	public static final String CONFIG_FILE = "gernerator.properties";

	@Autowired
	@Qualifier("classpath")
	private VelocityEngine velocityEngineClasspath;
	@Autowired
	@Qualifier("filesystem")
	private VelocityEngine velocityEngineFilesystem;
	@Autowired
	private DisplayTool display;

	@Override
	public Map<String, String> generatePageObjects(final PoModel poModel, final String rootFolder) throws IOException {
		Map<String, String> returnValue = new HashMap<String, String>();
		List<PoGeneric> poGenerics = poModel.getPoGenerics();
		for (PoGeneric poGeneric : poGenerics) {
			logger.debug(String.format("Starte Generierung für Po: [%s]", poGeneric.getIdentifier()));
			Map<String, String> poMap = generatePageObject(poGeneric, rootFolder);
			returnValue.putAll(poMap);
		}
		return returnValue;
	}

	@Override
	public Map<String, String> generatePageObject(final PoGeneric poGeneric, final String rootFolder)
			throws IOException {
		// Map to return
		Map<String, String> returnValue = new HashMap<String, String>();
		// Lese die Zielpfade ab dem Rootfolder aus der config aus
		Properties props = new Properties();
		try {
			// Wurde ein eigenes File vom User hinterlegt?
			String path = "./" + CONFIG_FILE;
			FileInputStream file = new FileInputStream(path);
			props.load(file);
			logger.info("Verwende generator.config des Users");
		} catch (Exception e) {
			props.load(this.getClass().getResourceAsStream("/" + CONFIG_FILE));
			logger.info("Verwende generator.config aus jar");
		}
		String generatedBasePath = props.getProperty("generator.generatedPath");
		String editableBasePath = props.getProperty("generator.editablePath");
		String packagePath = props.getProperty("generator.packagePath");
		logger.debug(String.format("Base path für generated: %s", generatedBasePath));
		logger.debug(String.format("Base path für editable: %s", editableBasePath));
		logger.debug(String.format("Base Package path für edit/generated: %s", packagePath));

		// Befülle liste mit unterschiedlichen DestinationPos für die Imports
		Set<PoGeneric> destinationPos = new HashSet<PoGeneric>();
		for (Transition transition : poGeneric.getTransitions()) {
			destinationPos.add(transition.getDestination());
		}

		// create a context and add data
		VelocityContext context = new VelocityContext();
		context.put("display", display);
		context.put("poGeneric", poGeneric);
		context.put("destionationPos", destinationPos);
		context.put("basePackagePath", packagePath.replaceAll("/", "."));

		logger.debug(String.format("RootFolder: %s", rootFolder));
		logger.debug("-Erzeuge Generated PO:");
		// Generate Path
		String fullPackagePath = packagePath;
		if (poGeneric.getPackageName() != null) {
			fullPackagePath = packagePath + "/" + poGeneric.getPackageName().replaceAll("\\.", "/");
		}
		logger.debug(String.format("-- packagePath: [%s]", packagePath));

		// Erzeuge BasePo

		// Get Template
		Template tBase;
		try {
			// Eigenes Tamplate von User vorhanden?
			tBase = velocityEngineFilesystem.getTemplate("poBase.vm");
			logger.info("Verwende poBase.vm des Users");
		} catch (Exception e) {
			tBase = velocityEngineClasspath.getTemplate("de/muenchen/selenipo/poBase.vm");
			logger.info("Verwende poBase.vm aus resourcen.");
		}
		final String wholePathBase = String.format("%s/%s/%s/%s.java", rootFolder, editableBasePath, packagePath,
				"BasePo");
		// Überprüfe ob das Po bereits vorhanden ist
		if (!doesEditPoAlreadyExist(wholePathBase)) {
			// Base Po wird nicht in die rückgabe übernommen da einfach eine
			// leere Klasse.
			writeFile(wholePathBase, context, tBase, poGeneric);
		}

		// Erzeuge Generated Po
		// Get Template
		Template tGenerated;
		try {
			// Eigenes Tamplate von User vorhanden?
			tGenerated = velocityEngineFilesystem.getTemplate("poGenerated.vm");
			logger.info("Verwende poGenerated.vm des Users");
		} catch (Exception e) {
			tGenerated = velocityEngineClasspath.getTemplate("de/muenchen/selenipo/poGenerated.vm");
			logger.info("Verwende poGenerated.vm aus resourcen.");
		}
		final String wholePathGenerated = String.format("%s/%s/%s/%sGenerated.java", rootFolder, generatedBasePath,
				fullPackagePath, poGeneric.getIdentifier());
		Map<String, String> mapGenerated = writeFile(wholePathGenerated, context, tGenerated, poGeneric);

		// Erzeuge Edit Po

		// Get Template
		Template tEdit;
		try {
			// Eigenes Tamplate von User vorhanden?
			tEdit = velocityEngineFilesystem.getTemplate("poEditable.vm");
			logger.info("Verwende poEdit.vm des Users");
		} catch (Exception e) {
			tEdit = velocityEngineClasspath.getTemplate("de/muenchen/selenipo/poEditable.vm");
			logger.info("Verwende poEdit.vm aus resourcen.");
		}
		final String wholePathEdit = String.format("%s/%s/%s/%s.java", rootFolder, editableBasePath, fullPackagePath,
				poGeneric.getIdentifier());
		// Überprüfe ob das Po bereits vorhanden ist
		if (!doesEditPoAlreadyExist(wholePathEdit)) {
			// Edit Po wird nicht in die rückgabe übernommen da einfach eine
			// leere Klasse.
			writeFile(wholePathEdit, context, tEdit, poGeneric);
		}
		returnValue.putAll(mapGenerated);
		return returnValue;
	}

	private boolean doesEditPoAlreadyExist(String wholePath) {
		File f = new File(wholePath);
		if (f.exists() && !f.isDirectory()) {
			logger.debug(String.format("EditPo [%s] gefunden.", wholePath));
			return true;
		} else {
			logger.debug(String.format("EditPo [%s] nicht gefunden.", wholePath));
			return false;
		}
	}

	private Map<String, String> writeFile(String wholePath, VelocityContext context, Template t, PoGeneric poGeneric)
			throws IOException {
		Map<String, String> returnValue = new HashMap<String, String>();
		logger.debug(String.format("-- wholePath: %s", wholePath));
		Path path = Paths.get(wholePath);

		// Create File
		Files.createDirectories(path.getParent());
		File fileGenerated = new File(path.toString());

		// Write Data
		FileWriter out = new FileWriter(fileGenerated);
		BufferedWriter w = new BufferedWriter(out);
		StringWriter sw = new StringWriter();
		t.merge(context, w);
		t.merge(context, sw);

		returnValue.put(poGeneric.getIdentifier(), sw.toString());
		w.flush();
		sw.flush();
		/* show the World */
		return returnValue;
	}
}
