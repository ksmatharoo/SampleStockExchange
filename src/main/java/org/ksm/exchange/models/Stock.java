package org.ksm.exchange.models;

import lombok.Data;
import org.ksm.exchange.exception.ExchangeException;

@Data
public class Stock {

    public enum StockType {
        Common,
        Preferred
    }

    private String symbol;
    private StockType type;
    private Double lastDividend;
    private Double fixedDividend;
    private Double parValue;


    public void validateStock() throws ExchangeException{
        if(parValue == 0) {
            throw new ExchangeException(String.format("parValue for %s cannot be 0", symbol));
        }
    }
}
