package ru.mephi.dr.configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import ru.mephi.dr.configuration.util.ConfigFolderResolver;

public class MainConfiguration {

	private final PropertiesConfiguration configuration;

	public MainConfiguration() throws ConfigurationException, FileNotFoundException {
		File file = new File(ConfigFolderResolver.getConfigFolder(), "main.properties");
		if (!file.exists()) {
			String message = String.format("Can't find property file at location %s", file.getAbsolutePath());
			throw new FileNotFoundException(message);
		}
		configuration = new PropertiesConfiguration(file);
	}

	public String getEmailLogin() {
		return configuration.getString("email.login");
	}

	public String getEmailPassword() {
		return configuration.getString("email.password");
	}

	public String getEmailType() {
		return configuration.getString("email.type");
	}

	public File getWorkFolder() {
		String folderName = configuration.getString("work.folder");
		File folder = new File(folderName);
		return folder;
	}

	public File getWorkAllFolder() {
		String folderName = configuration.getString("work.folder");
		File folder = new File(folderName, "all");
		return folder;
	}

	public File getWorkSortedFolder() {
		String folderName = configuration.getString("work.folder");
		File folder = new File(folderName, "sorted");
		return folder;
	}

	public File getWorkReportFolder() {
		String folderName = configuration.getString("work.folder");
		File folder = new File(folderName, "report");
		return folder;
	}

	public Date getLastLodadedFileDate() {
		long time = configuration.getLong("work.lastload", 0);
		return new Date(time);
	}

	public void setEmailLogin(String login) throws ConfigurationException {
		configuration.setProperty("email.login", login);
		configuration.save();
	}

	public void setEmailPassword(String password) throws ConfigurationException {
		configuration.setProperty("email.password", password);
		configuration.save();
	}

	public void setEmailType(String emailType) throws ConfigurationException {
		configuration.setProperty("email.type", emailType);
		configuration.save();
	}

	public void setWorkFolder(File folder) throws ConfigurationException {
		configuration.setProperty("work.folder", folder.getAbsolutePath());
		configuration.save();
	}

	public void setLastLodadedFileDate(Date date) throws ConfigurationException {
		configuration.setProperty("work.lastload", date.getTime());
		configuration.save();
	}

}
