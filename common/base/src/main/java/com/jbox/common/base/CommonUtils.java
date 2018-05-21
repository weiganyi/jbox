package com.jbox.common.base;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author ganyiwei
 */
public class CommonUtils {
	public static String getClassName(String name) {
		String[] field = name.split("\\.");
		int size = field.length;
		return field[size-1];
	}
}

