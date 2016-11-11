package com.jpmorgan.stockmarket.model;

import com.jpmorgan.stockmarket.enums.StockType;
import com.jpmorgan.stockmarket.infrastructure.model.Stock;

/**
 * Created by solanoah on 10/11/2016.
 */
public final class CommonStock extends Stock {

    /**
     * @param symbol
     * @param lastDividend
     * @param parValue
     */
    public CommonStock(String symbol, Double lastDividend, Double parValue) {
        super(symbol, lastDividend, parValue);
    }

    /**
     * @return the stock type of Common
     */
    public StockType getStockType() {
        return StockType.Common;
    }

    /**
     * @param price input price
     * @return the Dividend yield for common stock
     */
    protected Double CalculateDividendYield(Double price) throws IllegalArgumentException {

        // In the very unlikely case of zero price
        if (price == 0)
        {
            throw new IllegalArgumentException();
        }

        return this.getLastDividend()/(price);
    }
}
