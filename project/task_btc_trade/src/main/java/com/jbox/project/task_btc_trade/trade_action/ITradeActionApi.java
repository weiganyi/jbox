package com.jbox.project.task_btc_trade.trade_action;

import com.jbox.project.task_btc_trade.bean.KLineBean;

/**
 * Created by ganyiwei on 2018/9/7.
 */
public interface ITradeActionApi {
    //币币交易
    public boolean B2BTrade(String symbol, String type, Double price, Double amount);
}
