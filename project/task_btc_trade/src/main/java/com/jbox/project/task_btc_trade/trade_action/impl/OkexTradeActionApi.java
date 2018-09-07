package com.jbox.project.task_btc_trade.trade_action.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jbox.project.task_btc_trade.trade_action.ITradeActionApi;
import com.jbox.project.task_btc_trade.trade_client.OKExClient;
import org.apache.log4j.Logger;

/**
 * Created by ganyiwei on 2018/9/7.
 */
public class OkexTradeActionApi implements ITradeActionApi {
    OKExClient client;
    Logger logger;

    public OkexTradeActionApi(OKExClient param1, Logger param2) {
        client = param1;
        logger = param2;
    }

    public boolean B2BBuy(String symbol, Double price, Double amount) {
        String logPrefix = symbol + " " + price + " " + amount;
        String result;
        try {
            result = client.trade(symbol, "buy", price.toString(), amount.toString());
        } catch (Exception e) {
            logger.error(logPrefix + " catch a Exception:" + e.getMessage());
            return false;
        }
        logger.info(logPrefix + " result:" + result);

        //解析结果
        JSONObject jsonResults = JSON.parseObject(result);
        if (jsonResults.get("result") != true) {
            logger.error(logPrefix+" return fail, result:"+result);
            return false;
        }

        return true;
    }

    public boolean B2BSell(String symbol, Double price, Double amount) {
        String logPrefix = symbol + " " + price + " " + amount;
        String result;
        try {
            result = client.trade(symbol, "sell", price.toString(), amount.toString());
        } catch (Exception e) {
            logger.error(logPrefix + " catch a Exception:" + e.getMessage());
            return false;
        }
        logger.info(logPrefix + " result:" + result);

        //解析结果
        JSONObject jsonResults = JSON.parseObject(result);
        if (jsonResults.get("result") != true) {
            logger.error(logPrefix+" return fail, result:"+result);
            return false;
        }

        return true;
    }

}
