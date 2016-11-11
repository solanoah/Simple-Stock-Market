package com.jpmorgan.stockmarket.unittest.repository;

import com.jpmorgan.stockmarket.Exceptions.ItemExistException;
import com.jpmorgan.stockmarket.Repository.StockRepository;
import com.jpmorgan.stockmarket.infrastructure.model.Stock;
import com.jpmorgan.stockmarket.model.CommonStock;
import com.jpmorgan.stockmarket.model.PreferredStock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by solanoah on 10/11/2016.
 */
public class StockRepositoryTest  {

    @InjectMocks
    private StockRepository stockRepository;

    @Before
    public void setUp() throws ItemExistException {
        MockitoAnnotations.initMocks(this);
        stockRepository.Add(new CommonStock("TEA", 0D, 100D));
        stockRepository.Add(new CommonStock("POP", 8D, 100D));
        stockRepository.Add(new CommonStock("ALE", 23D, 60D));
        stockRepository.Add(new PreferredStock("GIN", 13D, 100D));
        stockRepository.Add(new CommonStock("JOE", 100D, 250D));
    }

    @Test
    public void Test_Get_Total() {
        Assert.assertEquals("There should be 5 stocks", 5, stockRepository.GetAll().size());
    }

    @Test
    public void Test_Get_Common_Total() {
        List<Stock> commonStocks = stockRepository.GetAll().stream().filter(c -> c instanceof CommonStock).collect(Collectors.toList());
        Assert.assertEquals("There should be 4 common stocks", 4, commonStocks.size());
    }

    @Test
    public void Test_Get_Preferred_Total() {
        List<Stock> preferredStocks = stockRepository.GetAll().stream().filter(c -> c instanceof PreferredStock).collect(Collectors.toList());
        Assert.assertEquals("There should be 1 common stocks", 1, preferredStocks.size());
    }

    @Test
    public void Test_Add() throws ItemExistException {
        stockRepository.Add(new CommonStock("TEX", 0D, 100D));
        Assert.assertEquals("There should be 6 stocks", 6, stockRepository.GetAll().size());
    }
    @Test
    public void Test_GetSingle() throws ItemExistException {
        Stock stock = stockRepository.GetSingle("TEA");
        Assert.assertNotNull("Stock TES should exist", stock) ;
    }

    @Test(expected = ItemExistException.class)
    public void Add_Duplicate_Hashcode_Equality_Test() throws ItemExistException {

        stockRepository.Add(new CommonStock("TEA", 0D, 100D));
    }
}
