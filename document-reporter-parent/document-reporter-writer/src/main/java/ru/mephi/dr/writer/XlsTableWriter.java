package ru.mephi.dr.writer;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class XlsTableWriter extends ExcellTableWriter {

	public XlsTableWriter() {
		super(HSSFWorkbook.class);
	}
}
