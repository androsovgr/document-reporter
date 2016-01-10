package ru.mephi.dr.parser.util;

import java.util.Iterator;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableIterator;

/**
 * This class is confient because of f*@king {@link TableIterator} doesn't
 * implement {@link Iterator}
 * 
 * @author Григорий
 *
 */
public class MyTableIterator implements Iterator<Table> {

	private final TableIterator ti;

	public MyTableIterator(HWPFDocument doc) {
		ti = new TableIterator(doc.getRange());
	}

	@Override
	public boolean hasNext() {
		return ti.hasNext();
	}

	@Override
	public Table next() {
		return ti.next();
	}

}
