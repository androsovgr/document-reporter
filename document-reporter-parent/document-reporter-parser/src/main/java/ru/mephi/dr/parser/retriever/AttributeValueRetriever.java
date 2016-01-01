package ru.mephi.dr.parser.retriever;

import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import ru.mephi.dr.parser.template.Template.Attribute.Parameter;
import ru.mephi.dr.parser.util.ParseException;
import ru.mephi.dr.parser.util.TemplateException;

/**
 * Common interface for all beans which seek something at document and returns
 * it
 * 
 * @author Gregory
 *
 */
public interface AttributeValueRetriever {

	/**
	 * Retrieves attribute value from document
	 * 
	 * @param parameters
	 *            - configuration of attribute
	 * @param docx
	 *            - parsing file
	 * @return attribute value
	 * @throws ParseException
	 *             if document doesn't match attribute configuration
	 * @throws TemplateException
	 *             if template contains error
	 */
	public String retrieve(List<Parameter> parameters, XWPFDocument docx) throws ParseException, TemplateException;
}
