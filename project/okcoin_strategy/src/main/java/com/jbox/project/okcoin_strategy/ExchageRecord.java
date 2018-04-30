package com.jbox.project.okcoin_strategy;


/**
 * @author ganyiwei
 */
public class ExchageRecord {
	private Double dBuyPrice;
	private Double dSellPrice;
	private Double dProfit;
	private String strBuyTime;
	private String strSellTime;

	public ExchageRecord() {
	}

	public ExchageRecord(Double dBuyPriceVal, Double dSellPriceVal, Double dProfitVal, String strBuyTimeVal, String strSellTimeVal) {
		dBuyPrice = dBuyPriceVal;
		dSellPrice = dSellPriceVal;
		dProfit = dProfitVal;
		strBuyTime = strBuyTimeVal;
		strSellTime = strSellTimeVal;
	}

	public Double getdBuyPrice() {
		return dBuyPrice;
	}

	public void setdBuyPrice(Double dBuyPrice) {
		this.dBuyPrice = dBuyPrice;
	}

	public Double getdProfit() {
		return dProfit;
	}

	public void setdProfit(Double dProfit) {
		this.dProfit = dProfit;
	}

	public String getStrSellTime() {
		return strSellTime;
	}

	public void setStrSellTime(String strSellTime) {
		this.strSellTime = strSellTime;
	}

	public Double getdSellPrice() {
		return dSellPrice;
	}

	public void setdSellPrice(Double dSellPrice) {
		this.dSellPrice = dSellPrice;
	}

	public String getStrBuyTime() {
		return strBuyTime;
	}

	public void setStrBuyTime(String strBuyTime) {
		this.strBuyTime = strBuyTime;
	}
}

