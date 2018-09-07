package com.jbox.project.task_btc_trade.trade_data.impl.okex;

import com.alibaba.fastjson.JSONArray;
import com.jbox.project.task_btc_trade.bean.KLineBean;
import com.jbox.project.task_btc_trade.trade_client.OKExClient;
import com.jbox.project.task_btc_trade.trade_data.ITradeDataApi;
import org.apache.log4j.Logger;

/**
 * Created by ganyiwei on 2018/9/7.
 */
public class OkexRealTimeTradeDataApi implements ITradeDataApi {
    OKExClient client;
    Logger logger;

    public OkexRealTimeTradeDataApi(OKExClient param1, Logger param2) {
        client = param1;
        logger = param2;
    }

    public boolean GetKlineData(String symbol, KLineBean data) {
        //获取kline数据
        String result;
        try {
            //String strTime = TimeUtils.GetNowTimeStamp();
            //Long lTime = Long.valueOf(strTime)*1000;	//ms
            Long lTime = 1516310887000L;
            logger.info(symbol+" lTime:"+lTime);
            result = client.kline(symbol, "1min", 3, lTime);
        }catch (Exception e) {
            logger.error(symbol+" catch a Exception:"+e.getMessage());
            return false;
        }
        logger.info(symbol+" result:"+result);

        //解析kline数据
        JSONArray jsonResults = JSONArray.parseArray(result);
        if (jsonResults.size() == 3) {
            JSONArray jsonResult = jsonResults.getJSONArray(0);
            if (jsonResult.size() == 6) {
                try {
                    data.setlDataTime(jsonResult.getLong(0));
                    data.setfOpen(jsonResult.getFloat(1));
                    data.setfHigh(jsonResult.getFloat(2));
                    data.setfLow(jsonResult.getFloat(3));
                    data.setfEnd(jsonResult.getFloat(4));
                    data.setfVol(jsonResult.getFloat(5));
                }catch (Exception e) {
                    logger.error(symbol+" catch a Exception:"+e.getMessage());
                    return false;
                }
            }else {
                logger.error(symbol+" jsonResult.size():"+jsonResult.size());
                return false;
            }
        }else {
            logger.error(symbol+" jsonResults.size():"+jsonResults.size());
            return false;
        }

        return true;
    }
}
