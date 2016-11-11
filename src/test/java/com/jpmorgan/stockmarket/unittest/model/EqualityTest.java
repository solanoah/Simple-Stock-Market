package com.jpmorgan.stockmarket.unittest.model;

import com.jpmorgan.stockmarket.enums.Indicator;
import com.jpmorgan.stockmarket.infrastructure.model.Stock;
import com.jpmorgan.stockmarket.model.CommonStock;
import com.jpmorgan.stockmarket.model.Trade;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by solanoah on 10/11/2016.
 */
public class EqualityTest {

    @Before
    public void setUp()  {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void Stock_Equal_Test() {
        Stock stockA = new CommonStock("A",1D, 10D);
        Stock stockB = new CommonStock("A",1D, 10D);
        assertEquals("These should be equal", stockA, stockB);
    }

    @Test
    public void Stock_HashCode_Equal_Test() {
        Stock stockA = new CommonStock("A",1D, 10D);
        Stock stockB = new CommonStock("A",1D, 10D);
        assertEquals("These should be equal", stockA.hashCode(), stockB.hashCode());
    }

    @Test
    public void Stock_NotEqual_Test() {
        Stock stockA = new CommonStock("A",1D, 10D);
        Stock stockB = new CommonStock("B",1D, 10D);
        assertNotEquals("These should not be equal", stockA, stockB);

        Assert.assertFalse(stockA.equals(null));
    }

    @Test
    public void Stock_HashCode_NotEqual_Test() {
        Stock stockA = new CommonStock("A",1D, 10D);
        Stock stockB = new CommonStock("B",1D, 10D);
        assertNotEquals("These should not be equal", stockA.hashCode(), stockB.hashCode());
    }

    @Test
    public void Stock_instanceof_Test() {
        Stock stockA = new CommonStock("A",1D, 10D);
        Assert.assertFalse("These should not be equal", stockA.equals(null));
    }

    @Test
    public void Trade_Should_Never_Be_Equal_Test() {
        Stock stock = new CommonStock("A",1D, 10D);
        Trade tradeA = new Trade(stock, Indicator.Buy,1D, 10D);
        Trade tradeB = new Trade(stock, Indicator.Buy,1D, 10D);
        assertNotEquals("These should never be equal", tradeA, tradeB);
    }

    @Test
    public void Trade_HashCode_Should_Never_Equal_Test() {
        Stock stock = new CommonStock("A",1D, 10D);
        Trade tradeA = new Trade(stock, Indicator.Buy,1D, 10D);
        Trade tradeB = new Trade(stock, Indicator.Buy,1D, 10D);
        assertNotEquals("These should never be equal", tradeA.hashCode(), tradeB.hashCode());
    }

    @Test
    public void Trade_instanceof_Test() {
        Trade tradeA = new Trade(null, Indicator.Buy,1D, 10D);
        Assert.assertFalse("These should not be equal", tradeA.equals(null));
    }
}
