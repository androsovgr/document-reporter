package ru.mephi.dr.parser.validator;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import ru.mephi.dr.parser.exception.TemplateException;
import ru.mephi.dr.parser.exception.ValidateException;
import ru.mephi.dr.xml.Template.Attribute.Validators.Validator.Parameters.Parameter;

/**
 * This class validates if attribute matches regular expression
 * <table border="1">
 * <caption>Configuration</caption>
 * <tr>
 * <th>Key</th>
 * <th>Description</th>
 * </tr>
 * <tr>
 * <td>{@value #REGEXP_PARAMETER_KEY}</td>
 * <td>Regular expression, which attribute must match.</td>
 * </tr>
 * </table>
 * 
 * @author Gregory
 *
 */
public class ValueLikeValdator implements AttributeValueValidator {

	public static final String REGEXP_PARAMETER_KEY = "regexp";

	@Override
	public void validate(String value, String validatorMessage, List<Parameter> parameters)
			throws ValidateException, TemplateException {
		String regexp = null;
		for (Parameter parameter : parameters) {
			if (StringUtils.equals(parameter.getKey(), REGEXP_PARAMETER_KEY)) {
				regexp = parameter.getValue();
				break;
			}
		}
		if (regexp == null) {
			String message = String.format("Attribute with key %s unspecified for converter %s", REGEXP_PARAMETER_KEY,
					ValueLikeValdator.class);
			throw new TemplateException(message);
		}
		if (!value.matches(regexp)) {
			String message = String.format("%s. Value %s doesn't matches pattern %s", validatorMessage, value, regexp);
			throw new ValidateException(message);
		}
	}

}
