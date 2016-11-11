package com.jpmorgan.stockmarket.Services;

import com.jpmorgan.stockmarket.Exceptions.DivideByZeroException;
import com.jpmorgan.stockmarket.Exceptions.NoStockException;
import com.jpmorgan.stockmarket.Exceptions.NoTradeException;
import com.jpmorgan.stockmarket.dto.WeightedStockPriceDto;
import com.jpmorgan.stockmarket.infrastructure.interfaces.ICalculationService;
import com.jpmorgan.stockmarket.infrastructure.interfaces.IRepository;
import com.jpmorgan.stockmarket.infrastructure.model.Stock;
import com.jpmorgan.stockmarket.model.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by solanoah on 10/11/2016.
 */
@SuppressWarnings("ALL")
@Service
public class CalculationService implements ICalculationService {

    /**
     * Duration could be added to the config
     */
    public static final Integer DURATIONMINUTE = 5;

    /**
     * Trade repository
     */
    private final IRepository<Trade> tradeRepository;

    /**
     * Stock repository
     */
    private final IRepository<Stock> stockRepository;

    /**
     * @param stockRepository
     * @param tradeRepository
     */
    @Autowired
    public CalculationService(IRepository<Stock> stockRepository, IRepository<Trade> tradeRepository) {
        this.stockRepository = stockRepository;
        this.tradeRepository = tradeRepository;
    }

    /**
     * @param stock
     * @return Weighted Price for a given stock
     * @throws NoTradeException
     * @throws DivideByZeroException
     */
    @Override
    public Double CalculateWeightedStockPrice(Stock stock) throws NoTradeException, DivideByZeroException{

        final Calendar dateRange = Calendar.getInstance();
        dateRange.add(Calendar.MINUTE, -CalculationService.DURATIONMINUTE);

        // get a list of all trades for the given stock in the last x mins
        List<Trade> trades = (List<Trade>) tradeRepository.GetAll()
                .parallelStream()
                .filter(t ->  ((Trade)t).getStock().equals(stock) && dateRange.getTime().compareTo(((Trade)t).getTimestamp()) <= 0)
                .collect(Collectors.toList());

        // No need to perform any calculation if no trade exist for stock
        if (trades.size() == 0)
        {
            throw new NoTradeException();
        }

        // calculate the cumulative of price and quantity for the given stock
        Double totalStockPriceQuantity = trades.parallelStream()
                .map(t -> t.getPrice() * t.getQuantity())
                .reduce((double) 0,(a, b) -> a+b);

        // get all quantity for the given stock
        Double totalStockQuantity = trades.parallelStream().map(Trade::getQuantity).reduce((double) 0,(a, b) -> a + b);

        // there could be a scenario where buying and selling sums to zero
        if (totalStockQuantity == 0)
        {
            throw new DivideByZeroException();
        }

        return totalStockPriceQuantity/totalStockQuantity;
    }

    /**
     * @return Weighted Prices for all stocks
     */
    @Override
    public List<WeightedStockPriceDto> CalculateAllWeightedStockPrices() {
        List<WeightedStockPriceDto> weightedStockPrices = new ArrayList<>();

        stockRepository.GetAll().forEach(obj -> {
            try {
                Stock stock = (Stock)obj;
                weightedStockPrices.add(new WeightedStockPriceDto(stock.getSymbol(), CalculateWeightedStockPrice(stock), stock.getStockType()));
            } catch (NoTradeException e) {
                e.printStackTrace();
            }
        });

        return weightedStockPrices;
    }

    /**
     * @param weightedStockPrices
     * @return Geometric mean for all Weighted Stock Price
     * @throws NoStockException
     */
    @Override
    public Double CalculateGeometricMean(List<WeightedStockPriceDto> weightedStockPrices) throws NoStockException {

        // No need to perform any calculation if no stock exist
        if (weightedStockPrices.size() == 0)
        {
            throw new NoStockException();
        }

        Double multiplyAllWeightedStockPrice = weightedStockPrices.parallelStream()
                .map(WeightedStockPriceDto::getWeightedPrice)
                .reduce((double) 1, (a, b) -> a*b);

        Double index = 1D/weightedStockPrices.size();

        return Math.pow(multiplyAllWeightedStockPrice, index);
    }
}
