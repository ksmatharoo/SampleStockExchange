package org.ksm.exchange.service;

import org.ksm.exchange.models.Stock;

import java.util.List;

public interface StockService {
    Double getDividendYield(String symbol, Double stockPrice);

    Stock getStock(String symbol);

    Double getPERatio(String symbol, Double stockPrice);

    boolean addStock(Stock stock);

    List<Stock> getAllStock();

}
