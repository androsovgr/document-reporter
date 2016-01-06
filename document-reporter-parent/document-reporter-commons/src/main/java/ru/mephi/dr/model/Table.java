package ru.mephi.dr.model;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Table<Col, Cell> implements Collection<Cell[]> {

	private final Col[] columns;
	private final List<Cell[]> rows;
	private final Class<Cell> cellClass;
	private final Map<Col, Integer> colReference;

	public Table(Class<Cell> cellClass, Col[] columns) {
		this.columns = columns;
		rows = new LinkedList<>();
		this.cellClass = cellClass;
		colReference = new HashMap<>();
		for (int i = 0; i < columns.length; i++) {
			Col col = columns[i];
			colReference.put(col, i);
		}
		if (colReference.size() != columns.length) {
			throw new IllegalArgumentException("Columns must be unique");
		}
	}

	@Override
	public String toString() {
		return "Table [columns=" + Arrays.toString(columns) + ", rows=" + rows + "]";
	}

	@Override
	public int size() {
		return rows.size();
	}

	@Override
	public boolean isEmpty() {
		return rows.isEmpty();
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean contains(Object o) {
		if (!validateObject(o)) {
			return false;
		}
		for (Cell[] row : rows) {
			if (Arrays.equals((Cell[]) o, row)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Iterator<Cell[]> iterator() {
		return rows.iterator();
	}

	@Override
	public Object[] toArray() {
		return rows.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return rows.toArray(a);
	}

	@Override
	public boolean add(Cell[] e) {
		if (e.length != columns.length) {
			String message = String.format("Row doesn't match table format");
			throw new IllegalArgumentException(message);
		}
		return rows.add(e);
	}

	public boolean add(Map<Col, Cell> map) {
		@SuppressWarnings("unchecked")
		Cell[] row = (Cell[]) Array.newInstance(cellClass, columns.length);
		for (Entry<Col, Cell> e : map.entrySet()) {
			Integer index = colReference.get(e.getKey());
			if (index == null) {
				String message = String.format("Can't find column for %s", e.getKey());
				throw new IllegalArgumentException(message);
			}
			row[index] = e.getValue();
		}
		return add(row);
	}

	@Override
	public boolean remove(Object o) {
		if (!validateObject(o)) {
			return false;
		}
		return rows.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		for (Object o : c) {
			if (!contains(o)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends Cell[]> c) {
		for (Cell[] e : c) {
			if (e.length != columns.length) {
				String message = String.format("Row doesn't match table format");
				throw new IllegalArgumentException(message);
			}
		}

		return rows.addAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return rows.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return rows.retainAll(c);
	}

	@Override
	public void clear() {
		rows.clear();
	}

	private boolean validateObject(Object o) {
		if (o == null) {
			return false;
		}
		if (!cellClass.isAssignableFrom(o.getClass())) {
			return false;
		}
		@SuppressWarnings("unchecked")
		Cell[] oArr = (Cell[]) o;
		if (oArr.length != columns.length) {
			return false;
		}
		return true;
	}

	public Col[] getColumns() {
		return columns;
	}

}
