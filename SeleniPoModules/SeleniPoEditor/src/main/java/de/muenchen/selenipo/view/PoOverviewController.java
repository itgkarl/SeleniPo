package de.muenchen.selenipo.view;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

import org.apache.commons.chain.web.MapEntry;
import org.apache.log4j.Logger;
import org.eclipse.jetty.util.StringUtil;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import de.muenchen.selenipo.ConverterService;
import de.muenchen.selenipo.HtmlParserService;
import de.muenchen.selenipo.HtmlParserService.IdentifiertStratFactory;
import de.muenchen.selenipo.HtmlParserService.IdentifiertStratFactory.IdentifiertStrat;
import de.muenchen.selenipo.IdentifierStrategy;
import de.muenchen.selenipo.MainApp;
import de.muenchen.selenipo.PoGeneric;
import de.muenchen.selenipo.Selector;
import de.muenchen.selenipo.impl.fxModel.ElementFx;
import de.muenchen.selenipo.impl.fxModel.PoGenericFx;
import de.muenchen.selenipo.impl.fxModel.TransitionFx;
import de.muenchen.selenipo.view.poOverviewStates.ElementTableState;
import de.muenchen.selenipo.view.poOverviewStates.HtmlSelectorComboboxState;
import de.muenchen.selenipo.view.poOverviewStates.HtmlTableState;
import de.muenchen.selenipo.view.poOverviewStates.PoComboBoxState;
import de.muenchen.selenipo.view.poOverviewStates.TransitionTableState;

public class PoOverviewController {

	private static final Logger logger = Logger
			.getLogger(PoOverviewController.class);

	@FXML
	private TextField urlField;
	@FXML
	private TableView<ElementFx> elementTable;
	@FXML
	private TableColumn<ElementFx, String> elemIdentifierColumn;
	@FXML
	private TableColumn<ElementFx, String> elemSelectorTypeColumn;
	@FXML
	private TableColumn<ElementFx, String> elemLocatorColumn;

	@FXML
	private TableView<TransitionFx> transitionTable;
	@FXML
	private TableColumn<TransitionFx, String> transIdentifierColumn;
	@FXML
	private TableColumn<TransitionFx, String> transSelectorTypeColumn;
	@FXML
	private TableColumn<TransitionFx, String> transLocatorColumn;
	@FXML
	private TableColumn<TransitionFx, String> transDestinationColumn;

	@FXML
	private TableView<ElementFx> htmlTable;
	@FXML
	private TableColumn<ElementFx, String> htmlIdentifierColumn;
	@FXML
	private TableColumn<ElementFx, String> htmlSelectorTypeColumn;
	@FXML
	private TableColumn<ElementFx, String> htmlLocatorColumn;

	@FXML
	private ComboBox<PoGenericFx> poComboBox;

	@FXML
	private ComboBox<Selector> htmlSelectorComboBox;

	@FXML
	private TextField matcherField;
	@FXML
	private TextField attrField;
	@FXML
	private TextField grpField;
	@FXML
	private CheckBox identCheckbox;

	private IdentifierStrategy currentIdentStrat;

	public enum Colour {
		RED, GREEN;
	};

	final ObservableMap<Integer, Colour> elementColour = FXCollections
			.observableHashMap();

	final ObservableMap<Integer, Colour> transitionColour = FXCollections
			.observableHashMap();

	final ObservableMap<Integer, Colour> htmlColour = FXCollections
			.observableHashMap();

	// Reference to the main application.
	private MainApp mainApp;

	private PoOverviewState poOverviewState;

