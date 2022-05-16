package org.ksm.exchange.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.ksm.exchange.models.Stock;

import java.util.List;

class StockServiceImplTest {
    StockService stockService = new StockServiceImpl();

    @Test
    void addStock() {
    }

    @Test
    void getStock() {
    }

    @Test
    void getAllStock() {
        List<Stock> allStock = stockService.getAllStock();
        Assertions.assertEquals(5, allStock.size());
    }

    @Test
    void getDividendYield() {
        final Double yield = stockService.getDividendYield("POP", 108d);
        Assertions.assertEquals(0.07407407407407407, yield);
    }

    @Test
    void getPERatio() {
        final Double peRatio = stockService.getPERatio("POP", 108d);
        Assertions.assertEquals(1458.0, peRatio);
    }
}