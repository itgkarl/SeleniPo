package de.muenchen.selenipo.view;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import de.muenchen.selenipo.Selector;
import de.muenchen.selenipo.impl.fxModel.ElementFx;

public class ElementEditDialogController {

	@FXML
	private TextField identifierField;
	@FXML
	private TextField locatorField;
	@FXML
	private ComboBox<Selector> typeComboBox;

	private Stage dialogStage;
	private ElementFx elementFx;
	private boolean okClicked = false;

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		typeComboBox.setItems(FXCollections.observableArrayList(Selector
				.values()));
	}

	/**
	 * Sets the stage of this dialog.
	 * 
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
		dialogStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			if (event.getCode().equals(KeyCode.ENTER)) {
				handleOk();
			}
		});
		typeComboBox.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			if (event.getCode().equals(KeyCode.ENTER)) {
				handleOk();
			}
		});
	}

	/**
	 * Sets the person to be edited in the dialog.
	 * 
	 * @param person
	 */
	public void setElement(ElementFx element) {
		this.elementFx = element;

		identifierField.setText(element.getIdentifier());
		locatorField.setText(element.getLocator());
		typeComboBox.getSelectionModel().select(element.getType());
	}

	/**
	 * Returns true if the user clicked OK, false otherwise.
	 * 
	 * @return
	 */
	public boolean isOkClicked() {
		return okClicked;
	}

	/**
	 * Called when the user clicks ok.
	 */
	@FXML
	private void handleOk() {
		if (isInputValid()) {
			elementFx.setIdentifier(identifierField.getText());
			elementFx.setLocator(locatorField.getText());
			elementFx.setType(typeComboBox.getSelectionModel()
					.getSelectedItem());
			okClicked = true;
			dialogStage.close();
		}
	}

	/**
	 * Called when the user clicks cancel.
	 */
	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	/**
	 * Validates the user input in the text fields.
	 * 
	 * @return true if the input is valid
	 */
	private boolean isInputValid() {
		StringBuilder errorMessage = new StringBuilder();
		if (identifierField.getText() == null
				|| identifierField.getText().trim().length() == 0) {
			errorMessage.append("No valid Identifier!\n");
		}
		if (locatorField.getText() == null
				|| locatorField.getText().trim().length() == 0) {
			errorMessage.append("No valid Locator!\n");
		}
		if (typeComboBox.getSelectionModel().getSelectedItem() == null) {
			errorMessage.append("No valid Type!\n");
		}

		if (errorMessage.length() == 0) {
			return true;
		} else {
			// Show the error message.
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(dialogStage);
			alert.setTitle("Invalid Fields");
			alert.setHeaderText("Please correct invalid fields");
			alert.setContentText(errorMessage.toString());

			alert.showAndWait();

			return false;
		}
	}
}