	/**
	 * The constructor. The constructor is called before the initialize()
	 * method.
	 */
	public PoOverviewController() {
		poOverviewState = new PoComboBoxState(this);
		this.currentIdentStrat = IdentifiertStratFactory
				.getStrat(IdentifiertStrat.BASE);
	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		elementTable.setPlaceholder(new Label("Elements of pageObject"));
		transitionTable.setPlaceholder(new Label("Transitions of pageObject"));
		htmlTable.setPlaceholder(new Label("Elements in browser"));

		htmlLocatorColumn.setCellValueFactory(cellData -> cellData.getValue()
				.locatorProperty());
		htmlSelectorTypeColumn.setCellValueFactory(cellData -> cellData
				.getValue().typeProperty().asString());
		htmlIdentifierColumn.setCellValueFactory(cellData -> cellData
				.getValue().identifierProperty());

		elemLocatorColumn.setCellValueFactory(cellData -> cellData.getValue()
				.locatorProperty());
		elemSelectorTypeColumn.setCellValueFactory(cellData -> cellData
				.getValue().typeProperty().asString());
		elemIdentifierColumn.setCellValueFactory(cellData -> cellData
				.getValue().identifierProperty());

		transLocatorColumn.setCellValueFactory(cellData -> cellData.getValue()
				.locatorProperty());
		transSelectorTypeColumn.setCellValueFactory(cellData -> cellData
				.getValue().typeProperty().asString());
		transIdentifierColumn.setCellValueFactory(cellData -> cellData
				.getValue().identifierProperty());
		transDestinationColumn.setCellValueFactory(cellData -> cellData
				.getValue().destinationProperty().asString());
		StringConverter<PoGenericFx> poConverter = new StringConverter<PoGenericFx>() {
			@Override
			public String toString(PoGenericFx object) {
				if (object == null) {
					return null;
				} else {
					return object.identifierProperty().get();
				}
			}

			@Override
			public PoGenericFx fromString(String string) {
				return null;
			}
		};
		poComboBox.setConverter(poConverter);

		setElementRowFactory(elementTable);
		setTransitionRowFactory(transitionTable);
		setHtmlRowFactory(htmlTable);

		elementTable.setOnKeyPressed(getDelteOnDelKeyEvent());
		transitionTable.setOnKeyPressed(getDelteOnDelKeyEvent());
		poComboBox.setOnKeyPressed(getDelteOnDelKeyEvent());

		setGrpOnlyNumberListener();

	}

