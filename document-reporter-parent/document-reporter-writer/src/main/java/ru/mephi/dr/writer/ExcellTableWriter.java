package ru.mephi.dr.writer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import ru.mephi.dr.model.Column;
import ru.mephi.dr.model.Table;

public abstract class ExcellTableWriter implements TableWriter {

	private final Class<? extends Workbook> claz;

	public ExcellTableWriter(Class<? extends Workbook> claz) {
		super();
		this.claz = claz;
	}

	@Override
	public void write(Table<Column, String> table, File target) throws IOException {
		try (Workbook workbook = claz.newInstance()) {
			Sheet sheet = workbook.createSheet();
			CellStyle headerStyle = createHeaderStyle(workbook);
			writeHeaders(table, sheet, headerStyle);
			CellStyle bodyStyle = createBodyStyle(workbook);
			writeBody(table, sheet, bodyStyle);
			try (OutputStream os = new FileOutputStream(target)) {
				workbook.write(os);
			}
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	private void writeHeaders(Table<Column, String> table, Sheet sheet, CellStyle headerStyle) {
		Row row = sheet.createRow(0);
		int colIndex = 0;
		for (Column col : table.getColumns()) {
			Cell cell = row.createCell(colIndex);
			cell.setCellValue(col.getLabel());
			cell.setCellStyle(headerStyle);
			sheet.autoSizeColumn(colIndex);
			colIndex++;
		}
	}

	private void writeBody(Table<Column, String> table, Sheet sheet, CellStyle bodyStyle) {
		int rowIndex = 1;
		for (String[] row : table) {
			int colIndex = 0;
			Row docRow = sheet.createRow(rowIndex++);
			for (String cell : row) {
				Cell docCell = docRow.createCell(colIndex++);
				// TODO: should use correct type (int, String, date, etc.)
				docCell.setCellValue(cell);
				docCell.setCellStyle(bodyStyle);
			}
		}
	}

	private CellStyle createHeaderStyle(Workbook workbook) {
		CellStyle headerStyle = workbook.createCellStyle();
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerStyle.setFont(headerFont);
		headerStyle.setBorderTop(CellStyle.BORDER_THIN);
		headerStyle.setBorderBottom(CellStyle.BORDER_THIN);
		headerStyle.setBorderLeft(CellStyle.BORDER_THIN);
		headerStyle.setBorderRight(CellStyle.BORDER_THIN);
		return headerStyle;
	}

	private CellStyle createBodyStyle(Workbook workbook) {
		CellStyle bodyStyle = workbook.createCellStyle();
		bodyStyle.setBorderTop(CellStyle.BORDER_THIN);
		bodyStyle.setBorderBottom(CellStyle.BORDER_THIN);
		bodyStyle.setBorderLeft(CellStyle.BORDER_THIN);
		bodyStyle.setBorderRight(CellStyle.BORDER_THIN);
		return bodyStyle;
	}

}
