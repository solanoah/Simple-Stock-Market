package com.jpmorgan.stockmarket.infrastructure.interfaces;

import com.jpmorgan.stockmarket.Exceptions.NoStockException;
import com.jpmorgan.stockmarket.Exceptions.NoTradeException;
import com.jpmorgan.stockmarket.dto.WeightedStockPriceDto;
import com.jpmorgan.stockmarket.infrastructure.model.Stock;

import java.util.List;

/**
 * Created by solanoah on 10/11/2016.
 */
public interface ICalculationService {

    /**
     * @param stock
     * @return Weighted Price for a given stock
     * @throws NoTradeException
     */
    Double CalculateWeightedStockPrice(Stock stock) throws NoTradeException;

    /**
     * @return
     */
    List<WeightedStockPriceDto> CalculateAllWeightedStockPrices();

    /**
     * @param weightedStockPrices
     * @return Geometric mean for all Weighted Stock Price
     * @throws NoStockException
     * @throws NoTradeException
     */
    Double CalculateGeometricMean(List<WeightedStockPriceDto> weightedStockPrices) throws NoStockException;
}
