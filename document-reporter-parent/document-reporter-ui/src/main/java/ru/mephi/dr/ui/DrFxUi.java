package ru.mephi.dr.ui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.mephi.dr.ui.action.view.LogController;
import ru.mephi.dr.ui.action.view.SettingsController;
import ru.mephi.dr.ui.action.view.WorkController;

public class DrFxUi extends Application {

	private Stage primaryStage;
	private AnchorPane rootLayout;
	private LogController logController;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		primaryStage.setTitle("Отчетность по рефератам");
		initRootLayout();
		initLogLayout();
		initSettingsLayout();
		initWorkLayout();
	}

	/**
	 * Initializes the root layout.
	 * 
	 * @throws IOException
	 */
	private void initRootLayout() throws IOException {
		// Load root layout from fxml file.
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(DrFxUi.class.getResource("/ru/mephi/dr/ui/view/RootLayout.fxml"));
		rootLayout = loader.load();
		// Show the scene containing the root layout.
		Scene scene = new Scene(rootLayout);
		scene.getStylesheets().add(getClass().getResource("/css/main.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private void initSettingsLayout() throws IOException {
		TabPane tabPane = (TabPane) rootLayout.getChildren().get(0);
		Tab settingsTab = tabPane.getTabs().get(1);
		// Load root layout from fxml file.
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(DrFxUi.class.getResource("/ru/mephi/dr/ui/view/SettingsLayout.fxml"));
		GridPane settingsPane = loader.load();
		SettingsController sc = loader.getController();
		sc.setStage(primaryStage);
		// Show the scene containing the root layout.
		settingsTab.setContent(settingsPane);
	}

	public static void main(String[] args) {
		launch(args);
	}

	private void initLogLayout() throws IOException {
		Stage dialogStage = new Stage();
		dialogStage.setTitle("Edit Person");
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.initOwner(primaryStage);
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(DrFxUi.class.getResource("/ru/mephi/dr/ui/view/LogLayout.fxml"));
		AnchorPane pane = loader.load();
		dialogStage.setScene(new Scene(pane));
		logController = loader.getController();
		logController.init(dialogStage);
	}

	private void initWorkLayout() throws IOException {
		TabPane tabPane = (TabPane) rootLayout.getChildren().get(0);
		Tab settingsTab = tabPane.getTabs().get(0);
		// Load root layout from fxml file.
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(DrFxUi.class.getResource("/ru/mephi/dr/ui/view/WorkLayout.fxml"));
		GridPane workingPane = loader.load();
		WorkController wc = loader.getController();
		wc.init(logController);
		// Show the scene containing the root layout.
		settingsTab.setContent(workingPane);
	}

}
