package ru.mephi.dr.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.mephi.dr.model.Column;
import ru.mephi.dr.model.Table;
import ru.mephi.dr.parser.exception.FileReadException;
import ru.mephi.dr.parser.exception.TemplateException;
import ru.mephi.dr.parser.template.Template;
import ru.mephi.dr.parser.template.Template.Attribute;
import ru.mephi.dr.parser.util.FileNameFilter;

public class DocumentFolderParser {

	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentFolderParser.class);

	private final File folder;
	private final Template template;
	private final Pattern fileNamePattern;

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
	 * @param fileMask
	 *            regular expression for source files name.
	 * @throws FileNotFoundException
	 *             if can't find template or source folder
	 * @throws FileReadException
	 *             if folder exists but can't read it.
	 * @throws TemplateException
	 *             if can't parse template.
	 * @throws PatternSyntaxException
	 *             if file name pattern is incorrect regular expression
	 */
	public DocumentFolderParser(String folderPath, String templatePath, String fileMask)
			throws FileNotFoundException, FileReadException, TemplateException, PatternSyntaxException {
		LOGGER.info("Init folder parser with folderPath={}, templatePath={}, fileMask={}", folderPath, templatePath,
				fileMask);
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
		fileNamePattern = Pattern.compile(fileMask);
	}

	/**
	 * Parse folder and return result. </br>
	 * Write WARN logs if directory contains files with wrong pattern name</br>
	 * Write ERROR logs for each file parse error.
	 * 
	 * @return {@link Table} - with labels at columns and parsed data inside
	 */
	public Table<Column, String> parse() {
		LOGGER.info("Started parsing");
		Table<Column, String> result = new Table<>(String.class, getColumns());
		FilenameFilter fnf = new FileNameFilter(fileNamePattern);
		for (File documentFile : folder.listFiles(fnf)) {
			try {
				DocumentParser dp = new DocumentParser(documentFile, template);
				Map<Column, String> parseResult = dp.parse();
				result.add(parseResult);
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
			result.add(new Column(attr.getKey(), attr.getLabel()));
		}
		return result.toArray(new Column[0]);
	}
}
