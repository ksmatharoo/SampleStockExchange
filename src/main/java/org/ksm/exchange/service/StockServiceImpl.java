package org.ksm.exchange.service;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.ksm.exchange.models.Stock;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.ksm.exchange.commons.Utils.readTextFile;

@Data
@Service
@Log4j2
public class StockServiceImpl implements StockService {

    ConcurrentHashMap<String, Stock> stocksMap;

    public StockServiceImpl() {
        stocksMap = new ConcurrentHashMap<>();
        init();
    }

    public void init() {
        try {
            List<String> collect = readTextFile("stocks.csv");
            for (String line : collect) {
                if (!line.startsWith("#")) {
                    String[] split = line.split(",");
                    Stock stock = new Stock();
                    stock.setSymbol(split[0]);
                    if (split[1].equalsIgnoreCase("common")) {
                        stock.setType(Stock.StockType.Common);
                    } else {
                        stock.setType(Stock.StockType.Preferred);
                    }
                    stock.setLastDividend(Double.parseDouble(split[2]));
                    stock.setFixedDividend(Double.parseDouble(split[3]));
                    stock.setParValue(Double.parseDouble(split[4]));
                    stocksMap.put(stock.getSymbol(), stock);
                }
            }
        } catch (Exception e) {
            log.error("error while loading the initial stock data", e.toString());
        }
    }

    public boolean checkStockExist(String symbol) {
        final Stock stock = stocksMap.get(symbol);
        return stock == null ? false : true;
    }

    public boolean addStock(Stock stock) {
        stock.validateStock();
        if (checkStockExist(stock.getSymbol())) {
            return false;
        } else {
            stocksMap.put(stock.getSymbol(), stock);
        }
        return true;
    }

    @Override
    public Stock getStock(String symbol) {
        final Stock stock = stocksMap.get(symbol);
        return stock;
    }

    @Override
    public List<Stock> getAllStock() {
        final ArrayList<Stock> stocks = new ArrayList<>(stocksMap.values());
        return stocks;
    }


    /***
     * Given any price as input, calculate the dividend yield
     * **/
    @Override
    public Double getDividendYield(String symbol, Double stockPrice) {
        log.debug("DividendYield for Symbol {}, stockPrice {} ", symbol, stockPrice);
        Stock stock = getStock(symbol);
        Double dividendYield = null;
        if (Stock.StockType.Common == stock.getType()) {
            dividendYield = stock.getLastDividend() / stockPrice;
        } else {
            dividendYield = (stock.getFixedDividend() * stock.getParValue()) / stockPrice;
        }
        return dividendYield;
    }

    /***
     * Given any price as input, calculate the P/E Ratio
     * **/

    @Override
    public Double getPERatio(String symbol, Double stockPrice) {
        log.debug("Finding PE Ratio with Symbol {} and StockPrice {}", symbol, stockPrice);
        Double dividendYield = getDividendYield(symbol, stockPrice);
        Double peRatio = stockPrice / dividendYield;
        log.debug("PE Ratio {} with Symbol {} and StockPrice {}", peRatio, symbol, stockPrice);
        return peRatio;
    }
}
