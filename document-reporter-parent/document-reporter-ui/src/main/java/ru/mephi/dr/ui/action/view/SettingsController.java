package ru.mephi.dr.ui.action.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import ru.mephi.dr.configuration.MainConfiguration;
import ru.mephi.dr.configuration.util.ConfigFolderResolver;

public class SettingsController {

	private static final String DATE_PATTERN = "dd.MM.yyyy hh:mm:ss";
	private static final DateFormat DF = new SimpleDateFormat(DATE_PATTERN);
	private static final Logger LOGGER = LoggerFactory.getLogger(SettingsController.class);

	@FXML
	private TextField loginField;
	@FXML
	private PasswordField passwordField;
	@FXML
	private PasswordField passwordRepeatField;
	@FXML
	private ComboBox<String> mailType;
	@FXML
	private TextField lastLoadDateField;
	@FXML
	private TextField workFolderField;
	@FXML
	private Button cancelButton;
	@FXML
	private Button okButton;

	@FXML
	private Label loginErrorLabel;
	@FXML
	private Label passwordErrorLabel;
	@FXML
	private Label mailTypeErrorLabel;
	@FXML
	private Label dateErrorLabel;

	private MainConfiguration mc;

	private Stage stage;

	@FXML
	private void initialize() throws ConfigurationException, FileNotFoundException {
		loadConfig();
	}

	private void loadConfig() throws ConfigurationException, FileNotFoundException {
		loadMailAllowedValues();
		mc = new MainConfiguration();
		loginField.setText(mc.getEmailLogin());
		passwordField.setText(mc.getEmailPassword());
		passwordRepeatField.setText(mc.getEmailPassword());
		lastLoadDateField.setText(DF.format(mc.getLastLodadedFileDate()));
		workFolderField.setText(mc.getWorkFolder().getAbsolutePath());
		mailType.setValue(mc.getEmailType());
		okButton.setDisable(true);
		clearErrors();
	}

	@FXML
	private void valueChanged() {
		okButton.setDisable(false);
	}

	@FXML
	private void selectWorkFolder() {
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("Выберите рабочую директорию");
		File selectedDirectory = chooser.showDialog(stage);
		if (selectedDirectory != null) {
			workFolderField.setText(selectedDirectory.getAbsolutePath());
			okButton.setDisable(false);
		}
	}

	@FXML
	private void saveConfig() throws ConfigurationException, ParseException, FileNotFoundException {
		clearErrors();
		boolean validationResult = validateInput();
		if (validationResult) {
			saveValidConfig();
			loadConfig();
			okButton.setDisable(true);
		}
	}

	private void saveValidConfig() throws ConfigurationException, ParseException {
		mc.setEmailLogin(loginField.getText());
		mc.setEmailPassword(passwordField.getText());
		Date date;
		if (lastLoadDateField.getText().isEmpty()) {
			date = new Date(0);
		} else {
			date = DF.parse(lastLoadDateField.getText());
		}
		mc.setLastLodadedFileDate(date);
		mc.setEmailType(mailType.getValue());
		mc.setWorkFolder(new File(workFolderField.getText()));
	}

	private boolean validateInput() {
		boolean result = true;
		if (StringUtils.isEmpty(loginField.getText())) {
			loginErrorLabel.setText("Пусто");
			result = false;
		}
		if (StringUtils.isEmpty(passwordField.getText())) {
			passwordErrorLabel.setText("Пусто");
			result = false;
		} else {
			if (!StringUtils.equals(passwordField.getText(), passwordRepeatField.getText())) {
				passwordErrorLabel.setText("Пароли не совпадают");
				result = false;
			}
		}
		if (StringUtils.isEmpty(mailType.getValue())) {
			mailTypeErrorLabel.setText("Не выбрано");
			result = false;
		}
		if (!StringUtils.isEmpty(lastLoadDateField.getText())) {
			try {
				DF.parse(lastLoadDateField.getText());
			} catch (ParseException e) {
				LOGGER.debug("Can't parse date {}", lastLoadDateField.getText(), e);
				dateErrorLabel.setText("Шаблон: " + DATE_PATTERN);
				result = false;
			}
		}
		if (StringUtils.isEmpty(workFolderField.getText())) {
			result = false;
			workFolderField.getStyleClass().add("error");
		}

		return result;
	}

	private void loadMailAllowedValues() {
		File configFolder = ConfigFolderResolver.getConfigFolder();
		File mailConfigFolder = new File(configFolder, "mail");
		List<String> mailConfigs = new ArrayList<>();
		for (File file : mailConfigFolder.listFiles()) {
			mailConfigs.add(file.getName().split("\\.")[0]);
		}
		mailType.getItems().addAll(mailConfigs);
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	private void clearErrors() {
		loginErrorLabel.setText("");
		passwordErrorLabel.setText("");
		mailTypeErrorLabel.setText("");
		dateErrorLabel.setText("");
		workFolderField.getStyleClass().remove("error");
	}

}
