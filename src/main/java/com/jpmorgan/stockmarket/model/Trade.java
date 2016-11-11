package com.jpmorgan.stockmarket.model;

import com.jpmorgan.stockmarket.enums.Indicator;
import com.jpmorgan.stockmarket.infrastructure.model.Stock;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by solanoah on 10/11/2016.
 */
public final class Trade {

    /**
     * Unique key
     */
    private UUID token;

    /**
     * The stock.
     */
    private final Stock stock;

    /** The time stamp of this trade.*/
    private final Date timestamp;

    /**
     * The quantify of this trade.
     */
    private final Double quantity;

    /**
     * The trade price.
     */
    private final Double price;

    /**
     * Buy/Sell
     */
    private final Indicator indicator;

    /**
     * @param stock
     * @param indicator
     * @param quantity
     * @param price
     */
    public Trade(Stock stock, Indicator indicator, Double quantity, Double price ) {
        this.stock = stock;

        if (quantity == 0)
            throw new IllegalArgumentException();

        this.quantity = quantity;
        this.price = price;
        this.indicator = indicator;

        // Set the timestamp to now
        this.timestamp = Calendar.getInstance().getTime();

        // In case others are not unique enough for HashSet
        this.token = UUID.randomUUID();
    }

    /**
     * @return the timestamp
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * @return the total stock quantity bought/sold
     */
    public Double getQuantity(){
        return quantity;
    }

    /**
     * @return the price
     */
    public Double getPrice() {
        return price;
    }

    /**
     * @return the current stock
     */
    public Stock getStock() {
        return stock;
    }

    /**
     * @return return Buy /sell
     */
    public Indicator getIndicator(){
        return indicator;
    }

    /**
     * @param obj = trade
     * @return
     */
    @Override
    public boolean equals(Object obj){
        if (!(obj instanceof Trade)) {
            return false;
        }

        Trade trade = (Trade)obj;

        return this.token.equals(trade.getToken());
    }

    /**
     * @return
     */
    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        return prime * result + ((token == null) ? 0 : token.hashCode()); //There can never be any duplicate in the store - HashSet
    }

    private UUID getToken() {
        return token;
    }
}
