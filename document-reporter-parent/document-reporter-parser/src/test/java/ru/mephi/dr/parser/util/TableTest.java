package ru.mephi.dr.parser.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TableTest {

	@Test(dataProvider = "addMapDataProvider")
	public void addMap(Map<String, String> map) {
		String[] columnsArr = Arrays.copyOf(map.keySet().toArray(), map.keySet().size(), String[].class);
		Table<String, String> sut = new Table<String, String>(String.class, columnsArr);
		sut.add(map);
		Assert.assertEquals(sut.size(), 1, "Invalid table size");
		String[] rowAdded = sut.iterator().next();
		int colIdx = 0;
		for (String column : sut.getColumns()) {
			String cell = map.get(column);
			Assert.assertEquals(rowAdded[colIdx], cell, "Row value mismatch for column " + column);
			colIdx++;
		}
	}

	@Test(dataProvider = "addMapDataProvider", expectedExceptions = IllegalArgumentException.class)
	public void addMapFail(Map<String, String> map) {
		String[] columnsArr = Arrays.copyOf(map.keySet().toArray(), map.keySet().size(), String[].class);
		Table<String, String> sut = new Table<String, String>(String.class, columnsArr);
		map.put(UUID.randomUUID().toString(), UUID.randomUUID().toString());
		sut.add(map);
	}

	@DataProvider
	public Object[][] addMapDataProvider() {
		String[] cols1 = { "col1", "col2" };
		Map<String, String> map1 = new HashMap<>();
		for (String col : cols1) {
			map1.put(col, UUID.randomUUID().toString());
		}
		Object[][] result = { { map1 } };
		return result;
	}
}
