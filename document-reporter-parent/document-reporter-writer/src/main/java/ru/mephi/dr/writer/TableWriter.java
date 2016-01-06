package ru.mephi.dr.writer;

import java.io.File;
import java.io.IOException;

import ru.mephi.dr.model.Column;
import ru.mephi.dr.model.Table;

public interface TableWriter {

	void write(Table<Column, String> table, File target) throws IOException;
}
