package com.jpmorgan.stockmarket.unittest.service;

import com.jpmorgan.stockmarket.Exceptions.DivideByZeroException;
import com.jpmorgan.stockmarket.Exceptions.NoStockException;
import com.jpmorgan.stockmarket.Exceptions.NoTradeException;
import com.jpmorgan.stockmarket.Services.CalculationService;
import com.jpmorgan.stockmarket.dto.StockRatioDto;
import com.jpmorgan.stockmarket.dto.WeightedStockPriceDto;
import com.jpmorgan.stockmarket.enums.Indicator;
import com.jpmorgan.stockmarket.infrastructure.interfaces.IRepository;
import com.jpmorgan.stockmarket.infrastructure.model.Stock;
import com.jpmorgan.stockmarket.model.CommonStock;
import com.jpmorgan.stockmarket.model.PreferredStock;
import com.jpmorgan.stockmarket.model.Trade;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;
/**
 * Created by solanoah on 10/11/2016.
 */
public class CalculationServiceTest {

    private CalculationService calculationService;

    @Mock
    private IRepository<Stock> stockRepository;
    @Mock
    private IRepository<Trade> tradeRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        calculationService = new CalculationService(stockRepository, tradeRepository);
        Mockito.when(stockRepository.GetAll()).thenReturn(CreateStocks().values());
        Mockito.when(tradeRepository.GetAll()).thenReturn(new CopyOnWriteArraySet<>());
    }

    private HashMap<String, Stock>  CreateStocks() {

        HashMap<String, Stock> stocks = new HashMap<>();

        stocks.put("TEA", new CommonStock("TEA", 0D, 100D));
        stocks.put("POP", new CommonStock("POP", 8D, 100D));
        stocks.put("ALE", new CommonStock("ALE", 23D, 60D));
        stocks.put("GIN", new PreferredStock("GIN",13D, 100D));
        stocks.put("JOE", new CommonStock("JOE", 100D, 250D));

        return stocks;
    }

    private CopyOnWriteArraySet<Trade>  CreateStockTrades() {

        CopyOnWriteArraySet<Trade> trades = new CopyOnWriteArraySet<>();
        int count = 1;
        Random randomGenerator = new Random();
        while (count < 50) {

            // Create Sell trade when count is even otherwise Buy trade
            Indicator indicator = (count%2)==0 ? Indicator.Sell : Indicator.Buy;
            CreateStocks().values().forEach(stock -> trades.add(new Trade(stock, indicator, randomGenerator.nextDouble(), randomGenerator.nextDouble())));
            count++;
        }

        return trades;
    }

    @Test(expected = NoStockException.class)
    public void When_no_trade_created_throw_NoStockException() throws NoStockException, NoTradeException {
        Mockito.when(stockRepository.GetAll()).thenReturn(new HashSet<>());
        List<WeightedStockPriceDto> weightedStockPrices = new ArrayList<>();
        for (Object obj : stockRepository.GetAll()) {
            Stock stock = (Stock)obj;
            weightedStockPrices.add(new WeightedStockPriceDto(stock.getSymbol(), calculationService.CalculateWeightedStockPrice(stock), stock.getStockType()));
        }
        calculationService.CalculateGeometricMean(weightedStockPrices);
    }

    @Test(expected = NoTradeException.class)
    public void When_no_trade_created_throw_NoTradeException() throws NoStockException, NoTradeException {

        for (Object obj : stockRepository.GetAll()) {
            Stock stock = (Stock)obj;
            calculationService.CalculateWeightedStockPrice(stock);
        }

        Mockito.verify(tradeRepository).GetAll();
        Assert.assertEquals("There should be 5 stocks", 5, stockRepository.GetAll().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void CalculatePERatio_throw_IllegalArgumentException_when_price_zero() throws NoStockException {

        for (Object stock: stockRepository.GetAll()){
            ((Stock)stock).CalculateStockRatio(0D);
        }

        Assert.assertEquals("There should be 5 stocks", 5, stockRepository.GetAll().size());
    }

    @Test(expected = DivideByZeroException.class)
    public void CalculatePERatio_StockTEA_Price10() throws NoStockException {

        CreateStocks().get("TEA").CalculateStockRatio(10D);
    }

    @Test
    public void CalculatePERatio_StockPOP_Price10() throws NoStockException {

        StockRatioDto ratio =  CreateStocks().get("POP").CalculateStockRatio(10D);
        Assert.assertEquals("Dividend yield should be ", new Double(0.8), ratio.getDividendYield());
        Assert.assertEquals("P/E ratio should be ", new Double(12.5), ratio.getPERatio());
    }

    @Test
    public void CalculatePERatio_StockALE_Price10() throws NoStockException {

        StockRatioDto ratio =  CreateStocks().get("ALE").CalculateStockRatio(10D);
        Assert.assertEquals("Dividend yield should be ", new Double(2.3), ratio.getDividendYield());
        Assert.assertEquals("P/E ratio should be ", new Double(4.347826086956522), ratio.getPERatio());
    }
    @Test
    public void CalculatePERatio_StockGIN_Price10() throws NoStockException {

        StockRatioDto ratio =  CreateStocks().get("GIN").CalculateStockRatio(10D);
        Assert.assertEquals("Dividend yield should be ", new Double(0.2), ratio.getDividendYield());
        Assert.assertEquals("P/E ratio should be ", new Double(50), ratio.getPERatio());
    }

    @Test
    public void CalculatePERatio_StockJOE_Price10() throws NoStockException {

        StockRatioDto ratio =  CreateStocks().get("JOE").CalculateStockRatio(10D);
        Assert.assertEquals("Dividend yield should be ", new Double(10), ratio.getDividendYield());
        Assert.assertEquals("P/E ratio should be ", new Double(1), ratio.getPERatio());
    }

    @Test
    public void CalculateWeightedStockPrice_Test() throws NoStockException, NoTradeException {

        Mockito.when(tradeRepository.GetAll()).thenReturn(CreateStockTrades());

        for (Stock stock : CreateStocks().values()) {
            Double weightedStockPrice =  calculationService.CalculateWeightedStockPrice(stock);
            Assert.assertFalse(weightedStockPrice <= 0D);
        }
    }


    @Test(expected = NoTradeException.class)
    @Ignore
    public void CalculateWeightedStockPrice_After_5mins_should_have_no_more_trade_Test() throws NoStockException, NoTradeException, InterruptedException {

        Mockito.when(tradeRepository.GetAll()).thenReturn(CreateStockTrades());

        for (Stock stock : CreateStocks().values()) {
            calculationService.CalculateWeightedStockPrice(stock);
        }

        Thread.sleep(TimeUnit.MINUTES.toMillis(CalculationService.DURATIONMINUTE));

        for (Stock stock : CreateStocks().values()) {
            calculationService.CalculateWeightedStockPrice(stock);
        }
    }

    @Test
    public void CalculateGeometricMean_Test() throws NoStockException, NoTradeException {

        Mockito.when(tradeRepository.GetAll()).thenReturn(CreateStockTrades());
        List<WeightedStockPriceDto> weightedStockPrices = new ArrayList<>();
        for (Object obj : stockRepository.GetAll()) {
            Stock stock = (Stock)obj;
            weightedStockPrices.add(new WeightedStockPriceDto(stock.getSymbol(), calculationService.CalculateWeightedStockPrice(stock), stock.getStockType()));
        }
        Double geometricMean = calculationService.CalculateGeometricMean(weightedStockPrices);
        Assert.assertFalse(geometricMean <= 0D);
    }
}
