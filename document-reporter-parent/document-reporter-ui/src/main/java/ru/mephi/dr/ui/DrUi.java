package ru.mephi.dr.ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import javax.xml.bind.JAXBException;

import ru.mephi.dr.parser.DocumentParser;
import ru.mephi.dr.parser.util.ParseException;
import ru.mephi.dr.parser.util.TemplateException;

public class DrUi {

	public static void main(String[] args)
			throws FileNotFoundException, IOException, JAXBException, ParseException, TemplateException {
		DocumentParser dp = new DocumentParser(args[0], args[1]);
		Map<String, String> result = dp.parse();
		System.out.println(result);
	}
}
