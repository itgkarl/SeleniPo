package de.muenchen.selenipo;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.loadui.testfx.GuiTest;
import org.loadui.testfx.categories.TestFX;

import de.muenchen.selenipo.view.PoOverviewController;

@Category(TestFX.class)
public class TestOverview extends GuiTest {

	@Override
	protected Parent getRootNode() {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class.getResource("view/PoOverview.fxml"));
		try {
			AnchorPane personOverview = (AnchorPane) loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Give the controller access to the main app. PoOverviewController
		PoOverviewController controller = loader.getController();
		controller = loader.getController();
		controller.setMainApp(new MainApp());
		return loader.getRoot();

	}

	@Test
	public void basicTest() {
		System.out.println("in");
		Button button = find("New..");
		click(button);
		button = find("Cancel");
		click(button);
		button = find("Edit..");
		click(button);
		button = find("OK");
		click(button);
		button = find("Delete");
		click(button);
		button = find("OK");
		click(button);
	}

}
