package ru.mephi.dr.parser.util;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import ru.mephi.dr.parser.template.Template.Attribute.Parameter;

public class ParameterParser {

	/**
	 * 
	 * @param parameters
	 *            - list of parameters
	 * @param key
	 *            - target parameter key
	 * @param defaultValue
	 *            - default value for parameter
	 * @return parameter value if exists. Otherwise - default value
	 */
	public static String getParameter(List<Parameter> parameters, String key, String defaultValue) {
		for (Parameter parameter : parameters) {
			if (StringUtils.equals(parameter.getKey(), key)) {
				return parameter.getValue();
			}
		}
		return defaultValue;
	}

	public static String getParameter(List<Parameter> parameters, String key) {
		return getParameter(parameters, key, null);
	}

	public static int getIntParameter(List<Parameter> parameters, String key, int defaultValue) {
		for (Parameter parameter : parameters) {
			if (StringUtils.equals(parameter.getKey(), key)) {
				String val = parameter.getValue();
				return Integer.parseInt(val);
			}
		}
		return defaultValue;
	}
}
