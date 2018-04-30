package com.jbox.project.okcoin_strategy;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.util.Vector;


/**
 * @author ganyiwei
 */
public class task_okcoin_strategy {
	private OKCoinClient client = new OKCoinClient();

	private Double QUEUE_NONE_TO_UP_THRESHOLD = 0.01;
	private Double QUEUE_NONE_TO_DOWN_THRESHOLD = 0.03;
	private Double QUEUE_DOWN_TO_UP_THRESHOLD = 0.01;
	private Double QUEUE_UP_TO_DOWN_THRESHOLD = 0.03;
	private Double BUY_UP_TO_DOWN_THRESHOLD = 0.01;

	private final long timeInterval = 1000;	// run in a second

	private Double dCurrPrice = 0.0;
	private Double dLowestPrice = 1000000.0;
	private Double dHighestPrice = 0.0;
	private Double dMiddlePrice = 0.0;
	private Double dPrevVol = 0.0;

	private RUN_STATE eCurrState = RUN_STATE.RUN_STATE_QUERY;
	private RUN_DIRECTION eCurrDir = RUN_DIRECTION.RUN_DIR_NONE;

	private String shutdownFile;
	private String saveValuesFile;

	enum RUN_STATE {
		RUN_STATE_QUERY,
		RUN_STATE_BUY,
		RUN_STATE_SELL
	};

	enum RUN_DIRECTION {
		RUN_DIR_NONE,
		RUN_DIR_DOWN,
		RUN_DIR_UP
	};

