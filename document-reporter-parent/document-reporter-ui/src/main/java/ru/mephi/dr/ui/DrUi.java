package ru.mephi.dr.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.mephi.dr.configuration.WritersConfiguration;
import ru.mephi.dr.ui.action.ReceiveMailAction;
import ru.mephi.dr.ui.action.ReportAction;

public class DrUi {

	private static final Logger LOGGER = LoggerFactory.getLogger(DrUi.class);

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			System.out.print("Please enter command: ");
			String command = br.readLine();
			switch (command) {
			case "receive":
				receiveMail();
				break;
			case "report":
				makeReport();
				break;
			case "exit":
				return;
			default:
				System.out.println("Allowed commands are: receive, report, exit");
				break;
			}
		}
	}

	private static void receiveMail() {
		try {
			ReceiveMailAction rma = new ReceiveMailAction();
			rma.action();
		} catch (Exception e) {
			LOGGER.error("Can't receive mail", e);
		}
	}

	private static void makeReport() {
		try {
			WritersConfiguration wc = new WritersConfiguration();
			List<String> wrIds = wc.getAllWriterIds();
			while (true) {
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				String message = String
						.format("Please enter report type. Allowed: %s. Or you may enter 'cancel' to cancel: ", wrIds);
				System.out.print(message);
				String writerId = br.readLine();
				if (wrIds.contains(writerId)) {
					ReportAction ra = new ReportAction();
					ra.makeReport(writerId);
					break;
				} else if (StringUtils.equalsIgnoreCase(writerId, "cancel")) {
					return;
				}
			}
		} catch (Exception e) {
			LOGGER.error("Can't compose report", e);
		}

	}
}
