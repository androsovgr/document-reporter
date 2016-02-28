package ru.mephi.dr.ui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import ru.mephi.dr.ui.action.view.SettingsController;

public class DrFxUi extends Application {

	private Stage primaryStage;
	private AnchorPane rootLayout;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		primaryStage.setTitle("Отчетность по рефератам");
		initRootLayout();
		initSettingsLayout();
	}

	/**
	 * Initializes the root layout.
	 */
	private void initRootLayout() {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(DrFxUi.class.getResource("/ru/mephi/dr/ui/view/RootLayout.fxml"));
			rootLayout = loader.load();
			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			scene.getStylesheets().add(getClass().getResource("/css/main.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initSettingsLayout() {
		TabPane tabPane = (TabPane) rootLayout.getChildren().get(0);
		Tab settingsTab = tabPane.getTabs().get(1);
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(DrFxUi.class.getResource("/ru/mephi/dr/ui/view/SettingsLayout.fxml"));
			GridPane settingsPane = loader.load();
			SettingsController sc = loader.getController();
			sc.setStage(primaryStage);
			// Show the scene containing the root layout.
			settingsTab.setContent(settingsPane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}
