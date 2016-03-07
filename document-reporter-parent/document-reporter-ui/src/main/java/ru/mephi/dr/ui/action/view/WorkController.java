package ru.mephi.dr.ui.action.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXML;
import ru.mephi.dr.ui.util.AsyncActionWithCallback;

public class WorkController {

	private static final Logger LOGGER = LoggerFactory.getLogger(WorkController.class);

	private LogController aac;
	private final AsyncActionWithCallback aawc = new AsyncActionWithCallback();

	@FXML
	private void initialize() {

	}

	@FXML
	private void receive() {
		aawc.submit(() -> {
			LOGGER.info("Called");
			try {
				Thread.sleep(4000);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			LOGGER.error("error");
		} , () -> aac.enableClose());
		aac.show();
	}

	public void init(LogController aac) {
		this.aac = aac;
	}
}
