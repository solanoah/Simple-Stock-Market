package com.jpmorgan.stockmarket;

import com.jpmorgan.stockmarket.Exceptions.DivideByZeroException;
import com.jpmorgan.stockmarket.Exceptions.ItemExistException;
import com.jpmorgan.stockmarket.Exceptions.NoStockException;
import com.jpmorgan.stockmarket.Exceptions.NoTradeException;
import com.jpmorgan.stockmarket.Repository.StockRepository;
import com.jpmorgan.stockmarket.Repository.TradeRepository;
import com.jpmorgan.stockmarket.Services.CalculationService;
import com.jpmorgan.stockmarket.dto.StockRatioDto;
import com.jpmorgan.stockmarket.dto.WeightedStockPriceDto;
import com.jpmorgan.stockmarket.enums.Indicator;
import com.jpmorgan.stockmarket.infrastructure.interfaces.ICalculationService;
import com.jpmorgan.stockmarket.infrastructure.interfaces.IRepository;
import com.jpmorgan.stockmarket.infrastructure.model.Stock;
import com.jpmorgan.stockmarket.model.CommonStock;
import com.jpmorgan.stockmarket.model.PreferredStock;
import com.jpmorgan.stockmarket.model.Trade;
import org.springframework.boot.Banner;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.currentThread;

/**
 * Entry point
 *
 */
@SuppressWarnings("ALL")
public class AppEntry
{
    private final IRepository<Stock> stockRepository;
    private final IRepository<Trade> tradeRepository;
    private final ICalculationService calculationService;

    /**
     * Constructor
     */
    private AppEntry()
    {
        ConfigurableApplicationContext context = new SpringApplicationBuilder()
                .sources(AppConfig.class)
                .bannerMode(Banner.Mode.CONSOLE)
                .run();

        calculationService = context.getBean(ICalculationService.class);
        stockRepository = context.getBean(StockRepository.class);
        tradeRepository = context.getBean(TradeRepository.class);
    }

    /**
     * @param args
     * @throws NoStockException
     * @throws ItemExistException
     * @throws NoTradeException
     * @throws InterruptedException
     */
    public static void main( String[] args ) throws NoStockException, ItemExistException, NoTradeException, InterruptedException {

        AppEntry appEntry = new AppEntry();
        appEntry.populateStock();

//        Thread to do the following
//        i. Given any price as input, calculate the dividend yield
//        ii. Given any price as input, calculate the P/E Ratio
//        iii. Record a trade, with timestamp, quantity, buy or sell indicator and price
        Thread t1 = new Thread(() -> {
            try {
                // Set the timestamp to now
                Date timestamp = Calendar.getInstance().getTime();
                while (true) {

                    // Creates 10 new inputs/trades
                    appEntry.processInputAndCreateTrades(10);

                    // Wait for 5 secs before creates another set
                    //noinspection AccessStaticViaInstance
                    currentThread().sleep(TimeUnit.SECONDS.toMillis(CalculationService.DURATIONMINUTE));

                    // Stops the creation of trades after 5 mins
                    final Calendar dateRange = Calendar.getInstance();
                    dateRange.add(Calendar.MINUTE, -CalculationService.DURATIONMINUTE);
                    if (dateRange.getTime().compareTo(timestamp) > 0)
                        break;
                }
            } catch (InterruptedException e) {
                System.out.println("Interrupted!");
            }
        });
        t1.start();

        // Ensure that the second thread does not start till after 5 mins or after the first thread dies.
        t1.join(TimeUnit.MINUTES.toMillis(CalculationService.DURATIONMINUTE));

        // thread to calculate and display stock weighted price
        // iv. Calculate Volume Weighted Stock Price based on trades in past 5 minutes
        // Calculate the GBCE All Share Index using the geometric mean of the Volume Weighted Stock Price for all stocks
        Thread t2 = new Thread(() ->
        {
            // calculate Weighted Stock Price for all stocks
            List<WeightedStockPriceDto> weightedStockPrices = appEntry.calculationService.CalculateAllWeightedStockPrices();

            // print all Weighted Stock Prices
            appEntry.printCalculatedWeightedStockPrice(weightedStockPrices);

            try {
                // calculate GBCE for all Weighted Stock Prices
                Double gbce = appEntry.calculationService.CalculateGeometricMean(weightedStockPrices);

                // print GBCE
                System.out.println(String.format("GBCE All Share Index:  %10.4f  ", gbce));
            } catch (NoStockException e) {
                e.printStackTrace();
            }
        });
        t2.start();
    }

