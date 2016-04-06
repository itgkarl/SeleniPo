package de.muenchen.selenipo.view.poOverviewStates;

import org.apache.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import de.muenchen.selenipo.Selector;
import de.muenchen.selenipo.impl.fxModel.ElementFx;
import de.muenchen.selenipo.impl.fxModel.PoGenericFx;
import de.muenchen.selenipo.view.PoOverviewController;
import de.muenchen.selenipo.view.PoOverviewState;
import de.muenchen.selenipo.view.PoOverviewController.Colour;

public class HtmlSelectorComboboxState implements PoOverviewState {

	private static final Logger logger = Logger
			.getLogger(HtmlSelectorComboboxState.class);

	PoOverviewController poOverviewController;

	public HtmlSelectorComboboxState(PoOverviewController poOverviewController) {
		super();
		this.poOverviewController = poOverviewController;
	}

	@Override
	public void handleNew() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleDelete() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleEdit() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean handleTest() {
		boolean noError = true;
		PoGenericFx htmlItem = poOverviewController.getMainApp()
				.getHtmlPoGenericFx();

		for (ElementFx element : htmlItem.getElementsFx()) {

			if (element != null) {
				WebDriver driver = poOverviewController.getMainApp()
						.getWebDriver();
				Selector type = element.getType();
				String locator = element.getLocator();
				try {
					WebElement webElement = driver
							.findElement(type.by(locator));
					poOverviewController.getHtmlColour().put(
							htmlItem.getElementsFx().indexOf(element),
							Colour.GREEN);

				} catch (NoSuchElementException e) {
					poOverviewController.getHtmlColour().put(
							htmlItem.getElementsFx().indexOf(element),
							Colour.RED);
					noError = false;
				}
			}
		}
		return noError;
	}

	@Override
	public boolean handleTestWithMessage() {
		// TODO Auto-generated method stub
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
