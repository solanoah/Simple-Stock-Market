package com.jpmorgan.stockmarket.dto;

import com.jpmorgan.stockmarket.enums.StockType;

/**
 * Created by solanoah on 10/11/2016.
 */
public class WeightedStockPriceDto {
    private final String symbol;
    private final Double weightedPrice;
    private final StockType stockType;

    /**
     * @param symbol
     * @param weightedPrice
     * @param stockType
     */
    public WeightedStockPriceDto(String symbol, Double weightedPrice, StockType stockType) {
        this.symbol = symbol;
        this.weightedPrice = weightedPrice;
        this.stockType = stockType;
    }

    /**
     * @return
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * @return
     */
    public Double getWeightedPrice() {
        return weightedPrice;
    }

    /**
     * @return
     */
    public StockType getStockType() {
        return stockType;
    }
}
