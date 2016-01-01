package ru.mephi.dr.parser.retriever;

import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import ru.mephi.dr.parser.template.Template.Attribute.Parameter;

/**
 * This class supports for retrieving document's pages count.
 * 
 * @author Gregory
 *
 */
public class PageCountRetriever implements AttributeValueRetriever {

	@Override
	public String retrieve(List<Parameter> parameters, XWPFDocument docx) {
		int pageCount = docx.getProperties().getExtendedProperties().getUnderlyingProperties().getPages();
		return pageCount + "";
	}

}
