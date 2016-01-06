package ru.mephi.dr.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.PatternSyntaxException;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.mephi.dr.model.Column;
import ru.mephi.dr.model.Table;
import ru.mephi.dr.parser.DocumentFolderParser;
import ru.mephi.dr.parser.exception.FileReadException;
import ru.mephi.dr.parser.exception.ParseException;
import ru.mephi.dr.parser.exception.TemplateException;
import ru.mephi.dr.writer.DocxTableWriter;
import ru.mephi.dr.writer.TableWriter;
import ru.mephi.dr.writer.XslxTableWriter;

public class DrUi {

	private static final Logger LOGGER = LoggerFactory.getLogger(DrUi.class);

	public static void main(String[] args) throws FileNotFoundException, IOException, JAXBException, ParseException,
			TemplateException, PatternSyntaxException, FileReadException {
		DocumentFolderParser dfp = new DocumentFolderParser(args[0], args[1], args[2]);
		Table<Column, String> result = dfp.parse();
		System.out.println(result);
		for (String[] row : result) {
			System.out.println(Arrays.toString(row));
		}
		TableWriter dtw = new DocxTableWriter();
		dtw.write(result, new File("E:\\TMP\\1.docx"));
		TableWriter xtw = new XslxTableWriter();
		xtw.write(result, new File("E:\\TMP\\1.xlsx"));
	}
}