    /**
     * Setup default stocks...The assumption is that stock are unique by Symbol and type - Common/Preferred
     * @throws ItemExistException
     */
    private void populateStock() throws ItemExistException {
        stockRepository.Add(new CommonStock("TEA", 0D, 100D ));
        stockRepository.Add(new CommonStock("POP", 8D, 100D));
        stockRepository.Add(new CommonStock("ALE", 23D, 60D));
        stockRepository.Add(new PreferredStock("GIN",13D, 100D, 2D));
        stockRepository.Add(new CommonStock("JOE", 100D, 250D));
    }

    /**
     * Generates random prices, calculate Dividend yield and P/E ratio
     */
    private void processInputAndCreateTrades(Integer noOfTradesToCreate) {

        int count = 1;
        while (count < noOfTradesToCreate) {

            // Create Sell trade when count is even otherwise Buy trade
            Indicator indicator = (count % 2)==0 ? Indicator.Sell : Indicator.Buy;

            // Calculate and print Dividend yield and p/e ratio
            stockRepository.GetAll().forEach(stock -> {

                Double price = (double) ThreadLocalRandom.current().nextInt(1, 100);
                Double quantity = (double) ThreadLocalRandom.current().nextInt(100, 1000);

                printCalculatedStockRatios((Stock)stock, price);

                // Create trade
                try {
                    tradeRepository.Add(new Trade((Stock)stock, indicator, quantity, price));
                } catch (ItemExistException e) {
                    e.printStackTrace();
                }
            });

            count++;
        }
    }

    /**
     * Calculate and Print Stock ratios - Dividend yield and P/E ratio
     * @param stock
     * @param price
     */
    private void printCalculatedStockRatios(Stock stock, Double price) {
        try {
            StockRatioDto ratio = stock.CalculateStockRatio(price);
            if (stock instanceof CommonStock) {
                System.out.println(String.format("Symbol: %-10s  |  Type: %-10s  |  Price: %4.0f  |  Yield: %8.2f  |  P/E Ratio: %8.2f  |  L Dividend: %8.2f",
                        stock.getSymbol(), stock.getStockType().toString(), price, ratio.getDividendYield(), ratio.getPERatio(), stock.getLastDividend()));
            }else{
                System.out.println(String.format("Symbol: %-10s  |  Type: %-10s  |  Price: %4.0f  |  Yield: %8.2f  |  P/E Ratio: %8.2f  |  F Dividend: %8.2f |  Par Value: %8.2f",
                        stock.getSymbol(), stock.getStockType().toString(), price, ratio.getDividendYield(), ratio.getPERatio(), ((PreferredStock)stock).getFixedDividend(), stock.getParValue()));
            }
        } catch (DivideByZeroException divByZeroEx) {
            System.out.println(String.format("Symbol: %-10s  |  Type: %-10s  |  Price: %4.0f  |  Error:  %s", stock.getSymbol(), stock.getStockType().toString(), price, "Divide By Zero Exception"));
        }
    }

    /**
     * Calculate weighted stock prices and print
     * @param weightedStockPrices
     */
    private void printCalculatedWeightedStockPrice(List<WeightedStockPriceDto> weightedStockPrices){

        weightedStockPrices.forEach(priceDto -> System.out.println(String.format("Symbol: %-10s  |  Type: %-10s  |  Weighted Stock Price: %8.2f  ", priceDto.getSymbol(), priceDto.getStockType().toString(), priceDto.getWeightedPrice())));
    }
}
