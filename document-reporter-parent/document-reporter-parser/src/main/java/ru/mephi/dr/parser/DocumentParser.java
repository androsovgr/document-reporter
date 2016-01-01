package ru.mephi.dr.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import ru.mephi.dr.parser.retriever.AttributeValueRetriever;
import ru.mephi.dr.parser.template.Template;
import ru.mephi.dr.parser.template.Template.Attribute;
import ru.mephi.dr.parser.util.ParseException;
import ru.mephi.dr.parser.util.TemplateException;

public class DocumentParser {

	private final XWPFDocument docx;
	private final Template template;

	/**
	 * Read DOCX document and template into memory.
	 * 
	 * @param filePath
	 * @param templatePath
	 * @throws IOException
	 * @throws JAXBException
	 *             - if some troubles with template
	 */
	public DocumentParser(String filePath, String templatePath) throws IOException, JAXBException {
		try (InputStream is = new FileInputStream(filePath)) {
			docx = new XWPFDocument(is);
		}

		File file = new File(templatePath);
		JAXBContext jaxbContext = JAXBContext.newInstance(Template.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		template = (Template) jaxbUnmarshaller.unmarshal(file);
	}

	/**
	 * Parse document.
	 * 
	 * @return Map with attribute key as map key and attribute value as map
	 *         value
	 * @throws ParseException
	 * @throws TemplateException
	 */
	public Map<String, String> parse() throws ParseException, TemplateException {
		Map<String, String> result = new HashMap<>();
		for (Attribute attribute : template.getAttribute()) {
			try {
				@SuppressWarnings("unchecked")
				Class<? extends AttributeValueRetriever> retrieverClass = (Class<? extends AttributeValueRetriever>) Class
						.forName(attribute.getRetrieverClass());
				AttributeValueRetriever r = retrieverClass.newInstance();
				String attrResult = r.retrieve(attribute.getParameter(), docx);
				result.put(attribute.getKey(), attrResult);
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
