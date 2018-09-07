package com.jbox.project.task_btc_trade.trade_data;

import com.jbox.project.task_btc_trade.bean.KLineBean;

/**
 * Created by ganyiwei on 2018/9/7.
 */
public interface ITradeDataApi {
    //获取k线数据
    public boolean GetKlineData(String symbol, KLineBean data);
}
