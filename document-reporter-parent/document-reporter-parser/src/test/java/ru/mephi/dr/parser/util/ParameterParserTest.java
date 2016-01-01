package ru.mephi.dr.parser.util;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import ru.mephi.dr.parser.template.Template.Attribute.Parameter;

public class ParameterParserTest {

	@Test(dataProvider = "existingDataProvider")
	public void testExisting(List<Parameter> parameters, String key, String realValue) {
		String result = ParameterParser.getParameter(parameters, key, "ololo");
		Assert.assertEquals(result, realValue);
	}

	@Test(dataProvider = "notExistingDataProvider")
	public void testNotExisting(List<Parameter> parameters, String key, String defaultValue) {
		String result = ParameterParser.getParameter(parameters, key, defaultValue);
		Assert.assertEquals(result, defaultValue);
	}

	@DataProvider
	public Object[][] existingDataProvider() {
		List<Parameter> params = generateList();
		Object[] case1 = { params, "key1", "value1" };
		Object[] case2 = { params, "key3", "value3" };
		Object[][] result = { case1, case2 };
		return result;
	}

	@DataProvider
	public Object[][] notExistingDataProvider() {
		List<Parameter> params = generateList();
		Object[] case1 = { params, "key4", "df1" };
		Object[] case2 = { params, "key5", "" };
		Object[][] result = { case1, case2 };
		return result;
	}

	private List<Parameter> generateList() {
		List<Parameter> params = new ArrayList<>();
		params.add(new Parameter("key1", "value1"));
		params.add(new Parameter("key2", "value2"));
		params.add(new Parameter("key3", "value3"));
		return params;
	}
}
