package com.jbox.common.base;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ganyiwei
 */
public class TimeUtils {
	public static String GetNowFormatTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		return df.format(new Date());
	}

	public static String GetNowTimeStamp() {
		long time = System.currentTimeMillis();
		String nowTimeStamp = String.valueOf(time / 1000);
		return nowTimeStamp;
	}

	public static String Date2TimeStamp(String dateStr) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return String.valueOf(sdf.parse(dateStr).getTime() / 1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}

