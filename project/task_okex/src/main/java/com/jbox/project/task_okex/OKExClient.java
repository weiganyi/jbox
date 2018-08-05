package com.jbox.project.task_okex;

import com.alibaba.fastjson.JSONObject;
import com.jbox.opensrc.okcoin.rest.stock.IStockRestApi;
import com.jbox.opensrc.okcoin.rest.stock.impl.StockRestApi;
import org.apache.http.HttpException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;


/**
 * @author ganyiwei
 */
public class OKExClient {
	private IStockRestApi stockGet;
	private IStockRestApi stockPost;

	public boolean DoInit() {
		String api_key = "e0e698e9-7783-4942-a5ad-c319fa695c74";  //OKCoin申请的apiKey
		String secret_key = "708AF8CEF6C6B4F2098E2AE286C15CFA";  //OKCoin 申请的secret_key
		String url_prex = "https://www.okex.com";  //注意：请求URL 国际站https://www.okcoin.com ; 国内站https://www.okcoin.cn

		/**
		 * get请求无需发送身份认证,通常用于获取行情，市场深度等公共信息
		 *
		 */
		stockGet = new StockRestApi(url_prex);

		/**
		 * post请求需发送身份认证，获取用户个人相关信息时，需要指定api_key,与secret_key并与参数进行签名，
		 * 此处对构造方法传入api_key与secret_key,在请求用户相关方法时则无需再传入，
		 * 发送post请求之前，程序会做自动加密，生成签名。
		 *
		 */
		stockPost = new StockRestApi(url_prex, api_key, secret_key);

		return true;
	}

	public String GetKline(String symbol, String type, Integer size, Long since) throws IOException, HttpException {
		//获取k线数据
		return stockGet.kline(symbol, type, size, since);
	}
}

