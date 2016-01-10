package ru.mephi.dr.writer;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XlsxTableWriter extends ExcellTableWriter {

	public XlsxTableWriter() {
		super(XSSFWorkbook.class);
	}
}
