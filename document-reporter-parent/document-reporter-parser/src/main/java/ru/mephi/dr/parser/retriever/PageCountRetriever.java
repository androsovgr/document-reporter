package ru.mephi.dr.parser.retriever;

import java.util.List;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import ru.mephi.dr.parser.exception.ParseException;
import ru.mephi.dr.parser.exception.TemplateException;
import ru.mephi.dr.xml.Template.Attribute.Parameters.Parameter;

/**
 * This class supports for retrieving document's pages count.
 * 
 * @author Gregory
 *
 */
public class PageCountRetriever implements AttributeValueRetriever {

	@Override
	public String retrieveDocx(List<Parameter> parameters, XWPFDocument docx) {
		int pageCount = docx.getProperties().getExtendedProperties().getUnderlyingProperties().getPages();
		return pageCount + "";
	}

	@Override
	public String retrieveDoc(List<Parameter> parameters, HWPFDocument doc) throws ParseException, TemplateException {
		// TODO Auto-generated method stub
		return null;
	}

}
