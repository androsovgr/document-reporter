package ru.mephi.dr.parser.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.TableCell;

/**
 * This class is convient because of {@link TableCell} doesn't support
 * retrieving all paragraphs. <b>Warning!</b> supported just
 * {@link ParagraphList#size()} and {@link ParagraphList#get(int)} methods
 * 
 * @author Григорий
 *
 */
public class ParagraphList implements List<Paragraph> {

	private final TableCell cell;

	public ParagraphList(TableCell cell) {
		super();
		this.cell = cell;
	}

	@Override
	public int size() {
		return cell.numParagraphs();
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
	public Iterator<Paragraph> iterator() {
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
	public boolean add(Paragraph e) {
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
	public boolean addAll(Collection<? extends Paragraph> c) {
		throw new RuntimeException("Method is unsupported");
	}

	@Override
	public boolean addAll(int index, Collection<? extends Paragraph> c) {
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
	public Paragraph get(int index) {
		return cell.getParagraph(index);
	}

	@Override
	public Paragraph set(int index, Paragraph element) {
		throw new RuntimeException("Method is unsupported");
	}

	@Override
	public void add(int index, Paragraph element) {
		throw new RuntimeException("Method is unsupported");

	}

	@Override
	public Paragraph remove(int index) {
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
	public ListIterator<Paragraph> listIterator() {
		throw new RuntimeException("Method is unsupported");
	}

	@Override
	public ListIterator<Paragraph> listIterator(int index) {
		throw new RuntimeException("Method is unsupported");
	}

	@Override
	public List<Paragraph> subList(int fromIndex, int toIndex) {
		throw new RuntimeException("Method is unsupported");
	}

}
