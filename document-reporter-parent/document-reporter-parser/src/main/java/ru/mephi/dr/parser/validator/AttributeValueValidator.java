package ru.mephi.dr.parser.validator;

import java.util.List;

import ru.mephi.dr.parser.exception.TemplateException;
import ru.mephi.dr.parser.exception.ValidateException;
import ru.mephi.dr.xml.Template.Attribute.Validators.Validator.Parameters.Parameter;

/**
 * Common interface for all validation rules.
 * 
 * @author Григорий
 *
 */
public interface AttributeValueValidator {

	/**
	 * Validates value of docx document
	 * 
	 * @param value
	 *            - value of attribute on validation
	 * @param validatorMessage
	 *            - message for validation failing
	 * @param parameters
	 *            - list of validation parameters
	 * @throws ValidateException
	 *             if attribute value is invalid
	 * @throws ValidateException
	 *             if validator defining is wrong
	 */
	public void validate(String value, String validatorMessage, List<Parameter> parameters)
			throws ValidateException, TemplateException;

}
