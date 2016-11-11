package com.jpmorgan.stockmarket.model;

import com.jpmorgan.stockmarket.enums.StockType;
import com.jpmorgan.stockmarket.infrastructure.model.Stock;

/**
 * Created by solanoah on 10/11/2016.
 */
public final class PreferredStock extends Stock {

    /**
     * Fixed dividend for Preferred stock
     */
    private Double fixedDividend = 2D;

    /**
     * @param symbol
     * @param lastDividend
     * @param parValue
     */
    public PreferredStock(String symbol, Double lastDividend, Double parValue) {
        super(symbol, lastDividend, parValue);
    }

    /**
     * @param symbol
     * @param lastDividend
     * @param parValue
     * @param fixedDividend
     */
    public PreferredStock(String symbol, Double lastDividend, Double parValue, Double fixedDividend) {
        this(symbol, lastDividend, parValue);
        this.fixedDividend = fixedDividend;
    }

    /**
     * @return Fixed dividend for Preferred stock
     */
    public Double getFixedDividend() {
        return this.fixedDividend;
    }

    /**
     * @return the stock type of Preferred
     */
    public StockType getStockType() {
        return StockType.Preferred;
    }

    /**
     * @param price input price
     * @return the Dividend yield for Preferred stock
     */
    protected Double CalculateDividendYield(Double price) throws IllegalArgumentException {
        // In the very unlikely case of zero price
        if (price == 0)
        {
            throw new IllegalArgumentException();
        }

        return (this.getParValue() * (this.fixedDividend/100))/price;
    }
}
