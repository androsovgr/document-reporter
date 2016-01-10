package ru.mephi.dr.ui.action;

import java.io.File;
import java.io.FileNotFoundException;

import javax.xml.bind.JAXBException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.mephi.dr.configuration.MainConfiguration;
import ru.mephi.dr.configuration.SortingConfigurations;
import ru.mephi.dr.configuration.WritersConfiguration;
import ru.mephi.dr.configuration.util.ConfigFolderResolver;
import ru.mephi.dr.model.Column;
import ru.mephi.dr.model.Table;
import ru.mephi.dr.model.util.FileUtils;
import ru.mephi.dr.parser.DocumentFolderParser;
import ru.mephi.dr.writer.TableWriter;
import ru.mephi.dr.xml.ReportWriters.ReportWriter;
import ru.mephi.dr.xml.Sortings.Sorting;

public class ReportAction {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReportAction.class);

	private final SortingConfigurations sc;
	private final MainConfiguration mc;
	private final WritersConfiguration wc;

	public ReportAction() throws FileNotFoundException, JAXBException, ConfigurationException {
		sc = new SortingConfigurations();
		mc = new MainConfiguration();
		wc = new WritersConfiguration();
	}

	public void makeReport(String writerId) {
		for (File dir : mc.getWorkSortedFolder().listFiles()) {
			try {
				// receive data
				File template = getTemplateForFolder(dir);
				if (template == null) {
					LOGGER.warn("Can't find template for folder {}", dir.getAbsolutePath());
					continue;
				}
				DocumentFolderParser dfp = new DocumentFolderParser(dir.getAbsolutePath(), template.getAbsolutePath());
				Table<Column, String> table = dfp.parse();
				// write data
				ReportWriter wr = wc.writerById(writerId);
				TableWriter tw = (TableWriter) Class.forName(wr.getWriterClass()).newInstance();
				FileUtils.checkDirectory(mc.getWorkReportFolder());
				File report = new File(mc.getWorkReportFolder(),
						dir.getName() + FilenameUtils.EXTENSION_SEPARATOR + wr.getExtention());
				tw.write(table, report);
				LOGGER.info("Saved report {} contains information about {} essays", report.getAbsolutePath(),
						table.size());
			} catch (Exception e) {
				LOGGER.error("Can't make report for folder {}", dir.getAbsolutePath(), e);
			}
		}
	}

	private File getTemplateForFolder(File folder) throws FileNotFoundException {
		String folderName = folder.getName();
		for (Sorting rule : sc.getAllSortingRules()) {
			if (StringUtils.equalsIgnoreCase(folderName, rule.getFolder())) {
				File cf = ConfigFolderResolver.getConfigFolder();
				File templateFile = new File(cf, "doc-template\\" + rule.getId() + ".xml");
				if (!templateFile.exists()) {
					String message = String.format("Tamplete file %s not founded", templateFile.getAbsolutePath());
					throw new FileNotFoundException(message);
				}
				return templateFile;
			}
		}
		return null;
	}
}
