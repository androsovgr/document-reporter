package ru.mephi.dr.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.mephi.dr.model.Column;
import ru.mephi.dr.model.Table;
import ru.mephi.dr.parser.exception.FileReadException;
import ru.mephi.dr.parser.exception.TemplateException;
import ru.mephi.dr.parser.exception.ValidateException;
import ru.mephi.dr.xml.Template;
import ru.mephi.dr.xml.Template.Attribute;

public class DocumentFolderParser {

	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentFolderParser.class);

	private final File folder;
	private final Template template;

	/**
	 * Make prepare for parsing.
	 * <ol>
	 * <li>Remember and check source folder</li>
	 * <li>Parse template</li>
	 * <li>Check and compile file name regular expression</li>
	 * </ol>
	 * 
	 * @param folderPath
	 *            path to directory with source files.
	 * @param templatePath
	 *            path to template for source files.
	 * @throws FileNotFoundException
	 *             if can't find template or source folder
	 * @throws FileReadException
	 *             if folder exists but can't read it.
	 * @throws TemplateException
	 *             if can't parse template.
	 */
	public DocumentFolderParser(String folderPath, String templatePath)
			throws FileNotFoundException, FileReadException, TemplateException {
		LOGGER.info("Init folder parser with folderPath={}, templatePath={}", folderPath, templatePath);
		this.folder = new File(folderPath);
		if (!folder.exists() || !folder.isDirectory()) {
			String message = String.format("Can't find directory with name %s", folderPath);
			throw new FileNotFoundException(message);
		}
		if (!folder.canRead()) {
			String message = String.format("Can't read from folder with name %s", folderPath);
			throw new FileReadException(message);
		}
		File file = new File(templatePath);
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Template.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			template = (Template) jaxbUnmarshaller.unmarshal(file);
		} catch (Exception e) {
			String message = String.format("Can't parse template %s", templatePath);
			throw new TemplateException(message, e);
		}
	}

	/**
	 * Parse folder and return result. Write WARN logs if directory contains
	 * files with wrong pattern name. Write ERROR logs for each file parse
	 * error. If troubles with template, returns.
	 * 
	 * @return {@link Table} - with labels at columns and parsed data inside
	 */
	public Table<Column, String> parse() {
		LOGGER.info("Started parsing");
		Table<Column, String> result = new Table<>(String.class, getColumns());
		for (File documentFile : folder.listFiles()) {
			try {
				DocumentParser dp = new DocumentParser(documentFile, template);
				Map<Column, String> parseResult = dp.parse();
				result.add(parseResult);
			} catch (ValidateException e) {
				LOGGER.warn("Can't parse file {}. {}", documentFile, e.getMessage());
			} catch (TemplateException e) {
				LOGGER.error("Some troubles with template for file {}", documentFile, e);
				break;
			} catch (Exception e) {
				LOGGER.error("Can't parse file {}", documentFile, e);
			}
		}
		LOGGER.info("Finished parsing");
		return result;
	}

	private Column[] getColumns() {
		List<Column> result = new ArrayList<>();
		for (Attribute attr : template.getAttribute()) {
			if (!attr.isSilence()) {
				result.add(new Column(attr.getKey(), attr.getLabel()));
			}
		}
		return result.toArray(new Column[0]);
	}
}
