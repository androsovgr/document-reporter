package ru.mephi.dr.configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang3.StringUtils;

import ru.mephi.dr.configuration.util.ConfigFolderResolver;
import ru.mephi.dr.xml.ReportWriters;
import ru.mephi.dr.xml.ReportWriters.ReportWriter;

public class WritersConfiguration {

	private final ReportWriters config;

	public WritersConfiguration() throws FileNotFoundException, JAXBException {
		File file = new File(ConfigFolderResolver.getConfigFolder(), "report-writers.xml");
		if (!file.exists()) {
			String message = String.format("Can't find confuguration file: %s", file.getAbsolutePath());
			throw new FileNotFoundException(message);
		}
		JAXBContext jaxbContext = JAXBContext.newInstance(ReportWriters.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		config = (ReportWriters) jaxbUnmarshaller.unmarshal(file);
	}

	public List<String> getAllWriterIds() {
		List<String> result = new ArrayList<>();

		for (ReportWriter rw : config.getReportWriter()) {
			result.add(rw.getId());
		}

		return result;
	}

	/**
	 * 
	 * @param writerId
	 * @return report writer is exists. Otherwise return null.
	 */
	public ReportWriter writerById(String writerId) {
		for (ReportWriter rw : config.getReportWriter()) {
			if (StringUtils.equals(rw.getId(), writerId)) {
				return rw;
			}
		}
		return null;
	}
}
