package ru.mephi.dr.writer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import ru.mephi.dr.model.Column;
import ru.mephi.dr.model.Table;

public class XslxTableWriter implements TableWriter {

	@Override
	public void write(Table<Column, String> table, File target) throws IOException {
		try (XSSFWorkbook workbook = new XSSFWorkbook()) {
			XSSFSheet sheet = workbook.createSheet();
			CellStyle headerStyle = createHeaderStyle(workbook);
			writeHeaders(table, sheet, headerStyle);
			CellStyle bodyStyle = createBodyStyle(workbook);
			writeBody(table, sheet, bodyStyle);
			try (OutputStream os = new FileOutputStream(target)) {
				workbook.write(os);
			}
		}
	}

	private void writeHeaders(Table<Column, String> table, XSSFSheet sheet, CellStyle headerStyle) {
		XSSFRow row = sheet.createRow(0);
		int colIndex = 0;
		for (Column col : table.getColumns()) {
			XSSFCell cell = row.createCell(colIndex);
			cell.setCellValue(col.getLabel());
			cell.setCellStyle(headerStyle);
			sheet.autoSizeColumn(colIndex);
			colIndex++;
		}
	}

	private void writeBody(Table<Column, String> table, XSSFSheet sheet, CellStyle bodyStyle) {
		int rowIndex = 1;
		for (String[] row : table) {
			int colIndex = 0;
			XSSFRow docRow = sheet.createRow(rowIndex++);
			for (String cell : row) {
				XSSFCell docCell = docRow.createCell(colIndex++);
				// TODO: should use correct type (int, String, date, etc.)
				docCell.setCellValue(cell);
				docCell.setCellStyle(bodyStyle);
			}
		}
	}

	private CellStyle createHeaderStyle(XSSFWorkbook workbook) {
		CellStyle headerStyle = workbook.createCellStyle();
		XSSFFont headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerStyle.setFont(headerFont);
		headerStyle.setBorderTop(CellStyle.BORDER_THIN);
		headerStyle.setBorderBottom(CellStyle.BORDER_THIN);
		headerStyle.setBorderLeft(CellStyle.BORDER_THIN);
		headerStyle.setBorderRight(CellStyle.BORDER_THIN);
		return headerStyle;
	}

	private CellStyle createBodyStyle(XSSFWorkbook workbook) {
		CellStyle bodyStyle = workbook.createCellStyle();
		bodyStyle.setBorderTop(CellStyle.BORDER_THIN);
		bodyStyle.setBorderBottom(CellStyle.BORDER_THIN);
		bodyStyle.setBorderLeft(CellStyle.BORDER_THIN);
		bodyStyle.setBorderRight(CellStyle.BORDER_THIN);
		return bodyStyle;
	}

}
