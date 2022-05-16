package org.ksm.exchange.service;

import org.ksm.exchange.models.Trade;

import java.util.List;

public interface TradeService {

    //Calculate Volume Weighted Stock Price based on trades in past 5 minutes
    Double getVolumeWeightedStockPrice(String symbol, long time);

    void addTrade(Trade trade);

    Double getGBCEAllShareIndex();

    List<Trade> getAllTrades();


}
