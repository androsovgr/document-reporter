package ru.mephi.dr.parser.retriever;

import java.util.List;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import ru.mephi.dr.parser.exception.ParseException;
import ru.mephi.dr.parser.exception.TemplateException;
import ru.mephi.dr.xml.Template.Attribute.Parameter;

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
	public String retrieveDocx(List<Parameter> parameters, XWPFDocument docx) throws ParseException, TemplateException;

	/**
	 * Same the {@link AttributeValueRetriever#retrieveDocx(List, XWPFDocument)}
	 * , but works with .doc files
	 */
	public String retrieveDoc(List<Parameter> parameters, HWPFDocument doc) throws ParseException, TemplateException;
}
