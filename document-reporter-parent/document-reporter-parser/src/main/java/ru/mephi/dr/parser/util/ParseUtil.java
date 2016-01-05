package ru.mephi.dr.parser.util;

import ru.mephi.dr.parser.constant.TablePosition;
import ru.mephi.dr.parser.exception.TemplateException;

public class ParseUtil {

	public static int parsePosition(String position, int totalCount) throws TemplateException {
		int tablePosition;
		if (TablePosition.FIRST.equals(position)) {
			tablePosition = 0;
		} else if (TablePosition.LAST.equals(position)) {
			tablePosition = totalCount - 1;
		} else {
			try {
				tablePosition = Integer.parseInt(position) - 1;
			} catch (NumberFormatException e) {
				String message = String.format("Wrong table position %s", position);
				throw new TemplateException(message);
			}
		}
		return tablePosition;
	}
}
