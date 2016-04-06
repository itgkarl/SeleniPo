package de.muenchen.selenipo.view.poOverviewStates;

import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ComboBox;

import org.apache.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import de.muenchen.selenipo.Selector;
import de.muenchen.selenipo.impl.fxModel.ElementFx;
import de.muenchen.selenipo.impl.fxModel.PoGenericFx;
import de.muenchen.selenipo.impl.fxModel.TransitionFx;
import de.muenchen.selenipo.view.PoOverviewController;
import de.muenchen.selenipo.view.PoOverviewState;
import de.muenchen.selenipo.view.PoOverviewController.Colour;

public class PoComboBoxState implements PoOverviewState {

	private static final Logger logger = Logger
			.getLogger(PoComboBoxState.class);

	PoOverviewController poOverviewController;

	public PoComboBoxState(PoOverviewController poOverviewController) {
		super();
		this.poOverviewController = poOverviewController;
	}

	@Override
	public void handleNew() {
		PoGenericFx poGenericFx = new PoGenericFx();
		boolean okClicked = poOverviewController.getMainApp().showPoEditDialog(
				poGenericFx);
		if (okClicked) {
			poOverviewController.getMainApp().getPoModelFx().getPoGenericsFx()
					.add(poGenericFx);
			int indexOf = poOverviewController.getMainApp().getPoModelFx()
					.getPoGenericsFx().indexOf(poGenericFx);
			poOverviewController.switchPoSelection(indexOf);
		}
	}

	@Override
	public void handleDelete() {
		ComboBox<PoGenericFx> poComboBox = poOverviewController.getPoComboBox();
		int selectedPoComboboxIndex = poComboBox.getSelectionModel()
				.getSelectedIndex();
		logger.debug("selectedPoComboboxIndex: [" + selectedPoComboboxIndex
				+ "]");
		if (selectedPoComboboxIndex >= 0) {
			String customText = "Element: ["
					+ poComboBox.getItems().get(selectedPoComboboxIndex)
							.getIdentifier() + "]";
			Alert createDialog = poOverviewController.createConfirmDelete(
					poOverviewController.getMainApp().getPrimaryStage(),
					customText);
			if (createDialog.getResult().getButtonData()
					.equals(ButtonData.OK_DONE)) {
				poComboBox.setValue(null);
				poComboBox.getItems().remove(selectedPoComboboxIndex);
				logger.debug("Removed index [" + selectedPoComboboxIndex
						+ "] from poCombobox.");
			}
		} else {
			poOverviewController.createNoElementSelectedAlert(
					poOverviewController.getMainApp().getPrimaryStage(),
					"po comboBox");
		}
	}

	@Override
	public void handleEdit() {
		ComboBox<PoGenericFx> poComboBox = poOverviewController.getPoComboBox();
		PoGenericFx poGenericFx = poComboBox.getSelectionModel()
				.getSelectedItem();
		if (poGenericFx != null) {
			boolean okClicked = poOverviewController.getMainApp()
					.showPoEditDialog(poGenericFx);
		} else {
			// Nothing selected.
			poOverviewController.createNoPoSelectedAlert(poOverviewController
					.getMainApp().getPrimaryStage());
		}
	}

	@Override
	public boolean handleTest() {
		boolean noError = true;
		PoGenericFx selectedItem = poOverviewController.getPoComboBox()
				.getSelectionModel().getSelectedItem();

		for (ElementFx element : selectedItem.getElementsFx()) {

			if (element != null) {
				WebDriver driver = poOverviewController.getMainApp()
						.getWebDriver();
				Selector type = element.getType();
				String locator = element.getLocator();
				try {
					WebElement webElement = driver
							.findElement(type.by(locator));
					poOverviewController.getElementColour().put(
							selectedItem.getElementsFx().indexOf(element),
							Colour.GREEN);

				} catch (NoSuchElementException e) {
					poOverviewController.getElementColour().put(
							selectedItem.getElementsFx().indexOf(element),
							Colour.RED);
					noError = false;
				}
			}
		}

		for (TransitionFx transition : selectedItem.getTransitionsFx()) {

			if (transition != null) {
				WebDriver driver = poOverviewController.getMainApp()
						.getWebDriver();
				Selector type = transition.getType();
				String locator = transition.getLocator();
				try {
					WebElement webElement = driver
							.findElement(type.by(locator));
					poOverviewController.getTransitionColour()
							.put(selectedItem.getTransitionsFx().indexOf(
									transition), Colour.GREEN);

				} catch (NoSuchElementException e) {
					poOverviewController.getTransitionColour()
							.put(selectedItem.getTransitionsFx().indexOf(
									transition), Colour.RED);
					noError = false;
				}
			}
		}
		return noError;

	}

	@Override
	public boolean handleTestWithMessage() {
		return false;
	}

	@Override
	public boolean handleTestAndClick() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void handleMoveHtmlToElement() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleMoveHtmlToTransition() {
		// TODO Auto-generated method stub

	}

}