	EventHandler<KeyEvent> getDelteOnDelKeyEvent() {
		return new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.DELETE) {
					handleDelete();
				}
			}
		};
	}

	private void setGrpOnlyNumberListener() {
		grpField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				if (!newValue.matches("\\d*") && newValue.length() <= 3) {
					grpField.setText(oldValue);
				}
			}
		});
	}

	private void setElementRowFactory(TableView<ElementFx> elementTable) {
		elementTable
				.setRowFactory(new Callback<TableView<ElementFx>, TableRow<ElementFx>>() {
					@Override
					public TableRow<ElementFx> call(
							TableView<ElementFx> tableView) {
						final TableRow<ElementFx> row = new TableRow<ElementFx>() {
							@Override
							protected void updateItem(ElementFx elementFx,
									boolean empty) {
								super.updateItem(elementFx, empty);
							}

						};
						elementColour
								.addListener(new MapChangeListener<Integer, Colour>() {
									@Override
									public void onChanged(
											javafx.collections.MapChangeListener.Change<? extends Integer, ? extends Colour> change) {
										row.getStyleClass().remove("okStatus");
										row.getStyleClass().remove(
												"errorStatus");
										if (elementColour.get(row.getIndex()) != null) {
											switch (elementColour.get(row
													.getIndex())) {
											case GREEN:
												row.getStyleClass().add(
														"okStatus");
												break;
											case RED:
												row.getStyleClass().add(
														"errorStatus");
												break;
											default:
												break;
											}
										}

									}
								});

						setElementTransitionRowMenu(row);

						// edit on doubleclick
						row.setOnMouseClicked(event -> {
							if (event.getClickCount() == 2 && (!row.isEmpty())) {
								handleEdit();
							}
						});

						return row;
					}
				});

	}

	private void setTransitionRowFactory(TableView<TransitionFx> transitionTable) {
		transitionTable
				.setRowFactory(new Callback<TableView<TransitionFx>, TableRow<TransitionFx>>() {
					@Override
					public TableRow<TransitionFx> call(
							TableView<TransitionFx> tableView) {
						final TableRow<TransitionFx> row = new TableRow<TransitionFx>() {
							@Override
							protected void updateItem(
									TransitionFx transitionFx, boolean empty) {
								super.updateItem(transitionFx, empty);
							}
						};
						transitionColour
								.addListener(new MapChangeListener<Integer, Colour>() {
									@Override
									public void onChanged(
											javafx.collections.MapChangeListener.Change<? extends Integer, ? extends Colour> change) {
										row.getStyleClass().remove("okStatus");
										row.getStyleClass().remove(
												"errorStatus");
										if (transitionColour.get(row.getIndex()) != null) {
											switch (transitionColour.get(row
													.getIndex())) {
											case GREEN:
												row.getStyleClass().add(
														"okStatus");
												break;
											case RED:
												row.getStyleClass().add(
														"errorStatus");
												break;
											default:
												break;
											}
										}

									}
								});

						setElementTransitionRowMenu(row);

						// edit on doubleclick
						row.setOnMouseClicked(event -> {
							if (event.getClickCount() == 2 && (!row.isEmpty())) {
								handleEdit();
							}
						});

						return row;
					}

				});
	}

	private void setHtmlRowFactory(TableView<ElementFx> htmlTable) {
		htmlTable
				.setRowFactory(new Callback<TableView<ElementFx>, TableRow<ElementFx>>() {
					@Override
					public TableRow<ElementFx> call(
							TableView<ElementFx> tableView) {
						final TableRow<ElementFx> row = new TableRow<ElementFx>() {
							@Override
							protected void updateItem(ElementFx elementFx,
									boolean empty) {
								super.updateItem(elementFx, empty);
							}

						};
						htmlColour
								.addListener(new MapChangeListener<Integer, Colour>() {
									@Override
									public void onChanged(
											javafx.collections.MapChangeListener.Change<? extends Integer, ? extends Colour> change) {
										row.getStyleClass().remove("okStatus");
										row.getStyleClass().remove(
												"errorStatus");
										if (htmlColour.get(row.getIndex()) != null) {
											switch (htmlColour.get(row
													.getIndex())) {
											case GREEN:
												row.getStyleClass().add(
														"okStatus");
												break;
											case RED:
												row.getStyleClass().add(
														"errorStatus");
												break;
											default:
												break;
											}
										}

									}
								});

						setHtmlRowMenu(row);

						// edit on doubleclick
						row.setOnMouseClicked(event -> {
							if (event.getClickCount() == 2 && (!row.isEmpty())) {
								handleMoveHtmlToElement();
							}
						});

						return row;
					}
				});
	}

	private void setElementTransitionRowMenu(TableRow<?> row) {
		// Contextmenü hinzufügen
		final ContextMenu rowMenu = new ContextMenu();
		MenuItem editItem = new MenuItem("Edit");
		editItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				handleEdit();
			}
		});
		MenuItem removeItem = new MenuItem("Delete");
		removeItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				handleDelete();
			}
		});
		MenuItem testWithMessage = new MenuItem("Test and message");
		testWithMessage.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				handleTestWithMesage();
			}
		});
		MenuItem testAndClick = new MenuItem("Test and click");
		testAndClick.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				handleTestAndClick();
			}
		});
		rowMenu.getItems().addAll(editItem, removeItem, testWithMessage,
				testAndClick);
		// only display context menu for non-null items:
		row.contextMenuProperty().bind(
				Bindings.when(Bindings.isNotNull(row.itemProperty()))
						.then(rowMenu).otherwise((ContextMenu) null));

	}

	private void setHtmlRowMenu(TableRow<?> row) {
		// Contextmenü hinzufügen
		final ContextMenu rowMenu = new ContextMenu();
		MenuItem testWithMessage = new MenuItem("Test and message");
		testWithMessage.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				handleTestWithMesage();
			}
		});
		MenuItem testAndClick = new MenuItem("Test and click");
		testAndClick.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				handleTestAndClick();
			}
		});
		rowMenu.getItems().addAll(testWithMessage, testAndClick);
		// only display context menu for non-null items:
		row.contextMenuProperty().bind(
				Bindings.when(Bindings.isNotNull(row.itemProperty()))
						.then(rowMenu).otherwise((ContextMenu) null));
	}

	@FXML
	private void poComboBoxAction() {
		resetColours();
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

	@FXML
	private void htmlSelectorComboBoxAction() {
		resetColours();
		int selectedIndex = htmlSelectorComboBox.getSelectionModel()
				.getSelectedIndex();

		htmlTable.setItems(null);
		if (selectedIndex >= 0) {
			Selector selectedType = htmlSelectorComboBox.getSelectionModel()
					.getSelectedItem();
			WebDriver driver = mainApp.getWebDriver();
			if (driver != null) {
				HtmlParserService htmlParserService = mainApp
						.getHtmlParserService();
				ConverterService converterService = mainApp
						.getConverterService();
				String htmlString = driver.getPageSource();
				PoGeneric htmlPoGeneric = htmlParserService
						.parseElementsFromHtmlForType(htmlString, selectedType,
								currentIdentStrat);
				logger.debug(String.format(
						"Habe %s Elemente gefunden. Convertiere sie..",
						htmlPoGeneric.getElements().size()));
				PoGenericFx htmlPoGenericFx = converterService
						.convertToPoGenericFx(htmlPoGeneric);
				logger.debug(String.format("Setze %s Elemente in Gui..",
						htmlPoGenericFx.getElements().size()));
				mainApp.setHtmlPoGenericFx(htmlPoGenericFx);
				htmlTable
						.setItems(mainApp.getHtmlPoGenericFx().getElementsFx());

			} else {
				logger.debug("Kein Parsing, da Webdriver null.");
			}
		}
	}

	@FXML
	private void handleIdentCheckBox() {
		logger.debug("IdentifiertCheckbox pressed..");
		if (identCheckbox.isSelected()) {
			this.currentIdentStrat = IdentifiertStratFactory.getStrat(
					IdentifiertStrat.MATCHER, matcherField, attrField, grpField);
		} else {
			IdentifiertStratFactory.getStrat(IdentifiertStrat.BASE);
		}
	}

	/**
	 * Called when the user clicks on the delete button.
	 */
	@FXML
	private void handleDelete() {
		logger.debug("Delete pressed..");
		resetColours();
		poOverviewState.handleDelete();
	}

	/**
	 * Called when the user clicks on the new button.
	 */
	@FXML
	private void handleNew() {
		logger.debug("New pressed..");
		resetColours();
		poOverviewState.handleNew();
	}

	/**
	 * Called when the user clicks on the new button next to the Po combobox.
	 */
	@FXML
	private void handleNewPo() {
		logger.debug("NewPo pressed..");
		resetColours();
		poComboboxClick();
		poOverviewState.handleNew();
	}

	/**
	 * Called when the user clicks on the edit button.
	 */
	@FXML
	private void handleEdit() {
		logger.debug("Edit pressed..");
		resetColours();
		poOverviewState.handleEdit();
	}

	/**
	 * Called when the user clicks on the edit button.
	 */
	@FXML
	private void handleTest() {
		logger.debug("Test pressed..");
		resetColours();
		WebDriver driver = mainApp.getWebDriver();
		if (driver != null) {
			poOverviewState.handleTest();
		} else {
			createBrowserNotStartedAlert(getMainApp().getPrimaryStage());
		}
	}

	/**
	 * Called when the user clicks on the Element > button.
	 */
	@FXML
	private void handleMoveHtmlToElement() {
		logger.debug("Element > pressed..");
		poOverviewState.handleMoveHtmlToElement();
	}

	/**
	 * Called when the user clicks on the Element > button.
	 */
	@FXML
	private void handleMoveHtmlToTransition() {
		logger.debug("Transition > pressed..");
		poOverviewState.handleMoveHtmlToTransition();
	}

	/**
	 * Called when the user clicks on the Element > button.
	 */
	@FXML
	private void handleParseHtml() {
		logger.debug("Parse Html pressed..");
		logger.debug("delegate to htmlSelectorComboBoxAction()");
		resetColours();
		htmlSelectorComboBoxAction();
	}

	/**
	 * Called when the user clicks on the edit button.
	 */
	@FXML
	private void handleTestWithMesage() {
		logger.debug("Test pressed..");
		resetColours();
		WebDriver driver = mainApp.getWebDriver();
		if (driver != null) {
			poOverviewState.handleTestWithMessage();
		} else {
			createBrowserNotStartedAlert(getMainApp().getPrimaryStage());
		}
	}

	/**
	 * Called when the user clicks on the edit button.
	 */
	@FXML
	private void handleTestAndClick() {
		logger.debug("Test pressed..");
		resetColours();
		WebDriver driver = mainApp.getWebDriver();
		if (driver != null) {
			try {
				poOverviewState.handleTestAndClick();
			} catch (Exception e) {
				createWebDriverClickAlert(getMainApp().getPrimaryStage(), e);
			}
		} else {
			createBrowserNotStartedAlert(getMainApp().getPrimaryStage());
		}
	}

	@FXML
	private void handleUrlStart() throws InterruptedException {
		logger.debug(String.format("Url Start.. [%s]", urlField.getText()));

		WebDriver driver = getMainApp().getContext().getBean(WebDriver.class);
		mainApp.setWebDriver(driver);
		driver.get(urlField.getText());
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

	@FXML
	private void htmlSelectorComboboxClick() {
		poOverviewState = new HtmlSelectorComboboxState(this);
	}

	@FXML
	private void htmlTableClick() {
		poOverviewState = new HtmlTableState(this);
	}

	private void resetColours() {
		elementColour.clear();
		transitionColour.clear();
		htmlColour.clear();
	}

	public Alert createNoElementSelectedAlert(Stage stage, String customText) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setResizable(true);
		alert.initOwner(stage);
		alert.setTitle("No Selection");
		alert.setHeaderText("No element selected.");
		String contentText = "Please select an element from the %s.";
		alert.setContentText(String.format(contentText, customText));
		alert.showAndWait();
		return alert;
	}

	public Alert createNoPoSelectedAlert(Stage stage) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setResizable(true);
		alert.initOwner(stage);
		alert.setTitle("No Selection");
		alert.setHeaderText("No pageObject selected.");
		String contentText = "Please select a pageObject from the dropdown.";
		alert.setContentText(contentText);
		alert.showAndWait();
		return alert;
	}

	public Alert createConfirmDelete(Stage stage, String customText) {
		Alert confirm = new Alert(AlertType.CONFIRMATION);
		confirm.setResizable(true);
		confirm.initOwner(mainApp.getPrimaryStage());
		confirm.setTitle("Delete?");
		confirm.setHeaderText("Do you really want to delete the Element?");
		confirm.setContentText(customText);
		confirm.showAndWait();
		return confirm;
	}

	public Alert createBrowserNotStartedAlert(Stage stage) {
		// Nothing selected.
		Alert alert = new Alert(AlertType.WARNING);
		alert.setResizable(true);
		alert.initOwner(mainApp.getPrimaryStage());
		alert.setTitle("No Browser");
		alert.setHeaderText("Browser not started.");
		alert.setContentText("Please start the Browser with the startbutton and"
				+ System.lineSeparator() + "navigate to the website.");

		alert.showAndWait();
		return alert;
	}

	public Alert createWebDriverClickAlert(Stage stage, Exception ex) {
		// Nothing selected.
		Alert alert = new Alert(AlertType.WARNING);
		alert.setResizable(true);
		alert.initOwner(mainApp.getPrimaryStage());
		alert.setTitle("WebDriver click");
		alert.setHeaderText("Click not successful.");
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

	public Alert createElementNotFoundAlert(Stage stage, Exception ex) {
		// Nothing selected.
		Alert alert = new Alert(AlertType.WARNING);
		alert.setResizable(true);
		alert.initOwner(mainApp.getPrimaryStage());
		alert.setTitle("Element not found");
		alert.setHeaderText(String.format("Exception occured: %s",
				ex.getClass()));
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

	public Alert createWebElementInfo(Stage stage, WebElement webElement,
			WebDriver driver) {
		StringBuffer message = new StringBuffer();
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setResizable(true);
		alert.initOwner(getMainApp().getPrimaryStage());
		alert.setTitle("Element Gefunden");
		alert.setHeaderText("Element Tag: " + webElement.getTagName());
		message.append("Text: " + webElement.getText() + System.lineSeparator());
		message.append("Attributes:" + System.lineSeparator());
		@SuppressWarnings("unchecked")
		Map<String, Object> executeScript = (Map<String, Object>) ((JavascriptExecutor) driver)
				.executeScript(
						"var items = {}; for (index = 0; index < arguments[0].attributes.length; ++index) { items[arguments[0].attributes[index].name] = arguments[0].attributes[index].value }; return items;",
						webElement);
		for (Map.Entry<String, Object> entry : executeScript.entrySet()) {
			message.append("	-- " + entry.getKey() + ": " + entry.getValue()
					+ System.lineSeparator());
		}
		TextArea textArea = new TextArea(message.toString());
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(textArea, 0, 1);

		// Set expandable Exception into the dialog pane.
		alert.getDialogPane().setContent(expContent);
		alert.showAndWait();
		return alert;
	}

	public void switchPoSelection(int index) {
		try {
			getMainApp().getPoModelFx().getPoGenericsFx().get(index);
		} catch (IndexOutOfBoundsException e) {
			logger.error("Das geforderte Element befindet sich nicht in der Liste");
		}
		getPoComboBox().getSelectionModel().select(index);
		poComboBoxAction();
	}

	/**
	 * Is called by the main application to give a reference back to itself.
	 * 
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		poComboBox.setItems(mainApp.getPoModelFx().getPoGenericsFx());
		ObservableList<Selector> observableSelectorList = FXCollections
				.observableArrayList(Selector.getParsableSelectors());
		htmlSelectorComboBox.setItems(observableSelectorList);
	}

	public TableView<ElementFx> getElementTable() {
		return elementTable;
	}

	public TableView<TransitionFx> getTransitionTable() {
		return transitionTable;
	}

	public ObservableMap<Integer, Colour> getElementColour() {
		return elementColour;
	}

	public ObservableMap<Integer, Colour> getTransitionColour() {
		return transitionColour;
	}

	public ObservableMap<Integer, Colour> getHtmlColour() {
		return htmlColour;
	}

	public TableView<ElementFx> getHtmlTable() {
		return htmlTable;
	}

	public void setHtmlTable(TableView<ElementFx> htmlTable) {
		this.htmlTable = htmlTable;
	}

	public MainApp getMainApp() {
		return mainApp;
	}

	public ComboBox<PoGenericFx> getPoComboBox() {
		return poComboBox;
	}

	public IdentifierStrategy getCurrentIdentStrat() {
		return currentIdentStrat;
	}

	public void setCurrentIdentStrat(IdentifierStrategy currentIdentStrat) {
		this.currentIdentStrat = currentIdentStrat;
	}
}
