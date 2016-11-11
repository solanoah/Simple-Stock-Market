package com.jpmorgan.stockmarket.infrastructure.model;

import com.jpmorgan.stockmarket.Exceptions.DivideByZeroException;
import com.jpmorgan.stockmarket.enums.StockType;
import com.jpmorgan.stockmarket.dto.*;

/**
 * Created by solanoah on 10/11/2016.
 */
public abstract class Stock {

    /**
     * Stock symbol
     */
    private final String symbol;

    /**
     * Last dividend
     */
    private final Double lastDividend;

    /**
     * Per Value
     */
    private final Double parValue;

    /**
     * @param symbol       Stock symbol
     * @param lastDividend the last dividend
     * @param parValue     per Value
     */
    protected Stock(String symbol, Double lastDividend, Double parValue) {
        this.symbol = symbol;
        this.lastDividend = lastDividend;
        this.parValue = parValue;
    }

    /**
     * @return get stock symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * @return The last Dividend
     */
    public Double getLastDividend() {
        return lastDividend;
    }

    /**
     * @return the Par Value
     */
    public Double getParValue() {
        return parValue;
    }

    /**
     * To be implemented in base for polymorphic behaviour
     *
     * @return
     */
    public abstract StockType getStockType();

    /**
     * @param price
     * @return The P/E ratio  and dividend yield for the current stock
     * @throws DivideByZeroException
     * @throws IllegalArgumentException
     */
    public StockRatioDto CalculateStockRatio(Double price) throws DivideByZeroException, IllegalArgumentException {
        // In the very unlikely case of zero price
        if (price == 0) {
            throw new IllegalArgumentException();
        }

        Double dy = this.CalculateDividendYield(price);

        // there could be a scenario where buying and selling sums to zero
        if (dy == 0) {
            throw new DivideByZeroException();
        }

        return new StockRatioDto(dy, price / dy);
    }

    /**
     * @param price
     * @return the Dividend yield for different stock type to be implemented in subclasses
     */
    protected abstract Double CalculateDividendYield(Double price) throws IllegalArgumentException;

    /**
     * @param obj = stock
     * @return
     */
    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof Stock)) {
            return false;
        }

        Stock stock = (Stock) obj;
        return this.symbol.equals(stock.getSymbol())
                && this.getStockType().equals(stock.getStockType());
    }

    /**
     * @return
     */
    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
        return prime * result + ((getStockType() == null) ? 0 : getStockType().hashCode());
    }
}
