package ru.mephi.dr.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import ru.mephi.dr.model.Column;
import ru.mephi.dr.parser.exception.ParseException;
import ru.mephi.dr.parser.exception.TemplateException;
import ru.mephi.dr.parser.retriever.AttributeValueRetriever;
import ru.mephi.dr.xml.Template;
import ru.mephi.dr.xml.Template.Attribute;

public class DocumentParser {

	private final XWPFDocument docx;
	private final Template template;

	/**
	 * Read DOCX document into memory and remember template.
	 * 
	 * @param documentFile
	 * @param templatePath
	 * @throws IOException
	 * @throws JAXBException
	 *             - if some troubles with template
	 */
	public DocumentParser(File documentFile, Template template) throws IOException, JAXBException {
		try (InputStream is = new FileInputStream(documentFile)) {
			docx = new XWPFDocument(is);
		}
		this.template = template;
	}

	/**
	 * Parse document.
	 * 
	 * @return Map with attribute key as map key and attribute value as map
	 *         value
	 * @throws ParseException
	 * @throws TemplateException
	 */
	public Map<Column, String> parse() throws ParseException, TemplateException {
		Map<Column, String> result = new HashMap<>();
		for (Attribute attribute : template.getAttribute()) {
			try {
				@SuppressWarnings("unchecked")
				Class<? extends AttributeValueRetriever> retrieverClass = (Class<? extends AttributeValueRetriever>) Class
						.forName(attribute.getRetrieverClass());
				AttributeValueRetriever r = retrieverClass.newInstance();
				String attrResult = r.retrieveDocx(attribute.getParameter(), docx);
				Column col = new Column(attribute.getKey(), attribute.getLabel());
				result.put(col, attrResult);
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				String message = String.format("Can't create instance of class %s for attribute %s",
						attribute.getRetrieverClass(), attribute.getKey());
				throw new ParseException(message, e);
			} catch (Exception e) {
				String message = String.format("Exception for attribute with key=%s", attribute.getKey());
				throw new ParseException(message, e);
			}
		}
		return result;
	}

}
