package ru.mephi.dr.configuration.util;

import java.io.File;

public class ConfigFolderResolver {

	public static File getConfigFolder() {
		String thisPath = System.getProperty("application.directory");
		File configDirectory = new File(new File(thisPath), "configuration");
		return configDirectory;
	}
}
