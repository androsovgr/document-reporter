package ru.mephi.dr.writer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import ru.mephi.dr.model.Column;
import ru.mephi.dr.model.Table;

public class DocxTableWriter implements TableWriter {

	@Override
	public void write(Table<Column, String> table, File target) throws IOException {
		try (XWPFDocument document = new XWPFDocument()) {
			XWPFTable docTable = document.createTable();
			writeHeaders(docTable, table);
			writeBody(docTable, table);
			try (OutputStream os = new FileOutputStream(target)) {
				document.write(os);
			}
		}
	}

	private void writeHeaders(XWPFTable docTable, Table<Column, String> table) {
		XWPFTableRow headRow = docTable.getRow(0);
		boolean first = true;
		for (Column col : table.getColumns()) {
			XWPFTableCell cell;
			if (first) {
				cell = headRow.getCell(0);
				first = false;
			} else {
				cell = headRow.addNewTableCell();
			}
			cell.setText(col.getLabel());
		}
	}

	private void writeBody(XWPFTable docTable, Table<Column, String> table) {
		for (String[] row : table) {
			XWPFTableRow docRow = docTable.createRow();
			int cellIndex = 0;
			for (XWPFTableCell docCell : docRow.getTableCells()) {
				docCell.setText(row[cellIndex++]);
			}
		}
	}
}
