package de.muenchen.selenipo.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import org.apache.log4j.Logger;

import de.muenchen.selenipo.MainApp;
import de.muenchen.selenipo.model.ElementFx;
import de.muenchen.selenipo.model.PoGenericFx;
import de.muenchen.selenipo.model.TransitionFx;
import de.muenchen.selenipo.view.poOverviewStates.ElementTableState;
import de.muenchen.selenipo.view.poOverviewStates.PoComboBoxState;
import de.muenchen.selenipo.view.poOverviewStates.TransitionTableState;

public class PoOverviewController {

	private static final Logger logger = Logger
			.getLogger(PoOverviewController.class);

	@FXML
	private TableView<ElementFx> elementTable;
	@FXML
	private TableColumn<ElementFx, String> elemIdentefierColumn;
	@FXML
	private TableColumn<ElementFx, String> elemSelectorTypeColumn;
	@FXML
	private TableColumn<ElementFx, String> elemLocatorColumn;

	@FXML
	private TableView<TransitionFx> transitionTable;
	@FXML
	private TableColumn<TransitionFx, String> transIdentefierColumn;
	@FXML
	private TableColumn<TransitionFx, String> transSelectorTypeColumn;
	@FXML
	private TableColumn<TransitionFx, String> transLocatorColumn;
	@FXML
	private TableColumn<TransitionFx, String> transDestinationColumn;

	@FXML
	private ComboBox<PoGenericFx> poComboBox;

	// Reference to the main application.
	private MainApp mainApp;

	private PoOverviewState poOverviewState;

	/**
	 * The constructor. The constructor is called before the initialize()
	 * method.
	 */
	public PoOverviewController() {
		poOverviewState = new PoComboBoxState(this);
	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		elemLocatorColumn.setCellValueFactory(cellData -> cellData.getValue()
				.locatorProperty());
		elemSelectorTypeColumn.setCellValueFactory(cellData -> cellData
				.getValue().typeProperty().asString());
		elemIdentefierColumn.setCellValueFactory(cellData -> cellData
				.getValue().identefierProperty());

		transLocatorColumn.setCellValueFactory(cellData -> cellData.getValue()
				.locatorProperty());
		transSelectorTypeColumn.setCellValueFactory(cellData -> cellData
				.getValue().typeProperty().asString());
		transIdentefierColumn.setCellValueFactory(cellData -> cellData
				.getValue().identefierProperty());
		transDestinationColumn.setCellValueFactory(cellData -> cellData
				.getValue().destinationProperty().asString());

		poComboBox.setConverter(new StringConverter<PoGenericFx>() {
			@Override
			public String toString(PoGenericFx object) {
				if (object == null) {
					return null;
				} else {
					return object.getIdentifier();
				}
			}

			@Override
			public PoGenericFx fromString(String string) {
				return null;
			}
		});
	}

	@FXML
	private void poComboBoxAction() {
		int selectedIndex = poComboBox.getSelectionModel().getSelectedIndex();

		elementTable.setItems(null);
		transitionTable.setItems(null);
		if (selectedIndex >= 0) {
			elementTable.setItems(mainApp.getPoModelFx().getPoGenericsFx()
					.get(selectedIndex).getElementsFx());
			transitionTable.setItems(mainApp.getPoModelFx().getPoGenericsFx()
					.get(selectedIndex).getTransitionsFx());
		}
	}

	/**
	 * Called when the user clicks on the delete button.
	 */
	@FXML
	private void handleDelete() {
		logger.debug("Delete pressed..");
		poOverviewState.handleDelete();
	}

	/**
	 * Called when the user clicks on the new button.
	 */
	@FXML
	private void handleNew() {
		logger.debug("New pressed..");
		poOverviewState.handleNew();
	}
	
	/**
	 * Called when the user clicks on the edit button.
	 */
	@FXML
	private void handleEdit() {
		logger.debug("Edit pressed..");
		poOverviewState.handleEdit();
	}

	public Alert createNoElementSelectedAlert(Stage stage, String customText) {
		// Nothing selected.
		Alert alert = new Alert(AlertType.WARNING);
		alert.initOwner(stage);
		alert.setTitle("No Selection");
		alert.setHeaderText("No element selected.");
		String contentText = "Please select an element from the %s.";
		alert.setContentText(String.format(contentText, customText));
		alert.showAndWait();
		return alert;
	}

	public Alert createConfirmDelete(Stage stage, String customText) {
		// Nothing selected.
		Alert confirm = new Alert(AlertType.CONFIRMATION);
		confirm.initOwner(mainApp.getPrimaryStage());
		confirm.setTitle("Delete?");
		confirm.setHeaderText("Do you really want to delete the Element?");
		confirm.setContentText(customText);
		confirm.showAndWait();
		return confirm;
	}

	@FXML
	private void elementTabeleClick() {
		poOverviewState = new ElementTableState(this);
	}

	@FXML
	private void transitionTableClick() {
		poOverviewState = new TransitionTableState(this);
	}

	@FXML
	private void poComboboxClick() {
		poOverviewState = new PoComboBoxState(this);
	}

	/**
	 * Is called by the main application to give a reference back to itself.
	 * 
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		poComboBox.setItems(mainApp.getPoModelFx().getPoGenericsFx());

	}

	public TableView<ElementFx> getElementTable() {
		return elementTable;
	}

	public TableView<TransitionFx> getTransitionTable() {
		return transitionTable;
	}

	public MainApp getMainApp() {
		return mainApp;
	}

	public ComboBox<PoGenericFx> getPoComboBox() {
		return poComboBox;
	}

}