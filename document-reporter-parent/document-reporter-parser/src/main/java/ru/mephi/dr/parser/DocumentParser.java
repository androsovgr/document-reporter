package ru.mephi.dr.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import ru.mephi.dr.model.Column;
import ru.mephi.dr.parser.exception.ParseException;
import ru.mephi.dr.parser.exception.TemplateException;
import ru.mephi.dr.parser.exception.ValidateException;
import ru.mephi.dr.parser.retriever.AttributeValueRetriever;
import ru.mephi.dr.parser.validator.AttributeValueValidator;
import ru.mephi.dr.xml.Template;
import ru.mephi.dr.xml.Template.Attribute;
import ru.mephi.dr.xml.Template.Attribute.Parameters.Parameter;
import ru.mephi.dr.xml.Template.Attribute.Validators.Validator;

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
	public Map<Column, String> parse() throws ParseException, TemplateException, ValidateException {
		Map<Column, String> result = new HashMap<>();
		fillResult(result);
		validateResult(result);
		cleanSilence(result);
		return result;
	}

	private void fillResult(Map<Column, String> result) throws ParseException {
		for (Attribute attribute : template.getAttribute()) {
			try {
				@SuppressWarnings("unchecked")
				Class<? extends AttributeValueRetriever> retrieverClass = (Class<? extends AttributeValueRetriever>) Class
						.forName(attribute.getRetrieverClass());
				AttributeValueRetriever r = retrieverClass.newInstance();
				List<Parameter> parameters;
				if (attribute.getParameters() == null) {
					parameters = new ArrayList<>();
				} else {
					parameters = attribute.getParameters().getParameter();
				}
				String attrResult = r.retrieveDocx(parameters, docx);
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
	}

	private void validateResult(Map<Column, String> result) throws TemplateException, ValidateException {
		for (Attribute attribute : template.getAttribute()) {
			if (attribute.getValidators() != null) {
				Column attributeCol = new Column(attribute.getKey(), attribute.getLabel());
				for (Validator validator : attribute.getValidators().getValidator()) {
					try {
						AttributeValueValidator vValidator = (AttributeValueValidator) Class
								.forName(validator.getValidatorClass()).newInstance();
						String value = result.get(attributeCol);
						List<ru.mephi.dr.xml.Template.Attribute.Validators.Validator.Parameters.Parameter> parameters;
						if (validator.getParameters() == null) {
							parameters = new ArrayList<>();
						} else {
							parameters = validator.getParameters().getParameter();
						}
						vValidator.validate(value, validator.getValidatorMessage(), parameters);
					} catch (InstantiationException | IllegalAccessException | ClassNotFoundException
							| ClassCastException e) {
						String message = String.format("Illegal validator class %s specified at template",
								validator.getValidatorClass());
						throw new TemplateException(message);
					}
				}
			}
		}
	}

	private void cleanSilence(Map<Column, String> result) {
		for (Attribute attribute : template.getAttribute()) {
			if (attribute.isSilence()) {
				Column attributeCol = new Column(attribute.getKey(), attribute.getLabel());
				result.remove(attributeCol);
			}
		}
	}
}
