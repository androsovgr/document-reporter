package ru.mephi.dr.configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import ru.mephi.dr.configuration.util.ConfigFolderResolver;
import ru.mephi.dr.xml.Sortings;
import ru.mephi.dr.xml.Sortings.Sorting;

public class SortingConfigurations {

	private final Sortings config;

	public SortingConfigurations() throws FileNotFoundException, JAXBException {
		File file = new File(ConfigFolderResolver.getConfigFolder(), "sortings.xml");
		if (!file.exists()) {
			String message = String.format("Can't find confuguration file: %s", file.getAbsolutePath());
			throw new FileNotFoundException(message);
		}
		JAXBContext jaxbContext = JAXBContext.newInstance(Sortings.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		config = (Sortings) jaxbUnmarshaller.unmarshal(file);
	}

	public List<Sorting> getAllSortingRules() {
		return config.getSorting();
	}
}
