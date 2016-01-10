package ru.mephi.dr.parser.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.poi.hwpf.usermodel.TableCell;
import org.apache.poi.hwpf.usermodel.TableRow;

/**
 * This class is convient because of {@link TableRow} doesn't support retrieving
 * all cells. <b>Warning!</b> supported just {@link TableCellList#size()} and
 * {@link TableCellList#get(int)} methods
 * 
 * @author Григорий
 *
 */
public class TableCellList implements List<TableCell> {

	private final TableRow row;

	public TableCellList(TableRow row) {
		super();
		this.row = row;
	}

	@Override
	public int size() {
		return row.numCells();
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
	public Iterator<TableCell> iterator() {
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
	public boolean add(TableCell e) {
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
	public boolean addAll(Collection<? extends TableCell> c) {
		throw new RuntimeException("Method is unsupported");
	}

	@Override
	public boolean addAll(int index, Collection<? extends TableCell> c) {
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
	public TableCell get(int index) {
		return row.getCell(index);
	}

	@Override
	public TableCell set(int index, TableCell element) {
		throw new RuntimeException("Method is unsupported");
	}

	@Override
	public void add(int index, TableCell element) {
		throw new RuntimeException("Method is unsupported");

	}

	@Override
	public TableCell remove(int index) {
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
	public ListIterator<TableCell> listIterator() {
		throw new RuntimeException("Method is unsupported");
	}

	@Override
	public ListIterator<TableCell> listIterator(int index) {
		throw new RuntimeException("Method is unsupported");
	}

	@Override
	public List<TableCell> subList(int fromIndex, int toIndex) {
		throw new RuntimeException("Method is unsupported");
	}

}
