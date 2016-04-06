package de.muenchen.selenipo.view;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import org.apache.log4j.Logger;

import de.muenchen.selenipo.MainApp;
import de.muenchen.selenipo.ValidationMessage;
import de.muenchen.selenipo.impl.fxModel.PoGenericFx;
import de.muenchen.selenipo.impl.fxModel.PoModelFx;
import de.muenchen.selenipo.impl.persistanceModel.PoModelImpl;

public class RootLayoutController {
	private static final Logger logger = Logger
			.getLogger(RootLayoutController.class);

	private static final String SAVE_FILE_NAME_PREFERENCES = "saveFile";
	private static final String GENERATE_FILE_NAME_PREFERENCES = "generateFile";

	@FXML
	private MenuItem menuItemSave;

	private MainApp mainApp;

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		menuItemSave.setAccelerator(new KeyCodeCombination(KeyCode.S,
				KeyCombination.CONTROL_DOWN));
	}

	@FXML
	public void handleSave() {
		logger.debug("Save..");
		// Lade alten Filepath aus den Preferences. Wenn einer existiert
		// werwende diesen.
		logger.debug("Versuche altes Verzeichnis aus den Preferences zu laden...");
		File saveFile = null;
		if (mainApp.isFileGotLoaded()) {
			// nur setzen wenn bereits einmal geladen wurde.
			saveFile = mainApp.getConverterService().loadFileFromPreferences(
					SAVE_FILE_NAME_PREFERENCES);
		}

		if (saveFile != null) {
			// Speichere File in den Preferences
			logger.debug("Speichere Filepath in den Preferences...");
			mainApp.getConverterService().putFileToPreferences(
					SAVE_FILE_NAME_PREFERENCES, saveFile);
			// Make sure it has the correct extension
			if (!saveFile.getPath().endsWith(".xml")) {
				saveFile = new File(saveFile.getPath() + ".xml");
			}
			PoModelImpl poModelImpl = mainApp.getConverterService()
					.convertToImpl(mainApp.getPoModelFx());
			// Validiere das Model
			List<ValidationMessage> validateMessages = mainApp
					.getConverterService().validateModel(poModelImpl);
			if (validateMessages.size() == 0) {
				mainApp.getConverterService().persistToXml(saveFile,
						poModelImpl);
			} else {
				createValidationErrorAlert(mainApp.getPrimaryStage(),
						validateMessages);
			}
		} else {
			handleSaveAs();
		}
	}

	@FXML
	public void handleSaveAs() {
		logger.debug("Save as..");
		FileChooser fileChooser = new FileChooser();

		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
				"XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);
		// Lade alten Filepath aus den Preferences. Wenn einer existiert
		// werwende diesen.
		logger.debug("Versuche altes Verzeichnis aus den Preferences zu laden...");
		File saveFilePath = mainApp.getConverterService()
				.loadFileFromPreferences(SAVE_FILE_NAME_PREFERENCES);
		if (saveFilePath != null) {
			fileChooser.setInitialDirectory(saveFilePath.getParentFile());
			logger.debug("Verzeichnis gefunden: " + saveFilePath.getPath());
		}

		// Show save file dialog
		File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

		if (file != null) {
			// Speichere File in den Preferences
			logger.debug("Speichere Filepath in den Preferences...");
			mainApp.getConverterService().putFileToPreferences(
					SAVE_FILE_NAME_PREFERENCES, file);
			// Make sure it has the correct extension
			if (!file.getPath().endsWith(".xml")) {
				file = new File(file.getPath() + ".xml");
			}
			PoModelImpl poModelImpl = mainApp.getConverterService()
					.convertToImpl(mainApp.getPoModelFx());
			// Validiere das Model
			List<ValidationMessage> validateMessages = mainApp
					.getConverterService().validateModel(poModelImpl);
			if (validateMessages.size() == 0) {
				mainApp.getConverterService().persistToXml(file, poModelImpl);
			} else {
				createValidationErrorAlert(mainApp.getPrimaryStage(),
						validateMessages);
			}
		}
	}

	@FXML
	public void handleLoad() {
		logger.debug("Load..");
		FileChooser fileChooser = new FileChooser();

		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
				"XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);

		// Lade alten Filepath aus den Preferences. Wenn einer existiert
		// werwende diesen.
		logger.debug("Versuche altes Verzeichnis aus den Preferences zu laden...");
		File saveFilePath = mainApp.getConverterService()
				.loadFileFromPreferences(SAVE_FILE_NAME_PREFERENCES);
		if (saveFilePath != null) {
			fileChooser.setInitialDirectory(saveFilePath.getParentFile());
			logger.debug("Verzeichnis gefunden: " + saveFilePath.getPath());
		}

		// Show save file dialog
		File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

		if (file != null) {
			// Speichere File in den Preferences
			logger.debug("Speichere Filepath in den Preferences...");
			mainApp.getConverterService().putFileToPreferences(
					SAVE_FILE_NAME_PREFERENCES, file);
			;
			// File wurde geladen. Beim Nächsten Save kann einfach gepeichert
			// werden.
			mainApp.setFileGotLoaded(true);
			PoModelImpl loadFromXml = (PoModelImpl) mainApp
					.getConverterService().loadFromXml(file);
			PoModelFx fxModel = mainApp.getConverterService().convertToFxModel(
					loadFromXml);
			mainApp.setPoModelFx(fxModel);
			mainApp.showPoOverview();
		}
	}

	@FXML
	public void handleGenerate() {
		logger.debug("generate..");
		// Lade directory von Preferences falls vorhanden
		File generateFile = mainApp.getConverterService()
				.loadFileFromPreferences(GENERATE_FILE_NAME_PREFERENCES);
		DirectoryChooser dirChooser = new DirectoryChooser();
		if (generateFile != null) {
			dirChooser.setInitialDirectory(generateFile);
			logger.debug("Verzeichnis gefunden: " + generateFile.getPath());
		}
		File dir = dirChooser.showDialog(mainApp.getPrimaryStage());
		if (dir != null) {
			try {
				// Validiere das Model
				List<ValidationMessage> validateMessages = mainApp
						.getConverterService().validateModel(
								mainApp.getPoModelFx());
				if (validateMessages.size() == 0) {
					mainApp.getGeneratorService().generatePageObjects(
							mainApp.getPoModelFx(), dir.getAbsolutePath());
					// Speichere das gewählte directory in die Preferneces
					mainApp.getConverterService().putFileToPreferences(
							GENERATE_FILE_NAME_PREFERENCES, dir);
				} else {
					createValidationErrorAlert(mainApp.getPrimaryStage(),
							validateMessages);
				}
			} catch (IOException e) {
				// Show the error message.
				createGeneratorException(mainApp.getPrimaryStage(), e);
			}
		}
	}

	private Alert createValidationErrorAlert(Stage stage,
			List<ValidationMessage> errors) {
		// Nothing selected.
		Alert alert = new Alert(AlertType.ERROR);
		alert.setResizable(true);
		alert.initOwner(mainApp.getPrimaryStage());
		alert.setTitle("Validation Error");
		alert.setHeaderText("Bei der Validierung des Models traten fehler auf.");

		Label label = new Label("Fehlermeldungen:");

		StringBuffer sb = new StringBuffer();
		for (ValidationMessage validationMessage : errors) {
			sb.append(validationMessage + System.getProperty("line.separator"));
		}

		TextArea textArea = new TextArea(sb.toString());
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);

		// Set expandable Exception into the dialog pane.
		alert.getDialogPane().setExpandableContent(expContent);

		alert.showAndWait();
		return alert;
	}

	private Alert createGeneratorException(Stage stage, Exception ex) {
		// Nothing selected.
		Alert alert = new Alert(AlertType.WARNING);
		alert.setResizable(true);
		alert.initOwner(mainApp.getPrimaryStage());
		alert.setTitle("Generator Exception");
		alert.setHeaderText("Fehler beim Generieren.");
		// Create expandable Exception.
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		String exceptionText = sw.toString();

		Label label = new Label("The exception stacktrace was:");

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);

		// Set expandable Exception into the dialog pane.
		alert.getDialogPane().setExpandableContent(expContent);

		alert.showAndWait();
		return alert;
	}

	/**
	 * Is called by the main application to give a reference back to itself.
	 * 
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;

	}
}
