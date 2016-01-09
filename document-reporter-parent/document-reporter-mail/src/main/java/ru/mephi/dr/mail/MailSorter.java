package ru.mephi.dr.mail;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.mephi.dr.exception.DirectoryException;
import ru.mephi.dr.model.util.FileUtils;
import ru.mephi.dr.xml.Sortings.Sorting;

public class MailSorter {

	private static final Logger LOGGER = LoggerFactory.getLogger(MailSorter.class);

	private final File folderFrom;
	private final File folderTo;

	/**
	 * Creates sorter instance and validate input folders
	 * 
	 * @param folderFrom
	 *            - source folder for sorting
	 * @param folderTo
	 *            - target sorting for sorting
	 * @throws DirectoryException
	 *             - if one of directory is incorrect
	 */
	public MailSorter(File folderFrom, File folderTo) throws DirectoryException {
		super();
		this.folderFrom = folderFrom;
		this.folderTo = folderTo;
		FileUtils.checkDirectory(folderTo);
		FileUtils.checkDirectory(folderFrom);
	}

	/**
	 * Move files by rules
	 * 
	 * @param sortingRules
	 *            - list of rules
	 * @throws IOException
	 *             if can't move file
	 */
	public void sort(List<Sorting> sortingRules) throws IOException {
		int moved = 0;
		for (File file : folderFrom.listFiles()) {
			Sorting rule = sortingByFile(file, sortingRules);
			if (rule == null) {
				LOGGER.warn("Can't find rule for file: {}", file.getAbsolutePath());
				continue;
			}
			File toDir = new File(folderTo, rule.getFolder());
			if (!toDir.exists()) {
				toDir.mkdirs();
			}
			try {
				FileUtils.moveFile(file, toDir);
				LOGGER.debug("Moved {} to folder {}", file.getName(), toDir.getAbsolutePath());
			} catch (Exception e) {
				LOGGER.error("Can't move file {} to folder {}", file, toDir, e);
			}
			moved++;
		}
		LOGGER.info("Totally sorted {} files", moved);
	}

	private Sorting sortingByFile(File f, List<Sorting> sortingRules) {
		String fileName = f.getName();
		for (Sorting sorting : sortingRules) {
			if (fileName.matches(sorting.getNamePattern())) {
				return sorting;
			}
		}
		return null;
	}

}
