package com.jbox.project.task_btc_trade.trade_data.impl.okex;

import com.jbox.project.task_btc_trade.bean.KLineBean;
import com.jbox.project.task_btc_trade.trade_data.ITradeDataApi;

/**
 * Created by ganyiwei on 2018/9/7.
 */
public class OkexBackTestTradeDataApi implements ITradeDataApi {
    public boolean GetKlineData(String symbol, KLineBean data) {
        return false;
    }
}
