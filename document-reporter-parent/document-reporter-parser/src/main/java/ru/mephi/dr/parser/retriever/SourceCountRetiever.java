package ru.mephi.dr.parser.retriever;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Section;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import ru.mephi.dr.parser.exception.ParseException;
import ru.mephi.dr.parser.exception.TemplateException;
import ru.mephi.dr.xml.Template.Attribute.Parameters.Parameter;

/**
 * This class supports for retrieving external sourse count from document.
 * <table border="1">
 * <caption>Configuration</caption>
 * <tr>
 * <th>Key</th>
 * <th>Description</th>
 * </tr>
 * <tr>
 * <td>{@value #HEADER_PATTERN_KEY}</td>
 * <td>Pattern of header text before external source list</td>
 * </tr>
 * </table>
 * 
 * @author Gregory
 *
 */
public class SourceCountRetiever implements AttributeValueRetriever {

	public static final String HEADER_PATTERN_KEY = "header-parttern";

	private String headerPattern;

	@Override
	public String retrieveDocx(List<Parameter> parameters, XWPFDocument docx) throws ParseException, TemplateException {
		parseParameters(parameters);
		for (Iterator<XWPFParagraph> iterator = docx.getParagraphsIterator(); iterator.hasNext();) {
			XWPFParagraph p = iterator.next();
			if (p.getText().trim().matches(headerPattern)) {
				int sourceCount = 0;
				while (iterator.hasNext()) {
					p = iterator.next();
					if (p.getNumIlvl() != null) {
						sourceCount++;
					} else {
						break;
					}
				}
				return sourceCount + "";
			}
		}
		// means header with right name not founded.
		String message = String.format("Document hasn't headers matching pattern %s", headerPattern);
		throw new ParseException(message);
	}

	@Override
	public String retrieveDoc(List<Parameter> parameters, HWPFDocument doc) throws ParseException, TemplateException {
		parseParameters(parameters);
		Range r = doc.getRange();
		Section s = r.getSection(0);
		for (int i = 0; i < s.numParagraphs(); i++) {
			Paragraph p = s.getParagraph(i);
			if (p.getLvl() == 0) {
				// first level header
				if (p.text().trim().matches(headerPattern)) {
					int sourceCount = 0;
					// Count list items after right header
					for (int j = i + 1; j < s.numParagraphs(); j++) {
						Paragraph listParagraph = s.getParagraph(j);
						if (listParagraph.isInList()) {
							sourceCount++;
						} else {
							break;
						}
					}
					return sourceCount + "";
				}
			}
		}
		// means header with right name not founded.
		String message = String.format("Document hasn't headers matching pattern %s", headerPattern);
		throw new ParseException(message);
	}

	private void parseParameters(List<Parameter> parameters) throws TemplateException {
		Map<String, String> paramMap = new HashMap<>();
		if (parameters != null) {
			for (Parameter parameter : parameters) {
				paramMap.put(parameter.getKey(), parameter.getValue());
			}
		}
		if (!paramMap.containsKey(HEADER_PATTERN_KEY)) {
			String message = String.format("Parameter %s wasn't specified", HEADER_PATTERN_KEY);
			throw new TemplateException(message);
		}
		if (StringUtils.isEmpty(paramMap.get(HEADER_PATTERN_KEY))) {
			String message = String.format("Parameter %s is empty", HEADER_PATTERN_KEY);
			throw new TemplateException(message);
		}
		try {
			Pattern.compile(paramMap.get(HEADER_PATTERN_KEY));
		} catch (PatternSyntaxException e) {
			String message = String.format("Parameter %s is not valid regular expression", HEADER_PATTERN_KEY);
			throw new TemplateException(message, e);
		}
		headerPattern = paramMap.get(HEADER_PATTERN_KEY);
	}
}
