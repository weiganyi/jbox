package com.jbox.project.okcoin_strategy;

import com.alibaba.fastjson.JSONObject;
import com.jbox.opensrc.okcoin.rest.stock.IStockRestApi;
import com.jbox.opensrc.okcoin.rest.stock.impl.StockRestApi;
import org.apache.http.HttpException;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author ganyiwei
 */
public class CommonUtil {
	public static void Log(String str) {
		SimpleDateFormat df = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss] ");//设置日期格式
		String currTime = df.format(new Date());
		System.out.println(currTime+str);
	}

	public static String getClassName(String name) {
		String[] field = name.split("\\.");
		int size = field.length;
		return field[size-1];
	}

	public static Double getDoubleScale(Double val, int scale) {
		BigDecimal b = new BigDecimal(val);
		return b.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
}

