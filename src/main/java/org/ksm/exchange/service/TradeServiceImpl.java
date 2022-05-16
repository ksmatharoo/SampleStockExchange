package org.ksm.exchange.service;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.ksm.exchange.models.Trade;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static org.ksm.exchange.commons.Utils.readTextFile;

@Service
@Data
@Log4j2
public class TradeServiceImpl implements TradeService {
    ConcurrentHashMap<String, List<Trade>> tradesMap;

    public TradeServiceImpl() {
        tradesMap = new ConcurrentHashMap<>();
        init();
    }

    public void init() {
        try {
            List<String> collect = readTextFile("trades.csv");
            for (String line : collect) {
                if (!line.startsWith("#")) {
                    String[] split = line.split(",");
                    Trade trade = new Trade();
                    trade.setSymbol(split[0]);
                    if (split[1].equalsIgnoreCase("buy")) {
                        trade.setType(Trade.TradeType.Buy);
                    } else {
                        trade.setType(Trade.TradeType.Sell);
                    }
                    trade.setTradedPrice(Double.parseDouble(split[2]));
                    trade.setSharesQuantity(Long.parseLong(split[3]));

                    addTrade(trade);
                }
            }
        } catch (Exception e) {
            log.error("error while loading the initial stock data", e.toString());
        }

    }

    /**
     * Record a trade, with timestamp, quantity, buy or sell indicator and price
     */
    @Override
    public synchronized void addTrade(Trade trade) {
        trade.validateTrade();
        String symbol = trade.getSymbol();
        trade.setTimestamp(System.currentTimeMillis());
        List<Trade> stockList = tradesMap.get(symbol);
        if (Objects.isNull(stockList)) {
            List<Trade> list = new ArrayList<>();
            list.add(trade);
            tradesMap.put(symbol, list);
        } else {
            stockList.add(trade);
            tradesMap.put(symbol, stockList);
        }
    }

    /**
     * Calculate the GBCE All Share Index using the geometric
     * mean of the Volume Weighted Stock Price for all stocks
     */
    @Override
    public Double getGBCEAllShareIndex() {
        List<Trade> trades = getAllTrades();
        Double exchangeIndex = 0d;

        if (trades.size() > 0) {
            Double priceProduct = 1d;
            for (Trade trade : trades) {
                priceProduct *= trade.getTradedPrice();
            }
            Double n = (double) trades.size();

            exchangeIndex = Math.pow(priceProduct, 1d / n);
        }
        return exchangeIndex;
    }

    public List<Trade> getTrades(String symbol) {
        List<Trade> stockList = tradesMap.get(symbol);
        return stockList;
    }


    /**
     * purpose : Calculate Volume Weighted Stock Price based on trades in past 5 minutes
     * by default configured with 5 mins
     */
    @Override
    public Double getVolumeWeightedStockPrice(String symbol, long time) {
        long currentTime = System.currentTimeMillis();
        // last 5 min data by default
        long mills = time == 0 ? (1000 * 60 * 5) : time;
        long timeBeforeNMilli = currentTime - mills;
        final List<Trade> trades = getTrades(symbol);
        ListIterator tradeListIterator = trades.listIterator(trades.size());

        List<Trade> collect = new ArrayList<>();
        while (tradeListIterator.hasPrevious()) {
            Trade trade = (Trade) tradeListIterator.previous();
            if (trade.getTimestamp() >= timeBeforeNMilli) {
                collect.add(trade);
            } else {
                // trades are in chronological order so break,no need to iterate all
                break;
            }
        }
        Double weightedPrice = 0d;
        Long quantity = 0l;
        if (collect.size() > 0) {
            for (Trade trade : collect) {
                quantity += trade.getSharesQuantity();
                weightedPrice += trade.getSharesQuantity() * trade.getTradedPrice();
            }
            weightedPrice /= quantity;
        }
        return weightedPrice;
    }

    public List<Trade> getAllTrades() {
        List<List<Trade>> stockList = new ArrayList<List<Trade>>(tradesMap.values());
        List<Trade> flatList = new ArrayList<>();
        stockList.forEach(list -> {
            flatList.addAll(list);
        });

        return flatList;
    }
}
