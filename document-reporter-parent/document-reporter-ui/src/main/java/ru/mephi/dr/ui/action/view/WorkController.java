package ru.mephi.dr.ui.action.view;

import java.io.FileNotFoundException;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import ru.mephi.dr.configuration.WritersConfiguration;
import ru.mephi.dr.ui.action.ReceiveMailAction;
import ru.mephi.dr.ui.action.ReportAction;
import ru.mephi.dr.ui.util.AsyncActionWithCallback;

public class WorkController {

	private static final Logger LOGGER = LoggerFactory.getLogger(WorkController.class);

	private LogController aac;
	private final AsyncActionWithCallback aawc = new AsyncActionWithCallback();
	@FXML
	private ComboBox<String> reportTypesBox;

	@FXML
	private void initialize() throws FileNotFoundException, JAXBException {
		loadReportTypes();
	}

	private void loadReportTypes() throws FileNotFoundException, JAXBException {
		WritersConfiguration wc = new WritersConfiguration();
		List<String> wrIds = wc.getAllWriterIds();
		reportTypesBox.getItems().addAll(wrIds);
		reportTypesBox.setValue(wrIds.get(0));
	}

	@FXML
	private void receive() {
		aac.clear();
		aawc.submit(() -> doReceive(), () -> aac.enableClose());
		aac.show();
	}

	private void doReceive() {
		try {
			ReceiveMailAction rma = new ReceiveMailAction();
			rma.action();
		} catch (Exception e) {
			LOGGER.error("Can't receive mail", e);
		}
	}

	@FXML
	private void report() {
		aac.clear();
		aawc.submit(() -> doReport(), () -> aac.enableClose());
		aac.show();
	}

	private void doReport() {
		try {
			ReportAction ra = new ReportAction();
			ra.makeReport(reportTypesBox.getValue());
		} catch (Exception e) {
			LOGGER.error("Can't compose report", e);
		}
	}

	public void init(LogController aac) {
		this.aac = aac;
	}
}
