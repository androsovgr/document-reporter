package ru.mephi.dr.ui.action.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import ru.mephi.dr.ui.util.TextFlowAppender;

public class LogController {

	@FXML
	private TextFlow textFlow;
	@FXML
	private Button closeButton;

	private Stage dialogStage;

	@FXML
	private void initialize() {
		TextFlowAppender.setTextFlow(textFlow);
	}

	@FXML
	private void close() {
		dialogStage.close();
	}

	public void enableClose() {
		closeButton.setDisable(false);
	}

	public void init(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public void show() {
		closeButton.setDisable(true);
		dialogStage.showAndWait();
	}

}
