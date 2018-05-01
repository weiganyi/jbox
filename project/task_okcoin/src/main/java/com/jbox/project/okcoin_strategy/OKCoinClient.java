package com.jbox.project.task_okcoin;

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
public class OKCoinClient {
	private IStockRestApi stockGet;
	private IStockRestApi stockPost;

	private Double dBuyPrice = 0.0;
	private String strBuyTime;

	private Double dProfit = 0.0;
	private Vector<ExchageRecord> clsRecord = new Vector<ExchageRecord>();
	private Integer iExchangeNum = 0;

	public boolean DoInit() {
		String api_key = "aeff3734-1c1b-43f0-81b0-ff2033a4eb88";  //OKCoin申请的apiKey
		String secret_key = "1E525C9E2DB87916AEE8F034419C736B";  //OKCoin 申请的secret_key
		String url_prex = "https://www.okcoin.cn";  //注意：请求URL 国际站https://www.okcoin.com ; 国内站https://www.okcoin.cn

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

	public TickerResult DoTicker() throws HttpException, IOException {
		//查询现货行情
		String result = stockGet.ticker("btc_cny");
		//CommonUtil.Log("OKCoinClient::DoTicker() result:" + result);

		JSONObject jsonResult = JSONObject.parseObject(result);
		if (!jsonResult.isEmpty()) {
			JSONObject jsonTicker = jsonResult.getJSONObject("ticker");
			if (!jsonTicker.isEmpty()) {
				TickerResult ticker = new TickerResult();
				String strLast = jsonTicker.getString("last");
				if (!strLast.isEmpty()) {
					ticker.setLast(Double.valueOf(strLast));
				}else {
					return null;
				}
				String strVol = jsonTicker.getString("vol");
				if (!strVol.isEmpty()) {
					ticker.setVol(Double.valueOf(strVol));
				}else {
					return null;
				}
				return ticker;
			}
		}

		return null;
	}

	public boolean DoBuyMarket(Double dCurrPrice) throws HttpException, IOException {
		//现货下单交易买入
		//String result = stockPost.trade("btc_cny", "buy_market", "300", null);
		//CommonUtil.Log("OKCoinClient::DoBuyMarket() result:"+result);

		CommonUtil.Log("OKCoinClient::DoBuyMarket() dCurrPrice:"+dCurrPrice);
		dBuyPrice = dCurrPrice;
		SimpleDateFormat df = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss] ");//设置日期格式
		strBuyTime = df.format(new Date());
		return true;
	}

	public boolean DoSellMarket(Double dCurrPrice) throws HttpException, IOException {
		//现货下单交易卖出
		//String result = stockPost.trade("btc_cny", "sell_market", null, "0.01");
		//CommonUtil.Log("OKCoinClient::DoSellMarket() result:"+result);

		CommonUtil.Log("OKCoinClient::DoSellMarket() dCurrPrice:"+dCurrPrice);
		dProfit += (dCurrPrice - dBuyPrice);
		dProfit -= dBuyPrice*0.02;

		SimpleDateFormat df = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss] ");//设置日期格式
		String strSellTime = df.format(new Date());
		clsRecord.add(new ExchageRecord(dBuyPrice, dCurrPrice, dProfit, strBuyTime, strSellTime));
		iExchangeNum++;
		return true;
	}

	public Integer getiExchangeNum() {
		return iExchangeNum;
	}

	public void setiExchangeNum(Integer iExchangeNum) {
		this.iExchangeNum = iExchangeNum;
	}

	public void setClsRecord(Vector<ExchageRecord> clsRecord) {
		this.clsRecord = clsRecord;
	}

	public Vector<ExchageRecord> getClsRecord() {
		return clsRecord;
	}
}

