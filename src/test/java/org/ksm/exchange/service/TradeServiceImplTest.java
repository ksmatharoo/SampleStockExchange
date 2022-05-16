package org.ksm.exchange.service;

import org.junit.jupiter.api.*;
import org.ksm.exchange.models.Trade;

import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TradeServiceImplTest {

    TradeService tradeService = new TradeServiceImpl();

    @Test
    @Order(1)
    void getAllTrade() {
        List<Trade> allTrades = tradeService.getAllTrades();
        Assertions.assertEquals(26, allTrades.size());
    }

    @Test
    @Order(2)
    void getGBCEAllShareIndex() {
        final Double gbceAllShareIndex = tradeService.getGBCEAllShareIndex();
        Assertions.assertEquals(126.81605082542161, gbceAllShareIndex);
    }

    @Test
    @Order(3)
    void getVolumeWeightedStockPrice() {
        Double pop = tradeService.getVolumeWeightedStockPrice("POP", 0);
        Assertions.assertEquals(108.0, pop);
    }

    @Test
    @Order(4)
    void addTrade() {
        Trade trade = new Trade("POP", Trade.TradeType.Buy, 102d, 100l, 0l);
        tradeService.addTrade(trade);
        List<Trade> allTrades = tradeService.getAllTrades();
        Assertions.assertEquals(27, allTrades.size());
    }
}