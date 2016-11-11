package com.jpmorgan.stockmarket.dto;

/**
 * Created by solanoah on 10/11/2016.
 */
public class StockRatioDto {

    private final Double pERatio;
    private final Double dividendYield;

    /**
     * @param dividendYield
     * @param pERatio
     */
    public StockRatioDto(Double dividendYield, Double pERatio) {
        this.pERatio = pERatio;
        this.dividendYield = dividendYield;
    }

    /**
     * @return P/E ratio
     */
    public Double getPERatio() {
        return pERatio;
    }

    /**
     * @return Dividend yield
     */
    public Double getDividendYield() {
        return dividendYield;
    }
}
