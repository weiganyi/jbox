package com.jbox.project.task_btc_trade.trade_action;

import com.jbox.project.task_btc_trade.bean.KLineBean;

/**
 * Created by ganyiwei on 2018/9/7.
 */
public interface ITradeActionApi {
    //币币交易买单
    public boolean B2BBuy(String symbol, Double price, Double amount);

    //币币交易卖单
    public boolean B2BSell(String symbol, Double price, Double amount);
}
