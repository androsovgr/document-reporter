package ru.mephi.dr.parser.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileNameFilter implements FilenameFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileNameFilter.class);

	private final Pattern p;

	public FileNameFilter(Pattern p) {
		super();
		this.p = p;
	}

	@Override
	public boolean accept(File dir, String name) {
		Matcher m = p.matcher(name);
		if (m.matches()) {
			return true;
		} else {
			LOGGER.warn("Filename {} in folder {} doesn't matches pattern {}", name, dir, p);
			return false;
		}
	}

}
