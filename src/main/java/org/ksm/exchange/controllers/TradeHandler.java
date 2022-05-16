package org.ksm.exchange.controllers;

import lombok.extern.log4j.Log4j2;
import org.ksm.exchange.exception.ExchangeException;
import org.ksm.exchange.models.Trade;
import org.ksm.exchange.service.StockServiceImpl;
import org.ksm.exchange.service.TradeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/***
 * this class is the webinterface for the program to interactive with the code through web
 * all logic exist in the service
 * ***/


@Log4j2
@RestController
@RequestMapping("/trade")
public class TradeHandler {

    @Autowired
    TradeServiceImpl tradeServiceImpl;

    @Autowired
    StockServiceImpl stockServiceImpl;

    @GetMapping("/{symbol}")
    public List<Trade> getTrades(@PathVariable String symbol) {
        return tradeServiceImpl.getTrades(symbol);
    }

    /**
     * Record a trade, with timestamp, quantity, buy or sell indicator and price
     * pass the timestamp as 0 will be updated by the engine
     *
     * @parm trade add trade with 0 as timestamp
     * @return return response as success or internal error with message
     */
    @PostMapping("/add")
    public ResponseEntity addTrade(@RequestBody Trade trade) {
        String response = String.format("%s", trade.getSymbol());
        if (stockServiceImpl.checkStockExist(trade.getSymbol())) {
            tradeServiceImpl.addTrade(trade);
            response += " added successfully";
        } else {
            response += " stock doesn't exist,Please add stock then create trade";
            log.error("{} doesn't exist", trade.getSymbol());
            throw new ExchangeException(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/volWeightedStock/{symbol}")
    public ResponseEntity getVolumeWeightedStockPrice(String symbol) {
        final Double volumeWeightedStockPrice = tradeServiceImpl.getVolumeWeightedStockPrice(symbol, 0);
        return ResponseEntity.ok(String.format("Volume Weighted Stock Price for stock [%s] is [%f]",
                symbol, volumeWeightedStockPrice));
    }

    @GetMapping("/getAllTrade")
    public List<Trade> getAllTrade() {
        return tradeServiceImpl.getAllTrades();
    }

    @GetMapping("/getGBCEAllShareIndex")
    public ResponseEntity getGBCEAllShareIndex() {
        Double exchangeIndex = tradeServiceImpl.getGBCEAllShareIndex();
        return ResponseEntity.ok(String.format("GBCEAllShareIndex is [%f]",
                exchangeIndex));
    }
}
