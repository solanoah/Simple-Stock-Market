package com.jpmorgan.stockmarket.Repository;

import com.jpmorgan.stockmarket.Exceptions.ItemExistException;
import com.jpmorgan.stockmarket.infrastructure.interfaces.IRepository;
import com.jpmorgan.stockmarket.infrastructure.model.Stock;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.LinkedHashMap;

/**
 * Created by solanoah on 10/11/2016.
 */
@Repository
public class StockRepository implements IRepository<Stock> {

    /**
     * internal memory store
     */
    private final LinkedHashMap<String, Stock> memoryStore;

    /**
     *  repository constructor
     */
    protected StockRepository()
    {
        memoryStore = new LinkedHashMap<>();
    }

    /**
     * @return all Trade on memory
     */
    public Collection<Stock> GetAll() {
        return memoryStore.values();
    }

    /**
     * Add an item to memory
     * @param stock
     * @throws ItemExistException
     */
    public void Add(Stock stock) throws ItemExistException {
        if (!memoryStore.containsValue(stock))
            memoryStore.put(stock.getSymbol(), stock);
        else
            throw new ItemExistException();
    }

    /**
     * @param key
     * @return a single stock
     */
    public Stock GetSingle(String key){
        return memoryStore.get(key);
    }
}

