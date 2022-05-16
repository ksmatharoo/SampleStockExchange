package org.ksm.exchange.controllers;

import org.ksm.exchange.exception.ExchangeException;
import org.ksm.exchange.models.Stock;
import org.ksm.exchange.service.StockServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/***
 * this class is the webinterface for the program to interactive with the code through web
 * all logic exist in the service
 * ***/

@RestController
@RequestMapping("/stock")
public class StockHandler {

    @Autowired
    StockServiceImpl stockService;

    @GetMapping("/{symbol}")
    public Stock getStock(@PathVariable String symbol) {
        final Stock stock = stockService.getStock(symbol);
        return stock;
    }

    @GetMapping("/")
    public List<Stock> getAllStock() {
        List<Stock> stockList = stockService.getAllStock();
        return stockList;
    }

    @PostMapping("/")
    public ResponseEntity addStock(@RequestBody Stock stock) {
        boolean isAdded = stockService.addStock(stock);
        if (isAdded) {
            return ResponseEntity.ok(stock.getSymbol() + " added successfully");
        } else {
            throw new ExchangeException(stock.getSymbol() + " already present");
        }
    }

    @GetMapping("/getDividend/{symbol}")
    public ResponseEntity getDividendYield(String symbol, Double stockPrice) {
        final Double dividendYield = stockService.getDividendYield(symbol, stockPrice);
        return ResponseEntity.ok(String.format("DividendYield for stock [%s] at price [%f] is [%f]",
                symbol, stockPrice, dividendYield));
    }

    @GetMapping("/getPERatio/{symbol}")
    public ResponseEntity getPERatio(String symbol, Double stockPrice) {
        final Double peRatio = stockService.getPERatio(symbol, stockPrice);
        return ResponseEntity.ok(String.format("PERatio for stock [%s] at price [%f] is [%f]",
                symbol, stockPrice, peRatio));
    }

}
