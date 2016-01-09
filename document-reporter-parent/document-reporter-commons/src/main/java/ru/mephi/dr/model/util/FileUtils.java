package ru.mephi.dr.model.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import ru.mephi.dr.exception.DirectoryException;

public class FileUtils {

	/**
	 * Check if <b>dir</b> is valid directory. If not exists try create it.
	 * 
	 * @param dir
	 *            - directory for checking
	 * @throws DirectoryException
	 *             if <b>dir</b> is not valid directory
	 */
	public static void checkDirectory(File dir) throws DirectoryException {
		if (!dir.exists()) {
			boolean result = dir.mkdirs();
			if (!result) {
				String message = String.format("Directory %s not exists and can't be created", dir.getAbsolutePath());
				throw new DirectoryException(message);
			}
		}
		if (!dir.isDirectory()) {
			String message = String.format("File %s is not directory", dir.getAbsolutePath());
			throw new DirectoryException(message);
		}
	}

	/**
	 * Moves file to passed directory. If directory already has file with such
	 * name, renamed file by pattern FILENAME(INDEX).EXTENTION
	 * 
	 * @param file
	 * @param toDir
	 * @throws IOException
	 */
	public static void moveFile(File file, File toDir) throws IOException {
		File moveTo = new File(toDir, file.getName());
		if (moveTo.exists()) {
			String baseName = FilenameUtils.removeExtension(moveTo.getName());
			for (int i = 1; moveTo.exists(); i++) {
				String moveToName = baseName + "(" + i + ")" + FilenameUtils.EXTENSION_SEPARATOR_STR
						+ FilenameUtils.getExtension(moveTo.getName());
				moveTo = new File(toDir, moveToName);
			}
		}
		Files.move(file.toPath(), moveTo.toPath());
	}

	/**
	 * Create file and fill it with <b>is</b>. If file with such name already
	 * exists, renames it by pattern FILENAME(INDEX).EXTENTION
	 * 
	 * @return actually saved file
	 * @throws IOException
	 */
	public static File createFile(InputStream is, File toDir, String fileName) throws IOException {
		File moveTo = new File(toDir, fileName);
		if (moveTo.exists()) {
			String baseName = FilenameUtils.removeExtension(fileName);
			for (int i = 1; moveTo.exists(); i++) {
				String moveToName = baseName + "(" + i + ")" + FilenameUtils.EXTENSION_SEPARATOR_STR
						+ FilenameUtils.getExtension(moveTo.getName());
				moveTo = new File(toDir, moveToName);
			}
		}
		try (FileOutputStream fos = new FileOutputStream(moveTo)) {
			IOUtils.copy(is, fos);
		}
		return moveTo;
	}
}