	public task_okcoin_strategy() {
		client.DoInit();

		try {
			shutdownFile = CommonUtil.getClassName(this.getClass().getName())+".shutdown";
			saveValuesFile = CommonUtil.getClassName(this.getClass().getName())+".json";

			File f = new File(shutdownFile);	//创建控制退出的本地文件
			f.createNewFile();

			loadValues();

			TickerResult ticker = client.DoTicker();
			if (ticker != null) {
				dMiddlePrice = ticker.getLast();
				dPrevVol = CommonUtil.getDoubleScale(ticker.getVol(), 2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean saveValues() throws IOException {
		JSONObject jsonValues = new JSONObject();
		jsonValues.put("dCurrPrice", dCurrPrice);
		jsonValues.put("dLowestPrice", dLowestPrice);
		jsonValues.put("dHighestPrice", dHighestPrice);
		jsonValues.put("dMiddlePrice", dMiddlePrice);
		jsonValues.put("eCurrState", eCurrState);
		jsonValues.put("eCurrDir", eCurrDir);
		jsonValues.put("iExchangeNum", client.getiExchangeNum());
		JSONArray jsonRecords = new JSONArray();
		Vector<ExchageRecord> vecRecords = client.getClsRecord();
		for (int i=0; i<vecRecords.size(); i++) {
			ExchageRecord record = vecRecords.get(i);
			JSONObject jsonRecord = new JSONObject();
			jsonRecord.put("dBuyPrice", record.getdBuyPrice());
			jsonRecord.put("dSellPrice", record.getdSellPrice());
			jsonRecord.put("dProfit", record.getdProfit());
			jsonRecord.put("strBuyTime", record.getStrBuyTime());
			jsonRecord.put("strSellTime", record.getStrSellTime());
			jsonRecords.add(jsonRecord);
		}
		jsonValues.put("clsRecord", jsonRecords);
		String strValues = jsonValues.toString();

		byte bt[] = new byte[1024*10];
		bt = strValues.getBytes();
		FileOutputStream in = new FileOutputStream(saveValuesFile);
		in.write(bt, 0, bt.length);
		in.close();

		return true;
	}

	private boolean loadValues() throws IOException {
		File f = new File(saveValuesFile);	//判断文件是否存在
		if (!f.exists()) {
			return true;
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(saveValuesFile)));
		String strValues = br.readLine();

		JSONObject jsonResult = JSONObject.parseObject(strValues);
		if (jsonResult.containsKey("dCurrPrice")) {
			dCurrPrice = jsonResult.getDouble("dCurrPrice");
		}
		if (jsonResult.containsKey("dLowestPrice")) {
			dLowestPrice = jsonResult.getDouble("dLowestPrice");
		}
		if (jsonResult.containsKey("dHighestPrice")) {
			dHighestPrice = jsonResult.getDouble("dHighestPrice");
		}
		if (jsonResult.containsKey("dMiddlePrice")) {
			dMiddlePrice = jsonResult.getDouble("dMiddlePrice");
		}
		if (jsonResult.containsKey("eCurrState")) {
			eCurrState = Enum.valueOf(RUN_STATE.class, jsonResult.getString("eCurrState"));
		}
		if (jsonResult.containsKey("eCurrDir")) {
			eCurrDir = Enum.valueOf(RUN_DIRECTION.class, jsonResult.getString("eCurrDir"));
		}
		if (jsonResult.containsKey("iExchangeNum")) {
			client.setiExchangeNum(jsonResult.getInteger("iExchangeNum"));
		}
		if (jsonResult.containsKey("clsRecord")) {
			JSONArray jsonRecords = jsonResult.getJSONArray("clsRecord");
			Vector<ExchageRecord> vecRecords = new Vector<ExchageRecord>();
			for (int i=0; i<jsonRecords.size(); i++) {
				JSONObject jsonRecord = (JSONObject) jsonRecords.get(i);
				ExchageRecord record = new ExchageRecord();
				if (jsonRecord.containsKey("dBuyPrice")) {
					record.setdBuyPrice(jsonRecord.getDouble("dBuyPrice"));
				}
				if (jsonRecord.containsKey("dSellPrice")) {
					record.setdSellPrice(jsonRecord.getDouble("dSellPrice"));
				}
				if (jsonRecord.containsKey("dProfit")) {
					record.setdProfit(jsonRecord.getDouble("dProfit"));
				}
				if (jsonRecord.containsKey("strBuyTime")) {
					record.setStrBuyTime(jsonRecord.getString("strBuyTime"));
				}
				if (jsonRecord.containsKey("strSellTime")) {
					record.setStrSellTime(jsonRecord.getString("strSellTime"));
				}
				vecRecords.add(record);
			}
			client.setClsRecord(vecRecords);
		}

		return true;
	}

	private boolean run() {
		CommonUtil.Log("task_okcoin_strategy::run() enter, QUEUE_NONE_TO_UP_THRESHOLD:"+QUEUE_NONE_TO_UP_THRESHOLD+
				", QUEUE_NONE_TO_DOWN_THRESHOLD:"+QUEUE_NONE_TO_DOWN_THRESHOLD+
				", QUEUE_DOWN_TO_UP_THRESHOLD:"+QUEUE_DOWN_TO_UP_THRESHOLD+
				", QUEUE_UP_TO_DOWN_THRESHOLD:"+QUEUE_UP_TO_DOWN_THRESHOLD+
				", BUY_UP_TO_DOWN_THRESHOLD:"+BUY_UP_TO_DOWN_THRESHOLD);

		try {
			while (true) {
				File f = new File(shutdownFile);	//判断是否退出
				if (!f.exists()) {
					break;
				}

				TickerResult ticker = client.DoTicker();
				if (ticker != null) {
					dCurrPrice = ticker.getLast();
					Double dCurrVol = CommonUtil.getDoubleScale(ticker.getVol(), 2);
					Double dDiffVol = CommonUtil.getDoubleScale((dCurrVol - dPrevVol), 2);
					dPrevVol = dCurrVol;
					CommonUtil.Log("task_okcoin_strategy::run() dCurrPrice:" + dCurrPrice + ", dPrevVol:" + dPrevVol + ", dDiffVol:" + dDiffVol
							+ ", dLowestPrice:" + dLowestPrice + ", dHighestPrice:" + dHighestPrice + ", dMiddlePrice:" + dMiddlePrice
							+ ", eCurrState:" + eCurrState + ", eCurrDir:" + eCurrDir + ", iExchangeNum:" + client.getiExchangeNum());
					switch (eCurrState) {
						case RUN_STATE_QUERY:
							switch (eCurrDir) {
								case RUN_DIR_NONE:
									if (dCurrPrice > dMiddlePrice) {
										if (((dCurrPrice - dMiddlePrice) / dMiddlePrice) > QUEUE_NONE_TO_UP_THRESHOLD) {    //确立上行趋势
											eCurrDir = RUN_DIRECTION.RUN_DIR_UP;    //上行
											dHighestPrice = dCurrPrice;
										}
									} else {
										if (((dMiddlePrice - dCurrPrice) / dMiddlePrice) > QUEUE_NONE_TO_DOWN_THRESHOLD) {    //确立下行趋势
											eCurrDir = RUN_DIRECTION.RUN_DIR_DOWN;    //下行
											dLowestPrice = dCurrPrice;
										}
									}
									break;

								case RUN_DIR_DOWN:
									if (dCurrPrice < dLowestPrice) {    //继续下行
										dLowestPrice = dCurrPrice;
									} else if (((dCurrPrice - dLowestPrice) / dLowestPrice) > QUEUE_DOWN_TO_UP_THRESHOLD) {    //确立上行趋势
										client.DoBuyMarket(dCurrPrice);    //买入
										eCurrDir = RUN_DIRECTION.RUN_DIR_UP;    //上行
										eCurrState = RUN_STATE.RUN_STATE_BUY;    //跳转到买入状态
										dHighestPrice = dCurrPrice;
									}
									break;

								case RUN_DIR_UP:
									if (dCurrPrice > dHighestPrice) {    //继续上行
										dHighestPrice = dCurrPrice;
									} else if (((dHighestPrice - dCurrPrice) / dHighestPrice) > QUEUE_UP_TO_DOWN_THRESHOLD) {    //确立下行趋势
										eCurrDir = RUN_DIRECTION.RUN_DIR_DOWN;
										dLowestPrice = dCurrPrice;
									}
									break;
							}
							break;

						case RUN_STATE_BUY:
							if (dCurrPrice < dHighestPrice) {
								if (((dHighestPrice - dCurrPrice) / dHighestPrice) > BUY_UP_TO_DOWN_THRESHOLD) {    //确立下行趋势
									client.DoSellMarket(dCurrPrice);    //卖出
									eCurrDir = RUN_DIRECTION.RUN_DIR_DOWN;    //下行
									eCurrState = RUN_STATE.RUN_STATE_SELL;    //跳转到卖出状态
								}
							} else {
								dHighestPrice = dCurrPrice;    //更新最高价
							}
							break;

						case RUN_STATE_SELL:
							dMiddlePrice = dCurrPrice;
							eCurrDir = RUN_DIRECTION.RUN_DIR_NONE;
							eCurrState = RUN_STATE.RUN_STATE_QUERY;    //跳转到查询状态
							break;
					}
				} else {
					CommonUtil.Log("task_okcoin_strategy::run() call DoTicker return fail!");
				}

				Thread.sleep(timeInterval);
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				saveValues();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return false;
		}

		try {
			saveValues();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	public static void main(String[] args) {
		CommonUtil.Log("task_okcoin_strategy::main() start!!!");

		while(true) {
			task_okcoin_strategy strategy = new task_okcoin_strategy();
			boolean ret = strategy.run();
			if (ret) {
				break;
			}
		}

		CommonUtil.Log("task_okcoin_strategy::main() end!!!");
	}
}

