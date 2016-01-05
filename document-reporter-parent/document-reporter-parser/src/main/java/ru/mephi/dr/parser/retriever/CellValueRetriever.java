package ru.mephi.dr.parser.retriever;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import ru.mephi.dr.parser.exception.ParseException;
import ru.mephi.dr.parser.exception.TemplateException;
import ru.mephi.dr.parser.template.Template.Attribute.Parameter;
import ru.mephi.dr.parser.util.ParameterParser;
import ru.mephi.dr.parser.util.ParseUtil;

/**
 * This class supports for retrieving value from some table of document.
 * <table border="1">
 * <caption>Configuration</caption>
 * <tr>
 * <th>Key</th>
 * <th>Description</th>
 * <th>Default value</th>
 * <th>Allowed value</th>
 * </tr>
 * <tr>
 * <td>table-position</td>
 * <td>Position of target table in document.</td>
 * <td>1</td>
 * <td rowspan="4"><b>first</b>, <b>last</b> or number of decimal (starting from 1)</td>
 * </tr>
 * <tr>
 * <td>row-number</td>
 * <td>Number of row in table</td>
 * <td>1</td>
 * </tr>
 * <tr>
 * <td>column-number</td>
 * <td>Cell number in row</td>
 * <td>1</td>
 * </tr>
 * <tr>
 * <td>line</td>
 * <td>Line of cell</td>
 * <td>1</td>
 * </tr>
 * <tr>
 * <td>expression</td>
 * <td>Regular expression of target line. Target value must be enclosed in
 * parentheses. E.g. expression <i>Ololo (.*) 111.*</i> for text <i>
 * "Ololo pishpish 111asd"</i> will return <i>"pishpish"</i></td>
 * <td>(.*)</td>
 * <td>Regular expression</td>
 * </tr>
 * <tr>
 * <td>expression-group-number</td>
 * <td>Number of target parentheses-enclosed value. E.g. for expression
 * "(.*) two (.*)", text "one two three" and group-number=2 value "three" will
 * be returned</td>
 * <td>1</td>
 * <td><b>first</b>, <b>last</b> or number of decimal (starting from 1)</td>
 * </tr>
 * </table>
 * 
 * @author Gregory
 *
 */
public class CellValueRetriever implements AttributeValueRetriever {

	private String expression;
	private int expressionGroupNumber;
	private String tablePosition;
	private String rowPosition;
	private String columnPosition;
	private String linePosition;

	@Override
	public String retrieve(List<Parameter> parameters, XWPFDocument docx) throws ParseException, TemplateException {
		parseParameters(parameters);
		String text = retrieveText(docx);
		String parsed = parse(text);
		return parsed;
	}

	private void parseParameters(List<Parameter> parameters) {
		expression = ParameterParser.getParameter(parameters, "expression", "(.*)");
		expressionGroupNumber = ParameterParser.getIntParameter(parameters, "expression-group-number", 1);
		tablePosition = ParameterParser.getParameter(parameters, "table-position", "1");
		rowPosition = ParameterParser.getParameter(parameters, "row-number", "1");
		columnPosition = ParameterParser.getParameter(parameters, "column-number", "1");
		linePosition = ParameterParser.getParameter(parameters, "line", "1");
	}

	private String retrieveText(XWPFDocument docx) throws ParseException, TemplateException {
		int tableNumber = ParseUtil.parsePosition(tablePosition, docx.getTables().size());
		XWPFTable table;
		try {
			table = docx.getTables().get(tableNumber);
		} catch (IndexOutOfBoundsException e) {
			String message = String.format("Can't find table with position %s at document ", tablePosition);
			throw new ParseException(message);
		}
		int rowNumber = ParseUtil.parsePosition(rowPosition, table.getRows().size());
		XWPFTableRow row;
		try {
			row = table.getRow(rowNumber);
		} catch (IndexOutOfBoundsException e) {
			String message = String.format("Can't find row with position %s at document", rowPosition);
			throw new ParseException(message);
		}
		int columnNumber = ParseUtil.parsePosition(columnPosition, row.getTableCells().size());
		XWPFTableCell docCell;
		try {
			docCell = row.getTableCells().get(columnNumber);
		} catch (IndexOutOfBoundsException e) {
			String message = String.format("Can't find cell with position %s at document", columnPosition);
			throw new ParseException(message);
		}
		int lineNumber = ParseUtil.parsePosition(linePosition, docCell.getParagraphs().size());
		XWPFParagraph docLine;
		try {
			docLine = docCell.getParagraphs().get(lineNumber);
		} catch (IndexOutOfBoundsException e) {
			String message = String.format("Can't find line with position %s at document", linePosition);
			throw new ParseException(message);
		}
		return docLine.getText();
	}

	private String parse(String text) throws ParseException, TemplateException {
		Pattern p = Pattern.compile(expression);
		Matcher m = p.matcher(text);
		if (!m.matches()) {
			String message = String.format("Value %s doesnt matches pattern %s", text, expression);
			throw new ParseException(message);
		}
		String result;
		try {
			result = m.group(expressionGroupNumber);
		} catch (IndexOutOfBoundsException e) {
			String message = String.format("Illegal group number %d for attribute", expressionGroupNumber);
			throw new TemplateException(message);
		}
		return result;
	}

}
