package ru.mephi.dr.parser.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableRow;

/**
 * This class is convient because of {@link Table} doesn't support retrieving
 * all rows. <b>Warning!</b> supported just {@link TableRowList#size()} and
 * {@link TableRowList#get(int)} methods
 * 
 * @author Григорий
 *
 */
public class TableRowList implements List<TableRow> {

	private final Table table;

	public TableRowList(Table table) {
		super();
		this.table = table;
	}

	@Override
	public int size() {
		return table.numRows();
	}

	@Override
	public boolean isEmpty() {
		throw new RuntimeException("Method is unsupported");
	}

	@Override
	public boolean contains(Object o) {
		throw new RuntimeException("Method is unsupported");
	}

	@Override
	public Iterator<TableRow> iterator() {
		throw new RuntimeException("Method is unsupported");
	}

	@Override
	public Object[] toArray() {
		throw new RuntimeException("Method is unsupported");
	}

	@Override
	public <T> T[] toArray(T[] a) {
		throw new RuntimeException("Method is unsupported");
	}

	@Override
	public boolean add(TableRow e) {
		throw new RuntimeException("Method is unsupported");
	}

	@Override
	public boolean remove(Object o) {
		throw new RuntimeException("Method is unsupported");
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		throw new RuntimeException("Method is unsupported");
	}

	@Override
	public boolean addAll(Collection<? extends TableRow> c) {
		throw new RuntimeException("Method is unsupported");
	}

	@Override
	public boolean addAll(int index, Collection<? extends TableRow> c) {
		throw new RuntimeException("Method is unsupported");
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new RuntimeException("Method is unsupported");
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new RuntimeException("Method is unsupported");
	}

	@Override
	public void clear() {
		throw new RuntimeException("Method is unsupported");

	}

	@Override
	public TableRow get(int index) {
		return table.getRow(index);
	}

	@Override
	public TableRow set(int index, TableRow element) {
		throw new RuntimeException("Method is unsupported");
	}

	@Override
	public void add(int index, TableRow element) {
		throw new RuntimeException("Method is unsupported");

	}

	@Override
	public TableRow remove(int index) {
		throw new RuntimeException("Method is unsupported");
	}

	@Override
	public int indexOf(Object o) {
		throw new RuntimeException("Method is unsupported");
	}

	@Override
	public int lastIndexOf(Object o) {
		throw new RuntimeException("Method is unsupported");
	}

	@Override
	public ListIterator<TableRow> listIterator() {
		throw new RuntimeException("Method is unsupported");
	}

	@Override
	public ListIterator<TableRow> listIterator(int index) {
		throw new RuntimeException("Method is unsupported");
	}

	@Override
	public List<TableRow> subList(int fromIndex, int toIndex) {
		throw new RuntimeException("Method is unsupported");
	}

}
