package com.jpmorgan.stockmarket.Repository;

import com.jpmorgan.stockmarket.infrastructure.interfaces.IRepository;
import com.jpmorgan.stockmarket.model.Trade;
import org.springframework.stereotype.Repository;

import java.util.HashSet;

/**
 * Created by solanoah on 10/11/2016.
 */
@Repository
public class TradeRepository implements IRepository<Trade> {
    /**
     * internal memory store
     */
    private final HashSet<Trade> memoryStore;

    /**
     *  repository constructor
     */
    protected TradeRepository()
    {
        memoryStore = new HashSet<>();
    }

    /**
     * @return all Trade on memory
     */
    public  HashSet<Trade> GetAll() {
        return memoryStore;
    }

    /**
     * Add an item to memory
     * @param t
     */
    public void Add(Trade t)
    {
        if (!memoryStore.contains(t))
            memoryStore.add(t);
    }

    /**
     * @param key
     * @return
     */
    @Override
    public Trade GetSingle(String key) {
        return null;
    }
}
