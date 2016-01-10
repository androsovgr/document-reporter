package ru.mephi.dr.parser.retriever;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.IteratorUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableCell;
import org.apache.poi.hwpf.usermodel.TableRow;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import ru.mephi.dr.parser.exception.ParseException;
import ru.mephi.dr.parser.exception.TemplateException;
import ru.mephi.dr.parser.util.MyTableIterator;
import ru.mephi.dr.parser.util.ParagraphList;
import ru.mephi.dr.parser.util.ParameterParser;
import ru.mephi.dr.parser.util.ParseUtil;
import ru.mephi.dr.parser.util.TableCellList;
import ru.mephi.dr.parser.util.TableRowList;
import ru.mephi.dr.xml.Template.Attribute.Parameters.Parameter;

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
 * <td rowspan="4"><b>first</b>, <b>last</b> or number of decimal (starting from
 * 1)</td>
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
	public String retrieveDocx(List<Parameter> parameters, XWPFDocument docx) throws ParseException, TemplateException {
		parseParameters(parameters);
		XWPFTable table = getAtPosition(tablePosition, docx.getTables(), XWPFTable.class);
		XWPFTableRow row = getAtPosition(rowPosition, table.getRows(), XWPFTableRow.class);
		XWPFTableCell docCell = getAtPosition(columnPosition, row.getTableCells(), XWPFTableCell.class);
		XWPFParagraph docLine = getAtPosition(linePosition, docCell.getParagraphs(), XWPFParagraph.class);
		String text = docLine.getText();
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

	@Override
	public String retrieveDoc(List<Parameter> parameters, HWPFDocument doc) throws ParseException, TemplateException {
		parseParameters(parameters);
		MyTableIterator mti = new MyTableIterator(doc);
		List<Table> tables = IteratorUtils.toList(mti);
		Table table = getAtPosition(tablePosition, tables, Table.class);
		TableRowList trl = new TableRowList(table);
		TableRow row = getAtPosition(rowPosition, trl, TableRow.class);
		TableCellList tcl = new TableCellList(row);
		TableCell docCell = getAtPosition(columnPosition, tcl, TableCell.class);
		ParagraphList pl = new ParagraphList(docCell);
		Paragraph paragraph = getAtPosition(linePosition, pl, Paragraph.class);
		String text = paragraph.text();
		String parsed = parse(text);
		return parsed;
	}

	private String parse(String text) throws ParseException, TemplateException {
		Pattern p = Pattern.compile(expression);
		// for doc compatibility
		text = text.trim();
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

	private <T> T getAtPosition(String position, List<T> list, Class<T> claz) throws TemplateException, ParseException {
		int positionNumber = ParseUtil.parsePosition(position, list.size());
		T t;
		try {
			t = list.get(positionNumber);
			return t;
		} catch (IndexOutOfBoundsException e) {
			String message = String.format("Can't find %s at position %s", claz, position);
			throw new ParseException(message);
		}
	}

}
