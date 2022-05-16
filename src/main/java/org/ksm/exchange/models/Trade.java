package org.ksm.exchange.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ksm.exchange.exception.ExchangeException;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Trade {

    public enum TradeType {
        Buy,
        Sell
    }

    private String symbol;
    private TradeType type;
    private Double tradedPrice;
    private Long sharesQuantity;
    // epoch time no need to fill this will be autofilled by code
    private Long timestamp;

    public void validateTrade() throws ExchangeException {
        if(tradedPrice == 0) {
            throw new ExchangeException(String.format("tradedPrice for %s cannot be 0", symbol));
        } else if(sharesQuantity == 0) {
            throw new ExchangeException(String.format("sharesQuantity for %s cannot be 0", symbol));
        }
    }
}
